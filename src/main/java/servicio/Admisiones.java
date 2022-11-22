package servicio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "perros",
    "gatos"
})

public class Admisiones {

  @JsonProperty("perros")
  private Boolean perros;
  @JsonProperty("gatos")
  private Boolean gatos;


  public  Admisiones() {
  }


  public Admisiones(Boolean perros, Boolean gatos) {
    super();
    this.perros = perros;
    this.gatos = gatos;
  }

  @JsonProperty("perros")
  public Boolean getPerros() {
    return perros;
  }

  @JsonProperty("perros")
  public void setPerros(Boolean perros) {
    this.perros = perros;
  }

  @JsonProperty("gatos")
  public Boolean getGatos() {
    return gatos;
  }

  @JsonProperty("gatos")
  public void setGatos(Boolean gatos) {
    this.gatos = gatos;
  }

}