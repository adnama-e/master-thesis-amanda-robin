import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from pandas import Series
from pandas.plotting import lag_plot, autocorrelation_plot
from utils import *

data = pd.read_csv("../datasets/KIA_driving_data.csv", header=0)
# It's a string. Dropping it for now.
data = data.drop(["Class"], axis=1)

def plot_all(data):
    col_names = list(filter(lambda col: col != "Fuel_consumption", list(data)))
    # We use 5 variables on each row.
    vars_on_row = 5
    nbr_rows = (vars_on_row + len(col_names) - 1) // vars_on_row
    f, axes = plt.subplots(nrows=nbr_rows, ncols=vars_on_row, figsize=(20,20), dpi=100)
    y = "Fuel_consumption"
    for i, ax in enumerate(axes):
        x = col_names[i*vars_on_row : vars_on_row * (i+1)]
        for j, col in enumerate(x):
            sns.regplot(col, y, data, scatter=True, fit_reg=True, ax=ax[j],
                        scatter_kws={'s':5, 'color':'b', 'alpha':0.5})

    f.tight_layout()
    plt.savefig("scaled_comparison.png")
    plt.show()


def visualize_drive(data, lag=1):
    data = split_data(data, 0)[0]
    cols = list(data[0])
    for i, drive in enumerate(data):
        if i > 0:
            break
        for j, col in enumerate(cols):
            Y = drive[col]
            y1 = Y[:-lag]
            y2 = Y[lag:]
            plt.scatter(y1, y2)
            plt.xlabel("{}(t)".format(col))
            plt.ylabel("{}(t + {})".format(col, lag))
            plt.show()


plot_all(data)