package servicio;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostResponseBody {

  @JsonProperty("bearer_token")
  private String token;

  public PostResponseBody(String token) {
    this.token = token;
  }

  public PostResponseBody() {
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}