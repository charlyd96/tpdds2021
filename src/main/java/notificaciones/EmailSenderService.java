package notificaciones;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSenderService {

  private final String USERNAME = "2021.vi.no.grupo.14@gmail.com";
  private final String PASSWORD = "2021Grupo14!";


  public void notificar(Contacto destinatario, String msg) {

    Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true"); //TLS

    Session session = Session.getInstance(prop,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(USERNAME, PASSWORD);
          }
        });

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(USERNAME));
      message.setRecipients(
          Message.RecipientType.TO,
          InternetAddress.parse(destinatario.getDetalleContacto())
      );
      message.setSubject("Novedades sobre mascota");
      message.setText("Hola " + destinatario.getNombreApellido()
          + "\n Nos comunicamos desde Rescate de Patitas "
          + "\n Te dejamos los datos de contacto: "
          + msg);

      Transport.send(message);
    } catch (MessagingException e) {
      throw new NotificacionException("Ocurrio un error inesperado notificando por Email"
          + e.getCause());
    }
  }

}

