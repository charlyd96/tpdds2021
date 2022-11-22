package adopcion;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Respuestas_Pregunta_Adopcion")
public class RespuestaPreguntaAdopcion {

  @Id
  @GeneratedValue
  @Column(name = "id_respuesta")
  private Long id;

  public Long getId() {
    return id;
  }

  @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
  @JoinColumn(name = "pregunta_adopcion_id")
  private PreguntaAdopcion preguntaAdopcion;

  @Column(name = "valor_respuesta")
  private String valorRespuesta;

  public RespuestaPreguntaAdopcion(PreguntaAdopcion preguntaAdopcion, String valorRespuesta) {
    this.preguntaAdopcion = preguntaAdopcion;
    this.valorRespuesta = valorRespuesta;
  }

  public RespuestaPreguntaAdopcion() {

  }

  private PreguntaAdopcion getPregunta() {
    return this.preguntaAdopcion;
  }

  private String getRespuesta() {
    return this.valorRespuesta;
  }

  public boolean cumpleCondicionDeAdopcion(List<RespuestaPreguntaAdopcion> respuestas) {
    return respuestas.stream().anyMatch(respuestaPreguntaAdopcion ->
        respuestaPreguntaAdopcion.tieneMismaRtaALaMismaPregunta(this));
  }

  private boolean tieneMismaRtaALaMismaPregunta(RespuestaPreguntaAdopcion otraRespuesta) {
    return tieneLaMismaPregunta(otraRespuesta) && tieneLaMismaRespuesta(otraRespuesta);
  }

  private boolean tieneLaMismaPregunta(RespuestaPreguntaAdopcion otraRespuesta) {
    return preguntaAdopcion.getIdentificador().equals(otraRespuesta.getPregunta().getIdentificador());
  }

  private  boolean tieneLaMismaRespuesta(RespuestaPreguntaAdopcion otraRespuesta) {
    return this.getRespuesta().equals(otraRespuesta.getRespuesta());
  }

}
