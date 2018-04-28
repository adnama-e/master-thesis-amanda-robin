from datetime import datetime as dt
import boto3
from decimal import Decimal
from statistics import mean, stdev


"""
Lambda handler for AWS Lambda.

Performs the post-drive analytics of BRUM.
Triggers when new driving data is uploaded to AWS DynamoDB.

Should preferably be written in Go for the final product. 
"""

OVERVIEW_TABLE = "brum-mobilehub-858586794-Overview"


def lambda_handler(event, context):
	image = event['Records'][0]['dynamodb']['NewImage']
	datetimes = [date['S'] for date in image['datetime']['L']]
	user_id = image['userId']['S']
	drive_id = image['driveId']['S']

	scores = []
	for score in image['score']['L']:
		if score['S'] != 'nan':
			scores.append(float(score['S']))

	# Fetch the database and get tables
	db = boto3.resource('dynamodb')
	overview_table = db.Table(OVERVIEW_TABLE)

	# Do the calculations
	avg = mean(scores)
	dev = stdev(scores, avg)
	imp = improvement_score(dev, avg)
	eco = eco_score(scores, avg, dev)
	dur = duration(datetimes)

	# Upload to the overview table
	overview_table.put_item(
		Item={
			'userId': user_id,
			'driveId': drive_id,
			'ecoScore': Decimal(str(eco)),
			'improvementScore': Decimal(str(imp)),
			'duration': str(dur)
		}
	)
	return "Success!"


def eco_score(scores, avg, dev):
	"""
	:param scores: 
	:return: 
	"""
	# TODO Not sure about this one.
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
