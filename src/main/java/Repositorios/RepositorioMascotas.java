package Repositorios;

import modelo.Mascota;

import java.util.List;

public class RepositorioMascotas extends Repositorio<Mascota> {

  public RepositorioMascotas() {
    super(Mascota.class);
  }

  public void agregarMascota(Mascota mascota) {
    agregar(mascota);
  }

  public void eliminarMascota(Mascota mascota) {
    eliminar(mascota);
  }
}
