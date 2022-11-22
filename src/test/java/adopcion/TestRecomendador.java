package adopcion;

import Repositorios.RepositorioAsociaciones;
import Repositorios.factories.FactoryRepositorioAsociaciones;
import modelo.*;
import notificaciones.Contacto;
import notificaciones.ContactoMail;
import notificaciones.ContactoSMS;
import notificaciones.EmailSenderService;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import usuario.Usuario;
import usuario.UsuarioRol;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestRecomendador extends AbstractPersistenceTest implements WithGlobalEntityManager {

  ContactoMail contactoMailMock = mock(ContactoMail.class);

  PreguntaAdopcion patio = new PreguntaAdopcion("Patio",
      "¿Necesitas que el adoptante tenga patio?",
      "¿Tenés patio?", Arrays.asList("SI","NO"));
  PreguntaAdopcion seLlevaBienConOtrasMascotas = new PreguntaAdopcion("Convivencia",
      "¿Tu mascota se lleva bien con otras mascotas?",
      "Tenes otras mascotas?", Arrays.asList("SI", "NO"));
  PreguntaAdopcion  color = new PreguntaAdopcion("Color",
      "¿De que de color es su pelaje",
      "De que color te gustaria que sea su pelo",
      Arrays.asList("MARRON","NEGRO","BLANCO","MACHITAS","GRIS","NARANJA"));
  PreguntaAdopcion amigableConChicos = new PreguntaAdopcion("ninos",
      "¿Es amigable con chicos pequenos?",
      "¿Tenes hijos pequenos?", Arrays.asList("SI","NO"));
  PreguntaAdopcion vacunado = new PreguntaAdopcion("vacunado",
      "¿Esta castrado?",
      "¿La mascota tiene que estar castrada", Arrays.asList("SI","NO"));

  Persona persona = new Persona(null,
      null, null, null, null, contactoMailMock);

  DuenioMascota duenoGenerico = duenoGenerico();

  Mascota garfield = crearMascota(TipoMascota.GATO, "Garfield",duenoGenerico);
  Mascota fatiga = crearMascota(TipoMascota.PERRO, "Fatiga",duenoGenerico);
  Mascota dylan = crearMascota(TipoMascota.PERRO, "Dylan",duenoGenerico);

  RespuestaPreguntaAdopcion conPatio = new RespuestaPreguntaAdopcion(patio,"SI");
  RespuestaPreguntaAdopcion sinPatio = new RespuestaPreguntaAdopcion(patio,"NO");

  RespuestaPreguntaAdopcion seLlevaPiolaConOtrasMascotas  = new RespuestaPreguntaAdopcion(seLlevaBienConOtrasMascotas,"SI");
  RespuestaPreguntaAdopcion noSeLlevaConMascotas= new RespuestaPreguntaAdopcion(seLlevaBienConOtrasMascotas,"NO");

  RespuestaPreguntaAdopcion colorMARRON = new RespuestaPreguntaAdopcion(color,"MARRON");
  RespuestaPreguntaAdopcion colorManchitas = new RespuestaPreguntaAdopcion(patio,"MACHITAS");
  RespuestaPreguntaAdopcion colorNaranja = new RespuestaPreguntaAdopcion(patio,"NARANJA");

  MascotaEnAdopcion GarfieldAdopcionSinPatio = new MascotaEnAdopcion(garfield,
      Arrays.asList(sinPatio,seLlevaPiolaConOtrasMascotas,colorNaranja));
  MascotaEnAdopcion GarfieldAdopcionConPatio = new MascotaEnAdopcion(garfield,
      Arrays.asList(conPatio,seLlevaPiolaConOtrasMascotas,colorNaranja));
  MascotaEnAdopcion GarfieldAdopcionMarron = new MascotaEnAdopcion(garfield,
      Arrays.asList(sinPatio,seLlevaPiolaConOtrasMascotas,colorMARRON));
  MascotaEnAdopcion fatigaAdopcion = new MascotaEnAdopcion(fatiga,
      Arrays.asList(conPatio,seLlevaPiolaConOtrasMascotas,colorMARRON));
  MascotaEnAdopcion dylanAdopcion = new MascotaEnAdopcion(dylan,
      Arrays.asList(noSeLlevaConMascotas,colorManchitas));


  EmailSenderService emailSenderMock = mock(EmailSenderService.class);
  ContactoMail guidoMail = new ContactoMail("Guido Martinez", "carlosdesiderio96@gmail.com",
      emailSenderMock);
  Persona guidoEnviador = new Persona("Julian",
      null,null,null,null, guidoMail);


  Asociacion asociacion1 = new Asociacion(null);

  IntencionAdoptar buscoGatoSinPatioyAmistosa = new IntencionAdoptar(Arrays.asList(seLlevaPiolaConOtrasMascotas,sinPatio),guidoEnviador,TipoMascota.GATO);
  IntencionAdoptar buscoGatoConPatio = new IntencionAdoptar(Arrays.asList(conPatio),guidoEnviador,TipoMascota.GATO);
  IntencionAdoptar buscoGatoNaranja = new IntencionAdoptar(Arrays.asList(colorNaranja),guidoEnviador,TipoMascota.GATO);
  IntencionAdoptar buscoGatoSinPatio = new IntencionAdoptar(Arrays.asList(sinPatio),guidoEnviador,TipoMascota.GATO);
  IntencionAdoptar buscoPerroConPatio = new IntencionAdoptar(Arrays.asList(conPatio),guidoEnviador,TipoMascota.PERRO);
  IntencionAdoptar buscoPerroConPatioyOrtiva = new IntencionAdoptar(Arrays.asList(conPatio,noSeLlevaConMascotas),guidoEnviador,TipoMascota.PERRO);
  IntencionAdoptar buscoPerroConManchas = new IntencionAdoptar(Arrays.asList(colorManchitas),guidoEnviador,TipoMascota.PERRO);

  List<Asociacion> asociaciones = new ArrayList<>();
  GeneradorRecomendacionesAdopcion generadorRecomendacionesAdopcion = new GeneradorRecomendacionesAdopcion(asociaciones);

  @BeforeEach
      public void antesDe() {
    this.beginTransaction();
    asociaciones.clear();
    asociaciones.add(asociacion1);
//    GeneradorRecomendacionesAdopcion generadorRecomendacionesAdopcion = new GeneradorRecomendacionesAdopcion(asociaciones);
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction(); // Comentar si se le quiere pegar a la DB
    //this.commitTransaction(); // descomentar si se le quiere pegar a la DB
  }


  @DisplayName("Interesado en adoptar Gato Sin Patio, Filtra OK y brinda recomendaciones correctas..")
  @Test
  void recomendaBienSegunFiltros() {
    asociacion1.agregarIntencionDeAdoptar(buscoGatoSinPatio);
    asociacion1.agregarMascotaEnAdopcion(GarfieldAdopcionSinPatio);
    asociacion1.agregarMascotaEnAdopcion(GarfieldAdopcionMarron);
    asociacion1.agregarMascotaEnAdopcion(GarfieldAdopcionConPatio);

    RepositorioAsociaciones repoAsoc = getAsociaciones();

    Assertions.assertEquals
      (2,
      generadorRecomendacionesAdopcion.generarRecomendacionParticular(
          buscoGatoSinPatio, repoAsoc.buscar(asociacion1.getId().intValue()).getMascotasEnAdopcion()).getMascotasEnAdopcionRecomendadas().size()) ;
  }




  @DisplayName("Interesado en adoptar no tiene recomendacion ya que no hay compatibles")
  @Test
  void noHayRecomendacionDisponibleEntoncesNoRealizaNada() {
    asociacion1.agregarIntencionDeAdoptar(buscoPerroConPatioyOrtiva);
    asociacion1.agregarMascotaEnAdopcion(GarfieldAdopcionSinPatio);
    asociacion1.agregarMascotaEnAdopcion(GarfieldAdopcionMarron);
    asociacion1.agregarMascotaEnAdopcion(GarfieldAdopcionConPatio);

    RepositorioAsociaciones repoAsoc = getAsociaciones();

    Assertions.assertNull(generadorRecomendacionesAdopcion.generarRecomendacionParticular(buscoPerroConPatioyOrtiva,repoAsoc.buscar(asociacion1.getId().intValue()).getMascotasEnAdopcion()));
  }

  //TODO Revisar este test porque no se esta validando nada referente a la recomendacion
  @DisplayName("Generar recomendaciones para todas mis asociaciones")
  @Test
  void recomendacionesGenerales() {
    Asociacion asociacion2 = new Asociacion(new Ubicacion(1.0,2.0,"Calle Falsa 123"));
    asociacion2.agregarIntencionDeAdoptar(buscoPerroConPatioyOrtiva);
    asociacion2.agregarIntencionDeAdoptar(buscoGatoSinPatio);
    asociacion2.agregarIntencionDeAdoptar(buscoGatoNaranja);
    asociacion2.agregarMascotaEnAdopcion(GarfieldAdopcionSinPatio);
    asociacion2.agregarMascotaEnAdopcion(GarfieldAdopcionMarron);
    asociacion2.agregarMascotaEnAdopcion(GarfieldAdopcionConPatio);


    RepositorioAsociaciones repoAsoc = getAsociaciones();
    //repoAsoc.agregar(asociacion2);

    generadorRecomendacionesAdopcion.generarRecomendacion();
    verify(emailSenderMock,times(3)).notificar(any(),any());
    // 1 x cada intención de adoptar + 2 por las dos masquetas (GatoSinPatio y GatoNaranja) que cruza
  }
  
  private DuenioMascota duenoGenerico(){

    Persona dueno = new Persona("Pepe Argento",
        LocalDate.of(2012, 5, 25),
        TipoDocumento.DNI,
        "11229750",
        new Ubicacion(-34.63333,-58.46667, "Bajo Flores"), contactoMailMock);

    return  new DuenioMascota( dueno, new Usuario("uss" , "fatiga05" , UsuarioRol.NORMAL));
  }
  private Mascota crearMascota(TipoMascota tipo, String nombre, DuenioMascota dueno){
    return new Mascota(tipo,nombre,"chicho", TamanioMascota.CHIQUITA,
        LocalDate.of(2017,8,23), Sexo.OTRO,
        "pequeño, no lo pises :c", null,dueno);

  }

  private RepositorioAsociaciones getAsociaciones() {
    RepositorioAsociaciones repoAsoc = new RepositorioAsociaciones();
    repoAsoc.agregar(asociacion1);
    return repoAsoc;
  }
}