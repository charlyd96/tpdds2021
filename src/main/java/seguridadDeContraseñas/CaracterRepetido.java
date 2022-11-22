package seguridadDeContraseñas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaracterRepetido implements RestriccionesPassword {

  // valido si aparece 3 o mas veces seguidas
  private String PASSWORD_PATTERN = "^.*(.)\\1{2}.*$";

  private Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

  public boolean cumpleRestriccion(String password) {
    Matcher matcher = pattern.matcher(password);
    return !matcher.matches();
  }
}
