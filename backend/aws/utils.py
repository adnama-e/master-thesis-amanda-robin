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
	import pandas as pd
	n_vars = 1 if type(data) is list else data.shape[1]
	df = pd.DataFrame(data)
	input_cols, output_cols, input_names, output_names = [], [], [], []
	# input sequence (t-n, ... t-1)
	for i in range(n_in, 0, -1):
		input_cols.append(df.shift(i))
		input_names += [('var%d(t-%d)' % (j + 1, i)) for j in range(n_vars)]
		
	# forecast sequence (t, t+1, ... t+n)
	for i in range(0, n_out):
		output_cols.append(df.shift(-i))
		if i == 0:
			output_names += [('var%d(t)' % (j + 1)) for j in range(n_vars)]
		else:
			output_names += [('var%d(t+%d)' % (j + 1, i)) for j in range(n_vars)]
	# put it all together
	input_agg = pd.concat(input_cols, axis=1)
	input_agg.columns = input_names
	output_agg = pd.concat(output_cols, axis=1)
	output_agg.columns = output_names
	output_agg = output_agg.iloc[n_in:]
	# drop rows with NaN values
	if dropnan:
		input_agg.dropna(inplace=True)
		output_agg.dropna(inplace=True)

	return input_agg, output_agg


def get_models():
	"""
	:return: A list of the trained pbfiles.
	"""
	import os
	model_dir = "/home/robintiman/master-thesis-amanda-robin/backend/learning/pbfiles"
	models = [model for model in os.listdir(model_dir) if model.endswith("h5")]
	return models


def export_to_pb(model, model_name):
	import tensorflow as tf
	from tensorflow.python.framework import graph_util
	from tensorflow.python.framework import graph_io
	from keras import backend as K
	import os
	
	nb_classes = 1  # The number of output nodes in the model
	prefix_output_node_names_of_final_network = 'output_node'
	
	K.set_learning_phase(0)
	
	pred = [None] * nb_classes
	pred_node_names = [None] * nb_classes
	for i in range(nb_classes):
		pred_node_names[i] = prefix_output_node_names_of_final_network + str(i)
		pred[i] = tf.identity(model.output[i], name=pred_node_names[i])
	print('output nodes names are: ', pred_node_names)
	
	sess = K.get_session()
	output_fld = 'pbfiles/'
	if not os.path.isdir(output_fld):
		os.mkdir(output_fld)
	output_graph_name = model_name + '.pb'
	output_graph_suffix = '_inference'
	
	constant_graph = graph_util.convert_variables_to_constants(sess, sess.graph.as_graph_def(), pred_node_names)
	graph_io.write_graph(constant_graph, output_fld, output_graph_name, as_text=False)
	print('saved the constant graph (ready for inference) at: ', os.path.join(output_fld, output_graph_name))


def print_graph_nodes(filename):
	import tensorflow as tf
	g = tf.GraphDef()
	g.ParseFromString(open(filename, 'rb').read())
	print()
	print(filename)
	print("=======================INPUT=========================")
	print([n for n in g.node if n.name.find('input') != -1])
	print("=======================OUTPUT========================")
	print([n for n in g.node if n.name.find('output') != -1])
	print("===================KERAS_LEARNING=====================")
	print([n for n in g.node if n.name.find('keras_learning_phase') != -1])
	print("======================================================")
	print()


# Credit to: https://github.com/anuradhacse/MachineLearningRepo/blob/master/regression.py
def export_model(saver, model, model_name):
	"""
	For usage in the android app we need to export the model to a protobuf file. 
	This is done by converting our Keras model into a Tensorflow model and then
	saving it as a pbtxt file. 
	:param model: The model to export
	:param model_name: Its name 
	"""
	import tensorflow as tf
	from tensorflow.python.tools import freeze_graph, optimize_for_inference_lib
	from keras import backend as K
	
	model_dir = "pbfiles/"
	input_node_names = [model.input._op.name]
	output_node_name = model.output._op.name
	tf.train.write_graph(K.get_session().graph_def, 'pbfiles',
	                     model_name + '_graph.pbtxt')

	saver.save(K.get_session(), model_dir + model_name + '.chkp')

	freeze_graph.freeze_graph(model_dir + model_name + '_graph.pbtxt', None,
	                          False, model_dir + model_name + '.chkp', output_node_name,
	                          "save/restore_all", "save/Const:0",
	                          model_dir + 'frozen_' + model_name + '.pb', True, "")

	input_graph_def = tf.GraphDef()
	with tf.gfile.Open(model_dir + 'frozen_' + model_name + '.pb', "rb") as f:
		input_graph_def.ParseFromString(f.read())

	output_graph_def = optimize_for_inference_lib.optimize_for_inference(
		input_graph_def, input_node_names, [output_node_name],
		tf.float32.as_datatype_enum)

	with tf.gfile.FastGFile(model_dir + 'opt_' + model_name + '.pb', "wb") as f:
		f.write(output_graph_def.SerializeToString())

	print("Model saved as protobuf file.")


def reshape_io(input, output, settings):
	X = input.as_matrix()
	y = output.as_matrix()[:, 0]
	X = X.reshape((X.shape[0], settings["timesteps"], settings["features"]))
	return X, y


def scale_data(data, interval=(0,1)):
	import pandas as pd
	from sklearn.preprocessing import MinMaxScaler
	
	scaler = MinMaxScaler(interval)
	scaled_values = scaler.fit_transform(data.as_matrix())
	scaled_df = pd.DataFrame(scaled_values, columns=list(data))
	print("Data has been scaled to fit in the range {}..".format(interval))
	return scaled_df, scaler


def split_data(data, ratio=0.1, concat=True):
	# TODO consider using K-fold validation
	from sklearn.model_selection import train_test_split
	import pandas as pd
	
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
	import numpy as np
	
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
	print("{removed} of {total} columns filtered..".format(removed=num_cols_before-num_cols_remaining,
	                                                       total=num_cols_before))
	return data


def load_data(filename):
	import pandas as pd
	data = pd.read_csv("../datasets/" + filename)
	data = data.drop("Class", axis=1)
	return data

