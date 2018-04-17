import base64
import subprocess
import os

def run_test():
	out = subprocess.check_output(['aws', 'lambda', 'invoke', '--function-name', 'PDA',
	                               '--invocation-type', 'RequestResponse',
	                               '--payload', 'file:///Users/robintiman/master-thesis-amanda-robin/aws/testfile.json',
	                               '--log-type', 'Tail',
	                               '/Users/robintiman/master-thesis-amanda-robin/aws/testoutput.txt'])[8:]
	bytes_log = base64.decodebytes(out)
	log = bytes_log.decode("utf-8")
	print(log)


def upload_handler(verbose=False):
	rm_cmd = "rm /Users/robintiman/master-thesis-amanda-robin/aws/PDA.zip"
	zip_cmd = "zip -rj -X PDA.zip /Users/robintiman/master-thesis-amanda-robin/aws/pda.py"
	upload_cmd = "aws lambda update-function-code --function-name PDA" \
	             " --region eu-west-1 " \
	             "--zip-file fileb:///Users/robintiman/master-thesis-amanda-robin/aws/PDA.zip"
	
	if not verbose:
		devnull = open(os.devnull, 'w')
		subprocess.call(rm_cmd.split(), stdout=devnull, stderr=devnull)
		subprocess.call(zip_cmd.split(), stdout=devnull, stderr=devnull)
		subprocess.call(upload_cmd.split(), stdout=devnull, stderr=devnull)
	else:
		subprocess.call(rm_cmd.split())
		subprocess.call(zip_cmd.split())
		subprocess.call(upload_cmd.split())

if __name__ == "__main__":
	upload_handler()
	run_test()