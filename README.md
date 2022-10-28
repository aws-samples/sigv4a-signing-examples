# Welcome to sigv4a-signing-examples project!

# Why this project exists

AWS is rolling out an extension to SigV4 called Signature Version 4A (SigV4A).
This extension enables signatures that are valid in more than one AWS Region.
This is required for signing multi-Region API requests, for example with [Amazon S3 Multi-Region Access Points](https://docs.aws.amazon.com/AmazonS3/latest/userguide/MultiRegionAccessPoints.html).

This repository servers to provide examples how to sign requests with SigV4A
to make Rest API requests to AWS services
with common languages such as Python, Node.js, Java, C#, Go and Rust.

## Examples

- [Python](./python)
- [Node.js](./node-js)
- [Java](./java)
- [C#](./dotnet)
- Go
- Rust
- C++

## Security

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.

## License

This library is licensed under the MIT-0 License. See the LICENSE file.
