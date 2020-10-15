package org.lavelle.dev;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName="Songs")
public class SongItemV1 {

    @DynamoDBRangeKey
    private String artist;

    @DynamoDBHashKey
    private String title;

    public SongItemV1() {
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBIgnore
    public static DynamoDBMapper dynamoDBMapper() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper dbMapper = new DynamoDBMapper(client);
        return dbMapper;
    }
}
