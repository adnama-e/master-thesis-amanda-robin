import base64
import subprocess
import os

"""
I wrote this script since there's obviously no way in hell I'll go through the pain of transferring the code to 
AWS manually each time I want to test it. 

Requires AWS command line interface. Install by:
	pip install awscli --upgrade --user
"""


def run_test(dir):
	out = subprocess.check_output('aws lambda invoke --function-name PDA '
	                              '--invocation-type RequestResponse '
	                              '--payload file://{dir}/testfile.json '
	                              '--log-type Tail '
	                              '{dir}/testoutput.txt'.format(dir=dir).split())[8:]
	bytes_log = base64.decodebytes(out)
	log = bytes_log.decode("utf-8")
	print(log)


def upload_handler(lang, dir, verbose=False):
	rm_cmd = "rm {}/PDA.zip".format(dir)
	zip_cmd = "zip -rj -X PDA.zip {}/{}".format(dir, lang)
	upload_cmd = "aws lambda update-function-code --function-name PDA " \
	             "--zip-file fileb://{}/PDA.zip".format(dir)


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
	dir = subprocess.check_output("pwd").decode()[:-1]
	upload_handler("PDA.go", dir)
	# upload_handler("pda.py", dir, verbose=True)
	run_test(dir)
