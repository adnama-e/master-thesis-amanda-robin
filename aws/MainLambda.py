import base64
import subprocess
from sys import stdout
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
	                              '{dir}/testoutput.txt'.format(dir=dir).split())
	out = out[out.find('\t'.encode()) + 1:]
	bytes_log = base64.decodebytes(out)
	log = bytes_log.decode()
	print(log)


def upload_handler(lang, dir, verbose):
	rm_cmd = "rm {}/PDA.zip".format(dir)
	zip_cmd = "zip -rj -X PDA.zip {}/{}".format(dir, lang)
	upload_cmd = "aws lambda update-function-code --function-name PDA " \
	             "--zip-file fileb://{}/PDA.zip".format(dir)
	
	if verbose:
		output_dir = open(stdout, "w")
	else:
		output_dir = open(os.devnull, 'w')

	subprocess.call(rm_cmd.split(), stdout=output_dir, stderr=output_dir)
	subprocess.call(zip_cmd.split(), stdout=output_dir, stderr=output_dir)
	subprocess.call(upload_cmd.split(), stdout=output_dir, stderr=output_dir)

if __name__ == "__main__":
	dir = subprocess.check_output("pwd").decode()[:-1]  # There's a newline at the end that we don't want
	# upload_handler("PDA.go", dir)
	upload_handler("PDA.py", dir, verbose=False)
	run_test(dir)