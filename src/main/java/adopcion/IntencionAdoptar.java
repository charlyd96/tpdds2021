package adopcion;


import modelo.Persona;
import modelo.TipoMascota;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "IntencionesAdoptar")
public class IntencionAdoptar {

  @Id
  @GeneratedValue
  @Column(name = "id_intencion_adoptar")
  private Long id;

  public Long getId() {
    return id;
  }

  // TODO: revisar este cascade
  @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
  @JoinColumn(name = "persona_id")
  private Persona persona;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_mascota")
  private TipoMascota tipoMascota;

  @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
  @JoinColumn(name = "intencion_adoptar_id")
  private List<RespuestaPreguntaAdopcion> preferencias;

  @Enumerated(EnumType.STRING)
  private EstadoAdopcion estadoAdopcion = EstadoAdopcion.ACTIVA;


  public IntencionAdoptar(List<RespuestaPreguntaAdopcion> respuestasAdopciones,
                          Persona persona, TipoMascota tipoMascota) {
    this.preferencias = respuestasAdopciones;
    this.persona = persona;
    this.tipoMascota = tipoMascota;
  }

  public IntencionAdoptar(){

  }

  public boolean coincideCon(MascotaEnAdopcion mascota) {
    return preferencias.stream().allMatch(
        preferencia -> preferencia.cumpleCondicionDeAdopcion(mascota.getCondicionesAdopcion())) && this.tipoMascota == mascota.getMascota().getTipo();
  }

  public List<RespuestaPreguntaAdopcion> getPreferencias() {
    return preferencias;
  }

  public TipoMascota getTipoMascota() {
    return tipoMascota;
  }

  public void darDeBajaAdopcion() {
    this.estadoAdopcion = EstadoAdopcion.INACTIVA;
  }

  public boolean estaActiva() {
    return this.estadoAdopcion == EstadoAdopcion.ACTIVA;
  }

  public Persona getPersona() {
    return persona;
  }
}
