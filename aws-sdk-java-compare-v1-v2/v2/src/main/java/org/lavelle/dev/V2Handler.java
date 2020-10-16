package org.lavelle.dev;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class V2Handler implements RequestHandler<Map<String, String>, String> {

    private static final Logger LOGGER = Logger.getLogger("handler");

    private DynamoDbTable<SongItemV2> MUSIC_TABLE;

    public V2Handler() {
        LOGGER.info("V2 Lambda handler initializing");
        MUSIC_TABLE = SongItemV2.dynamoDbEnhancedClient().table("Songs", TableSchema.fromBean(SongItemV2.class));
    }

    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        try {
            LOGGER.info("V2 Lambda handler invoked");
            String operation = input.get("operation");

            if (operation.equals("init")) {
                // no op
            }
            else if (operation.equals("get")){
                getItem();
            }
            else if (operation.equals("put")) {
                putItem();
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING , "Lambda execution error", e);
            return "INTERNAL_SERVER_ERROR";
        }

        return "OK";
    }

    private void getItem() {
        // GET item
        SongItemV2 songToGet = new SongItemV2();
        songToGet.setArtist("Black Sabbath");
        songToGet.setTitle("Paranoid");

        LOGGER.info("Get Song Item");
        SongItemV2 songRetrieved = MUSIC_TABLE.getItem(songToGet);

        LOGGER.log(Level.INFO, String.format("Retrieved item from DynamoDb: Artist = %s, Song = %s",
                songRetrieved.getArtist(), songRetrieved.getTitle()));
    }

    private void putItem() {
        // PUT item
        SongItemV2 songToPut = new SongItemV2();
        songToPut.setArtist("Lavelle");
        songToPut.setTitle("Howling Wind");

        MUSIC_TABLE.putItem(songToPut);
        LOGGER.info(String.format("Put Song Item: %s, %s", songToPut.getArtist(), songToPut.getTitle()));
    }
}
