package Repositorios;

import modelo.DuenioMascota;
import modelo.Mascota;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositorioDuenios extends Repositorio<DuenioMascota> {

  public RepositorioDuenios() {
    super(DuenioMascota.class);
  }


  public void eliminar(DuenioMascota duenio) {
    super.eliminar(duenio);
    List<Mascota> mascotas = new RepositorioMascotas()
        .buscarTodos()
        .stream()
        .filter(mascota -> mascota.getDuenio().equals(duenio)).collect(Collectors.toList());
    mascotas.forEach(mascota -> new RepositorioMascotas().eliminar(mascota));

  }

  public DuenioMascota buscarPorUsuarioId(Long idUsuario) {
    return buscarTodos().stream().filter(d -> d.getUsuario().getId() == idUsuario).findFirst().get();
  }

}
