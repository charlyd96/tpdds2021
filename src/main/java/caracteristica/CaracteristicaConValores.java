package caracteristica;

import static java.util.Objects.requireNonNull;

import java.util.List;
import javax.persistence.*;


@Entity
@DiscriminatorValue("OPCIONES")
public class CaracteristicaConValores extends Caracteristica {

  @ElementCollection
  @CollectionTable(name = "Valores_Caracteristicas" , joinColumns = @JoinColumn(name = "caracteristica_id"))
  @Column(name = "valor")
  private List<String> valoresPosibles;

  public CaracteristicaConValores(String descripcion, List<String> valoresPosibles) {
    this.descripcion = descripcion;
    this.valoresPosibles = requireNonNull(valoresPosibles, "Valores de caracteristica requeridos");
  }

  public CaracteristicaConValores() {

  }

  public List<String> getValoresPosibles() {
    return valoresPosibles;
  }

  public void agregarValor(String valor) {
    this.valoresPosibles.add(valor);
  }

  @Override
  public void validar(String valor) {
    if (!valoresPosibles.contains(valor)) {
      throw new ValorDeCaracteristicaInvalidoException("El valor debe encontrarse dentro de los "
          + "posibles :" + String.join(" | ", valoresPosibles));
    }
  }
}
