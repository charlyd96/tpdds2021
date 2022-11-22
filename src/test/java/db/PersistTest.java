package db;

import Repositorios.*;
import modelo.*;
import notificaciones.ContactoMail;
import notificaciones.EmailSenderService;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import usuario.Usuario;
import usuario.UsuarioRol;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class PersistTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  RepositorioAsociaciones repoAsoc = new RepositorioAsociaciones();
  RepositorioMascotas repoMascotas = new RepositorioMascotas();
  RepositorioDuenios repoDuenios = new RepositorioDuenios();
  EmailSenderService emailSenderMock = mock(EmailSenderService.class);
  BuilderMascotaEncontrada builderMascotaEncontrada = new BuilderMascotaEncontrada(repoAsoc);
  Asociacion asociacionEnBuenosAires = unaAsociacionEnBuenosAires();
  Mascota mascota;
  DuenioMascota duenio;


  @AfterEach
  public void despues() {
    this.rollbackTransaction(); // Comentar si se le quiere pegar a la DB
    //this.commitTransaction(); // descomentar si se le quiere pegar a la DB
  }

  @BeforeEach
  void vaciarRepo() {
    this.beginTransaction();
    emailSenderMock = mock(EmailSenderService.class);
    duenio = creoAPepeArgento();
    mascota = crearMascota(duenio);
    builderMascotaEncontrada.setMascota(mascota);
    builderMascotaEncontrada.setRescatista(santosRescatista());


  }

  @DisplayName("Cuando se elimina una asociacion la mascota de una MascotaEncontrada o su duenio persisten en la DB")
  @Test
  void laMascotaPersisteAunqueSeElimineLaAsociacion() {
    doNothing().when(emailSenderMock).notificar(any(), any());
    repoAsoc.agregar(asociacionEnBuenosAires);
    MascotaEncontrada mascota = mascotaRescatadaEnBuenosAires();
    repoAsoc.modificar(asociacionEnBuenosAires);

    Assertions.assertEquals(1, repoAsoc.buscar(asociacionEnBuenosAires.getId().intValue()).getMascotasIdentificadas().size());
    Assertions.assertEquals(1, repoAsoc.buscarTodos().size());

    repoAsoc.eliminar(repoAsoc.buscar(asociacionEnBuenosAires.getId().intValue()));
    Assertions.assertEquals(0, repoAsoc.buscarTodos().size());
    Assertions.assertEquals(mascota.getMascota(), repoMascotas.buscar(mascota.getMascota().getId().intValue()));
    Assertions.assertEquals(mascota.getMascota().getDuenio(), repoDuenios.buscar(mascota.getMascota().getDuenio().getId().intValue()));

  }


  @DisplayName("Si elimino a una mascota su dueño persiste")
  @Test
  void elDuenioPersisteAunqueSeElimineALaMascota() {
    Assertions.assertEquals(mascota, repoMascotas.buscar(mascota.getId().intValue()));
    Assertions.assertEquals(repoMascotas.buscar(mascota.getId().intValue()).getDuenio(), repoDuenios.buscar(duenio.getId().intValue()));

    repoMascotas.eliminar(mascota);

    Assertions.assertEquals(0, repoMascotas.buscarTodos().size());
    Assertions.assertEquals(duenio, repoDuenios.buscar(duenio.getId().intValue()));
  }

  @DisplayName("El sistema arroja una excepcion cuando una entidad no fue persistida y se la busca en la DB")
  @Test
  void excepcionAlBuscarUnaEntidadNoPersistida() {

    Exception miException = null;
    DuenioMascota duenioNoPersistido = new DuenioMascota(new Persona("Pepe Argento", null, null, null, new Ubicacion(null, null, ""),
        new ContactoMail("Pepe", "pepe@racing", emailSenderMock)), new Usuario("PepeArg", "fatiga05", UsuarioRol.NORMAL));

    try {
      repoDuenios.buscar(duenioNoPersistido.getId().intValue());
    } catch (Exception e) {
      miException = e;
    }

    Assertions.assertEquals(miException.toString(), "java.lang.NullPointerException");
  }


  @DisplayName("Cuando elimino a un dueño elimino a todas sus mascotas")
  @Test
  void eliminoAlDueñoYEliminoATodasSusMascotas() {
    Assertions.assertEquals(mascota, repoMascotas.buscar(mascota.getId().intValue()));
    Assertions.assertEquals(repoMascotas.buscar(mascota.getId().intValue()).getDuenio(), repoDuenios.buscar(duenio.getId().intValue()));

    repoDuenios.eliminar(duenio);

    Assertions.assertEquals(0, repoMascotas.buscarTodos().size());
    Assertions.assertEquals(0, repoDuenios.buscarTodos().size());

  }


  private Persona santosRescatista() {
    return new Persona("MaximoCosetti", null,
        TipoDocumento.DNI, "99999999", new Ubicacion(null, null, null),
        new ContactoMail("Mario Santos", "santos@simuladores", emailSenderMock));
  }

  private DuenioMascota creoAPepeArgento() {

    Persona pepe = new Persona("Pepe Argento", null, null, null, new Ubicacion(null, null, ""),
        new ContactoMail("Pepe", "pepe@racing", emailSenderMock));

    DuenioMascota duenio = new DuenioMascota(pepe, new Usuario("PepeArg", "fatiga05", UsuarioRol.NORMAL));
    repoDuenios.agregar(duenio);
    return duenio;
  }

  private Mascota crearMascota(DuenioMascota duenio) {
    Mascota mascota = new Mascota(TipoMascota.PERRO, "Firulais", "chicho", TamanioMascota.CHIQUITA,
        LocalDate.of(2017, 8, 23), Sexo.OTRO, "pequeño, no lo pises :c", null, duenio);
    repoMascotas.agregar(mascota);
    return mascota;
  }

  private Asociacion unaAsociacionEnBuenosAires() {
    return new Asociacion(new Ubicacion(-34.6083, -58.3712, ""));
  }

  private MascotaEncontrada mascotaRescatadaEnBuenosAires() {

    builderMascotaEncontrada.setFechaRescate(LocalDate.now().minusDays(1));
    builderMascotaEncontrada.setLugarEncontrado(new Ubicacion(-34.6083, -58.3712, ""));
    return builderMascotaEncontrada.generarMascotaConChapitaYasociarla();
  }
}
