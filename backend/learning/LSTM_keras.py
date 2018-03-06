from sklearn.metrics import mean_squared_error
from keras.models import Sequential, load_model
from keras.layers import Dense, LSTM
from math import sqrt
from matplotlib import pyplot
import argparse
from utils import *
import pickle
import os


def build_model(data, settings):
	# var1 is Fuel_consumption
	n_obs = settings["timesteps"] * settings["features"]
	X, y = data.values[:, :n_obs], data.values[:, -settings["features"]]
	# reshape input to be 3D [samples, timesteps, features]
	X = X.reshape((X.shape[0], settings["timesteps"], settings["features"]))
	
	# Design network
	model = Sequential()
	model.add(LSTM(settings["units"], input_shape=(X.shape[1], X.shape[2])))
	model.add(Dense(1))
	model.compile(loss="mae", optimizer="adam")
	
	# Fit network
	model.fit(X, y, epochs=settings["epochs"], batch_size=settings["batch_size"], verbose=1, shuffle=False)
	
	return model


def prepare_data(scaled_file="scaled_kia.csv"):
	"""
	Filter and scale data. Save to csv.
	"""
	if os.path.isfile(scaled_file):
		print("Prepared data found. Opening..")
		return pd.read_csv(scaled_file), pickle.load("f")
	
	# Read data from csv
	data = pd.read_csv("../datasets/KIA_driving_data.csv")
	data = data.drop("Class", axis=1)
	time = data["Time(s)"]
	
	# Filter unuseful columns
	data = filter_data(data)
	
	# Scale data to fit into [0,1]
	scaled_df, scaler = scale_data(data)
	scaled_df["Time(s)"] = time
	scaled_df.to_csv(scaled_file)
	return scaled_df, scaler


def plot_time_series(data):
	pass
	

def predict_fuel_consumption(model, state, settings):
	X = state.reshape(1, 1, state.size)
	prediction = model.predict(X, batch_size=settings["batch_size"])
	return prediction[0]


def predict(model, test, settings):
	num_states = test.shape[0]
	predictions = []
	for i in range(num_states):
		state = test.iloc[[i]]
		state_values = state.drop("Fuel_consumption", axis=1).as_matrix()
		predicted_fuel = predict_fuel_consumption(model, state_values, settings)[0]
		predictions.append(predicted_fuel)
	return predictions


def evaluate(predictions, data, scaler):
	for i, pred in enumerate(predictions):
		state = data.iloc[[i]]
		y_true = scaler.inverse_transform(state.as_matrix())
		state["Fuel_consumption"] = pred
		y_pred = scaler.inverse_transform(state.as_matrix())
		rmse = sqrt(mean_squared_error(y_true, y_pred))
		print("RMSE: {}, for state {}".format(rmse, i))


def plot_predictions(predictions, true_values, time_series):
	pyplot.plot(time_series, predictions, label="Predicted")
	pyplot.plot(time_series, true_values, 'r--', label="Actual")
	pyplot.show()


# ###############################################
# ###########          MAIN           ###########
# ###############################################


settings = {"batch_size": 12,
            "timesteps": 5,
            "units": 50,
            "epochs": 30}

parser = argparse.ArgumentParser()
parser.add_argument("--new-model", action="store_true", dest="train_new")
parser.add_argument("--name", dest="model_name", default="lstm_model")
parser.add_argument("-v", action="store_true", dest="visualize_it")
args = parser.parse_args()

model_name = args.model_name
train_new_model = args.train_new
visualize = args.visualize_it

data, scaler = prepare_data()
training_drives, test_drives = split_data(data, concat=False)
train = pd.concat(training_drives)
train.drop("Time(s)", axis=1, inplace=True)

settings["features"] = train.shape[1] - 1

train_reframed = series_to_supervised(train, settings["timesteps"], 1)

if train_new_model:
	print("Training new model..")
	model = build_model(train_reframed, settings)
	print("Model trained..")
	model.save(model_name + ".h5")
	pickle.dump(scaler, open(model_name + ".scl", "wb"))
	pickle.dump(test_drives, open(model_name + ".test", "wb"))
else:
	print("Loading pre-trained model..")
	model = load_model(model_name + ".h5")
	scaler = pickle.load(open(model_name + ".scl" ))
	test_drives = pickle.load(open(model_name + ".test"))

if visualize:
	for drive in test_drives:
		# This is just the timestamo, it shouldn't be part of the calculations,
		time_series = drive["Time(s)"].values
		drive = drive.drop("Time(s)", axis=1)
		scaled_drive = scaler.transform(drive.as_matrix())
		scaled_drive_df = pd.DataFrame(scaled_drive, columns=list(drive))
		predictions = predict(model, scaled_drive_df, settings)
		
		# Invert the transformation before plotting
		true_fuel_consumption = drive["Fuel_consumption"].values
		scaled_drive_df["Fuel_consumption"] = predictions
		unscaled = scaler.inverse_transform(scaled_drive_df.as_matrix())
		predicted_fuel_consumption = pd.DataFrame(unscaled, columns=list(drive))["Fuel_consumption"].values
		
		plot_predictions(predicted_fuel_consumption, true_fuel_consumption, time_series)

