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
	data = data.drop("Class", axis=1)
	time = data["Time(s)"]
	
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
	

def predict_fuel_consumption(model, state):
	X = state.reshape(1, 1, state.size)
	prediction = model.predict(X, batch_size=settings["batch_size"])
	return prediction[0]


def predict(model, test):
	num_states = test.shape[0]
	predictions = []
	for i in range(num_states):
		state = test.iloc[[i]]
		state_values = state.drop("Fuel_consumption", axis=1).as_matrix()
		predicted_fuel = predict_fuel_consumption(model, state_values)[0]
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
	models = [model for model in os.listdir(".") if model.endswith("h5")]
	print("Available models are:")
	for model in models:
		print("* {}".format(model))
	exit(1)

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
		drive = series_to_supervised(drive, settings["timesteps"], 1)
		for time in range(drive.shape[0]):
			row = drive.iloc[[time]]
			X, y = split_x_y(row)
			y_pred = model.predict(X, batch_size=1)[0]
			print(y_pred, y)
	
	if visualize:
		pass
			
			

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

