package servicio;

public class APIInvalidResponse extends RuntimeException {

  public APIInvalidResponse(String message) {
    super(message);
  }

}
