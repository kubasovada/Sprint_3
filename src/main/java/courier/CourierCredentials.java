package courier;

import lombok.Data;

@Data
public class CourierCredentials {
  private String login;
  private String password;

  public CourierCredentials(String login, String password) {
    this.login = login;
    this.password = password;
  }

  public CourierCredentials(Courier courier) {
    this.login = courier.getLogin();
    this.password = courier.getPassword();
  }


  public CourierCredentials(String password) { // для падающего теста новый конструктор
    this.password = password;
  }

  public static CourierCredentials from(Courier courier) {

    return new CourierCredentials(courier);

  }

}
