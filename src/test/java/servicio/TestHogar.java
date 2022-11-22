package servicio;

import Repositorios.RepositorioAsociaciones;
import Repositorios.RepositorioHogares;
import caracteristica.Atributo;
import caracteristica.CaracteristicaConValores;
import modelo.*;
import notificaciones.ContactoMail;
import notificaciones.EmailSenderService;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import usuario.Usuario;
import usuario.UsuarioRol;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class TestHogar   extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction(); // Comentar si se le quiere pegar a la DB
//    this.commitTransaction(); // descomentar si se le quiere pegar a la DB
  }
/*

    ===============================================
     ------------------Setup inicial-----------------
     ================================================
*/

  CaracteristicaConValores revoltoso = new CaracteristicaConValores("REVOLTOSO",
      Arrays.asList("SI", "NO"));
  CaracteristicaConValores tranquilo = new CaracteristicaConValores("TRANQUILO",
      Arrays.asList("SI", "NO"));
//  CaracteristicaConValores tamano = new CaracteristicaConValores("TAMAÑO",
//      Arrays.asList("CHICO", "MEDIANO", "GRANDE"));

  Hogar aceptaPerrosYGatosTranquilosConPatioEnMarDelPlata = new Hogar(null, "Hogar Patitas - Mar del Plata", new UbicacionHogarAPI("", -38.0033, -57.5528), null, new Admisiones(true, true), 10, 10, true, Arrays.asList("TRANQUILO"));
  Hogar aceptaPerrosYGatosTranquilosSinPatioEnBariloche = new Hogar(null, "Hogar Nahuel Huapi - Bariloche", new UbicacionHogarAPI("", -41.14557, -71.30822), null, new Admisiones(true, true), 10, 10, false, Arrays.asList("TRANQUILO"));
  Hogar aceptaPerrosRevoltososConPatioEnBahiaBlanca = new Hogar(null, "Zaguates Refugio - Bahía Blanca", new UbicacionHogarAPI("", -38.71959, -62.27243), null, new Admisiones(true, false), 15, 15, true, Arrays.asList("REVOLTOSO"));
  Hogar aceptaGatosRevoltososConPatioEnPosadas = new Hogar(null, "Hogar Moquillo - Posadas", new UbicacionHogarAPI("", -27.36708, -55.89608), null, new Admisiones(false, true), 10, 15, true, Arrays.asList("REVOLTOSO"));
  Hogar aceptaGatosSinCaracteristicasConPatioEnQuito = new Hogar(null, "Hogar Quito - Ecuador", new UbicacionHogarAPI("", -0.225219, -78.5248), null, new Admisiones(false, true), 10, 15, true, Arrays.asList());
  Hogar aceptaPerrosSinCaracteristicasConPatioEnBogota = new Hogar(null, "Hogar Bogota - Colombia", new UbicacionHogarAPI("", 4.60971, -74.08175), null, new Admisiones(true, false), 10, 15, true, Arrays.asList());


  List<Hogar> listaHogares = Arrays.asList(
      aceptaPerrosYGatosTranquilosConPatioEnMarDelPlata,
      aceptaPerrosYGatosTranquilosSinPatioEnBariloche,
      aceptaPerrosRevoltososConPatioEnBahiaBlanca,
      aceptaGatosRevoltososConPatioEnPosadas,
      aceptaGatosSinCaracteristicasConPatioEnQuito,
      aceptaPerrosSinCaracteristicasConPatioEnBogota);

  EmailSenderService emailMock = mock(EmailSenderService.class);
  ServicioGetPostApi apiMock = mock(ServicioGetPostApi.class);

  RepositorioAsociaciones repoAsoc = new RepositorioAsociaciones();

  RepositorioHogares repoHogares = new RepositorioHogares(apiMock);

  BuilderMascotaEncontrada builderMascotaEncontrada;

  Asociacion asociacionBsAs = unaAsocicionEnBuenosAires();
  Asociacion asociacionPosadas = unaAsocicionEnPosadas();


  @BeforeEach
  void init() {
    builderMascotaEncontrada = new BuilderMascotaEncontrada(repoAsoc);
    repoAsoc.agregar(asociacionBsAs);
    repoAsoc.agregar(asociacionPosadas);
  }

//  ===============================================
//   ------------------Test distancia----------------
//   ================================================
  @DisplayName("Si no hay hogares dentro del radio indicado (10km), la lista de hogares está vacía")
  @Test
  void noHayHogaresEnElRadioIndicado() {
    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires
    List<Hogar> hogarQueCumplen = listaHogares.stream()
        .filter(hogar -> repoHogares.estaDentroDelRadio(10, hogar, unPersona.getDireccion()))
        .collect(Collectors.toList());

    Assertions.assertEquals(0, hogarQueCumplen.size());
  }

  @DisplayName("Si hay hogares dentro del radio indicado (400km), la lista tiene a esos hogares")
  @Test
  void hayHogaresEnElRadioIndicado() {
    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires
    List<Hogar> hogarQueCumplen = listaHogares.stream()
        .filter(hogar -> repoHogares.estaDentroDelRadio(400, hogar, unPersona.getDireccion()))
        .collect(Collectors.toList());

    Assertions.assertTrue(hogarQueCumplen.size() > 0);
  }
//
//     ===============================================
//     -------------Test informar al dueño--------------
//     ================================================


  @DisplayName("Un Persona informa que encontro una mascota con chapita y su dueño es notificado y su estado pasa a ser RECLAMADA")
  @Test
  void seInformaUnaMascotaEncontradaYElDuenioEsNotificado() {
    Persona unPersona = unPersona(-34.6085, -58.3712);

    MascotaEncontrada mascotaEncontradaEnBuenosAires = unPerroRevoltoso(unPersona);

    doNothing().when(emailMock).notificar(any(), any());
    verify(emailMock, times(1)).notificar(any(), any());

    Assertions.assertEquals(1,asociacionBsAs.getMascotasIdentificadas().size());
    Assertions.assertEquals(0,asociacionPosadas.getMascotasIdentificadas().size());
    Assertions.assertEquals(EstadoMascotaRescatada.RECLAMADA, mascotaEncontradaEnBuenosAires.getEstadoMascotaRescatada());

  }

//
//
//    ===============================================
//     ------------------Test gatos--------------------
//     ================================================

  @DisplayName("No hay hogar que reciba a un gato sin chapita (sin características) en un radio de 2000km")
  @Test
  void noSeRecibeUnGatoSinCaracteristicasEn2000km() {
    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);
    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires
    Publicacion publicacionDeUnGatoSinChapita = unGatoSinCaracteristicas(unPersona);
    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaSinChapitaYEstanCerca(unPersona.getDireccion(), publicacionDeUnGatoSinChapita, 2000);
    Assertions.assertEquals(0, hogarQueCumplen.size());
  }

  @DisplayName("Hay un hogar en Quito con patio que recibe a un gato sin chapita (sin características) en un radio de 5000km")
  @Test
  void seRecibeUnGatoSinCaracteristicasEn5000km() {
    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);
    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires

    Publicacion publicacionDeUnGatoSinChapita = unGatoSinCaracteristicas(unPersona);
    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaSinChapitaYEstanCerca(unPersona.getDireccion(), publicacionDeUnGatoSinChapita, 5000);

    Assertions.assertTrue(hogarQueCumplen.size() > 0);
  }

  @DisplayName("Hay un hogar en Posadas que recibe a un gato revoltoso con chapita en un radio de 2000km")
  @Test
  void seRecibeUnGatoRevoltosoConChapita() {
    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);
    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires
    MascotaEncontrada gatoRevoltoso = unGatoRevoltoso(unPersona);

    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaConChapitaYEstanCerca(gatoRevoltoso.getLugarEncontrado(), gatoRevoltoso, 2000);
    Assertions.assertEquals(1, hogarQueCumplen.size());
  }

  @DisplayName("Hay hogares en Bariloche y Mar del Plata que reciben a un gato tranquilo y chico en un radio de 2000km")
  @Test
  void seRecibeUnGatoTranquiloYChico() {
    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);
    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires
    MascotaEncontrada gatoTranquiloYChico = unGatoTranquiloYChico(unPersona);
    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaConChapitaYEstanCerca(gatoTranquiloYChico.getLugarEncontrado(), gatoTranquiloYChico, 2000);
    Assertions.assertTrue(hogarQueCumplen.size() > 1);
  }

  @DisplayName("No hay hogares que reciban a un gato revoltoso con chapita en un radio de 2000km por falta de disponibilidad")
  @Test
  void noSeRecibeUnGatoRevoltosoConChapitaPorFaltaDeDisponibilidad() {
    listaHogares.forEach(hogar -> hogar.setLugaresDisponibles(0));
    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);

    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires
    MascotaEncontrada gatoRevoltoso = unGatoRevoltoso(unPersona);
    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaConChapitaYEstanCerca(gatoRevoltoso.getLugarEncontrado(), gatoRevoltoso, 2000);
    Assertions.assertEquals(0, hogarQueCumplen.size());
  }
//
//  ===============================================
//   ------------------Test perros--------------------
//   ================================================
  @DisplayName("No hay hogar que reciba a un perro sin chapita (sin características) en un radio de 2000km")
  @Test
  void noSeRecibeUnPerroSinCaracteristicasEn2000km() {
    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);

    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires

    Publicacion unPerroSinCaracteristicas = unPerroSinCaracteristicas(unPersona);

    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaSinChapitaYEstanCerca(unPersona.getDireccion(), unPerroSinCaracteristicas, 2000);

    Assertions.assertEquals(0, hogarQueCumplen.size());
  }

  @DisplayName("Hay un hogar en Bogota con patio que recibe a un perro sin chapita (sin características) en un radio de 5000km")
  @Test
  void seRecibeUnPerroSinCaracteristicasEn5000km() {
    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);

    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires
    Publicacion unPerroSinCaracteristicas = unPerroSinCaracteristicas(unPersona);
    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaSinChapitaYEstanCerca(unPersona.getDireccion(), unPerroSinCaracteristicas, 5000);
    Assertions.assertTrue(hogarQueCumplen.size() > 0);
  }

  @DisplayName("Hay un hogar en Posadas que recibe a un perro revoltoso con chapita en un radio de 2000km")
  @Test
  void seRecibeUnPerroRevoltosoConChapita() {
    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);

    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires
    MascotaEncontrada perroRevoltoso = unPerroRevoltoso(unPersona);
    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaConChapitaYEstanCerca(unPersona.getDireccion(), perroRevoltoso, 2000);
    Assertions.assertEquals(1, hogarQueCumplen.size());
  }

  @DisplayName("Hay hogares en Bariloche y Mar del Plata que reciben a un perro tranquilo y chico con chapita en un radio de 2000km")
  @Test
  void seRecibeUnPerroTranquiloYChico() {
    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);

    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires

    MascotaEncontrada perroTranquiloYChico = unPerroTranquiloYChico(unPersona);

    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaConChapitaYEstanCerca(unPersona.getDireccion(), perroTranquiloYChico, 2000);

    Assertions.assertTrue(hogarQueCumplen.size() > 1);
  }


  @DisplayName("No hay hogares que reciban a un perro revoltoso con chapita en un radio de 2000km por falta de disponibilidad")
  @Test
  void noSeRecibeUnPerroRevoltosoConChapitaPorFaltaDeDisponibilidad() {
    listaHogares.forEach(hogar -> hogar.setLugaresDisponibles(0));

    when(apiMock.respuestaDeHogaresApi()).thenReturn(listaHogares);

    Persona unPersona = unPersona(-34.6083, -58.3712); //Buenos Aires
    MascotaEncontrada perroRevoltoso = unPerroRevoltoso(unPersona);
    List<Hogar> hogarQueCumplen = repoHogares.hogaresQueAceptanLaMascotaConChapitaYEstanCerca(unPersona.getDireccion(), perroRevoltoso, 2000);
    Assertions.assertEquals(0, hogarQueCumplen.size());
  }

//
//  ===============================================
//   -------------Funciones auxiliares---------------
//   ================================================

  private Persona unPersona(Double latitud, Double longitud) {
    return new Persona("Pepe Argento",
        null,
        null,
        null,
        new Ubicacion(latitud, longitud, ""),
        new ContactoMail("Pepe Argento", "pepeArgentoRacing@gmail.com", emailMock)) {
    };
  }

  public Publicacion unGatoSinCaracteristicas(Persona unPersona) {

    builderMascotaEncontrada.setMascota(new Mascota(TipoMascota.GATO, "Pimienta", "Pimi", TamanioMascota.MEDIANA, LocalDate.of(2017, 9, 16), Sexo.FEMENINO, "Pelo corto tricolor", null, creoAPepeArgento()));
    builderMascotaEncontrada.setRescatista(unPersona);
    builderMascotaEncontrada.setLugarEncontrado(unPersona.getDireccion());
    return builderMascotaEncontrada.generarPublicacionYasociarla();
  }

  public MascotaEncontrada unGatoRevoltoso(Persona unPersona) {
    Mascota gatoRevoltoso = new Mascota(TipoMascota.GATO, "Garfield", "Michi", TamanioMascota.GRANDE, LocalDate.of(2011, 7, 19), Sexo.MASCULINO, "Peludo", null, creoAPepeArgento());
    gatoRevoltoso.agregarAtributo(new Atributo(revoltoso, "SI"));


    builderMascotaEncontrada.setMascota(gatoRevoltoso);
    builderMascotaEncontrada.setRescatista(unPersona);
    builderMascotaEncontrada.setLugarEncontrado(unPersona.getDireccion());
    return builderMascotaEncontrada.generarMascotaConChapitaYasociarla();
  }

  public MascotaEncontrada unGatoTranquiloYChico(Persona unPersona) {
    Mascota gatoTranquiloYChico = new Mascota(TipoMascota.GATO, "Nermal", "Nerm", TamanioMascota.CHIQUITA, LocalDate.of(2020, 7, 19), Sexo.MASCULINO, "Pelo corto", null, creoAPepeArgento());
    gatoTranquiloYChico.agregarAtributo(new Atributo(tranquilo, "SI"));


    builderMascotaEncontrada.setMascota(gatoTranquiloYChico);
    builderMascotaEncontrada.setRescatista(unPersona);
    builderMascotaEncontrada.setLugarEncontrado(unPersona.getDireccion());
    return builderMascotaEncontrada.generarMascotaConChapitaYasociarla();
  }

  public Publicacion unPerroSinCaracteristicas(Persona unPersona) {


    builderMascotaEncontrada.setTipo(TipoMascota.PERRO);
    builderMascotaEncontrada.setRescatista(unPersona);
    builderMascotaEncontrada.setLugarEncontrado(unPersona.getDireccion());
    return builderMascotaEncontrada.generarPublicacionYasociarla();
  }

  public MascotaEncontrada unPerroRevoltoso(Persona rescatista) {
    Mascota perroRevoltoso = new Mascota(TipoMascota.PERRO, "Fatiga", "Fatiga", TamanioMascota.MEDIANA, LocalDate.of(2011, 7, 19), Sexo.MASCULINO, "Peludo", null, creoAPepeArgento());
    perroRevoltoso.agregarAtributo(new Atributo(revoltoso, "SI"));

    builderMascotaEncontrada.setMascota(perroRevoltoso);
    builderMascotaEncontrada.setRescatista(rescatista);
    builderMascotaEncontrada.setLugarEncontrado(new Ubicacion(-34.6090, -58.3700, "Directorio 156"));

    return builderMascotaEncontrada.generarMascotaConChapitaYasociarla();
  }

  public MascotaEncontrada unPerroTranquiloYChico(Persona unPersona) {
    Mascota perroTranquiloYChico = new Mascota(TipoMascota.PERRO, "Fatiga", "Fatiga", TamanioMascota.CHIQUITA, LocalDate.of(2011, 7, 19), Sexo.MASCULINO, "Peludo", null, creoAPepeArgento());
    perroTranquiloYChico.agregarAtributo(new Atributo(tranquilo, "SI"));


    builderMascotaEncontrada.setMascota(perroTranquiloYChico);
    builderMascotaEncontrada.setRescatista(unPersona);
    builderMascotaEncontrada.setLugarEncontrado(unPersona.getDireccion());

    return builderMascotaEncontrada.generarMascotaConChapitaYasociarla();
  }

  private DuenioMascota creoAPepeArgento() {
       return new DuenioMascota( unPersona(-34.6083, -58.3712),
           new Usuario("PepeArg", "fatiga05", UsuarioRol.NORMAL));
  }

  private Asociacion unaAsocicionEnBuenosAires() {
    return new Asociacion(new Ubicacion(-34.6083, -58.3712, "Directorio 1256"));
  }

  private Asociacion unaAsocicionEnPosadas() {
    return new Asociacion(new Ubicacion(-27.36708, -55.89608, ""));
  }
}