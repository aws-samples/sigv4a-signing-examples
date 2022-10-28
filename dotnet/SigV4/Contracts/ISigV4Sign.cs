using Aws.Crt.Auth;
using Aws.Crt.Http;

namespace SigV4.Contracts;

public interface ISigV4Sign
{
    IEnumerable<HttpHeader> SignRequest(string method, string endpoint, string service, Credentials credentials);
}
