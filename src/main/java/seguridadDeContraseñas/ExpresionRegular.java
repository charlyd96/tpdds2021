package seguridadDeContraseñas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpresionRegular implements RestriccionesPassword {

  // digit + lowercase char + uppercase char + punctuation + symbol + at least 8 chars
  private String PASSWORD_PATTERN =
      "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,}$";

  private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

  public boolean cumpleRestriccion(String password) {
    Matcher matcher = pattern.matcher(password);
    return matcher.matches();
  }

}
