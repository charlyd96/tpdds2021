package modelo;

import usuario.Usuario;

import javax.persistence.*;

@Entity
@Table(name = "Duenios_mascota")
public class DuenioMascota {

  @Id
  @GeneratedValue
  @Column(name = "id_duenio")
  private Long id;

  public Long getId() {
    return id;
  }

  @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "persona_id")
  private Persona duenio;


  public DuenioMascota(Persona datos, Usuario usuario) {
    this.duenio = datos;
    this.usuario = usuario;
  }

  public DuenioMascota() {

  }


  public Persona getDuenio() {
    return duenio;
  }

  public String getDescripcionContactos() {
    return duenio.getDescripcionContactos();
  }

  public Ubicacion getDireccion() {
    return duenio.getDireccion();
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void notificar(String mensaje) {
    getDuenio().notificar(mensaje);
  }


}
