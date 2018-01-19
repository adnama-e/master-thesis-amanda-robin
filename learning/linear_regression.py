import pandas as pd
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from scipy.stats.stats import pearsonr

"""
A linear regression model using sci-kit learn.
"""

def split_data(data):
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

    train_runs, test_runs = train_test_split(splitted_data, test_size=0.1)
    return pd.concat(train_runs), pd.concat(test_runs)


def train_regression_models(data):
    """
    Train one model for each parameter to be able to predict how an action will affect the current state.
    :param data:
    :param variables:
    :return:
    """
    models = {}
    variables = list(data)
    for i, y in enumerate(variables):
        not_linear_vars = [y]
        model = LinearRegression(fit_intercept=True, normalize=True, n_jobs=-1)
        Y = data.as_matrix([y])
        for j, x in enumerate(variables):
            if i == j:
                continue

            X = data.as_matrix([x])
            if not vars_are_linear(X, Y):
                not_linear_vars.append(x)

        if len(not_linear_vars) == len(variables):
            continue

        X_linear = data.drop(not_linear_vars, axis=1)
        models[y] = model.fit(X_linear, Y)

    return models

def get_outliers(dataset, outliers_fraction=0.25):
    # clf = svm.OneClassSVM(nu=0.95 * outliers_fraction + 0.05, kernel="rbf", gamma=0.1)
    # clf.fit(dataset)
    # result = clf.predict(dataset)
    # return result
    pass


def suggest_action(current_state):
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
    for action in actions:
        # TODO need a way of determining how the action will affect the state values.
        next_state = calc_next_state(action)

def vars_are_linear(x, y, threshold=0.3):
    """
    Calculates the Pearson correlation for x and y
    :return: True if correlation > threshold, Fales otherwise.
    """
    corr = pearsonr(x, y)[0]
    return abs(corr) > threshold

def calc_next_state(action):
    pass

def get_current_state():
    pass

def drive():
    """
    Simulates a drive using the test data.
    Using the linear regression model we will calculate the expected savings for each time-step
    :return:
    """
    while True:
        state = get_current_state()
        a = suggest_action(state)



actions = {1: "Accelerate", 2: "De-accelerate", 3: "Shift gear down",
           4: "Shift gear up", 5: "Do nothing"}

lin_vars = ['Accelerator_Pedal_value', 'Throttle_position_signal', 'Absolute_throttle_position',
            'Engine_speed', 'Flywheel_torque_(after_torque_interventions)', 'Calculated_LOAD_value', 'Flywheel_torque',
            'Torque_converter_speed', 'Torque_converter_turbine_speed_-_Unfiltered', 'Vehicle_speed']

data = pd.read_csv("../datasets/KIA_driving_data.csv")
data = data.drop("Class", axis=1)
train, test = split_data(data)
models = train_regression_models(train)
print(models)



