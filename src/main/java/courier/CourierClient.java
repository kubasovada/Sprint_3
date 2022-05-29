package courier;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

// методы для работы с API курьеров
public class CourierClient extends RestAssuredClient {

  private final String ROOT  = "/courier";
  private final String LOGIN = ROOT + "/login";
  private final String COURIER = ROOT + "/{courierId}";


    public boolean create(Courier courier) {

      return  given()
              .header("Content-type", "application/json")
              .baseUri(URL)
              .body(courier)
              .when()
              .post(ROOT)
              .then().log().all()
              .assertThat()
              .statusCode(201)
              .extract()
              .path("ok");
    }

  public ValidatableResponse login(CourierCredentials creds) {
    return  given().log().all()
            .spec(getBaseSpec())
            .body(creds)
            .when()
            .post(LOGIN)
            .then();

  }

  public void delete(int courierId) {
    given().log().all()
            .spec(getBaseSpec())
            .pathParam("courierId", courierId)
            .when()
            .delete(COURIER)
            .then().log().all()
            .assertThat()
            .statusCode(200);
  }
}
