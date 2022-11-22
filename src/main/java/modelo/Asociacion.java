package modelo;

import Repositorios.RepositorioMascotasEncontradas;
import adopcion.IntencionAdoptar;
import adopcion.MascotaEnAdopcion;
import adopcion.PreguntaAdopcion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
// TODO: revisar todos los cascade de esta clase

@Entity
@Table(name = "Asociaciones")
public class Asociacion  {

  @Id
  @GeneratedValue
  @Column(name = "id_asociacion")
  private Long id;

  public Long getId() {
    return id;
  }

  @Embedded
  private Ubicacion ubicacion;

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "id_asociacion")
  private final List<MascotaEncontrada> mascotasIdentificadas = new ArrayList<>();

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "id_asociacion")
  private final List<Publicacion> publicaciones = new ArrayList<>();

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "id_asociacion")
  private final List<MascotaEnAdopcion> mascotasEnAdopcion = new ArrayList<>();

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "id_asociacion")
  private final List<IntencionAdoptar> intencionesDeAdopcion = new ArrayList<>();

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "id_asociacion")
  private final List<PreguntaAdopcion> preguntasAdopciones = new ArrayList<>();

  public Asociacion(Ubicacion ubicacion) {
    this.ubicacion = ubicacion;
  }

  public Asociacion() {

  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

  public List<MascotaEncontrada> getMascotasIdentificadas() {
    return mascotasIdentificadas;
  }

  public List<MascotaEncontrada> getMascotasRescatadasUltNDias(
      int diasParaMostrarUltimasMascotasEncontradas) {
    LocalDate diasDiferencia = LocalDate.now().minusDays(diasParaMostrarUltimasMascotasEncontradas);
    return mascotasIdentificadas.stream().filter(mascota -> mascota.getFechaRescate()
        .isAfter(diasDiferencia)).collect(Collectors.toList());
  }

  public void agregarMascotaConChapita(MascotaEncontrada mascotaEncontrada) {
    mascotasIdentificadas.add(mascotaEncontrada);
    // new RepositorioMascotasEncontradas().agregar(mascotaEncontrada);
    informarDuenoAlRescatista(mascotaEncontrada);
  }

  private void informarDuenoAlRescatista(MascotaEncontrada mascotaConDueno) {
    mascotaConDueno.getRescatista().notificar(mascotaConDueno
        .getMascota().getDuenio().getDescripcionContactos());
  }

  //***************************** Preguntas Adopcion *****************

  public void eliminarPreguntaAdopcion(PreguntaAdopcion preguntaAdopcion) {
    preguntasAdopciones.remove(preguntaAdopcion);
  }

  public void agregarPreguntaAdopcion(PreguntaAdopcion preguntaAdopcion) {
    preguntasAdopciones.add(preguntaAdopcion);
  }

  public List<PreguntaAdopcion> getPreguntasAdopciones() {
    return preguntasAdopciones;
  }

  //*****************************Publicaciones (mascota perdida sin chapita)*****************


  public void agregarPublicacionPendiente(Publicacion publicacion) {
    publicaciones.add(publicacion);
  }

  public void aceptarPublicacion(Publicacion publicacion) {
    publicacion.aceptar();
  }

  public void rechazarPublicacion(Publicacion publicacion) {
    publicacion.rechazar();
    publicacion.getRescatista().notificar("La publicacion fue rechazada");
  }

  public List<Publicacion> getPublicacionesPendientes() {
    return publicaciones.stream()
        .filter(publicacion -> publicacion.getEstadoPublicacion() == EstadoPublicacion.PENDIENTE)
        .collect(Collectors.toList());
  }

  public List<Publicacion> getPublicacionesActivas() {
    return publicaciones.stream()
        .filter(publicacion -> publicacion.getEstadoPublicacion() == EstadoPublicacion.ACEPTADA)
        .collect(Collectors.toList());
  }


  //**************************************Adopciones*****************************************


  public void agregarMascotaEnAdopcion(MascotaEnAdopcion publi) {
    mascotasEnAdopcion.add(publi);
  }

  public void eliminarMascotaEnAdopcion(MascotaEnAdopcion publi) {
    mascotasEnAdopcion.remove(publi);
  }

  public List<MascotaEnAdopcion> getMascotasEnAdopcion() {
    return mascotasEnAdopcion;
  }

  //**************************************Intencion Adoptar ****************************************


  public void agregarIntencionDeAdoptar(IntencionAdoptar publi) {
    intencionesDeAdopcion.add(publi);
    publi.getPersona().notificar("La publicacion se creo correctamente, link baja");
  }

  public List<IntencionAdoptar> getIntencionesDeAdopcion() {
    return intencionesDeAdopcion.stream()
        .filter(IntencionAdoptar::estaActiva).collect(Collectors.toList());
  }

  public List<IntencionAdoptar> getIntencionesAdoptarActivas() {
    return intencionesDeAdopcion.stream()
        .filter(IntencionAdoptar::estaActiva).collect(Collectors.toList());
  }
}