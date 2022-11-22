package caracteristica;

import static java.util.Objects.requireNonNull;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue("LIBRE")
public class CaracteristicaLibre extends Caracteristica {

  public CaracteristicaLibre(String descripcion) {
    this.descripcion = descripcion;
  }

  public CaracteristicaLibre() {
  }

  @Override
  public void validar(String valor) {
    requireNonNull(valor, "El valor de la caracteristica "
        + descripcion + "no puede ser null");
  }

  @Override
  public List<String> getValoresPosibles() {
    return null;
  }
}
