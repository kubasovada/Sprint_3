package courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

// методы для работы с API курьеров
public class CourierClient extends RestAssuredClient {

  private final String ROOT = "/courier";
  private final String LOGIN = ROOT + "/login";
  private final String COURIER = ROOT + "/{courierId}";

  @Step("Send POST request to create courier /courier")
  public ValidatableResponse createCourier(Courier courier) {

    return given()
            .header("Content-type", "application/json")
            .baseUri(URL)
            .body(courier)
            .when()
            .post(ROOT)
            .then().log().all();
  }

  @Step("Send POST request to login courier /courier/login")
  public ValidatableResponse loginCourier(CourierCredentials creds) {
    return given().log().all()
            .spec(getBaseSpec())
            .body(creds)
            .when()
            .post(LOGIN)
            .then();
  }

  @Step("Send DELETE request to delete courier /courier/{courierId}")
  public void deleteCourier(int courierId) {
    given().log().all()
            .spec(getBaseSpec())
            .pathParam("courierId", courierId)
            .when()
            .delete(COURIER)
            .then().log().all()
            .assertThat()
            .statusCode(SC_OK);
  }
}
