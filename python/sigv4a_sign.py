import boto3
from botocore import crt, awsrequest


class SigV4ASign:

    def __init__(self, boto3_session=boto3.Session()):
        self.session = boto3_session

    def get_headers(self, service, region, aws_request_config):
        sigV4A = crt.auth.CrtS3SigV4AsymAuth(self.session.get_credentials(), service, region)
        request = awsrequest.AWSRequest(**aws_request_config)
        sigV4A.add_auth(request)
        prepped = request.prepare()

        return prepped.headers

    def get_headers_basic(self, service, region, method, url):
        sigV4A = crt.auth.CrtS3SigV4AsymAuth(self.session.get_credentials(), service, region)
        request = awsrequest.AWSRequest(method=method, url=url)
        sigV4A.add_auth(request)
        prepped = request.prepare()

        return prepped.headers
