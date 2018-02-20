import pandas as pd
from utils import *
import tensorflow as tf
from tensorflow.contrib import rnn
import pickle

"""
Recurrent neural network

Notes:
    Keep data values between -2 and 2. Normalize it. 
"""

def build_model(x, W, b):
    cell = rnn.BasicLSTMCell(num_hidden)
    outputs, states = rnn.static_rnn(cell, x, dtype=tf.float32)
    return tf.matmul(outputs[-1], W['out'] + b['out'])


data = pd.read_csv("../datasets/KIA_driving_data.csv")

# It's a string. Dropping it for now.
data = data.drop(["Class"], axis=1)
train, test = split_data(data)

# Training Parameters
learning_rate = 0.001
training_steps = 10000
batch_size = 128
display_step = 20

# Network Parameters
num_actions = 5 # data input
timesteps = 28 # time steps
num_hidden = 128 # hidden layer num of features
num_classes = 10 # total classes

X = tf.placeholder(tf.float32)
Y = tf.placeholder(tf.float32)

W = {'out': tf.Variable(tf.random_normal([num_hidden, num_actions]))}
b = {'out': tf.Variable(tf.random_normal([num_actions]))}

# Loss and optimizer


# model = build_model(x, W, b)


