package adopcion;

import Repositorios.RepositorioAsociaciones;
import Repositorios.RepositorioCaracteristicas;
import Repositorios.factories.FactoryRepositorioAsociaciones;
import caracteristica.Caracteristica;
import modelo.*;
import notificaciones.ContactoMail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import usuario.Usuario;
import usuario.UsuarioRol;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

public class TestAdopcion {
  ContactoMail contactoMailMock = mock(ContactoMail.class);
  Mascota fatiga = crearMascota(TipoMascota.PERRO,"fatiga",creoAPepeArgento());
  Asociacion asociacionDePosadas = unaAsociacionEnPosadas();
  RepositorioAsociaciones repoAsociaciones = new RepositorioAsociaciones();

  @BeforeEach
  void initTests(){
    asociacionDePosadas = unaAsociacionEnPosadas();
    repoAsociaciones.agregar(asociacionDePosadas);
  }

  @DisplayName("Siendo dueno puedo dar en adopcion a mi mascota y se carga OK en la asociacion")
  @Test
  void puedoDarEnAdopcionAMiMascota() {

    Assertions.assertEquals(0,asociacionDePosadas.getMascotasEnAdopcion().size());
    MascotaEnAdopcion fatigaEnAdopcion = new MascotaEnAdopcion(fatiga,null);
    asociacionDePosadas.agregarMascotaEnAdopcion(fatigaEnAdopcion);
    Assertions.assertEquals(1,asociacionDePosadas.getMascotasEnAdopcion().size());

  }

  @DisplayName("Notifico al dueno cuando hay un interesado en su mascota en adopcion")
  @Test
  void notificoAlDuenoInteresadoEnAdoptar() {

    MascotaEnAdopcion fatigaEnAdopcion = new MascotaEnAdopcion(fatiga,null);
    asociacionDePosadas.agregarMascotaEnAdopcion(fatigaEnAdopcion);
    Persona interesado = unaPersona("Lean");

    doNothing().when(contactoMailMock).notificar(any());

    fatigaEnAdopcion.comunicarInteresadoEnAdoptar(interesado);

    verify(contactoMailMock,times(1)).notificar(any());
  }

  @DisplayName("Una persona puede generar un publicacion demostrando" +
      " su interes por adoptar una mascota y es notificado sobre la creacion OK en la asociacion")
  @Test
  void  unaPersonaGeneraUnaIntencionDeAdoptarYEsNotificado() {
    Persona pepe = unaPersona("pepe");
    IntencionAdoptar quierUnPerrito = new IntencionAdoptar(null,pepe,TipoMascota.PERRO);

    Assertions.assertEquals(0,asociacionDePosadas.getMascotasEnAdopcion().size());

    doNothing().when(contactoMailMock).notificar(any());

    asociacionDePosadas.agregarIntencionDeAdoptar(quierUnPerrito);

    Assertions.assertEquals(1,asociacionDePosadas.getIntencionesAdoptarActivas().size());
    verify(contactoMailMock,times(1)).notificar(any());

  }




  private Mascota crearMascota(TipoMascota tipo, String nombre, DuenioMascota dueno){
    return new Mascota(tipo,nombre,"chicho", TamanioMascota.CHIQUITA,
        LocalDate.of(2017,8,23), Sexo.OTRO,
        "pequeño, no lo pises :c", null,dueno);

  }

  private DuenioMascota creoAPepeArgento(){

    Persona pepeArgento = new Persona("Pepe Argento",
        LocalDate.of(2012, 5, 25),
        TipoDocumento.DNI,
        "11229750",
        new Ubicacion(-34.63333,-58.46667, "Bajo Flores"), contactoMailMock
    );

    return  new DuenioMascota( pepeArgento, new Usuario("PepeArg" , "fatiga05" , UsuarioRol.NORMAL));
  }

  private Persona unaPersona(String nombre) {
    return new Persona(nombre, null, null, null,
        new Ubicacion(-27.36708, -55.89608, ""), contactoMailMock);
  }

  private Asociacion unaAsociacionEnBajoFlores() {
    return new Asociacion(new Ubicacion(-34.63334, -58.46668, ""));
  }
  private Asociacion unaAsociacionEnPosadas() {
    return new Asociacion(new Ubicacion(-27.36708, -55.89608, ""));
  }
}
