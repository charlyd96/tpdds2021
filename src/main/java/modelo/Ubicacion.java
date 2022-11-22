package modelo;

import static java.lang.Math.*;

import javax.persistence.*;

@Embeddable
public class Ubicacion {

  @Transient
  public static final double RADIO_DE_LA_TIERRA = 6371;

  private Double longitud;
  private Double latitud;
  private String direccion;

  public Ubicacion() {

  }

  public Ubicacion(Double latitud, Double longitud, String direccion) {
    this.longitud = longitud;
    this.latitud = latitud;
    this.direccion = direccion;
  }

  public double distanciaA(Ubicacion otraUbicacion) {

    double diferenciaLatitud = toRadians(otraUbicacion.getLatitud() - latitud);

    double diferenciaLongitud = toRadians(otraUbicacion.getLongitud() - longitud);

    double a = pow(sin(diferenciaLatitud / 2), 2)

        + cos(toRadians(latitud)) * cos(toRadians(otraUbicacion.getLatitud()))

        * pow(sin(diferenciaLongitud / 2), 2);

    double distanciaAngular = 2 * atan2(sqrt(a), sqrt(1 - a));

    return RADIO_DE_LA_TIERRA * distanciaAngular;
  }

  public Double getLongitud() {
    return longitud;
  }

  public Double getLatitud() {
    return latitud;
  }

  public String getDireccion() {
    return direccion;
  }
}
