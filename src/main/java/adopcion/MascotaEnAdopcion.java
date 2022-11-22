package adopcion;

import modelo.Mascota;
import modelo.Persona;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Mascotas_en_adopcion")
public class MascotaEnAdopcion  {

  @Id
  @GeneratedValue
  @Column(name = "id_mascota_adopcion")
  private Long id;

  public Long getId() {
    return id;
  }

  // TODO: revisar este cascade
  @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER )
  @JoinColumn(name = "mascota_id")
  private Mascota mascota;

  @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
  @JoinColumn(name = "mascota_adopcion_id")
  private List<RespuestaPreguntaAdopcion> condicionesAdopcion;


  public MascotaEnAdopcion(Mascota mascota,
                           List<RespuestaPreguntaAdopcion> respuestasDePreguntasDeAdopcion) {
    this.mascota = mascota;
    this.condicionesAdopcion = respuestasDePreguntasDeAdopcion;
  }

  public MascotaEnAdopcion() {

  }

  public Mascota getMascota() {
    return mascota;
  }

  public List<RespuestaPreguntaAdopcion> getCondicionesAdopcion() {
    return condicionesAdopcion;
  }

  public void comunicarInteresadoEnAdoptar(Persona persona) {
    this.mascota.getDuenio().notificar(persona.getDescripcionContactos());
  }
}

