package org.lavelle.dev;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@DynamoDbBean
public class SongItemV2 {
    private String artist;
    private String title;

    public SongItemV2() {
    }

    @DynamoDbSortKey
    public String getArtist() {
        return this.artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }

    @DynamoDbPartitionKey
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Tuning V2 to reduce startup time: https://aws.amazon.com/blogs/developer/tuning-the-aws-java-sdk-2-x-to-reduce-startup-time/
     *
     * 1. Instantiate the DB client in the handler constructor, so it can be reused across multiple lambda invocations.
     * 2. Use the lightweight UrlConnectionHttpClient - lower instantiation time (& throughput) compared to ApacheHttpClient.
     * 3. Exclude unused HTTP Client dependencies (Apache & Netty clients - see pom.xml) to minimise deployment package size.
     * 4. Load credentials from the Lambda environment with EnvironmentVariableCredentialsProvider (avoids cred lookups).
     * 5. Set the Region using the Lambda environment variable AWS_REGION (avoids region lookups).
     */
    @DynamoDbIgnore
    private static final DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable())))
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build();
    }

    @DynamoDbIgnore
    public static final DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient())
                .build();
    }

}
