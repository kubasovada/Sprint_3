import courier.Courier;
import courier.CourierClient;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
  private CourierClient courierClient;

  @Before
  public void setup() {
    courierClient = new CourierClient();
  }

  @Test
  public void courierCreationWithNullLogin() {
    String password = RandomStringUtils.randomAlphanumeric(10);
    ValidatableResponse createResponse = courierClient.createCourier(new Courier(null, password, null));

    int statusCode = createResponse.extract().statusCode();
    assertThat("ups", statusCode, equalTo(SC_BAD_REQUEST));

    String message = createResponse.extract().path("message");
    assertThat(message, equalTo("Недостаточно данных для создания учетной записи"));
  }

  @Test
  public void courierCreationWithNullPassword() {
    String login = RandomStringUtils.randomAlphanumeric(10);
    ValidatableResponse createResponse = courierClient.createCourier(new Courier(login, null, "Some name"));

    int statusCode = createResponse.extract().statusCode();
    assertThat("ups", statusCode, equalTo(SC_BAD_REQUEST));

    String message = createResponse.extract().path("message");
    assertThat(message, equalTo("Недостаточно данных для создания учетной записи"));
  }

  @Test
  public void courierCreationWithAllNullFields() {
    ValidatableResponse createResponse = courierClient.createCourier(new Courier(null, null, null));

    int statusCode = createResponse.extract().statusCode();
    assertThat("ups", statusCode, equalTo(SC_BAD_REQUEST));

    String message = createResponse.extract().path("message");
    assertThat(message, equalTo("Недостаточно данных для создания учетной записи"));
  }


}
