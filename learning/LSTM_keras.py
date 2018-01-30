import pandas as pd
from matplotlib import pyplot
from utils import *
import numpy as np
from sklearn.metrics import mean_squared_error
from sklearn.preprocessing import MinMaxScaler
from keras.models import Sequential
from keras.layers import Dense, LSTM
from math import sqrt
from matplotlib import pyplot
from numpy import array


data = pd.read_csv("../datasets/KIA_driving_data.csv")
data = data.drop("Class", axis=1)

settings = {"batch_size": 1,
            "lag": 1,
            "units": 1,
            "num_epochs": 100}

# The training data need to be transformed into a 3-D array [samples, timesteps, features]


def filter_data(data, threshold=0.2):
    correlations = data.corr(method="kendall")["Fuel_consumption"]
    cols = list(data)
    for i, corr in enumerate(correlations):
        if np.isnan(corr) or np.abs(corr) < threshold:
            if cols[i] != "Time(s)":
                data = data.drop(cols[i], axis=1)

    return data


def scale_data(data):
    scaler = MinMaxScaler((-1, 1))
    scaled_values = scaler.fit_transform(data.as_matrix())
    scaled_df = pd.DataFrame(scaled_values, columns=list(data))
    return scaled_df


def build_model(data, settings):
    y = data["Fuel_consumption"].as_matrix()
    X = data.drop("Fuel_consumption", axis=1).as_matrix()
    X = X.reshape(X.shape[0], 1, X.shape[1])
    model = Sequential()
    model.add(LSTM(settings["units"],
                   batch_input_shape=(settings["batch_size"], X.shape[1], X.shape[2]), stateful=True))
    model.add(Dense(1))
    model.compile(loss="mean_squared_error", optimizer="Adam")

    for i in range(settings["num_epochs"]):
        model.fit(X, y, epochs=1, batch_size=settings["batch_size"], verbose=1, shuffle=False)
        model.reset_states()

    return model


data = filter_data(data)
train, test = split_data(data)
train = train.drop("Time(s)", axis=1)
test = test.drop("Time(s)", axis=1)
train = scale_data(train)
test = scale_data(test)
model = build_model(train, settings)
model.save("lstm_model.h5")
