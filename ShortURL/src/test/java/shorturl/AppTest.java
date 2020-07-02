package shorturl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;


public class AppTest {
  @Test
  public void shortUrlShouldHaveALengthOf10() {
    App app = new App();
    String shortUrlValue = app.getShortUrl();
    assertNotNull(shortUrlValue);
    assertEquals(shortUrlValue.length(), 10);
  }
  @Test
  public void successfullyWriteDynamoDb() {
    App app = new App();
    AmazonDynamoDB client = mock(AmazonDynamoDB.class);
    assertNotNull(app.shortenLongUrl("https://canvas.seattlecentral.edu/login/canvas", client));
  }
  @Test
  public void doesNotCrashWithNullValues() {
    App app = new App();
    AmazonDynamoDB client = mock(AmazonDynamoDB.class);
    assertNotNull(app.shortenLongUrl(null, client));
  }
  @Test
  public void doesNotCrashWithInvalidUrlValue() {
    App app = new App();
    AmazonDynamoDB client = mock(AmazonDynamoDB.class);
    app.shortenLongUrl("dummy", client);
  }
}
