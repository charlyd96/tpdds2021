package adopcion;

import modelo.*;
import notificaciones.ContactoMail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import usuario.Usuario;
import usuario.UsuarioRol;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.mock;

public class TestRespuestasAdopcion {

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
  PreguntaAdopcion vacunado = new PreguntaAdopcion("vacunado",
      "¿Esta castrado?",
      "¿La mascota tiene que estar castrada", Arrays.asList("SI","NO"));

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

  MascotaEnAdopcion garfieldAdopcion = new MascotaEnAdopcion(garfield,
      Arrays.asList(sinPatio,seLlevaPiolaConOtrasMascotas,colorNaranja));
  MascotaEnAdopcion fatigaAdopcion = new MascotaEnAdopcion(fatiga,
      Arrays.asList(conPatio,seLlevaPiolaConOtrasMascotas,colorMARRON));
  MascotaEnAdopcion dylanAdopcion = new MascotaEnAdopcion(dylan,
      Arrays.asList(noSeLlevaConMascotas,colorManchitas));

  @DisplayName("Creo una MascotaEnAdopcion con sus preferencias OK")
  @Test
  void puedoCrearOkLasRespuestasParaUnaMascotaEnAdopcion(){

    Mascota lassie = crearMascota(TipoMascota.PERRO, "lassie",duenoGenerico);

    RespuestaPreguntaAdopcion masBuenoQueTodos  = new RespuestaPreguntaAdopcion(seLlevaBienConOtrasMascotas,"SI");
    RespuestaPreguntaAdopcion estaVacunado = new RespuestaPreguntaAdopcion(vacunado,"NI IDEA");

    MascotaEnAdopcion lassieEnAdopcion = new MascotaEnAdopcion(lassie,Arrays.asList(masBuenoQueTodos,estaVacunado));

    Assertions.assertEquals(2,lassieEnAdopcion.getCondicionesAdopcion().size());
  }

  @DisplayName("Creo Una Intencion De Adoptar Con Preferencias OK")
  @Test
  void puedoCrearOkLasRespuestasParaIntencionDeAdoptar(){

    Persona adoptante = new Persona("adoptante",null,
        null,null,null,contactoMailMock);

    RespuestaPreguntaAdopcion masBuenoQueTodos  = new RespuestaPreguntaAdopcion(seLlevaBienConOtrasMascotas,"SI");
    RespuestaPreguntaAdopcion colorNEGRO = new RespuestaPreguntaAdopcion(color,"NEGRO");

    IntencionAdoptar intencionAdoptar = new IntencionAdoptar(Arrays.asList(masBuenoQueTodos,colorNEGRO),
        adoptante, TipoMascota.PERRO);

    Assertions.assertEquals(2,intencionAdoptar.getPreferencias().size());

  }

  @DisplayName("No coincide intención de adoptar con MascotaEnAdopción si no cumplen con las preguntas/rtas")
  @Test
  void siNoRespetanMismasRtasArrojaFalse()
  {

    IntencionAdoptar intencion = new IntencionAdoptar
        (Arrays.asList(sinPatio,seLlevaPiolaConOtrasMascotas,colorNaranja),null,TipoMascota.GATO);

    Assertions.assertFalse(intencion.coincideCon(dylanAdopcion));
  }

  @DisplayName("Coincide intención de adoptar con MascotaEnAdopción  si las preguntas/rtas son iguales ")
  @Test
  void siRespetanMismasRtasArrojaVerdadero()
  {

    IntencionAdoptar intencion = new IntencionAdoptar
        (Arrays.asList(sinPatio,seLlevaPiolaConOtrasMascotas,colorNaranja),null,TipoMascota.GATO);

    Assertions.assertTrue(intencion.coincideCon(garfieldAdopcion));
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



}
