package org.lavelle.dev;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


/**
 * After a Lambda function finishes, Lambda may reuse the previous container for subsequent invocations.
 * In such case, objects declared outside of the function handler method remain initialized.
 * To take the advantage of the Lambda execution context reuse,
 * we recommend initializing the SDK client outside of the handler method so that subsequent executions processed by
 * the same instance can reuse the client and connections.
 */
public class V2Handler implements RequestHandler<Object, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(V2Handler.class);

    private final DynamoDbTable<MusicItem> MUSIC_TABLE;

    public V2Handler() {
        MUSIC_TABLE = MusicItem.dynamoDbEnhancedClient().table("Music", TableSchema.fromBean(MusicItem.class));
    }

    @Override
    public Object handleRequest(Object o, Context context) {
        LOGGER.info("V2 Lambda invoked");

        MusicItem songToGet = new MusicItem();
        songToGet.setArtist("Black Sabbath");
        songToGet.setSongTitle("Paranoid");

        // Get item from dynamodb
        MusicItem item = MUSIC_TABLE.getItem(songToGet);

        LOGGER.info("Retrieved item from DynamoDb: Artist = {}, Song = {}", item.getArtist(), item.getSongTitle());

        return "OK";

    }
}
