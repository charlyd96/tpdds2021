package modelo;

import Repositorios.RepositorioAsociaciones;
import Repositorios.factories.FactoryRepositorioAsociaciones;

import java.time.LocalDate;
import java.util.List;

public class BuilderMascotaEncontrada {

  private TipoMascota tipo;
  private List<String> pathFotos;
  private String descripcion;
  private LocalDate fechaRescate;
  private Ubicacion lugarEncontrado;
  private Persona rescatista;
  private Mascota mascota = null; //QR
  private TamanioMascota tamanioMascota;
  private TipoDeAlojamiento tipoDeAlojamiento;
  private RepositorioAsociaciones repoAsoc;

  public BuilderMascotaEncontrada(RepositorioAsociaciones repoAsoc) {
    this.repoAsoc = repoAsoc;
  }

  public void setTipo(TipoMascota tipo) {
    this.tipo = tipo;
  }

  public void setPathFotos(List<String> pathFotos) {
    this.pathFotos = pathFotos;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public void setFechaRescate(LocalDate fechaRescate) {
    this.fechaRescate = fechaRescate;
  }

  public void setLugarEncontrado(Ubicacion lugarEncontrado) {
    this.lugarEncontrado = lugarEncontrado;
  }

  public void setRescatista(Persona rescatista) {
    this.rescatista = rescatista;
  }

  public void setTamanioMascota(TamanioMascota tamanioMascota) {
    this.tamanioMascota = tamanioMascota;
  }

  public void setTipoDeAlojamiento(TipoDeAlojamiento tipoDeAlojamiento) {
    this.tipoDeAlojamiento = tipoDeAlojamiento;
  }

  public void setMascota(Mascota mascota) {
    if (this.mascota == null && mascota != null) {
      this.mascota = mascota;
      this.tamanioMascota = mascota.getTamanio();
      this.tipo = mascota.getTipo();
    }
  }

  private MascotaEncontrada generarMascota() {
    return new MascotaEncontrada(tipo, pathFotos,
        descripcion, fechaRescate,
        lugarEncontrado, rescatista,
        tamanioMascota, mascota);
  }

  public MascotaEncontrada generarMascotaConChapitaYasociarla() {
    tamanioMascota = mascota.getTamanio();
    MascotaEncontrada mascotaRescatada = generarMascota();
    mascotaRescatada.reclamada();
    repoAsoc.asociarMascotaEncontradaConChapita(mascotaRescatada);
    return mascotaRescatada;
  }

  public Publicacion generarPublicacionYasociarla() {
    MascotaEncontrada mascotaRescatada = generarMascota();
    Publicacion publicacion = new Publicacion(mascotaRescatada);
    repoAsoc.asociarPublicacion(publicacion);
    return publicacion;
  }



}


