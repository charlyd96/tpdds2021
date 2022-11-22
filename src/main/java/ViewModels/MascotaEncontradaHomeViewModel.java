package ViewModels;

import modelo.MascotaEncontrada;
import utils.MapperToViewModel;
import java.util.Arrays;

public class MascotaEncontradaHomeViewModel {

  MascotaEncontrada mascotaEncontrada;
  String primeraFoto;
  Long id;

  public MascotaEncontradaHomeViewModel(MascotaEncontrada mascotaEncontrada)   {
    this.mascotaEncontrada = mascotaEncontrada;
    this.primeraFoto = MapperToViewModel.convertirFotosABase64(Arrays.asList(mascotaEncontrada.getPathFotos().get(0))).get(0);
    this.id = mascotaEncontrada.getId();
  }

  public MascotaEncontrada getMascota() {
    return mascotaEncontrada;
  }

  public String getPrimeraFoto() {
    return primeraFoto;
  }

  public Long getId() { return this.id; }
}
