package seguridadDeContraseñas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PeoresContrasenas implements RestriccionesPassword {

  private final static String pathTop10000OWASP = "src/main/resources/PeoresContrasenas.txt";

  public boolean cumpleRestriccion(String password) {
    try (Stream<String> peoresPass = Files.lines(Paths.get(pathTop10000OWASP))) {
      if (peoresPass.filter(pass -> pass.contains(password)).count() >= 1) {
        return false;
      }
    } catch (IOException e) {
      return true;
      //throw new RuntimeException("No se puede acceder al listado de 10.000 peores contrasenas. Error: " + e + ". Mensaje: " + e.getMessage());
    }
    return true;
  }

}



