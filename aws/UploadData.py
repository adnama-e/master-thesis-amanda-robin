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

prev_id = '1'
date_pattern = "%a, %d %b %Y %H:%M:%S"
start_time = dt.now()
num_rows = data.shape[0]
for i in range(1, num_rows):
	row = data.iloc[[i]]
	drive_id = str(row['DriveID'].values[0])
	score = str(row["Drive_score"].values[0])
	time = start_time + timedelta(seconds=i+1)
	datetimes.append(time.strftime(date_pattern))
	scores.append(score)
	if drive_id != prev_id or i == num_rows - 1:
		table.put_item(
			Item={
				'datetime': datetimes,
				'driveId': prev_id,
				'score': scores,
				'userId': 'devdata'
			}
		)
		print("UPLOAD. Drive ID: {}".format(prev_id))
		prev_id = drive_id
