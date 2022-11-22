package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import notificaciones.Contacto;
import org.uqbarproject.jpa.java8.extras.convert.LocalDateConverter;


@Entity
@Table(name = "Personas")
public class Persona  {

  @Id
  @GeneratedValue
  @Column(name = "id_persona")
  private Long id;

  public Long getId() {
    return id;
  }

  @Column(name = "nombre_apellido")
  private String nombreApellido;

  @Column(name = "fecha_nacimiento")
  @Convert(converter = LocalDateConverter.class)
  private LocalDate fechaNacimiento;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_documento")
  private TipoDocumento tipoDocumento;

  private String nroDocumento;

  @Embedded
  private Ubicacion direccion;

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "persona_id")
  private final List<Contacto> contactos = new ArrayList<>();

  public Persona(String nombreApellido, LocalDate fechaNacimiento,
                 TipoDocumento tipoDocumento, String nroDocumento, Ubicacion direccion,
                 Contacto contacto) {

    this.nombreApellido = nombreApellido;
    this.fechaNacimiento = fechaNacimiento;
    this.tipoDocumento = tipoDocumento;
    this.nroDocumento = nroDocumento;
    this.direccion = direccion;
    this.contactos.add(contacto);
  }

  public Persona() {

  }

  public Ubicacion getDireccion() {
    return direccion;
  }

  public void setDireccion(Ubicacion nuevaDireccion) {
    this.direccion = nuevaDireccion;
  }

  public List<Contacto> getContactos() {
    return this.contactos;
  }

  public String getDescripcionContactos() {
    return this.toString().concat(this.contactos.stream()
        .map(Object::toString).collect(Collectors.joining(", ")));
  }

  public void agregarContacto(Contacto contacto) {
    contactos.add(contacto);
  }

  public void notificar(String mensaje) {
    contactos.forEach(contacto -> contacto.notificar(mensaje));
  }

  @Override
  public String toString() {
    return "Nombre: " + nombreApellido + ", ";
  }

  public String getNombreApellido() {
    return nombreApellido;
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  public String getNroDocumento() {
    return nroDocumento;
  }
}

