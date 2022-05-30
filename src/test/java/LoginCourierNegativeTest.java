import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;


public class LoginCourierNegativeTest {

  private CourierClient courierClient;
  private int courierId;

  @Before
  public void setup() {
    courierClient = new CourierClient();
  }

  @Test
  public void courierCantLoginWithoutLoginInBody404() {

    String password = RandomStringUtils.randomAlphanumeric(10);
    CourierCredentials creds = new CourierCredentials(password);
    ValidatableResponse loginResponse = courierClient.loginCourier(creds);
    int statusCode = loginResponse.extract().statusCode();
    String message = loginResponse.extract().path("message");

    assertThat("Courier can login without Login", statusCode, equalTo(SC_BAD_REQUEST));
    assertThat(message, equalTo("Недостаточно данных для входа"));
  }

  @Test
  public void courierCantLoginWithNullLogin400() {
    Courier courier = Courier.getRandom();
    ValidatableResponse loginResponse = courierClient.loginCourier(new CourierCredentials(null, courier.getPassword()));
    int statusCode = loginResponse.extract().statusCode();
    String message = loginResponse.extract().path("message");
    assertThat("Courier can login with null login", statusCode, equalTo(SC_BAD_REQUEST));
    assertEquals(message, "Недостаточно данных для входа");
  }

  @Test
  public void courierCantLoginWithNullPassword() {
    Courier courier = Courier.getRandom();
    ValidatableResponse loginResponse = courierClient.loginCourier(new CourierCredentials(courier.getLogin(), null));
    int statusCode = loginResponse.extract().statusCode();
    assertThat("Что-то пошло не так", statusCode, equalTo(SC_BAD_REQUEST));
    String message = loginResponse.extract().path("message");
    assertEquals(message, "Недостаточно данных для входа");
  }

  @Test
  public void courierCantLoginWithIncorrectLogin404() {
    Courier courier = Courier.getRandom();
    courierClient.createCourier(courier);

    CourierCredentials creds = CourierCredentials.from(courier);
    ValidatableResponse loginResponse = courierClient.loginCourier(creds); //логин для id
    courierId = loginResponse.extract().path("id"); // для удаления

    loginResponse = courierClient.loginCourier(new CourierCredentials("         " + courier.getLogin(), courier.getPassword()));
    int statusCode = loginResponse.extract().statusCode();
    String message = loginResponse.extract().path("message");
    courierClient.deleteCourier(courierId);


    assertThat("Courier can login with incorrect login", statusCode, equalTo(SC_NOT_FOUND));
    assertEquals(message, "Учетная запись не найдена");
  }

  @Test
  public void courierCantLoginWithIncorrectPassword404() {
    Courier courier = Courier.getRandom();
    courierClient.createCourier(courier);

    CourierCredentials creds = CourierCredentials.from(courier);
    ValidatableResponse loginResponse = courierClient.loginCourier(creds); //логин для id
    courierId = loginResponse.extract().path("id"); // для удаления

    loginResponse = courierClient.loginCourier(new CourierCredentials(courier.getLogin(), "        " + courier.getPassword()));
    int statusCode = loginResponse.extract().statusCode();
    String message = loginResponse.extract().path("message");
    courierClient.deleteCourier(courierId);

    assertThat("Courier can login with incorrect login", statusCode, equalTo(SC_NOT_FOUND));
    assertEquals(message, "Учетная запись не найдена");
  }

  @Test
  public void courierCantLoginWithoutCreatedData404() {

    ValidatableResponse loginResponse = courierClient.loginCourier(new CourierCredentials("loginWithoutCreation", "sOmepssw"));
    int statusCode = loginResponse.extract().statusCode();
    String message = loginResponse.extract().path("message");

    assertThat("Courier can login without creation", statusCode, equalTo(SC_NOT_FOUND));
    assertEquals(message, "Учетная запись не найдена");
  }

}
