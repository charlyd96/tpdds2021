package notificaciones;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("MAIL")
public class ContactoMail extends Contacto {

  private String email;

  @Transient
  private EmailSenderService sender;


  public ContactoMail(String nombreApellido, String email, EmailSenderService sender) {
    this.nombreApellido = nombreApellido;
    this.email = email;
    this.sender = sender;
  }

  public ContactoMail() {

  }

  public void notificar(String mensaje) {
    sender.notificar(this, mensaje);
  }

  public String getDetalleContacto() {
    return email;
  }

  @Override
  public String toString() {
    return "Email: " + email;
  }
}

