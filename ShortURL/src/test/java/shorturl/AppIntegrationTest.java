package shorturl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;


public class AppIntegrationTest {
    @Test
    public void testingThatTheConnectionBetweenLambdaAndAPIGatewayIsSuccessful() throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("input", "http://zetcode.com/java/getpostrequest/");
        }};
        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://13s27or1b8.execute-api.us-west-2.amazonaws.com/beta/shorturl"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

    }
    /*@Test
    public void deleteDuplicatedItemsInDynamoDB() {
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    DynamoDB dynamoDB = new DynamoDB(client);
    Table table = dynamoDB.getTable("tinyUrl");
    Item item = table.getItem("url", "http://zetcode.com/java/getpostrequest/");
    DeleteItemOutcome outcome = table.deleteItem("url", "http://zetcode.com/java/getpostrequest/");
    }
     */
}
