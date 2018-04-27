import json
from datetime import datetime as dt
import boto3
from boto3.dynamodb.conditions import Key
from decimal import Decimal
from statistics import variance, mean, stdev
from functools import reduce
from math import sqrt


"""
Lambda handler for AWS Lambda.

Performs the post-drive analytics of BRUM.
Triggers when new driving data is uploaded to AWS DynamoDB.

Should preferably be written in Go for the final product. 
"""

DRIVESCORES_TABLE = "brum-mobilehub-858586794-DriveScores"
OVERVIEW_TABLE = "brum-mobilehub-858586794-Overview"


def lambda_handler(event, context):
	# Fetch the database and get tables
	db = boto3.resource('dynamodb')
	overview_table = db.Table(OVERVIEW_TABLE)
	user_id = event['userId']

	# Get the previous averages scores from the overview table
	response = overview_table.query(
		KeyConditionExpression=Key('userId').eq(user_id)
	)
	if response["Count"] > 0:
		# Get the previous scores
		prev_averages = [float(score['improvementScore']) for score in response['Items']]
	else:
		prev_averages = []
	
	scores = list(map(float, event["score"]))
	avg = mean(scores)
	dev = stdev(scores, avg)

	imp = improvement_score(dev, avg)
	eco = eco_score(scores, avg, dev)
	dur = duration(event["datetime"])

	# Upload to the overview table
	overview_table.put_item(
		Item={
			'userId': event["userId"],
			'driveId': event["driveId"],
			'ecoScore': Decimal(str(eco)),
			'improvementScore': Decimal(str(imp)),
			'duration': str(dur)
		}
	)
	return "Success!"


def eco_score(scores, avg, dev):
	"""
	Defined as the smoothness of the drive.
	:param scores: 
	:return: 
	"""
	# TODO I'm guessing that this score is greatly affected by the fact that the car seems to be standing still for
	# TODO long periods of time. Filter out these instances directly in the app.
	num_outside = 0
	for score in scores:
		if score > avg + dev or score < avg - dev:
			num_outside += 1
	return 1 - num_outside / len(scores)


def improvement_score(dev, avg):
	"""
	Ranges between 0 and 1 and a value over 0.5 means improvement.
	:return: The improvement score
	"""
	deviation_area = dev * 2
	improvement_area = avg + dev
	return improvement_area / deviation_area


def duration(datetime):
	"""
	Total duration in time.
	:param datetime: The collected datetimes instances from running the app. 
	:return: The duration between the first instance and the last. 
	"""
	date_pattern = "%a, %d %b %Y %H:%M:%S"
	start_time = dt.strptime(datetime[0], date_pattern)
	end_time = dt.strptime(datetime[-1], date_pattern)
	return end_time - start_time


if __name__ == "__main__":
	for i in range(1,4):
		event = json.load(open("devdata{}.json".format(i), "r"))
		lambda_handler(event, 0)
