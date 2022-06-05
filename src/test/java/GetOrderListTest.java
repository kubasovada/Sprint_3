import courier.OrderClient;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GetOrderListTest {
  private OrderClient orderClient;

  @Before
  public void setup() {
    orderClient = new OrderClient();
  }

  @Test
  public void getListOrder() {
    ValidatableResponse listOrderResponse =  orderClient.getOrderList();
    int statusCode = listOrderResponse.extract().statusCode();
    assertEquals(statusCode, 200);
    List<Object> orders = listOrderResponse.extract().path("orders");
    assertNotEquals(0, orders.size());
  }
}
