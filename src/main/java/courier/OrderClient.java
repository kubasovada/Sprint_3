package courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends  RestAssuredClient {

  private final String ORDERS = "/orders";

  @Step("Send POST request to create order /orders")
  public ValidatableResponse createOrder(Order order) {
    return given().log().all()
            .header("Content-type", "application/json")
            .baseUri(URL)
            .body(order)
            .when()
            .post(ORDERS)
            .then();
  }

  @Step("Send PUT request to cancel order /orders")
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

  @Step("Send GET request to get order list /orders")
  public ValidatableResponse getOrderList() {
    return given().log().all()
            .header("Content-type", "application/json")
            .baseUri(URL)
            .get(ORDERS)
            .then();
  }

}
