package courier;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class RestAssuredClient {
  protected final String URL = "http://qa-scooter.praktikum-services.ru/api/v1";

  public RequestSpecification getBaseSpec() {
    return new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(URL)
            .build();
  }

}
