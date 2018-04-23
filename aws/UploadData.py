import boto3
import pandas as pd
from datetime import datetime as dt
from datetime import timedelta

"""
Script for uploading scores to DynamoDB
"""

DRIVESCORES_TABLE = "brum-mobilehub-858586794-DriveScores"

data = pd.read_csv("../backend/datasets/driving_score.csv")
table = boto3.resource("dynamodb").Table(DRIVESCORES_TABLE)
datetimes, scores = [], []

prev_id = 1
date_pattern = "%a, %d %b %Y %H:%M:%S"
start_time = dt.now()
for i in range(data.shape[0]):
	row = data.iloc[[i]]
	drive_id = str(row['DriveID'].values[0])
	score = str(row["Drive_score"].values[0])
	time = start_time + timedelta(seconds=i+1)
	datetimes.append(time.strftime(date_pattern))
	scores.append(score)
	if drive_id != prev_id:
		table.put_item(
			Item={
				'datetime': datetimes,
				'driveId': drive_id,
				'score': scores,
				'userId': 'devdata'
			}
		)
		prev_id = drive_id
		print("UPLOAD. Drive ID: {}".format(drive_id))