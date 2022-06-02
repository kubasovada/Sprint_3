import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CreateCourierWithDeletionTest {
  private CourierClient courierClient;
  private int courierId;

  @Before
  public void setup() {
    courierClient = new CourierClient();
  }

  @After
  public void teardown() {
    courierClient.deleteCourier(courierId);
  }

  @Test
  public void courierCreationWithValidCredentials() {
    Courier courier = Courier.getRandom();
    ValidatableResponse createResponse = courierClient.createCourier(courier);
    int statusCode = createResponse.extract().statusCode();
    boolean textOk = createResponse.extract().path("ok");
    assertThat("Courier creation failed", statusCode, equalTo(SC_CREATED));
    assertTrue(textOk);

    CourierCredentials courierCredentials = CourierCredentials.from(courier);
    courierId = courierClient.loginCourier(courierCredentials).extract().path("id"); // логин для дальнейшего удаления

  }

  @Test
  public void courierCreationWithValidCredentialsTwice() {
    Courier courier = Courier.getRandom();
    courierClient.createCourier(courier);
    ValidatableResponse createResponse = courierClient.createCourier(courier);
    int statusCode = createResponse.extract().statusCode();
    assertThat("ups", statusCode, equalTo(SC_CONFLICT));
    String message = createResponse.extract().path("message");
    assertThat(message, equalTo("Этот логин уже используется. Попробуйте другой."));

    CourierCredentials courierCredentials = CourierCredentials.from(courier);
    courierId = courierClient.loginCourier(courierCredentials).extract().path("id"); // for deletion
  }

}
