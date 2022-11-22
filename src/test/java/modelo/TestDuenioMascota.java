package modelo;

import notificaciones.ContactoMail;
import notificaciones.EmailSenderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import usuario.UsuarioRol;
import usuario.Usuario;

import java.time.LocalDate;
import java.time.Period;


import static org.mockito.Mockito.mock;

public class TestDuenioMascota {

  DuenioMascota pepeArgento = creoAPepeArgento();

  @DisplayName("Un dueno puede tener mas de una mascota")
  @Test
  void dosMascotasDistintasTienenElMismoDueno(){
    Mascota fatiga = crearAfatigaArgento();
    Mascota betun = crearMascota(TipoMascota.PERRO,"BETUN",pepeArgento);

    Assertions.assertEquals(betun.getDuenio(), fatiga.getDuenio());
  }

  @DisplayName("La edad de Fatiga es = añoActual - 23/08/2017")
  @Test
  void calcularEdadFatiga(){
    int deberiaTener = Period.between(LocalDate.of(2011,7,19), LocalDate.now()).getYears();
    Assertions.assertEquals(crearAfatigaArgento().calcularEdad(), deberiaTener);
  }

  private Mascota crearAfatigaArgento(){

    return new Mascota(TipoMascota.PERRO,
        "Fatiga",
        "Fatiga",
        TamanioMascota.MEDIANA,
        LocalDate.of(2011,7,19),
        Sexo.FEMENINO,
        "fachera",
        null,
        pepeArgento);

  }

  private Mascota crearMascota(TipoMascota tipo, String nombre, DuenioMascota dueno){
    return new Mascota(tipo,nombre,"chicho", TamanioMascota.CHIQUITA,
        LocalDate.of(2017,8,23), Sexo.OTRO,"pequeño, no lo pises :c", null, dueno);

  }

  private DuenioMascota creoAPepeArgento(){

    Persona pepeArgento = new Persona("Pepe Argento",
        LocalDate.of(2012, 5, 25),
        TipoDocumento.DNI,
        "11229750",
        new Ubicacion(-34.63333,-58.46667, "Bajo Flores"),
        new ContactoMail("Pepe","pepe@racing",mock(EmailSenderService.class))
        );


    return  new DuenioMascota( pepeArgento, new Usuario("PepeArg" , "fatiga05" ,
        UsuarioRol.NORMAL));
  }

}
