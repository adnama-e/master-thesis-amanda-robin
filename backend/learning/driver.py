from utils import *
from keras.models import load_model
import time
import pickle
import pandas as pd

class Driver():
	
	def __init__(self, model, driving_sequences, scaler):
		"""
		Initialize a driver module.
		:param model: The ML model used for predicting fuel consumption.
		:param driving_sequences: A list of pandas dataframes. Each dataframe is a driving sequence.
		"""
		self.model = model
		self.seqs = driving_sequences
		self.scaler = scaler
	
	
	def drive(self, id):
		"""
		Simulates a drive by processing a data row each second.
		:param id:
		:return:
		"""
		data = self.seqs[id]
		data = data.drop("Time(s)", axis=1)
		scaled_data = scaler.transform(data.as_matrix())
		data = pd.DataFrame(scaled_data, columns=list(data))
		for i in range(data.shape[0]):
			state = data.iloc[[i]]
			y_true = state["Fuel_consumption"]
			X = state.drop("Fuel_consumption", axis=1).as_matrix()
			X = X.reshape(1, 1, X.size)
			y_pred = self.model.predict(X, batch_size=1)
			cls = self.RTA(y_pred, y_true)
			print(cls)
			time.sleep(1)
			
	
	def RTA(self, prediction, truth, tol=0.01):
		"""
		Real-time analysis performed during driving.
		:param prediction: Expected fuel consumption
		:param truth: Real fuel consumption
		:param tol: The tolerance
		:return:
		"""
		# TODO consider using a more sophisticated classifying function
		if prediction <= truth + tol:
			return ":)"
		else:
			return ":("
		
	def PDA(self):
		"""
		Post-drive analysis performed after each drive.
		Displays a graphical representation of a drive.
		:return:
		"""
		pass
	

if __name__ == '__main__':
	model = load_model("lstm_model.h5")
	test_drives = pickle.load(open("lstm_model.test", "rb"))
	scaler = pickle.load(open("lstm_model.scl", "rb"))
	driver = Driver(model, test_drives, scaler)
	driver.drive(0)



