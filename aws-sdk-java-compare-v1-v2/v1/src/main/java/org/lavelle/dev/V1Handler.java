package org.lavelle.dev;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class V1Handler implements RequestHandler<Object, Object> {

    private static final Logger LOGGER = Logger.getLogger("handler");
    private DynamoDBMapper dbMapper;

    public V1Handler() {
        LOGGER.info("V1 Lambda handler initializing");
        dbMapper = SongItemV1.dynamoDBMapper();
    }

    @Override
    public Object handleRequest(Object o, Context context) {
        // TODO: Create new lambda for V1 comparison, assign IAM role to access dynamodb songs table
        try {
            LOGGER.info("V1 Lambda handler invoked");

            // PUT item
            SongItemV1 songToPut = new SongItemV1();
            songToPut.setArtist("Lavelle");
            songToPut.setTitle("Howling Wind");

            LOGGER.info(String.format("Putting Song Item: %s, %s", songToPut.getArtist(), songToPut.getTitle()));
            dbMapper.save(songToPut);

            // GET item
            SongItemV1 songToGet = new SongItemV1();
            songToGet.setArtist("Black Sabbath");
            songToGet.setTitle("Paranoid");

            LOGGER.info("Get Song Item");
            SongItemV1 songRetrieved = dbMapper.load(songToGet);

            LOGGER.log(Level.INFO, String.format("Retrieved item from DynamoDb: Artist = %s, Song = %s",
                    songRetrieved.getArtist(), songRetrieved.getTitle()));
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING , "Lambda execution error", e);
        }
        return "OK";
    }
}
