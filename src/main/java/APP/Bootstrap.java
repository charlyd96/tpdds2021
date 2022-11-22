package APP;

import caracteristica.CaracteristicaConValores;
import caracteristica.CaracteristicaLibre;
import modelo.*;
import notificaciones.ContactoSMS;
import notificaciones.SMSSenderService;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import usuario.Usuario;
import usuario.UsuarioRol;

import java.time.LocalDate;
import java.util.Arrays;


public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public static void main(String[] args) {
    new Bootstrap().run();
  }

  Persona persona = new Persona("leandro", LocalDate.now(),TipoDocumento.DNI,"37996640",new Ubicacion(null, null, "asd"),
      new ContactoSMS("leandro", "123456", new SMSSenderService()));
  Usuario usuarioFinal = new Usuario("normal", "Prueba1*", UsuarioRol.NORMAL);

  public void run() {
    withTransaction(() -> {
      persist(new Usuario("admin", "Prueba1*", UsuarioRol.ADMINISTRADOR));
      persist(usuarioFinal);
      persist(persona);
      persist(new DuenioMascota (persona, usuarioFinal));
      persist(new CaracteristicaConValores("¿Está castrado?", Arrays.asList("SI","NO")));
      persist(new CaracteristicaConValores("¿De que color es su pelo?", Arrays.asList("NEGRO","BLANCO","MARRON","GRIS","DORADO")));
      persist(new CaracteristicaConValores("¿Está vacunado?", Arrays.asList("SI","NO")));
      persist(new CaracteristicaConValores("Tamano", Arrays.asList("GRANDE","MEDIANO","PEQUENO")));
      persist(new CaracteristicaLibre("Estado de animo"));
      persist(new Asociacion(new Ubicacion(-34.6083, -58.3712, "")));
    });
  }

}