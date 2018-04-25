import json
from datetime import datetime as dt
import boto3
from boto3.dynamodb.conditions import Key
from decimal import Decimal
from statistics import pvariance, harmonic_mean, mean, variance
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
	
	process(event)
	return
	
	# Do the calculations
	scores = list(map(float, event["score"]))
	driving_time = duration(event["datetime"])
	rating = rating_score(scores)
	improvement = improvement_score(rating, prev_averages)

	# Upload to the overview table
	overview_table.put_item(
		Item={
			'userId': event["userId"],
			'driveId': event["driveId"],
			'ecoScore': Decimal(str(rating)),
			'improvementScore': Decimal(str(improvement)),
			'duration': str(driving_time)
		}
	)
	return "Success!"


def process(event):
	scores = list(map(lambda x: float(x) * 100, event["score"]))
	
	# Harmonic mean
	hmean = mean(scores)
	# Pvariance
	pvar = variance(scores, hmean)
	pvar_zero = variance(scores, 0)
	print(hmean, pvar, pvar_zero, var)
	

def eco_score(scores):
	"""
	Converts the driving scores into a 1 to 5 star rating for the current drive.
	:param scores: The scores from the latest drive.
	:return: The rating.
	"""
	pvar = pvariance(scores)
	return


def improvement_score(current_score, prev_scores):
	"""
	The improvement score is the product of the normalized previous score
	in regard to the current one.
	:return: The improvement score as a multiplier of the current score
	"""
	if len(prev_scores) == 0:
		return 1
	
	norm = 1
	for score in prev_scores:
		norm *= float(score) / current_score
	return norm


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
	for i in range(1,4):
		event = json.load(open("devdata{}.json".format(i), "r"))
		lambda_handler(event, 0)
