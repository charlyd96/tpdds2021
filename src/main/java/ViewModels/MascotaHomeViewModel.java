package ViewModels;

import modelo.Mascota;
import utils.MapperToViewModel;
import java.util.Arrays;

public class MascotaHomeViewModel {

  Mascota mascota;
  String primeraFoto;

  public MascotaHomeViewModel(Mascota mascota) {
    this.mascota = mascota;
    //this.primeraFoto = MapperToViewModel.convertirFotosABase64(Arrays.asList(mascota.getPathFotos().get(0))).get(0);
  }

  public Mascota getMascota() {
    return mascota;
  }

  public String getPrimeraFoto() {
    return primeraFoto;
  }


}
