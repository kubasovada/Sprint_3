import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestTimedOutException;

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
    ValidatableResponse loginResponse = courierClient.login(creds);
    int statusCode =  loginResponse.extract().statusCode();
    String message = loginResponse.extract().path("message");

    assertThat("Courier can login without Login", statusCode, equalTo(SC_BAD_REQUEST));
    assertThat(message, equalTo("Недостаточно данных для входа"));
  }

  @Test
  public void courierCantLoginWithNullLogin400() {
    Courier courier = Courier.getRandom();
    ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(null, courier.getPassword()));
    int statusCode =  loginResponse.extract().statusCode();
    String message = loginResponse.extract().path("message");
    assertThat("Courier can login with null login", statusCode, equalTo(SC_BAD_REQUEST));
    assertEquals(message, "Недостаточно данных для входа");
  }

  @Test(timeout = 5000) // поставила таймаут, чтобы не ждать зависающий тест
  public void courierCantLoginWithNullPassword() {
    Courier courier = Courier.getRandom();
    ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), null));
    int statusCode =  loginResponse.extract().statusCode();
    String message = loginResponse.extract().path("message");
    assertThat("Что-то пошло не так", statusCode, equalTo(SC_BAD_REQUEST));
    assertEquals(message, "Недостаточно данных для входа");
  }

  @Test
  public void courierCantLoginWithIncorrectLogin404() {
    Courier courier = Courier.getRandom();
    courierClient.create(courier);

    CourierCredentials creds = CourierCredentials.from(courier);
    ValidatableResponse loginResponse = courierClient.login(creds); //логин для id
    courierId = loginResponse.extract().path("id"); // для удаления

    loginResponse = courierClient.login(new CourierCredentials("         " + courier.getLogin(), courier.getPassword()));
    int statusCode =  loginResponse.extract().statusCode();
    String message = loginResponse.extract().path("message");
    courierClient.delete(courierId);


    assertThat("Courier can login with incorrect login", statusCode, equalTo(SC_NOT_FOUND));
    assertEquals(message, "Учетная запись не найдена");
  }


}
