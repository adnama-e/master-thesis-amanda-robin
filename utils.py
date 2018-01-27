from sklearn.model_selection import train_test_split
import pandas as pd
import pickle
from sklearn import svm
from scipy.stats.stats import pearsonr
import numpy as np


def split_data(data, ratio=0.1):
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
    return pd.concat(train_runs), pd.concat(test_runs)


def remove_outliers(data, outliers_fraction=0.25):
    clf = svm.OneClassSVM(nu=0.95 * outliers_fraction + 0.05, kernel="linear", gamma=0.1)
    clf = clf.fit(data)
    pickle.dump(clf, open("outlier_detector.model", "wb"))
    result = clf.predict(data)
    return result


def vars_are_linear(x, y, threshold=0.3):
    """
    Calculates the Pearson correlation for x and y
    :return: True if correlation > threshold, False otherwise.
    """
    corr = pearsonr(x, y)[0]
    return abs(corr) > threshold


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


