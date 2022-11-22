package ViewModels;

import caracteristica.CaracteristicaConValores;

public class CaracteristicasConValoresViewModel {

  private Long id;
  private String descripcion;
  private String valoresPosibles;

  public CaracteristicasConValoresViewModel(Long id, String descripcion, String valoresPosibles) {
    this.id = id;
    this.descripcion = descripcion;
    this.valoresPosibles = valoresPosibles;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getValoresPosibles() {
    return valoresPosibles;
  }

  public Long getId() {
    return id;
  }
}
