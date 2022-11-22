package usuario;

public class SinValidacionDePasswordException extends RuntimeException{
  public SinValidacionDePasswordException(String message) {
    super(message);
  }
}
