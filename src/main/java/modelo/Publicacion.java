package modelo;

import javax.persistence.*;

@Entity
@Table(name = "Publicaciones")
public class Publicacion {

  @Id
  @GeneratedValue
  @Column(name = "id_publicacion")
  private Long id;

  public Long getId() {
    return id;
  }

  @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "mascota_encontrada_id")
  private MascotaEncontrada mascotaEncontrada;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_publicacion")
  private EstadoPublicacion estadoPublicacion = EstadoPublicacion.PENDIENTE;

  public Publicacion(MascotaEncontrada mascotaEncontrada) {
    this.mascotaEncontrada = mascotaEncontrada;
  }

  public Publicacion() {

  }

  public void aceptar() {
    this.estadoPublicacion = EstadoPublicacion.ACEPTADA;
  }

  public void rechazar() {
    this.estadoPublicacion = EstadoPublicacion.RECHAZADA;
  }

  public void finalizar() {
    this.estadoPublicacion = EstadoPublicacion.FINALIZADA;
  }

  public EstadoPublicacion getEstadoPublicacion() {
    return estadoPublicacion;
  }

  public MascotaEncontrada getMascotaEncontrada() {
    return mascotaEncontrada;
  }

  public Persona getRescatista() {
    return mascotaEncontrada.getRescatista();
  }

  public void reclamarMascota(Persona dueno) {

    mascotaEncontrada.getRescatista().notificar("El dueno encontro a su mascota, "
                + "comunicate con el para coordinar el reencuentro"
                + dueno.getDescripcionContactos());
    mascotaEncontrada.reclamada();
  }


}
