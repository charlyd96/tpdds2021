package Repositorios;

import modelo.Publicacion;

public class RepositorioPublicaciones extends Repositorio<Publicacion>{

  public RepositorioPublicaciones() {
    super(Publicacion.class);
  }

  public Publicacion buscarPublicacionPorMascotaId(Long mascotaId) {
    return buscarTodos().stream().filter(p -> p.getMascotaEncontrada().getId() == mascotaId).findFirst().get();
  }
}
