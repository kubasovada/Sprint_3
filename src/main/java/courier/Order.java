package courier;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Order {

  private String firstName;
  private String lastName;
  private String address;
  private int metroStation;
  private String phone;
  private int rentTime;
  private String deliveryDate;
  private String comment;
  public List<String> color;


  public static Order orderData(List<String> color) {

    return new Order("Naruto",
            "Uchiha",
            "Konoha, 142 apt.",
            4,
            "+7 800 355 35 35",
            5,
            "2020-06-06",
            "Saske, come back to Konoha",
            color);
  }

}
