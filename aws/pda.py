def lambda_handler(event, context):
	dates = event["datetime"]
	drive_id = event["driveId"]
	scores = event["score"]
	user_id = event["userId"]
	return "really?"
