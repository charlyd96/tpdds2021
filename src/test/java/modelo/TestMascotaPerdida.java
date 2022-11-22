package modelo;

import Repositorios.RepositorioAsociaciones;
import notificaciones.ContactoMail;
import notificaciones.EmailSenderService;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import usuario.Usuario;
import usuario.UsuarioRol;

import static org.mockito.Mockito.*;

import java.time.LocalDate;


public class TestMascotaPerdida extends AbstractPersistenceTest implements WithGlobalEntityManager {

    @BeforeEach
    public void antes() {
        this.beginTransaction();
    }

    @AfterEach
    public void despues() {
        this.rollbackTransaction(); // Comentar si se le quiere pegar a la DB
//    this.commitTransaction(); // descomentar si se le quiere pegar a la DB
    }

    RepositorioAsociaciones repoAsoc = new RepositorioAsociaciones();

    Asociacion asociacionEnBuenosAires = unaAsociacionEnBuenosAires();
    Asociacion asociacion = new Asociacion(null);
    Mascota mascota = crearMascota();
    EmailSenderService emailSenderMock = mock(EmailSenderService.class);
    BuilderMascotaEncontrada builderMascotaEncontrada = new BuilderMascotaEncontrada(repoAsoc);
    MascotaEncontrada mascotaEncontradaHaceMasDeDiez = new MascotaEncontrada(null,null,null,
        LocalDate.now().minusDays(30),null,santosRescatista(),null,mascota);
    MascotaEncontrada mascotaEncontradaHaceMenosDeDiez = new MascotaEncontrada(null,null,null,
        LocalDate.now().minusDays(1),null,santosRescatista(),null,mascota);



    @BeforeEach
    void vaciarRepo() {
        emailSenderMock = mock(EmailSenderService.class);
        builderMascotaEncontrada.setMascota(mascota);
        builderMascotaEncontrada.setRescatista(santosRescatista());
    }

    @DisplayName("Rescatista informa mascota encontrada con chapita y es notificado con datos del dueno")
    @Test
    void rescatistaInformadoCuandoInformaMascotaConChapita(){

        doNothing().when(emailSenderMock).notificar(any(),any());

        repoAsoc.agregar(asociacionEnBuenosAires);
        MascotaEncontrada mascota = mascotaRescatadaEnBuenosAires();
        verify(emailSenderMock,times(1)).notificar(any(),any());

    }

    @DisplayName("Si rescate mascotas hace mas de 10 diaz, solo consulto las de los ultimos 10")
    @Test
    void siAgregoUnaMascotaRescatadaMayoraDiezDiasyUnaMenorADiezDiasDevuelvoSoloLaPrimera() {
        doNothing().when(emailSenderMock).notificar(any(),any());

        asociacion.agregarMascotaConChapita(mascotaEncontradaHaceMasDeDiez);
        asociacion.agregarMascotaConChapita(mascotaEncontradaHaceMenosDeDiez);


        Assertions.assertEquals(asociacion
            .getMascotasRescatadasUltNDias(10).size(), 1);
    }

    @DisplayName("Si no rescate mascotas en los ultimos 10 dias, me devuelve una lista vacia")
    @Test
    void siNoRescateMascotasEnLosUltimos10DiazLaListaEstaVacia() {
        doNothing().when(emailSenderMock).notificar(any(),any());

        asociacion.agregarMascotaConChapita(mascotaEncontradaHaceMasDeDiez);
        Assertions.assertEquals(asociacion.getMascotasRescatadasUltNDias(10).size(), 0);

    }

    @DisplayName("Una mascota recien encontrada tiene un estado de EN_TRANSITO")
    @Test
    void unaMascotaRecienEncontradaEstaEnTransito() {

        MascotaEncontrada mascotaEncontradaSinChapita = mascotaEncontradaHaceMasDeDiez;

        Assertions.assertEquals(mascotaEncontradaSinChapita.getEstadoMascotaRescatada(),
            EstadoMascotaRescatada.EN_TRANSITO);

    }



    private Persona santosRescatista() {
        return new Persona("MaximoCosetti", null,
            TipoDocumento.DNI, "99999999", new Ubicacion(null, null, null),
            new ContactoMail("Mario Santos", "santos@simuladores", emailSenderMock));
    }

    private DuenioMascota creoAPepeArgento() {

         Persona pepe = new Persona("Pepe Argento", null, null, null, new Ubicacion(null, null, ""),
             new ContactoMail("Pepe", "pepe@racing", emailSenderMock));

        return new DuenioMascota(pepe, new Usuario("PepeArg", "fatiga05", UsuarioRol.NORMAL));
    }

    private Mascota crearMascota() {
        return new Mascota(TipoMascota.PERRO, "Firulais", "chicho", TamanioMascota.CHIQUITA,
            LocalDate.of(2017, 8, 23), Sexo.OTRO, "pequeño, no lo pises :c", null, creoAPepeArgento());
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

