import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

data = pd.read_csv("datasets/KIA_driving_data.csv")
# It's a string. Dropping it for now.
data = data.drop(["Class"], axis=1)

def plot_all(data):
    col_names = list(filter(lambda col: col != "Fuel_consumption", list(data)))
    # data = data[:20]
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

    print("Plottar")
    f.tight_layout()
    plt.savefig("linear_comparison.png")
    plt.show()


def plot_fuel_consumption(data):
    fuel = data["Fuel_consumption"]
    time = data["Time(s)"]
    speed = data["Vehicle_speed"]
    fuel_per_m =


plot_fuel_consumption(data)