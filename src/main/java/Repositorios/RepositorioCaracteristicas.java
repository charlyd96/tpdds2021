package Repositorios;

import caracteristica.Caracteristica;
import caracteristica.CaracteristicaConValores;
import caracteristica.CaracteristicaLibre;

import java.util.List;
import java.util.stream.Collectors;

public class RepositorioCaracteristicas extends Repositorio<Caracteristica> {

  public RepositorioCaracteristicas() {
    super(Caracteristica.class);
  }

  public List<Caracteristica> getCaracteristicas() {
    return buscarTodos();
  }

  public List<Caracteristica> getCaracteristicasConValores() {
    return buscarTodos().stream().filter(c ->
        c.getClass() == CaracteristicaConValores.class).collect(Collectors.toList());
  }

  public List<Caracteristica> filtrarPorDescripcion(List<Caracteristica> caracteristicas, String texto) {
    if (texto != null && !texto.isEmpty())
      return caracteristicas.stream().filter(c -> c.getDescripcion().contains(texto)).collect(Collectors.toList());
    else
      return caracteristicas;
  }

  public List<Caracteristica> getCaracteristicasLibres() {
    return buscarTodos().stream().filter(c ->
        c.getClass() == CaracteristicaLibre.class).collect(Collectors.toList());
  }

  public Caracteristica buscarCaracteristicaPorId(int id) {
    return buscar(id);
  }
}
