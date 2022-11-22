package Repositorios;

import java.util.List;
import java.util.stream.Collectors;

import modelo.MascotaEncontrada;
import modelo.Publicacion;
import modelo.Ubicacion;
import servicio.Hogar;
import servicio.ServicioGetPostApi;



public class RepositorioHogares {

  private ServicioGetPostApi api;

  public RepositorioHogares(ServicioGetPostApi api) {
    this.api = api;
  }

  public List<Hogar> hogaresQueAceptanLaMascotaConChapitaYEstanCerca(
      Ubicacion ubicacion,
      MascotaEncontrada mascota,
      int radioCercania
  ) {
    return api.respuestaDeHogaresApi().stream()
        .filter(hogar ->
            estaDentroDelRadio(radioCercania, hogar, ubicacion) && hogar.aceptoMascota(mascota)
        )
        .collect(Collectors.toList());
  }


  public List<Hogar> hogaresQueAceptanLaMascotaSinChapitaYEstanCerca(
      Ubicacion ubicacion,
      Publicacion publicacion,
      int radioCercania
  ) {
    return this.hogaresQueAceptanLaMascotaConChapitaYEstanCerca(ubicacion,
        publicacion.getMascotaEncontrada(), radioCercania);
  }

  public boolean estaDentroDelRadio(int radioCercania, Hogar hogar, Ubicacion ubicacion) {
    Ubicacion ubicacionHogar = new Ubicacion(
        hogar.getUbicacion().getLat(),
        hogar.getUbicacion().getLong(),
        null);

    return ubicacion.distanciaA(ubicacionHogar) <= radioCercania;
  }

}