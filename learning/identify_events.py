import pandas as pd
import numpy as np
from sklearn.neighbors import LocalOutlierFactor
from sklearn.ensemble import IsolationForest
import matplotlib.pyplot as plt
from sklearn.preprocessing import MinMaxScaler
from sklearn import svm
from utils import split_data


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
    return scaled_df, scaler

def filter_outliers(pred):
    # Filter on outliers
    inliers_idx = []
    outliers_idx = []
    for i, cls in enumerate(pred):
        if cls == -1:
            outliers_idx.append(i)
        else:
            inliers_idx.append(i)
    return inliers_idx, outliers_idx

def plot(outliers_idx, inliers_idx):
    y_col = "Fuel_consumption"
    for i, col in enumerate(cols):
        f = plt.figure(i)
        plt.title(col)
        outliers = data.iloc[outliers_idx]
        inliers = data.iloc[inliers_idx]
        a = plt.scatter(inliers[col], inliers[y_col], alpha=0.1)
        b = plt.scatter(outliers[col], outliers[y_col], alpha=0.9)
        plt.axis('tight')
        plt.xlim((-1.5, 1.5))
        plt.ylim((-1.5, 1.5))
        plt.legend([a, b],
                   ["normal observations",
                    "abnormal observations"],
                   loc="upper left")
        f.show()
        plt.waitforbuttonpress()

cols = ["Accelerator_Pedal_value", "Current_Gear",
        "Absolute_throttle_position", "Throttle_position_signal"]

# Read and filter data
data = pd.read_csv("../datasets/KIA_driving_data.csv")
# data = data.sample(1000)
data = filter_data(data)
time = data["Time(s)"]
data = data.drop("Time(s)", axis=1)
data = scale_data(data)[0]
data["Time(s)"] = time
splitted_data = split_data(data, concat=False)[0]
train, test = (splitted_data[0], splitted_data[1])
train = train.drop(["PathOrder", "Time(s)"], axis=1)
test = test.drop(["PathOrder", "Time(s)"], axis=1)

# Create classifier
# clf = LocalOutlierFactor(n_neighbors=1, n_jobs=-1, algorithm="ball_tree", metric="l2")
# clf = IsolationForest(max_samples=100, n_jobs=-1, contamination=0.05)
clf = svm.OneClassSVM(nu=0.1, kernel="rbf", gamma=0.1)

# Fit and predict states
# y_pred = clf.fit_predict(data.as_matrix())
clf.fit(train)
y_pred_train = clf.predict(train)
y_pred_test = clf.predict(test)
# clf.fit(data.as_matrix())
# y_pred = clf.predict(data.as_matrix())

# plot the level sets of the decision function
# xx, yy = np.meshgrid(np.linspace(-1, 1, data.shape[1]), np.linspace(-1, 1, data.shape[1]))
# Z = clf._decision_function(np.c_[xx.ravel(), yy.ravel()])
# Z = Z.reshape(xx.shape)
# plt.contourf(xx, yy, Z, cmap=plt.cm.Blues_r)
train_outliers, train_inliers = filter_outliers(y_pred_train)
test_outliers, test_inliers = filter_outliers(y_pred_test)


plot(train_outliers, train_inliers)
plot(test_outliers, train_outliers)


