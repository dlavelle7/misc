package org.lavelle.dev;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * After a Lambda function finishes, Lambda may reuse the previous container for subsequent invocations.
 * In such case, objects declared outside of the function handler method remain initialized.
 * To take the advantage of the Lambda execution context reuse,
 * we recommend initializing the SDK client outside of the handler method so that subsequent executions processed by
 * the same instance can reuse the client and connections.
 */
public class V2Handler implements RequestHandler<Object, Object> {

    private static final Logger LOGGER = Logger.getLogger("handler");

    private DynamoDbTable<SongItem> MUSIC_TABLE;

    public V2Handler() {
        LOGGER.info("V2 Lambda handler initializing");
        MUSIC_TABLE = SongItem.dynamoDbEnhancedClient().table("Songs", TableSchema.fromBean(SongItem.class));
    }

    @Override
    public Object handleRequest(Object o, Context context) {
        try {
            LOGGER.info("V2 Lambda invoked");

            SongItem songToPut = new SongItem();
            songToPut.setArtist("Lavelle");
            songToPut.setTitle("Howling Wind");
            LOGGER.info(String.format("Putting Song Item: %s, %s", songToPut.getArtist(), songToPut.getTitle()));
            MUSIC_TABLE.putItem(songToPut);

            // Create a KEY object
            Key key = Key.builder()
                    .partitionValue("Paranoid")
                    .sortValue("Black Sabbath")
                    .build();

            // Get item from dynamodb
            LOGGER.info("Get Song Item");
            SongItem item = MUSIC_TABLE.getItem(r->r.key(key));

            LOGGER.log(Level.INFO, String.format("Retrieved item from DynamoDb: Artist = %s, Song = %s", item.getArtist(), item.getTitle()));
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING , "Lambda execution error", e);
        }

        return "OK";

    }
}
