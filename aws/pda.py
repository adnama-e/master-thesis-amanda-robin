import json
from datetime import datetime as dt
import boto3
from boto3.dynamodb.conditions import Key
from decimal import Decimal
from operator import mul
from functools import reduce

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
	user_id = event["userId"]

	# Get the previous averages scores from the overview table
	response = overview_table.query(
		KeyConditionExpression=Key('userId').eq(user_id)
	)
	if response["Count"] > 0:
		# Get the previous scores
		prev_averages = []
	else:
		prev_averages = []

	# Do the calculations
	driving_time = duration(event["datetime"])
	average = avg_score(event["score"])
	improvement = improvement_score(average, prev_averages)

	# Upload to the overview table
	overview_table.put_item(
		Item={
			'userId': event["userId"],
			'driveId': event["driveId"],
			'ecoScore': Decimal(str(average)),
			'improvementScore': Decimal(str(improvement)),
			'duration': str(driving_time)
		}
	)
	return "Success!"


def avg_score(scores):
	"""
	Converts the driving scores into a 1 to 5 star rating for the current drive.
	:param scores: The scores from the latest drive.
	:return: The rating.
	"""
	summed_score = 0
	for score in scores:
		summed_score += float(score)
	return summed_score / len(scores)


def improvement_score(current_score, prev_scores):
	"""
	The improvement score is the product of normalizing each previous score
	with the current one.
	:return: The improvement score as a multiplier of the current score
	"""
	if len(prev_scores) == 0:
		return float(-1)

	normalized_scores = [float(score) / current_score for score in prev_scores]
	return reduce(mul, normalized_scores)


def duration(datetime):
	date_pattern = "%a, %d %b %Y %H:%M:%S"
	start_time = dt.strptime(datetime[0], date_pattern)
	end_time = dt.strptime(datetime[-1], date_pattern)
	return end_time - start_time


def total_duration(table, current):
	"""
	Fetches the duration up to this drive and add the current duration to it.
	"""
	pass


def total_distance():
	pass


if __name__ == "__main__":
	event = json.load(open("testfile.json", "r"))
	lambda_handler(event, 0)
