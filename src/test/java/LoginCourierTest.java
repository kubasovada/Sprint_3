import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
    courierClient.deleteCourier(courierId);
  }

  @Test
  @DisplayName("Courier can login with valid credentials")
  @Description("Логин с валидными данными")
  public void courierCanLoginWithValidCredentials() {
    Courier courier = Courier.getRandom();
    courierClient.createCourier(courier);

    CourierCredentials creds = CourierCredentials.from(courier);
    ValidatableResponse loginResponse = courierClient.loginCourier(creds);
    int statusCode = loginResponse.extract().statusCode();
    courierId = loginResponse.extract().path("id"); // достать значение, которое в json под таким ключом "id"

    assertThat("Courier cannot login", statusCode, equalTo(SC_OK));
    assertNotEquals(0, courierId);
  }

}