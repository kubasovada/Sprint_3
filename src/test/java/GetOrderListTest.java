import courier.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GetOrderListTest {
  private OrderClient orderClient;

  @Before
  public void setup() {
    orderClient = new OrderClient();
  }

  @Test
  @DisplayName("Get list order")
  @Description("Проверяет, что в теле ответа возвращается непустой список и код 200")
  public void getListOrder() {
    ValidatableResponse listOrderResponse =  orderClient.getOrderList();
    int statusCode = listOrderResponse.extract().statusCode();
    assertEquals(statusCode, SC_OK);
    List<Object> orders = listOrderResponse.extract().path("orders");
    assertNotEquals(0, orders.size());
  }
}
