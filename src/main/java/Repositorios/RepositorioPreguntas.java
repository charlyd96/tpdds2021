package Repositorios;

import adopcion.PreguntaAdopcion;
import java.util.List;

public class RepositorioPreguntas extends Repositorio<PreguntaAdopcion> {

  public RepositorioPreguntas() {
    super(PreguntaAdopcion.class);
  }

  public List<PreguntaAdopcion> getPreguntas() {
    return buscarTodos();
  }

}
