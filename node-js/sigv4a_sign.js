const crt = require("aws-crt");
const {HttpRequest} = require("aws-crt/dist/native/http");

function sigV4ASign(method, endpoint, config = crt.auth.AwsSigningConfig) {
    const host = new URL(endpoint).host;
    const request = new HttpRequest(method, endpoint);
    request.headers.add('host', host);

    crt.auth.aws_sign_request(request, config);
    return request.headers;
}

function sigV4ASignBasic(method, endpoint, service) {
    const host = new URL(endpoint).host;
    const request = new HttpRequest(method, endpoint);
    request.headers.add('host', host);

    const config = {
        service: service,
        region: "*",
        algorithm: crt.auth.AwsSigningAlgorithm.SigV4Asymmetric,
        signature_type: crt.auth.AwsSignatureType.HttpRequestViaHeaders,
        signed_body_header: crt.auth.AwsSignedBodyHeaderType.XAmzContentSha256,
        provider: crt.auth.AwsCredentialsProvider.newDefault()
    };

    crt.auth.aws_sign_request(request, config);
    return request.headers;
}

module.exports = {
    sigV4ASign,
    sigV4ASignBasic,
}
