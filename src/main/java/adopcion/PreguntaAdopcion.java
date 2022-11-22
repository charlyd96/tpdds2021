package adopcion;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Preguntas_Adopcion")
public class PreguntaAdopcion  {

  @Id
  @GeneratedValue
  @Column(name = "id_preguntas_adopcion")
  private Long id;

  public Long getId() {
    return id;
  }

  @Column(name = "pregunta_duenio")
  private String preguntaDuenio;

  @Column(name = "pregunta_adoptante")
  private String preguntaAdoptante;

  @Column(name = "identificador")
  private String identificador;

  @ElementCollection
  @CollectionTable(name = "Respuestas_Posibles" , joinColumns = @JoinColumn (name = "pregunta_adopcion_id"))
  @Column(name = "respuesta")
  private List<String> posiblesRespuestas;

  public PreguntaAdopcion(String identificador, String preguntaDuenio, String preguntaAdoptante, List<String> posiblesRespuestas) {
    this.identificador = identificador;
    this.preguntaDuenio = preguntaDuenio;
    this.preguntaAdoptante = preguntaAdoptante;
    this.posiblesRespuestas = posiblesRespuestas;
  }

  public PreguntaAdopcion() {

  }

  public String getPreguntaDuenio() {
    return preguntaDuenio;
  }

  public String getPreguntaAdoptante() {
    return preguntaAdoptante;
  }

  public String getIdentificador() {
    return identificador;
  }

  public List<String> getPosiblesRespuestas() {
    return posiblesRespuestas;
  }
}
