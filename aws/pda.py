import json
from datetime import datetime as dt

def lambda_handler(event, context):
	datetime = event["datetime"]
	drive_id = event["driveId"]
	scores = event["score"]
	user_id = event["userId"]
	drive_score(scores)
	total_time(datetime)
	return "really?"


def drive_score(scores):
	"""
	Converts the driving scores into a 1 to 5 star rating for the current drive.
	:param scores: The scores from the latest drive.
	:return: The rating.
	"""
	summed_score = sum([float(score) for score in scores])
	print(summed_score)
	

def improvement_score():
	pass


def total_time(datetime):
	print(datetime)
	date = datetime[0]
	start_time = dt.strptime(date, "%a %b %d %H:%M:%S %Z%z %Y")
	print(start_time)
	

def total_distance():
	pass




if __name__=="__main__":
	event = json.load(open("testfile.json", "r"))
	lambda_handler(event, 0)

