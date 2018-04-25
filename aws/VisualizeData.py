import json
from matplotlib import pyplot as plt
from PDA import *
from statistics import *


def visualize_event(event):
	scores = [float(score) for score in event["score"]]
	avg = mean(scores)
	deviation = stdev(scores, avg)
	x = range(len(scores))
	plt.plot(x, scores)
	# The area and placement of the filled area is a measurement of the driving quality.
	plt.fill_between(x, avg - deviation, avg + deviation, alpha=0.2)
	plt.show()


if __name__ == "__main__":
	for i in range(1, 4):
		event = json.load(open("devdata{}.json".format(i), "r"))
		visualize_event(event)