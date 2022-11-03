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
var endpoint = args[0];
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
    chain.TryGetAWSCredentials("default", out var credentials);
    var creds = credentials.GetCredentials();

    return  new Credentials(creds.AccessKey, creds.SecretKey, creds.Token);
}