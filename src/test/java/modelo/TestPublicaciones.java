package modelo;

import Repositorios.RepositorioAsociaciones;
import notificaciones.ContactoMail;
import notificaciones.EmailSenderService;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import seguridadDeContraseñas.*;
import usuario.CreadorUsuarios;
import usuario.PermisosInsuficientesException;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestPublicaciones extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction(); // Comentar si se le quiere pegar a la DB
//    this.commitTransaction(); // descomentar si se le quiere pegar a la DB
  }

  private EmailSenderService emailSenderMock;
  private Asociacion asociacion;
  private BuilderMascotaEncontrada builderMascotaEncontrada ;


  RepositorioAsociaciones repositorioAsociaciones = new RepositorioAsociaciones();

  @BeforeEach
  void initMock() {
    emailSenderMock = mock(EmailSenderService.class);
    asociacion = new Asociacion(null);
    builderMascotaEncontrada = new BuilderMascotaEncontrada(repositorioAsociaciones);
  }

  @BeforeEach
  void cargoElRepoDeAsociaciones(){
    repositorioAsociaciones.agregar(unaAsociacionEnPosadas());
    repositorioAsociaciones.agregar(unaAsociacionEnBuenosAires());
  }

//
//  @DisplayName("Un voluntario puede aceptar publicaciones")
//  @Test
//  void soloUnUsuarioVoluntarioPuedeAceptarPublicaciones() {
//    Usuario voluntario = crearUsuario().crearUsuarioVoluntario();
//    Usuario dueno = crearUsuario().crearUsuarioNormal();
//    Publicacion mascotaParaPublicar = mascotaRescatadaEnBuenosAires();
//    asociacion.agregarPublicacionPendiente(mascotaParaPublicar);
//
//    Assertions.assertThrows(PermisosInsuficientesException.class, () -> dueno.aceptar(mascotaParaPublicar));
//    Assertions.assertEquals(0, asociacion.getPublicacionesActivas().size());
//    voluntario.aceptar(mascotaParaPublicar);
//    Assertions.assertEquals(1, asociacion.getPublicacionesActivas().size());
//
//  }
//
//  @DisplayName("Un voluntario puede rechazar publicaciones")
//  @Test
//  void soloUnUsuarioVoluntarioPuedeRechazarPublicaciones() {
//    Usuario voluntario = crearUsuario().crearUsuarioVoluntario(new Asociacion(null));
//    Usuario dueno = crearUsuario().crearUsuarioNormal();
//
//    Publicacion mascotaParaPublicar = mascotaRescatadaEnBuenosAires();
//    asociacion.agregarPublicacionPendiente(mascotaParaPublicar);
//
//    doNothing().when(emailSenderMock).notificar(any(), any());
//
//    Assertions.assertThrows(PermisosInsuficientesException.class, () -> dueno.rechazar(mascotaParaPublicar));
//
//    voluntario.rechazar();
//    Assertions.assertEquals(0, asociacion.getPublicacionesActivas().size());
//
//  }
//
//  @DisplayName("cuando un voluntario rechaza una publicacion su rescatista es informado")
//  @Test
//  void alrechazarUnaPublicacionElRescatistaEsInformado() {
//    Usuario voluntario = crearUsuario().crearUsuarioVoluntario(new Asociacion(null));
//    Publicacion mascotaParaPublicar = mascotaRescatadaEnBuenosAires();
//    asociacion.agregarPublicacionPendiente(mascotaParaPublicar);
//
//    doNothing().when(emailSenderMock).notificar(any(), any());
//
//    voluntario.rechazar(mascotaParaPublicar);
//    verify(emailSenderMock, times(1)).notificar(any(), any());
//
//  }

  @DisplayName("Una persona puede buscar a su mascota en las publicaciones, reclamarla y el rescatista es informado del hecho")
  @Test
  void unDuenoEncuentraASuMascotaEnUnaPublicacionYLaReclama() {
    Publicacion unaPublicacion = mascotaRescatadaEnBuenosAires();
    Persona reclamador = unaPersona();
    doNothing().when(emailSenderMock).notificar(any(), any());

    Assertions.assertEquals(unaPublicacion.getMascotaEncontrada().getEstadoMascotaRescatada(), EstadoMascotaRescatada.EN_TRANSITO);
    unaPublicacion.reclamarMascota(reclamador);
    verify(emailSenderMock, times(1)).notificar(any(), any());
    Assertions.assertEquals(unaPublicacion.getMascotaEncontrada().getEstadoMascotaRescatada(), EstadoMascotaRescatada.RECLAMADA);
  }

  // TODO Revisar este test, le pega directo a la DB por las responsabilidades de cada clase.

//  @DisplayName("Se pueden consultar las últimas publicaciones de todas las asociaciones del sistema")
//  @Test
//  void seObtieneUnaListaDeLasPublicacionesDeTodasLasAsociacionesDelSistema() {
//    Publicacion unaPublicacionDeBuenosAires = mascotaRescatadaEnBuenosAires();
//    Publicacion unaPublicacionDePosadas = mascotaRescatadaEnPosadas();
//
//    Usuario voluntarioBuenosAires = crearUsuario().crearUsuarioVoluntario(unaAsociacionEnBuenosAires());
//    Usuario voluntarioPosadas = crearUsuario().crearUsuarioVoluntario(unaAsociacionEnPosadas());
//
//    voluntarioBuenosAires.aceptar(unaPublicacionDeBuenosAires);
//    voluntarioPosadas.aceptar(unaPublicacionDePosadas);
//
//
//
//    Assertions.assertEquals(2, repositorioAsociaciones.getPublicacionesAceptadas().size());
//  }

  /*Funciones Auxiliares*/
//
//  private CreadorUsuarios crearUsuario() {
//    List<RestriccionesPassword> reglas = new ArrayList<>();
//
//    reglas.add(new PeoresContrasenas());
//    reglas.add(new ExpresionRegular());
//    reglas.add(new CaracterRepetido());
//    reglas.add(new CaracterSecuencial());
//
//    return new CreadorUsuarios("usuario", "sinmayuscul4!S", reglas);
//  }

  private Publicacion mascotaRescatadaEnBuenosAires() {

    builderMascotaEncontrada.setFechaRescate(LocalDate.now().minusDays(1));
    builderMascotaEncontrada.setLugarEncontrado(new Ubicacion(-34.6083, -58.3712, ""));
    builderMascotaEncontrada.setRescatista(unaPersona());

    return builderMascotaEncontrada.generarPublicacionYasociarla();
  }

  private Publicacion mascotaRescatadaEnPosadas() {

    builderMascotaEncontrada.setFechaRescate(LocalDate.now().minusDays(1));
    builderMascotaEncontrada.setLugarEncontrado(new Ubicacion(-27.36708, -55.89608, ""));
    builderMascotaEncontrada.setRescatista(unaPersona());

    return builderMascotaEncontrada.generarPublicacionYasociarla();


  }

  private Persona unaPersona() {
    return new Persona("Pepe Argento", null, null, null, null, new ContactoMail("Pepe", "mail@mail", emailSenderMock));
  }

  private Asociacion unaAsociacionEnPosadas() {
    return new Asociacion(new Ubicacion(-27.36708, -55.89608, ""));
  }

  private Asociacion unaAsociacionEnBuenosAires() {
    return new Asociacion(new Ubicacion(-34.6083, -58.3712, ""));
  }
}
