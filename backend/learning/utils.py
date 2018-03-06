from sklearn.model_selection import train_test_split
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
import numpy as np


# Credit to: https://machinelearningmastery.com/convert-time-series-supervised-learning-problem-python/
def series_to_supervised(data, n_in=1, n_out=1, dropnan=True):
	"""
	Frame a time series as a supervised learning dataset.
	Arguments:
		data: Sequence of observations as a list or NumPy array.
		n_in: Number of lag observations as input (X).
		n_out: Number of observations as output (y).
		dropnan: Boolean whether or not to drop rows with NaN values.
	Returns:
		Pandas DataFrame of series framed for supervised learning.
	"""
	n_vars = 1 if type(data) is list else data.shape[1]
	df = pd.DataFrame(data)
	cols, names = list(), list()
	# input sequence (t-n, ... t-1)
	for i in range(n_in, 0, -1):
		cols.append(df.shift(i))
		names += [('var%d(t-%d)' % (j + 1, i)) for j in range(n_vars)]
	# forecast sequence (t, t+1, ... t+n)
	for i in range(0, n_out):
		cols.append(df.shift(-i))
		if i == 0:
			names += [('var%d(t)' % (j + 1)) for j in range(n_vars)]
		else:
			names += [('var%d(t+%d)' % (j + 1, i)) for j in range(n_vars)]
	# put it all together
	agg = pd.concat(cols, axis=1)
	agg.columns = names
	# drop rows with NaN values
	if dropnan:
		agg.dropna(inplace=True)
	return agg


def scale_data(data):
	scaler = MinMaxScaler((-1, 1))
	scaled_values = scaler.fit_transform(data.as_matrix())
	scaled_df = pd.DataFrame(scaled_values, columns=list(data))
	print("Data has been scaled to fit in the range -1 to 1")
	return scaled_df, scaler


def split_data(data, ratio=0.1, concat=True):
	# TODO consider using K-fold validation
	splitted_data = []
	new_instance = False
	start_index = 0
	for index, time in enumerate(data["Time(s)"]):
		if time == 1 and start_index != index:
			new_instance = True

		if new_instance and start_index != index:
			splitted_data.append(data[start_index:index])
			start_index = index
			new_instance = False

	train_runs, test_runs = train_test_split(splitted_data, test_size=ratio)
	if concat:
		return pd.concat(train_runs), pd.concat(test_runs)
	else:
		return train_runs, test_runs


def filter_data(data, threshold=0.2, filter_discrete=False):
	# They're practically identical to Vehicle_speed.
	num_cols_before = len(list(data))
	always_drop = ["Wheel_velocity_front_left-hand", "Wheel_velocity_rear_right-hand",
				   "Wheel_velocity_front_right-hand", "Wheel_velocity_rear_left-hand"]
	data = data.drop(always_drop, axis=1)
	correlations = data.corr(method="kendall")["Fuel_consumption"]
	cols = list(data)
	for i, corr in enumerate(correlations):
		if filter_discrete:
			col = data[cols[i]]
			# print(cols[i], col.nunique() / col.count())
			is_discrete = col.nunique() / col.count() < 0.001
			if is_discrete:
				data = data.drop(cols[i], axis=1)
				continue

		if np.isnan(corr) or np.abs(corr) < threshold:
			if cols[i] != "Time(s)":
				data = data.drop(cols[i], axis=1)
				
	num_cols_remaining = len(list(data))
	print("{removed} columns filtered.\n"
	      "{kept} columns remaining".format(removed=num_cols_before - num_cols_remaining,
	                                        kept=num_cols_remaining))
	return data


def calc_road_limit(data, timestep, window_size=10):
	"""
	This is done by taking the median vehicle speed of the last 10 time steps and the rounding off
	to the nearest tenth.
	We're assuming that the speed kept is reasonably withing the allowed limit.
	:return: The speed
	"""
	# TODO consider a more sophisticated way of doing this
	steps_back = steps_forward = int(window_size / 2)
	if timestep < steps_back:
		steps_back = timestep
		steps_forward = window_size - timestep

	vehicle_speed = data[timestep - steps_back: timestep + steps_forward].get("Vehicle_speed")
	med = np.median(vehicle_speed.as_matrix())
	return int(round(med/10)*10)


def load_data(filename):
	data = pd.read_csv("../datasets/" + filename)
	data = data.drop("Class", axis=1)
	return data

