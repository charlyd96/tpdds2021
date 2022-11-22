package usuario;

public class PermisosInsuficientesException extends RuntimeException{
  public PermisosInsuficientesException(String message) {
    super(message);
  }
}
