## Gradle

To install gradle, please follow [gradle installation](https://gradle.org/install/).

Tested `build.gradle` with Gradle version: `7.1`

Install dependencies:

```shell
gradle build
```

Run with gradle CLI:

```shell
gradle -PmainClass=com.sigv4aSigning.SigV4ASign run
```

Compile fatJar (with all dependencies):

```shell
# After build completes, jar is located in ./build/libs.
gradle fatJar
```

## Making requests using a Multi-Region Access Point (MRAP) in Amazon S3

You can access data in Amazon S3 through a Multi-Region Access Point (MRAP) using the hostname of the Multi-Region
Access Point. The hostname for the request is `<MRAP_alias>.accesspoint.s3-global.amazonaws.com`. For more details,
see [Multi-Region Access Point hostnames](https://docs.aws.amazon.com/AmazonS3/latest/userguide/MultiRegionAccessPointRequests.html#MultiRegionAccessPointHostname)
.

### Examples Overview
- [If all you need is to get the headers with most common config with AWS credentials from environment variables.](#Example-1)
- [If all you need is to get the headers with most common config and explicitly passing accessKeyId and secretAccessKey.](#Example-2)
- [If you need high customization.](#Example-3)
- [Full example to get signed headers and make an API call.](#Example-4)


### Examples

#### Example 1
If all you need is to get the headers with most common config with AWS credentials from environment variables.

```java
import com.sigv4aSigning.SigV4ASign;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.RegionScope;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class MyClass {
    public static void main(String[] args) {
        SigV4ASign sigV4ASign = SigV4ASign.create();

        String url = "https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>";
        URI uri = URI.create(url);
        String serviceName = "s3";
        RegionScope globalRegion = RegionScope.GLOBAL;
        SdkHttpMethod method = SdkHttpMethod.GET;

        Map<String, List<String>> headers = sigV4ASign.getHeadersBasic(serviceName, globalRegion, method, uri);
        System.out.println(headers);
    }
}
```

#### Example 2
If all you need is to get the headers with most common config and explicitly passing `accessKeyId` and `secretAccessKey`.

```java
import com.sigv4aSigning.SigV4ASign;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.RegionScope;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class MyClass {
    public static void main(String[] args) {
        String accessKeyId = "<ACCESS-KEY-ID-HERE>";
        String secretAccessKey = "<SECRET-ACCESS-KEY-HERE>";

        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        SigV4ASign sigV4ASign = SigV4ASign.create(awsCredentials);

        String url = "https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>";
        URI uri = URI.create(url);
        String serviceName = "s3";
        RegionScope globalRegion = RegionScope.GLOBAL;
        SdkHttpMethod method = SdkHttpMethod.GET;

        Map<String, List<String>> headers = sigV4ASign.getHeadersBasic(serviceName, globalRegion, method, uri);
        System.out.println(headers);
    }
}
```

#### Example 3
If you need high customization.

```java
import com.sigv4aSigning.SigV4ASign;

import software.amazon.awssdk.auth.signer.AwsSignerExecutionAttribute;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.RegionScope;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class MyClass {
    public static void main(String[] args) {
        SigV4ASign sigV4ASign = SigV4ASign.create();

        String url = "https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>";
        URI uri = URI.create(url);
        String serviceName = "s3";
        RegionScope globalRegion = RegionScope.GLOBAL;
        SdkHttpMethod method = SdkHttpMethod.GET;

        SdkHttpFullRequest request = SdkHttpFullRequest.builder()
                .method(method)
                .encodedPath(uri.toString())
                .port(SigV4ASign.PORT)
                .protocol(SigV4ASign.PROTOCOL_HTTPS)
                .host(uri.getHost())
                .build();

        ExecutionAttributes ea = new ExecutionAttributes();
        ea.putAttribute(AwsSignerExecutionAttribute.AWS_CREDENTIALS, sigV4ASign.getAwsCredentials());
        ea.putAttribute(AwsSignerExecutionAttribute.SERVICE_SIGNING_NAME, serviceName);

        Map<String, List<String>> headers = sigV4ASign.getHeaders(request, ea, globalRegion);
        System.out.println(headers);
    }
}
```

#### Example 4
Full example to get signed headers and make an API call.

```java
import com.sigv4aSigning.SigV4ASign;

import software.amazon.awssdk.auth.signer.AwsSignerExecutionAttribute;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.RegionScope;


import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

public class MyClass {
    public static void main(String[] args) {
        SigV4ASign sigV4ASign = SigV4ASign.create();

        String url = "https://<MRAP_alias>.accesspoint.s3-global.amazonaws.com/<s3-object-key>";
        URI uri = URI.create(url);
        String serviceName = "s3";
        RegionScope globalRegion = RegionScope.GLOBAL;
        SdkHttpMethod method = SdkHttpMethod.GET;

        SdkHttpFullRequest request = SdkHttpFullRequest.builder()
                .method(method)
                .encodedPath(uri.toString())
                .port(SigV4ASign.PORT)
                .protocol(SigV4ASign.PROTOCOL_HTTPS)
                .host(uri.getHost())
                .build();

        ExecutionAttributes ea = new ExecutionAttributes();
        ea.putAttribute(AwsSignerExecutionAttribute.AWS_CREDENTIALS, sigV4ASign.getAwsCredentials());
        ea.putAttribute(AwsSignerExecutionAttribute.SERVICE_SIGNING_NAME, serviceName);

        Map<String, List<String>> headers = sigV4ASign.getHeaders(request, ea, globalRegion);

        try {
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");
            headers.forEach((key, value) -> con.setRequestProperty(key, value.get(0)));

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuffer sb = new StringBuffer();

            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            in.close();
            con.disconnect();
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
