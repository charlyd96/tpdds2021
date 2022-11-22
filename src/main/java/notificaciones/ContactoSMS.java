package notificaciones;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("SMS")
public class ContactoSMS extends Contacto {

  private String telefono;

  @Transient
  private SMSSenderService sender;

  public ContactoSMS(String nombreApellido, String telefono, SMSSenderService sender) {
    this.nombreApellido = nombreApellido;
    this.telefono = telefono;
    this.sender = sender;
  }

  public ContactoSMS() {

  }

  public void notificar(String mensaje) {
    sender.notificar(this, mensaje);
  }

  public String getDetalleContacto() {
    return telefono;
  }

  @Override
  public String toString() {
    return "Telefono: " + telefono;
  }
}
