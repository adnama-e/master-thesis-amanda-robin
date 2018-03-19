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
import tensorflow as tf

def build_model(input, output):
	# reshape input to be 3D [samples, timesteps, features]
	X, y = reshape_io(input, output, settings)
	
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

	# Save as pb file
	export_model(tf.train.Saver(), model, model_name)
	return model


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


def evaluate(predictions, data, scaler):
	for i, pred in enumerate(predictions):
		state = data.iloc[[i]]
		y_true = scaler.inverse_transform(state.as_matrix())
		state["Fuel_consumption"] = pred
		y_pred = scaler.inverse_transform(state.as_matrix())
		rmse = sqrt(mean_squared_error(y_true, y_pred))
		print("RMSE: {}, for state {}".format(rmse, i))


def plot_predictions(predictions, true_values, time_series):
	pyplot.plot(time_series, predictions, 'r--', label="Predicted")
	pyplot.plot(time_series, true_values, label="Actual")
	pyplot.legend()
	pyplot.show()


# ###############################################
# ###########          MAIN           ###########
# ###############################################


settings = {"batch_size": 32,
            "timesteps": 5,
            "delay": 3,
            "units": 32,
            "epochs": 30}

parser = argparse.ArgumentParser()
parser.add_argument("--new-model", action="store_true", dest="train_new")
parser.add_argument("--name", dest="model_name", default="lstm_model")
parser.add_argument("--visualize", action="store_true", dest="visualize_it")
parser.add_argument("--list-pbfiles", action="store_true", dest="list_models")
parser.add_argument("--predict", action="store_true", dest="predict")
parser.add_argument("--save-pb", action="store_true", dest="save_pb")
args = parser.parse_args()

model_name = args.model_name
train_new_model = args.train_new
visualize = args.visualize_it
list_models = args.list_models
do_predict = args.predict
dataset = "kia"

if list_models:
	models = get_models()
	print("Available pbfiles are:")
	for model in models:
		print("* {}".format(model))

if train_new_model:
	data, scaler = prepare_data(dataset)
	training_drives, test_drives = split_data(data, ratio=0.05, concat=False)
	print("Using {} drives for training and {} for testing..".format(len(training_drives), len(test_drives)))
	train = pd.concat(training_drives)
	train = train.drop("Time(s)", axis=1)

	settings["features"] = train.shape[1]
	train_reframed, output_reframed = series_to_supervised(train, settings["timesteps"], 1)

	print("Training new model..")
	model = build_model(train_reframed, output_reframed)
	print("Model trained..")
	model.save(model_name + ".h5")
	pickle.dump(test_drives, open(model_name + ".test", "wb"))
	pickle.dump(settings, open("settings.d", "wb"))
else:
	model = load_model(model_name + ".h5")
	scaler = pickle.load(open(dataset + ".scl" , "rb"))
	test_drives = pickle.load(open(model_name + ".test", "rb"))
	print("Loaded pre-trained model.")

if args.save_pb:
	export_to_pb(model, model_name)
	# export_model(tf.train.Saver(), model, model_name)

if do_predict:
	for drive in test_drives:
		drive = drive.drop("Time(s)", axis=1)
		settings["features"] = drive.shape[1]
		input_df, output_df = series_to_supervised(drive, settings["timesteps"], 1)
		y_pred, y_truth = [], []
		for step in range(input_df.shape[0]):
			# Predict each timestep one at a time.
			row = input_df.iloc[[step]]
			out = output_df.iloc[[step]]
			X, y = reshape_io(row, out, settings)
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
			
