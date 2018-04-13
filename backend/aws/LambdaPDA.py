import json
# import pandas as pd
# from utils import *
# import matplotlib.pyplot as plt

# TODO add dates and coordinates to data
def lambda_handler(event, context):
	# print("Received event: " + json.dumps(event, indent=2))
	for record in event['Records']:
		print(record['eventID'])
		print(record['eventName'])
		print("DynamoDB Record: " + json.dumps(record['dynamodb'], indent=2))
	return 'Successfully processed {} records.'.format(len(event['Records']))


def smooth_score(drive):
	"""
	How smooth is your driving?
	:return: Smooth score (SS)
	"""
	speed_data = drive["Vehicle_speed"].as_matrix()
	# plt.plot(range(len(speed_data)), speed_data)
	# plt.show()
	

def improvement_score(drive):
	"""
	Have your driving improved?
	:return: Improvement score (IS)
	"""
	score = drive.sum()
	return score


def eco_score(drive):
	"""
	How economical is your driving?
	:return: Eco score (ES)
	"""
	pass


# driving_data = pd.read_csv("../datasets/KIA_driving_data.csv")
# train, test = split_data(driving_data, concat=False)
# driving_data = train + test
#
# driving_scores = pd.read_csv("../datasets/driving_score.csv")
# for driveID in range(1,4):
# 	drive = driving_scores.where(driving_scores["DriveID"] == driveID)
# 	print(drive)
#
# smooth_score(driving_data[0])
