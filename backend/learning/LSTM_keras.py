from sklearn.metrics import mean_squared_error
from keras.models import Sequential, load_model
from keras.layers import Dense, LSTM
from keras.callbacks import EarlyStopping
from math import sqrt
from matplotlib import pyplot
import argparse
from utils import *
import pickle
import os
import pandas as pd


def build_model(data):
	X, y = split_x_y(data)
	print("Training data has shape {}".format(X.shape))

	# Design network
	model = Sequential()
	model.add(LSTM(settings["units"], input_shape=(X.shape[1], X.shape[2])))
	model.add(Dense(1))
	model.compile(loss="mae", optimizer="adam")

	# Fit network
	early_stopping = EarlyStopping(patience=3, verbose=2)
	model.fit(X, y, epochs=settings["epochs"], verbose=1, batch_size=settings["batch_size"],
			  shuffle=False, callbacks=[early_stopping], validation_split=0.1)
	
	# Training is preferably done with a batch size > 1 but since we're looking to do
	# online prediction we need a model that will accept batch size = 1
	model = convert_to_online_model(model)
	return model


def split_x_y(data):
	n_obs = settings["timesteps"] * settings["features"]
	X, y = data.values[:, :n_obs], data.values[:, -settings["features"] - 1]
	# reshape input to be 3D [samples, timesteps, features]
	X = X.reshape((X.shape[0], settings["timesteps"], settings["features"]))
	return X, y


def convert_to_online_model(model):
	"""
	Saves the weights of a model and uses them to create one with batch size = 1.
	"""
	online_model = Sequential()
	online_model.add(LSTM(settings["units"], batch_input_shape=(1, settings["timesteps"],
	                                                            settings["features"])))
	online_model.add(Dense(1))
	trained_weights = model.get_weights()
	online_model.set_weights(trained_weights)
	online_model.compile(loss="mae", optimizer="adam")
	return online_model


def prepare_data(file):
	"""
	Filter and scale data. Save to csv.
	"""
	scaled_data_file = "scaled_" + file + ".csv"
	scaler_file = file + ".scl"
	if os.path.isfile(scaled_data_file) and os.path.isfile(scaler_file):
		print("Prepared data found. Opening..")
		return pd.read_csv(scaled_data_file), pickle.load(open(scaler_file, "rb"))
	
	# Read data from csv
	data = pd.read_csv("../datasets/KIA_driving_data.csv")
	time = data["Time(s)"]
	data = data.drop(["Class", "Time(s)"], axis=1)

	# Filter unuseful columns
	data = filter_data(data)
	
	# Scale data to fit into [0,1]
	scaled_df, scaler = scale_data(data)
	scaled_df["Time(s)"] = time
	scaled_df.to_csv(scaled_data_file, index=False)
	pickle.dump(scaler, open(scaler_file, "wb"))
	return scaled_df, scaler


def plot_time_series(data):
	pass
	

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
	pyplot.legend()
	pyplot.show()


# ###############################################
# ###########          MAIN           ###########
# ###############################################


settings = {"batch_size": 32,
            "timesteps": 5,
            "units": 32,
            "epochs": 30}

parser = argparse.ArgumentParser()
parser.add_argument("--new-model", action="store_true", dest="train_new")
parser.add_argument("--name", dest="model_name", default="lstm_model")
parser.add_argument("--visualize", action="store_true", dest="visualize_it")
parser.add_argument("--list-models", action="store_true", dest="list_models")
parser.add_argument("--predict", action="store_true", dest="predict")
args = parser.parse_args()

model_name = args.model_name
train_new_model = args.train_new
visualize = args.visualize_it
list_models = args.list_models
do_predict = args.predict
dataset = "kia"

if list_models:
	models = get_models()
	print("Available models are:")
	for model in models:
		print("* {}".format(model))

data, scaler = prepare_data(dataset)
training_drives, test_drives = split_data(data, ratio=0.05, concat=False)
print("Using {} drives for training and {} for testing..".format(len(training_drives), len(test_drives)))
train = pd.concat(training_drives)
train = train.drop("Time(s)", axis=1)

settings["features"] = train.shape[1] - 1
train_reframed = series_to_supervised(train, settings["timesteps"], 1)

if train_new_model:
	print("Training new model..")
	model = build_model(train_reframed)
	model = convert_to_online_model(model)
	print("Model trained..")
	model.save(model_name + ".h5")
	pickle.dump(test_drives, open(model_name + ".test", "wb"))
	
else:
	print("Loading pre-trained model..")
	model = load_model(model_name + ".h5")
	scaler = pickle.load(open(dataset + ".scl" , "rb"))
	test_drives = pickle.load(open(model_name + ".test", "rb"))

if do_predict:
	for drive in test_drives:
		drive = drive.drop("Time(s)", axis=1)
		drive_reframed = series_to_supervised(drive, settings["timesteps"], 1)
		y_pred, y_truth = [], []
		for time in range(drive_reframed.shape[0]):
			# Predict each timestep one at a time.
			row = drive_reframed.iloc[[time]]
			X, y = split_x_y(row)
			y_hat = model.predict(X, batch_size=1)[0]
			y_pred.append(y_hat[0])
			y_truth.append(y[0])


		# Drop the first columns
		drive = drive.iloc[settings["timesteps"]:]

		# Invert the scaling.
		truth = scaler.inverse_transform(drive.as_matrix())[:,0]
		drive["Fuel_consumption"] = y_pred
		predictions = scaler.inverse_transform(drive.as_matrix())[:,0]

		# Plot the predictions
		plot_predictions(predictions, truth, range(len(predictions)))
			

if visualize:
	for drive in test_drives:
		# This is just the timestamo, it shouldn't be part of the calculations,
		time_series = drive["Time(s)"].values
		drive = drive.drop("Time(s)", axis=1)
		scaled_drive = scaler.transform(drive.as_matrix())
		scaled_drive_df = pd.DataFrame(scaled_drive, columns=list(drive))
		predictions = predict(model, scaled_drive_df)
		
		# Invert the transformation before plotting
		true_fuel_consumption = drive["Fuel_consumption"].values
		scaled_drive_df["Fuel_consumption"] = predictions
		unscaled = scaler.inverse_transform(scaled_drive_df.as_matrix())
		predicted_fuel_consumption = pd.DataFrame(unscaled, columns=list(drive))["Fuel_consumption"].values
		
		plot_predictions(predicted_fuel_consumption, true_fuel_consumption, time_series)

