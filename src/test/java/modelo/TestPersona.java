package modelo;

import modelo.DuenioMascota;
import modelo.Persona;
import notificaciones.ContactoMail;
import notificaciones.ContactoSMS;
import notificaciones.EmailSenderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestPersona {

  EmailSenderService emailSenderMock = mock(EmailSenderService.class);

  @DisplayName("Notifico todos los contactos de la otra persona al interesado")
  @Test
  void notificoOKPorEmail() {

    ContactoMail guidoMail = new ContactoMail("Guido Martinez", "guidojmartinez@gmail.com",
        emailSenderMock);
    ContactoSMS guidoSMS = new ContactoSMS("Guido Martinez", "+541169761628",null);
    ContactoMail guidoMail2 = new ContactoMail("Guido Martinez", "guidojmartinez@gmail.com",
        null);

    Persona guidoEnviador = new Persona("Julian",
        null,null,null,null, guidoSMS);
    guidoEnviador.agregarContacto(guidoMail2);

    DuenioMascota dueno = new DuenioMascota(guidoEnviador,null);

    doNothing().when(emailSenderMock).notificar(any(), any());

    guidoMail.notificar(guidoSMS.toString());
    guidoMail.notificar(dueno.getDescripcionContactos());

    verify(emailSenderMock,times(2)).notificar(any(),any());
  }
}
