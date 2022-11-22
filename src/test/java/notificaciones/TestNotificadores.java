package notificaciones;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Para probar la API de EmailSender, descomentar los metodos reales.
// Para probar la API de SMSSender, descomentar los metodos reales, es necesario agregar telefono a whiteList
// en la pag de Twilio con las credenciales
public class TestNotificadores {

  EmailSenderService emailSenderMock = mock(EmailSenderService.class);
  SMSSenderService smsSenderMock = mock(SMSSenderService.class);

  @DisplayName("Notifico por email")
  @Test
  void notificoOKPorEmail() {
//    EmailSenderService emailSender = new EmailSenderService();

    ContactoMail guidoMail = new ContactoMail("Guido Martinez", "guidojmartinez@gmail.com",
        emailSenderMock);
    ContactoSMS guidoSMS = new ContactoSMS("Guido Martinez", "+541169761628",null);

    doNothing().when(emailSenderMock).notificar(any(), any());

    guidoMail.notificar(guidoSMS.toString());

    verify(emailSenderMock,times(1)).notificar(any(),any());
  }

    @Test
  void notificoOKPorSMS() {
//    SMSSenderService smsSender = new SMSSenderService();

      ContactoMail guidoMail = new ContactoMail("Guido Martinez", "guidojmartinez@gmail.com",
          null);
      ContactoSMS guidoSMS = new ContactoSMS("Guido Martinez", "+541169761628",smsSenderMock);

      doNothing().when(smsSenderMock).notificar(any(), any());

      guidoSMS.notificar(guidoMail.toString());

      verify(smsSenderMock,times(1)).notificar(any(),any());
    }

  @Test
  void siNoPuedoNotificarPorMailLanzaUnaException() {

    ContactoMail guidoMail = new ContactoMail("Guido Martinez", "guidojmartinez@gmail.com",
        emailSenderMock);

    doThrow(NotificacionException.class).when(emailSenderMock).notificar(any(), any());

    Assertions.assertThrows(NotificacionException.class,
        () -> guidoMail.notificar("Encontramos a tu mascota! Comunicate con"));

    verify(emailSenderMock,times(1)).notificar(any(), any());
  }

  @Test
  void siNoPuedoNotificarPorSMSLanzaUnaException() {

    ContactoSMS guidoSMS = new ContactoSMS("Guido Martinez", "+541169761628",smsSenderMock);

    doThrow(NotificacionException.class).when(smsSenderMock).notificar(any(), any());

    Assertions.assertThrows(NotificacionException.class,
        () -> guidoSMS.notificar("Encontramos a tu mascota! Comunicate con"));

    verify(smsSenderMock,times(1)).notificar( any(), any());
  }

}
