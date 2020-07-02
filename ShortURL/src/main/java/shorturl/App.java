package shorturl;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.commons.validator.UrlValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<ConvertLongToShortUrlInput, Object> {

    public Object handleRequest(final ConvertLongToShortUrlInput input, final Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .build();
                return shortenLongUrl(input.getInput(),  client);
    }

    protected Object shortenLongUrl(String longUrlInput, AmazonDynamoDB client){
        Map<String, String> headers = new HashMap<>();
        UrlValidator urlValidator = new UrlValidator();
        if(longUrlInput == null){
            System.out.println("Input cannot be null");
            return new shorturl.GatewayResponse("Input cannot be null.", headers, 400);
        } else if(!urlValidator.isValid(longUrlInput)) {
            System.out.print("Invalid Url.");
            return new shorturl.GatewayResponse("Invalid Url.", headers, 400);
        }
            String shortUrlInput = getShortUrl();
            writeDynamoDb(shortUrlInput,"longurl", longUrlInput, client);
            writeDynamoDb(longUrlInput,"shorturl", shortUrlInput, client);
            headers.put("Content-Type", "application/json");
            headers.put("X-Custom-Header", "application/json");

            return new GatewayResponse(shortUrlInput, headers, 200);

    }

    private void writeDynamoDb(String urlValue, String urlMetadataKey, String urlMetadataValue, AmazonDynamoDB client) {
        Map<String, AttributeValue> item = new HashMap<>();
        AttributeValue url = new AttributeValue(urlValue);
        AttributeValue urlMetadata = new AttributeValue(urlMetadataValue);
        item.put("url", url);
        item.put(urlMetadataKey, urlMetadata);
        PutItemRequest putItemRequest = new PutItemRequest("tinyurl", item);
        client.putItem(putItemRequest);
    }
    public String getShortUrl() {
        char abc[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't','u','v', 'w', 'x', 'y', 'z'};
        String shortUrl = "";
        for (int i = 0; i < 10; i++) {
            shortUrl += abc[(int) (Math.random() * (22 - 0)) + 0];
        }
        return shortUrl;
    }
}
