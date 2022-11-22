package Repositorios;

import modelo.*;
import java.util.List;
import java.util.stream.Collectors;


public class RepositorioAsociaciones extends Repositorio<Asociacion> {

  public RepositorioAsociaciones() {
    super(Asociacion.class);
  }

  public List<Publicacion> getPublicacionesAceptadas() {
    return getListadoAsociaciones().stream()
        .flatMap(asociacion -> asociacion
            .getPublicacionesActivas()
            .stream())
        .collect(Collectors.toList());
  }

  public Asociacion asociacionMasCercana(Ubicacion ubicacion) {
    Double distanciaMenor = getListadoAsociaciones().stream()
        .mapToDouble(asociacion -> asociacion.getUbicacion()
            .distanciaA(ubicacion)).min().getAsDouble();

    return getListadoAsociaciones().stream()
        .filter(asociacion -> asociacion.getUbicacion()
            .distanciaA(ubicacion)
            == distanciaMenor)
        .collect(Collectors.toList()).get(0);
  }

  public void asociarMascotaEncontradaConChapita(MascotaEncontrada mascotaEncontrada) {
    this.asociacionMasCercana(mascotaEncontrada.getLugarEncontrado())
        .agregarMascotaConChapita(mascotaEncontrada);
  }

  public void asociarPublicacion(Publicacion publicacion) {
    this.asociacionMasCercana(publicacion.getMascotaEncontrada().getLugarEncontrado())
        .agregarPublicacionPendiente(publicacion);
  }

  public List<Asociacion> getListadoAsociaciones() {
    return buscarTodos();
  }


}
