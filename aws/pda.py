import json
from datetime import datetime as dt
import boto3
from operator import mul
from functools import reduce

"""
Lambda handler for AWS Lambda.

Performs the post-drive analysis of BRUM.
Triggers when new driving data is uploaded to AWS DynamoDB.
"""

BRUM_TABLE = "brum-mobilehub-858586794-DriveScores"


def lambda_handler(event, context):
	# Fetch the database and get tables
	client = boto3.client('dynamodb')
	
	driving_time = duration(event["datetime"])
	average = avg_score(event["score"])
	user_id = event["userId"]
	
	# Get the previous averages scores from the overview table
	item = client.get_item(
		TableName=BRUM_TABLE,
		Key={
			# TODO don't hard code userId and driveId.
			'userId': {
				'S': user_id
			}
		}
	)
	# TODO look up if this is really necessary. Seems a bit redundant.
	prev_averages = [float(score['S']) for score in item['Item']['score']['L']]
	
	# Do the calculations
	improvement = improvement_score(average, prev_averages)
	drive_id = event["driveId"]
	user_id = event["userId"]
	return "Success!"


def upload(table_name, item):
	table = boto3.client(table_name)
	response = table.put_item(
		TableName=table_name,
		Item=item
	)
	print(response)


def download(table):
	pass


def avg_score(scores):
	"""
	Converts the driving scores into a 1 to 5 star rating for the current drive.
	:param scores: The scores from the latest drive.
	:return: The rating.
	"""
	summed_score = sum([float(score) for score in scores])
	return summed_score / len(scores)


def improvement_score(current_score, prev_scores):
	"""
	The improvement score is the product of normalizing each previous score
	with the current one.
	:return: The improvement score as a multiplier of the current score
	"""
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
