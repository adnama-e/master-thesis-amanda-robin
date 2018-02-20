from sklearn.model_selection import train_test_split
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
import numpy as np


def scale_data(data):
    scaler = MinMaxScaler((-1, 1))
    scaled_values = scaler.fit_transform(data.as_matrix())
    scaled_df = pd.DataFrame(scaled_values, columns=list(data))
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


