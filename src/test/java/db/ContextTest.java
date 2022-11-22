package db;

import static org.junit.Assert.*;

import modelo.Persona;
import modelo.TipoDocumento;
import modelo.Ubicacion;
import notificaciones.Contacto;
import notificaciones.ContactoSMS;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.time.LocalDate;


public class ContextTest extends AbstractPersistenceTest implements WithGlobalEntityManager {




	@Test
	public void contextUp() {
		assertNotNull(entityManager());
	}

	@Test
	public void contextUpWithTransaction() throws Exception {
		withTransaction(() -> {});
	}

//	---- TEST QUE LE PEGA A LA DB!
//	@Test
//	public void testdb()
//	{
//
//		Contacto contactoGuido = new ContactoSMS("asd","asd",null) ;
//		Ubicacion ubi = new Ubicacion(200.00,200.00,"asd");
//		Persona guido = new Persona("asd", LocalDate.now(), TipoDocumento.DNI,"asd",ubi,contactoGuido);
//		EntityManagerHelper.beginTransaction();
//
//		EntityManagerHelper.getEntityManager().persist(contactoGuido);
//		EntityManagerHelper.getEntityManager().persist(guido);
//
//		EntityManagerHelper.commit();
//
//	}

}