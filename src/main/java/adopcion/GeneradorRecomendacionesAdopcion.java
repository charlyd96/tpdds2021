package adopcion;

import modelo.Asociacion;
import Repositorios.RepositorioAsociaciones;

import java.util.List;
import java.util.stream.Collectors;

public class GeneradorRecomendacionesAdopcion {

  private List<Asociacion> totalAsociaciones;

  public GeneradorRecomendacionesAdopcion(List<Asociacion> asociaciones) {
    this.totalAsociaciones = asociaciones;
  }

  public Recomendacion generarRecomendacionParticular(IntencionAdoptar intencion,
                                                      List<MascotaEnAdopcion> listaMascotas) {
    List<MascotaEnAdopcion> mascotasCompatibles = listaMascotas.stream()
        .filter(intencion::coincideCon)
        .collect(Collectors.toList());
    if (mascotasCompatibles.size() > 0) {
      Recomendacion recomendacion = new Recomendacion(intencion, mascotasCompatibles);
      recomendacion.sugerirMascotasAlInteresado();
      return recomendacion;
    }
    return null;
  }

  private void generarRecomendacionAsociacion(Asociacion asociacion) {
    List<IntencionAdoptar> intencionAdoptars = asociacion.getIntencionesAdoptarActivas();
    List<MascotaEnAdopcion> mascotaEnAdopcions = asociacion.getMascotasEnAdopcion();
    intencionAdoptars.forEach(intencionAdoptar ->
        generarRecomendacionParticular(intencionAdoptar, mascotaEnAdopcions));
  }

  public void generarRecomendacion() {
    totalAsociaciones.forEach(this::generarRecomendacionAsociacion);
  }

}
