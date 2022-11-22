package adopcion;

import java.util.List;

public class Recomendacion {

  private IntencionAdoptar intencionDeAdoptar;
  private List<MascotaEnAdopcion> mascotasEnAdopcion;

  public Recomendacion(IntencionAdoptar intencionDeAdoptar, List<MascotaEnAdopcion> mascotasEnAdopcion) {
    this.intencionDeAdoptar = intencionDeAdoptar;
    this.mascotasEnAdopcion = mascotasEnAdopcion;
  }

  public List<MascotaEnAdopcion> getMascotasEnAdopcionRecomendadas() {
    return mascotasEnAdopcion;
  }

  // TODO ver como paso los datos de la mascota  capaz me conviene pasar un String armando la info de la lista de mascotasEnAdopcion que mapeen*/
  public void sugerirMascotasAlInteresado() {
    if (mascotasEnAdopcion.size() > 0) {
      intencionDeAdoptar.getPersona().notificar(" Encotramos mascotas que pueden interesarte! ");
    }
  }

}
