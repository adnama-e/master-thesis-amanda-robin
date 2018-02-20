import tensorflow as tf
import pandas as pd
from utils import *
import tensorflow.contrib.rnn as rnn

"""
An implementation using LSTM to predict future fuel consumption based. 
"""

def read_data():
    data = pd.read_csv("../datasets/KIA_driving_data.csv")
    data = data.drop("Class", axis=1)
    train, test = split_data(data)
    return train, test

def build_model(data, settings):
    num_units = settings["num_units"]
    num_layers = settings["num_layers"]
    batch_size = settings["batch_size"]

    stacked_lstm = rnn.MultiRNNCell([rnn.BasicLSTMCell(num_units) for _ in range(num_layers)])

    initial_state = state = stacked_lstm.zero_state(batch_size, tf.float32)
    for i in range(batch_size):
        # The value of state is updated after processing each batch of words.

        output, state = stacked_lstm(words[:, i], state)

        # The rest of the code.
        # ...

    final_state = state


def predict():
    pass


settings = {
    "num_units" : 30,
    "batch_size" : 10,
    "num_layers" : 3
}

train, test = read_data()
model = build_model(train, settings)
