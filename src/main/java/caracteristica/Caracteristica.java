package caracteristica;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Caracteristicas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_caracteristica",discriminatorType = DiscriminatorType.STRING)
public abstract class Caracteristica  {

  @Id
  @GeneratedValue
  @Column(name = "id_caracteristica")
  private Long id;

  public Long getId() {
    return id;
  }

  protected String descripcion;

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public abstract void validar(String valor);

  public abstract List<String> getValoresPosibles();
}
