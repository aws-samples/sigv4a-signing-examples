## Installing Node.js dependencies

```npm install```

## Making requests using a Multi-Region Access Point (MRAP) in Amazon S3

You can access data in Amazon S3 through a Multi-Region Access Point (MRAP) using the hostname of the Multi-Region Access Point.
The hostname for the request is `<MRAP_alias>.accesspoint.s3-global.amazonaws.com`. For more details, see [Multi-Region Access Point hostnames](https://docs.aws.amazon.com/AmazonS3/latest/userguide/MultiRegionAccessPointRequests.html#MultiRegionAccessPointHostname).

### Examples Overview
- [If all you need is to get the headers with most common config.](#Example-1)
- [If you need high customization.](#Example-2)
- [Full example to get signed headers and make an API call.](#Example-3)

### Examples

#### Example 1
If all you need is to get the headers with most common config, use sigV4ASignBasic(method, endpoint, service).

```js
const { sigV4ASignBasic } = require('./sigv4a_sign')

var method = 'GET';
var endpoint = 'https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>';
var service = 's3';

// function from ./sigv4a_sign.js
var headers = sigV4ASignBasic(method, endpoint, service);
console.log(headers._flatten());
```

#### Example 2
If you need high customization, use sigV4ASign(method, endpoint, config = crt.auth.AwsSigningConfig).

```js
const { sigV4ASignBasic } = require('./sigv4a_sign')

var config = {
    service: 's3',
    region: '*',
    algorithm: crt.auth.AwsSigningAlgorithm.SigV4Asymmetric,
    signature_type: crt.auth.AwsSignatureType.HttpRequestViaHeaders,
    signed_body_header: crt.auth.AwsSignedBodyHeaderType.XAmzContentSha256,
    provider: crt.auth.AwsCredentialsProvider.newDefault()
};
var method = 'GET';
var endpoint = 'https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>';

// function from ./sigv4a_sign.js
var headers = sigV4ASign(method, endpoint, config);
console.log(headers._flatten());
```

#### Example 3
Full example to get signed headers and make an API call.

```js
const { sigV4ASignBasic } = require('./sigv4a_sign')
const https = require('https');

var method = 'GET';
var endpoint = 'https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>';
var service = 's3';

// function from ./sigv4a_sign.js
var headers = sigV4ASignBasic(method, endpoint, service);

const options = {
    hostname: headers.get('host'),
    path: new URL(endpoint).pathname,
    method: 'GET',
    headers: headers._flatten()
}

const req = https.request(options, res => {
    res.on('data', d => {
        process.stdout.write(d);
    })
}).on('error', error => {
    console.error(error)
}).end()
```
