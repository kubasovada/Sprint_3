package courier;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends  RestAssuredClient {

  private final String ORDERS = "/orders";

  public ValidatableResponse createOrder(Order order) {
    return given().log().all()
            .header("Content-type", "application/json")
            .baseUri(URL)
            .body(order)
            .when()
            .post(ORDERS)
            .then();
  }

  public ValidatableResponse cancelOrder(int track) {
    String json = String.format("{\"track\": %d}", track);
    return given().log().all()
            .header("Content-type", "application/json")
            .baseUri(URL)
            .body(json)
            .when()
            .put(ORDERS)
            .then();
  }

}
