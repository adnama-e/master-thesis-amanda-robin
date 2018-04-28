from matplotlib import pyplot as plt
from statistics import *
from utils import *
import json

def visualize_event(event):
	scores = convert_scores_to_float(event["score"])
	avg = mean(scores)
	deviation = stdev(scores, avg)
	x = range(len(scores))
	plt.plot(x, scores)
	# The area and placement of the filled area is a measurement of the driving quality.
	plt.fill_between(x, avg - deviation, 0, alpha=0.2, facecolor='blue')
	plt.fill_between(x, 0, avg + deviation, alpha=0.2, facecolor='red')
	plt.show()


if __name__ == "__main__":
	for i in range(1, 4):
		event = json.load(open("devdata{}.json".format(i), "r"))
		visualize_event(event)