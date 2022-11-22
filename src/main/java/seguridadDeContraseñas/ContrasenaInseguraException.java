package seguridadDeContraseñas;

public class ContrasenaInseguraException extends RuntimeException {

  public ContrasenaInseguraException(String message) {
    super(message);
  }
}
