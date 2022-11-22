package notificaciones;

import javax.persistence.*;

@Entity
@Table(name = "Contactos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_contacto",discriminatorType = DiscriminatorType.STRING)
public abstract class Contacto  {

  @Id
  @GeneratedValue
  @Column(name = "id_contacto")
  private Long id;

  public Long getId() {
    return id;
  }

  @Column (name = "nombre_apellido")
  protected String nombreApellido;

  public abstract void notificar(String mensaje);

  public abstract String getDetalleContacto();

  public String getNombreApellido() {
    return nombreApellido;
  }

}
