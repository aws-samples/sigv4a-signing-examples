package com.sigv4aSigning;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.auth.signer.AwsSignerExecutionAttribute;
import software.amazon.awssdk.authcrt.signer.AwsCrtS3V4aSigner;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.RegionScope;

import java.net.*;
import java.util.List;
import java.util.Map;

public class SigV4ASign {

    private final AwsCredentials awsCredentials;
    public static final Integer PORT = 443;
    public static final String PROTOCOL_HTTPS = "https";

    public static SigV4ASign create() {
        return new SigV4ASign(EnvironmentVariableCredentialsProvider.create().resolveCredentials());
    }

    public static SigV4ASign create(AwsCredentials awsCredentials) {
        return new SigV4ASign(awsCredentials);
    }

    private SigV4ASign(AwsCredentials awsCredentials) {
        this.awsCredentials = awsCredentials;
    }

    public Map<String, List<String>> getHeadersBasic(String serviceName,
                                                     RegionScope regionScope,
                                                     SdkHttpMethod method,
                                                     URI url) {

        SdkHttpFullRequest request = SdkHttpFullRequest.builder()
                .method(method)
                .encodedPath(url.toString())
                .port(PORT)
                .protocol(PROTOCOL_HTTPS)
                .host(url.getHost())
                .build();

        ExecutionAttributes ea = new ExecutionAttributes();
        ea.putAttribute(AwsSignerExecutionAttribute.AWS_CREDENTIALS, awsCredentials);
        ea.putAttribute(AwsSignerExecutionAttribute.SERVICE_SIGNING_NAME, serviceName);

        AwsCrtS3V4aSigner signer = AwsCrtS3V4aSigner.builder()
                .defaultRegionScope(regionScope)
                .build();

        return signer.sign(request, ea)
                .headers();
    }

    public Map<String, List<String>> getHeaders(SdkHttpFullRequest request,
                                                ExecutionAttributes ea,
                                                RegionScope regionScope) {

        AwsCrtS3V4aSigner signer = AwsCrtS3V4aSigner.builder()
                .defaultRegionScope(regionScope)
                .build();

        return signer.sign(request, ea)
                .headers();
    }

    public AwsCredentials getAwsCredentials() {
        return awsCredentials;
    }
}
