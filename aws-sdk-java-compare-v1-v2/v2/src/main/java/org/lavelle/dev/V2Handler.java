package org.lavelle.dev;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
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

    private DynamoDbTable<MusicItem> MUSIC_TABLE;

    public V2Handler() {
        LOGGER.info("V2 Lambda handler initializing");
        // TODO: Remove try catch and make MUSIC_TABLE final again once you get db connection working
        try {
            MUSIC_TABLE = MusicItem.dynamoDbEnhancedClient().table("Music", TableSchema.fromBean(MusicItem.class));
        }
        catch (Exception e) {
            MUSIC_TABLE = null;
            LOGGER.log(Level.WARNING, String.format("Initialization error: %s %s", e.getMessage(), e));
        }
    }

    @Override
    public Object handleRequest(Object o, Context context) {
        try {
            LOGGER.info("V2 Lambda invoked");

            MusicItem songToGet = new MusicItem();
            songToGet.setArtist("Black Sabbath");
            songToGet.setSongTitle("Paranoid");

            // Get item from dynamodb
            MusicItem item = MUSIC_TABLE.getItem(songToGet);

            LOGGER.log(Level.INFO, String.format("Retrieved item from DynamoDb: Artist = %s, Song = %s", item.getArtist(), item.getSongTitle()));
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING , "Error: {}", e);
        }

        return "OK";

    }
}
