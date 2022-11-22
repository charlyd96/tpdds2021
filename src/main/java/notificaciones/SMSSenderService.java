package notificaciones;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SMSSenderService {

  public static final String ACCOUNT_SID = "AC0eff6eef7b38a62f9ad3f3291bbb142f";
  public static final String AUTH_TOKEN  = "f4f34e9f8eb6ae466d089d4f37ca2139";
  public static final String TWILIO_NUMBER = "+13177933573";

  public void notificar(Contacto destinatario, String msg) {

    try {
      Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
      Message message = Message.creator(
          new PhoneNumber(destinatario.getDetalleContacto()),
          new PhoneNumber(TWILIO_NUMBER), ("\n Rescate de Patitas "
                  + "\n Datos de contacto: "
                  + msg))
          .create();

    } catch (Exception e) {
      throw new NotificacionException("Ocurrio un error inesperado notificando por SMS");
    }
  }
}