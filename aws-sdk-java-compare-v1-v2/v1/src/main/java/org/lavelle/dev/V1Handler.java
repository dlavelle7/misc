package org.lavelle.dev;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.logging.Logger;

public class V1Handler implements RequestHandler<Object, Object> {

    private static final Logger LOGGER = Logger.getLogger("handler");

    public V1Handler() {
        LOGGER.info("V1 Lambda handler initializing");
    }

    @Override
    public Object handleRequest(Object o, Context context) {
        // TODO: Create new lambda for V1 comparison, assign IAM role to access dynamodb songs table
        // TODO: Implement V1 DynamoDBMapper for Songs dynamoDB table
        LOGGER.info("V1 Lambda handler invoked");
        return "OK";
    }
}
