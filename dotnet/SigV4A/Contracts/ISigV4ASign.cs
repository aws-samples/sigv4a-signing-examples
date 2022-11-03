using Aws.Crt.Auth;
using Aws.Crt.Http;

namespace SigV4A.Contracts;

public interface ISigV4ASign
{
    IEnumerable<HttpHeader> SignRequest(string method, string endpoint, string service, Credentials credentials);
}
