import courier.Order;
import courier.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotEquals;

@RunWith(Parameterized.class)
public class CreateOrderTest {
  private final List<String> color;
  private final int expectedCode;
  private OrderClient orderClient;
  private int track;

  public CreateOrderTest(List<String> color, int expectedCode) {
    this.color = color;
    this.expectedCode = expectedCode;
  }

  @Before
  public void setup() {
    orderClient = new OrderClient();
  }

  @After
  public void teardown() {
    orderClient.cancelOrder(track);
  }

  @Test
  @DisplayName("Create order with valid data")
  @Description("Параметризованные тесты с комбинациями цветов")
  public void createOrderWithValidData() {
    Order order = Order.orderData(color);
    ValidatableResponse orderCreationResponse = orderClient.createOrder(order);
    int statusCode = orderCreationResponse.extract().statusCode();
    track = orderCreationResponse.extract().path("track");

    assertThat(statusCode, equalTo(expectedCode));
    assertNotEquals(0, track);
  }

  @Parameterized.Parameters
  public static Object[][] createOrderWithColor() {
    return new Object[][]{
            {List.of("BLACK"), 201},
            {List.of("GREY"), 201},
            {List.of("BLACK", "GREY"), 201},
            {null, 201},
    };
  }

}
