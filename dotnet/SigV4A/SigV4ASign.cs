using Aws.Crt.Auth;
using Aws.Crt.Http;
using SigV4A.Contracts;

namespace SigV4A;

public class SigV4ASign : ISigV4ASign
{
    public IEnumerable<HttpHeader> SignRequest(string method, string endpoint, string service, Credentials credentials)
    {
        var endpointUri = new Uri(endpoint);
        var request = new HttpRequest
        {
            Method = method,
            Uri = endpointUri.PathAndQuery,
            Headers = new [] { new HttpHeader("host", endpointUri.Host) }
        };

        var config = new AwsSigningConfig
        {
            Service = service,
            Region = "*",
            Algorithm = AwsSigningAlgorithm.SIGV4A,
            SignatureType = AwsSignatureType.HTTP_REQUEST_VIA_HEADERS,
            SignedBodyHeader = AwsSignedBodyHeaderType.X_AMZ_CONTENT_SHA256,
            Credentials = credentials,
        };

        var result = AwsSigner.SignHttpRequest(request, config);
        AwsSigner.CrtSigningResult signingResult = result.Get();
        HttpRequest signedRequest = signingResult.SignedRequest;

        return signedRequest.Headers;
    }
 }