## Installing python dependencies

```pip install -r ./requirements.txt```

## Making requests using a Multi-Region Access Point (MRAP) in Amazon S3

You can access data in Amazon S3 through a Multi-Region Access Point (MRAP) using the hostname of the Multi-Region Access Point.
The hostname for the request is `<MRAP_alias>.accesspoint.s3-global.amazonaws.com`. For more details, see [Multi-Region Access Point hostnames](https://docs.aws.amazon.com/AmazonS3/latest/userguide/MultiRegionAccessPointRequests.html#MultiRegionAccessPointHostname).

### Examples Overview
- [If all you need is to get the headers with most common config.](#Example-1)  
- [If you want to define boto3 session first and pass it to SigV4ASign.](#Example-2)  
- [If you need high customization.](#Example-3)  
- [Full example to get signed headers and make an API call.](#Example-4)  

### Examples

#### Example 1
If all you need is to get the headers with most common config.

```python
from sigv4a_sign import SigV4ASign

service = 's3'
region = '*'
method = 'GET'
url = 'https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>'

headers = SigV4ASign().get_headers_basic(service, region, method, url)
print(headers)
```

#### Example 2
If you want to define boto3 session first and pass it to `SigV4ASign`.

```python
from sigv4a_sign import SigV4ASign

service = 's3'
region = '*'
method = 'GET'
url = 'https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>'

session = boto3.Session(aws_access_key_id='<aws_access_key_id>', aws_secret_access_key='<aws_secret_access_key>')

# class from ./sigv4a_sign.py
headers = SigV4ASign(session).get_headers_basic(service, region, method, url)
print(headers)
```

#### Example 3
If you need high customization.

```python
from sigv4a_sign import SigV4ASign

service = 's3'
region = '*'
aws_request_config = {
    'method': 'GET',
    'url': 'https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>',
}

# class from ./sigv4a_sign.py
headers = SigV4ASign().get_headers(service, region, aws_request_config)
print(headers)
```

#### Example 4
Full example to get signed headers and make an API call.

```python
from sigv4a_sign import SigV4ASign

# pip install requests
import requests

service = 's3'
region = '*'
method = 'GET'
url = 'https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>'

headers = SigV4ASign().get_headers_basic(service, region, method, url)
r = requests.get(url, headers=headers)
print(f'status_code: {r.status_code} \nobject text: {r.text}')
```
