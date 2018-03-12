from utils import *
from keras.models import load_model
from time import sleep
import pickle
import pandas as pd
from argparse import ArgumentParser
import matplotlib.pyplot as plt
from matplotlib import image


class Simulation:

	def __init__(self, model, scaler, settings):
		"""
		Initialize a driver module.
		:param model: The ML model used for predicting fuel consumption.
		:param scaler: A MinMaxScaler to inverse the scaled values. 
		"""
		self.model = model
		self.scaler = scaler
		self.settings = settings

	def run(self, drive):
		"""
		Simulates a drive by processing a data row each second.
		"""
		drive = drive.drop("Time(s)", axis=1)
		self.settings["features"] = drive.shape[1] - 1
		drive_reframed = series_to_supervised(drive, settings["timesteps"], 1)
		for time in range(drive_reframed.shape[0]):
			# Predict each timestep one at a time.
			row = drive_reframed.iloc[[time]]
			X, y_actual = split_x_y(row, settings)
			y_pred = model.predict(X, batch_size=1)[0]

			# Invert the scaling.
			pred_array = np.array([y_pred[0]] * drive.shape[1]).reshape(1,-1)
			actual_array = np.array([y_actual[0]] * drive.shape[1]).reshape(1,-1)
			y_actual = scaler.inverse_transform(pred_array)[0, 0]
			y_pred = scaler.inverse_transform(actual_array)[0, 0]

			print(self.RTA(y_pred, y_actual))
			# sleep(1)


	def RTA(self, prediction, truth, tol=0.01):
		"""
		Real-time analysis performed during driving.
		:param prediction: Expected fuel consumption
		:param truth: Real fuel consumption
		:param tol: The tolerance
		:return:
		"""
		# TODO find a more sophisticated classifying function.
		# TODO implement a fader function for use with the warning triangle.
		HAPPY, NEUTRAL, SAD = ":)", ":|", ":("
		

	def PDA(self):
		"""
		Post-drive analysis performed after each drive.
		Displays a graphical representation of a drive.
		:return:
		"""
		pass


if __name__ == '__main__':
	# TODO this is not good coding practice. Consider saving the settings as a file as well.
	settings = {"batch_size": 32,
	            "timesteps": 5,
	            "units": 32,
	            "epochs": 30}

	parser = ArgumentParser()
	parser.add_argument("--model", dest="model_name")
	parser.add_argument("--dataset", dest="dataset", default="kia")
	args = parser.parse_args()

	model_suffix, scaler_suffix, test_suffix = ".h5", ".scl", ".test"
	model_name = args.model_name
	dataset = args.dataset

	if not model_name:
		avail_models = get_models()
		print("Choose one of the available models:")
		for idx, model in enumerate(avail_models):
			print("* {} ({})".format(model, idx+1))

		model_name = avail_models[int(input()) - 1]
		model_name = model_name.replace(model_suffix, "")

	model = load_model("models/" + model_name + model_suffix)
	test_drives = pickle.load(open(model_name + test_suffix, "rb"))
	scaler = pickle.load(open(dataset + scaler_suffix, "rb"))
	sim = Simulation(model, scaler, settings)

	for drive in test_drives:
		sim.run(drive)

