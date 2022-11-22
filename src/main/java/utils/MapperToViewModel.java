package utils;

import ViewModels.CaracteristicasConValoresViewModel;
import ViewModels.MascotaEncontradaHomeViewModel;
import ViewModels.MascotaHomeViewModel;
import caracteristica.Caracteristica;
import modelo.Mascota;
import modelo.MascotaEncontrada;
import modelo.TipoMascota;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapperToViewModel {

  public static List<CaracteristicasConValoresViewModel> caracteristicasToViewModel(List<Caracteristica> source) {
    return source.stream().map(c -> new CaracteristicasConValoresViewModel(c.getId(), c.getDescripcion(),
            String.join(", ", c.getValoresPosibles()))).collect(Collectors.toList());
  }

  public static List<MascotaHomeViewModel> mascotasToViewModel(List<Mascota> source)  {
    return source.stream().map(MascotaHomeViewModel::new).collect(Collectors.toList());
  }

  public static List<MascotaEncontradaHomeViewModel> mascotasEncontradasToViewModel(List<MascotaEncontrada> source)  {
    return source.stream().map(MascotaEncontradaHomeViewModel::new).collect(Collectors.toList());
  }

  public static List<String> convertirFotosABase64(List<String> fotos) {
    List<String> fotosABase64 = new ArrayList<>();

    return fotos.stream().map(f -> {
      try {
        return "data:image/" + FileHandler.getFileExtension(f) + ";base64," + FileHandler.imageToBase64(f);
      } catch (IOException e) {
        e.printStackTrace();
      } return null;
    }).collect(Collectors.toList());

  }

}