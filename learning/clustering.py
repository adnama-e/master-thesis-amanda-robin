from sklearn.cluster import Birch
import argparse
import pandas as pd
from utils import split_data, filter_data, scale_data
import matplotlib.pyplot as plt
from matplotlib import colors


parser = argparse.ArgumentParser()
parser.add_argument("-m", default='birch', dest="method")
args = parser.parse_args()

data = pd.read_csv("../datasets/KIA_driving_data.csv")

if args.method == "birch":
    model = Birch()
else:
    model = None
    print("No model selected. Exiting..")
    exit(1)

data = data.drop("Class", axis=1)
time = data["Time(s)"]
data = data.drop("Time(s)", axis=1)
data = filter_data(data, filter_discrete=True)
data = scale_data(data)[0]
data["Time(s)"] = time
train_drives, test_drives = split_data(data, concat=False)

for i, drive in enumerate(train_drives):
    clrs = ['g', 'r', 'c', 'm', 'y', 'k', 'w']
    assigned_clrs = {}

    # Train and predict
    time = drive["Time(s)"].as_matrix()
    drive = drive.drop("Time(s)", axis=1)
    y_pred = model.fit_predict(drive.as_matrix())
    fuel_cons = drive["Fuel_consumption"].as_matrix()

    # Plot
    f = plt.figure(i)

    for j, cls in enumerate(y_pred):
        if cls in assigned_clrs:
            assigned_clrs[cls][0].append(j+1)
            assigned_clrs[cls][1].append(fuel_cons[j])
        else:
            clr = clrs.pop()
            assigned_clrs[cls] = ([j+1], [fuel_cons[j]], clr)

    for _, vals in assigned_clrs.items():
        time, fuel, clr = vals
        plt.scatter(time, fuel, c=clr, alpha=0.3)
    plt.show()
    plt.waitforbuttonpress()
