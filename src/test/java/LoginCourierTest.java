import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class LoginCourierTest {

  private CourierClient courierClient;
  private int courierId;

  @Before
  public void setup() {
    courierClient = new CourierClient();
  }
  @After
  public void teardown() {
    courierClient.delete(courierId);
  }

  @Test
  public void courierCanLoginWithValidCredentials() {
    Courier courier = Courier.getRandom();
    courierClient.create(courier);

    CourierCredentials creds = CourierCredentials.from(courier);
    ValidatableResponse loginResponse = courierClient.login(creds);
    int statusCode =  loginResponse.extract().statusCode();
    courierId = loginResponse.extract().path("id"); // достать значение, которое в json под таким ключом "id"

    assertThat("Courier cannot login", statusCode, equalTo(SC_OK));
    //assertEquals(SC_OK, statusCode);
    assertNotEquals(0, courierId);
  }



}
