## .NET

### Making requests using a Multi-Region Access Point (MRAP) in Amazon S3

You can access data in Amazon S3 through a Multi-Region Access Point (MRAP) using the hostname of the Multi-Region Access Point. The hostname for the request is <MRAP_alias>.accesspoint.s3-global.amazonaws.com. For more details, see Multi-Region Access Point hostnames.

#### Build solution

`dotnet build`

#### Run the sample solution providing an S3 Multi-region-access-point argument

`cd TestHarness && dotnet run <MY-AP-URL>`

#### Full example to get signed headers and make an API call.

```c#
using Amazon.Runtime.CredentialManagement;
using Aws.Crt.Auth;
using SigV4A;
using SigV4A.Contracts;
using Microsoft.Extensions.DependencyInjection;

var services = new ServiceCollection();
services.AddTransient<ISigV4ASign, SigV4ASign>();
var provider = services.BuildServiceProvider();
var signingService = provider.GetRequiredService<ISigV4ASign>();

var method = HttpMethod.Get;
var endpoint = "https://<MRAP_ALIAS>.mrap.accesspoint.s3-global.amazonaws.com/<s3-object-key>";
var service = "s3";
var headers = signingService.SignRequest(method.Method, endpoint, service, GetCredentials());

using var client = new HttpClient();
client.DefaultRequestHeaders.Clear();
foreach (var header in headers)
{
    client.DefaultRequestHeaders.TryAddWithoutValidation(header.Name, header.Value);
}
var signedRequest = client.GetAsync(endpoint);

var response = await signedRequest;
Console.WriteLine($"Response: {response}");
Console.WriteLine($"Response Body: {await response.Content.ReadAsStringAsync()}");

Credentials GetCredentials()
{
    var chain = new CredentialProfileStoreChain();
    var result = chain.TryGetAWSCredentials("default", out var credentials);
    var creds = credentials.GetCredentials();

    return  new Credentials(creds.AccessKey, creds.SecretKey, creds.Token);
}
```
