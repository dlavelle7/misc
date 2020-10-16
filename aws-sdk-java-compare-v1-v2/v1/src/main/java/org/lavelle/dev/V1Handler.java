package org.lavelle.dev;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class V1Handler implements RequestHandler<Map<String, String>, String> {

    private static final Logger LOGGER = Logger.getLogger("handler");
    private DynamoDBMapper dbMapper;

    public V1Handler() {
        LOGGER.info("V1 Lambda handler initializing");
        dbMapper = SongItemV1.dynamoDBMapper();
    }

    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        try {
            LOGGER.info("V1 Lambda handler invoked");
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
        SongItemV1 songToGet = new SongItemV1();
        songToGet.setArtist("Black Sabbath");
        songToGet.setTitle("Paranoid");

        LOGGER.info("Get Song Item");
        SongItemV1 songRetrieved = dbMapper.load(songToGet);

        LOGGER.log(Level.INFO, String.format("Retrieved item from DynamoDb: Artist = %s, Song = %s",
                songRetrieved.getArtist(), songRetrieved.getTitle()));
    }

    private void putItem() {
        // PUT item
        SongItemV1 songToPut = new SongItemV1();
        songToPut.setArtist("Lavelle");
        songToPut.setTitle("Howling Wind");

        dbMapper.save(songToPut);
        LOGGER.info(String.format("Putting Song Item: %s, %s", songToPut.getArtist(), songToPut.getTitle()));
    }
}
