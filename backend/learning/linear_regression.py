import pandas as pd
from sklearn.linear_model import LinearRegression
import numpy as np
import pickle
from utils import *

"""
A linear regression model using sci-kit learn.
"""


def train_regression_models(data, save_models=False, retrieve_models=False):
    """
    Train one model for each parameter to be able to predict how an action will affect the current state.
    :param data:
    :param variables:
    :return:
    """
    # TODO consider using SVM
    if retrieve_models:
        models = pickle.load(open("linear_models.data", "rb"))
        relations = pickle.load(open("linear_relations.data", "rb"))
        return models, relations

    models = {}
    variables = list(data)
    relations = {}
    for i, y in enumerate(variables):
        not_linear_vars = [y]
        relations[y] = []
        model = LinearRegression(fit_intercept=True, normalize=True, n_jobs=-1)
        Y = data.as_matrix([y])
        for j, x in enumerate(variables):
            if i == j:
                continue

            X = data.as_matrix([x])

        if vars_are_linear(X, Y):
            relations[y].append(x)
        else:
            not_linear_vars.append(x)

        if len(not_linear_vars) == len(variables):
            continue

        X_linear = data.drop(not_linear_vars, axis=1)
        models[y] = model.fit(X_linear, Y)

    if save_models:
        pickle.dump(models, open("linear_models.data", "wb"))
        pickle.dump(relations, open("linear_relations.data", "wb"))

    return models, relations


def suggest_action(current_state, relations, data, timestep, models, header):
    """
    Given the current state; calculate what action that minimizes the fuel consumption.
    Available actions:
    * Accelerate
    * De-accelerate
    * Shift gear up
    * Shift gear down
    * Do nothing
    :return: The action
    """
    fuel_gains = []
    speed_limit = calc_road_limit(data, timestep)
    for action, affected_vars in actions.items():

        # What variables are affected by this action?
        for aff_var in affected_vars:
            # How much are the variables affected?
            # Pick the value with the lowest resulting fuel consumption.
            future_state = current_state



def perform_action(action):
    pass

def drive(data, relations, models):
    """
    Simulates a drive using the test data.
    Using the linear regression model we will calculate the expected savings for each time-step
    :return:
    """
    timestep = 1
    header = {index: col for index, col in enumerate(list(data))}
    for state in data.values:
        # The first row is the header. We skip that.
        a = suggest_action(state, relations, data, timestep, models, header)
        gain = perform_action(a)
        timestep += 1


actions = {"Accelerate": ['Accelerator_Pedal_value', 'Throttle_position_signal', 'Absolute_throttle_position'],
           "De-accelerate": ['Accelerator_Pedal_value', 'Throttle_position_signal', 'Absolute_throttle_position'],
           "Shift gear down": ['Current_gear'],
           "Shift gear up": ['Current_gear'],
           "Do nothing": []}

lin_vars = ['Accelerator_Pedal_value', 'Throttle_position_signal', 'Absolute_throttle_position',
            'Engine_speed', 'Flywheel_torque_(after_torque_interventions)', 'Calculated_LOAD_value', 'Flywheel_torque',
            'Torque_converter_speed', 'Torque_converter_turbine_speed_-_Unfiltered', 'Vehicle_speed']

data = pd.read_csv("../datasets/KIA_driving_data.csv")
data = data.drop("Class", axis=1)
train, test = split_data(data)

# models, relations = train_regression_models(train, save_models=True)
models, relations = train_regression_models(train, retrieve_models=True)
drive(test, relations, models)


