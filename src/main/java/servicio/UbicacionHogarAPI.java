package servicio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "direccion",
    "lat",
    "long"
})

public class UbicacionHogarAPI {

  @JsonProperty("direccion")
  private String direccion;
  @JsonProperty("lat")
  private Double lat;
  @JsonProperty("long")
  private Double longitud;


  public UbicacionHogarAPI() {
  }

   public UbicacionHogarAPI(String direccion, Double lat, Double longitud) {
    super();
    this.direccion = direccion;
    this.lat = lat;
    this.longitud = longitud;
  }

  @JsonProperty("direccion")
  public String getDireccion() {
    return direccion;
  }

  @JsonProperty("direccion")
  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  @JsonProperty("lat")
  public Double getLat() {
    return lat;
  }

  @JsonProperty("lat")
  public void setLat(Double lat) {
    this.lat = lat;
  }

  @JsonProperty("long")
  public Double getLong() {
    return longitud;
  }

  @JsonProperty("long")
  public void setLong(Double _long) {
    this.longitud = _long;
  }

}