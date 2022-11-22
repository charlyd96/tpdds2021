package controllers;
import Repositorios.RepositorioMascotas;
import Repositorios.RepositorioPublicaciones;
import modelo.Mascota;
import modelo.Publicacion;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.MapperToViewModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeController extends SessionController {

  public ModelAndView getHome(Request request, Response response) {
    Map<String, Object> modelo = modeloUsuarioLoggeado(request);

      RepositorioMascotas repositorioMascotas = new RepositorioMascotas();
      RepositorioPublicaciones repositorioPublicaciones = new RepositorioPublicaciones();


      List<Mascota> mascotas = repositorioMascotas.buscarTodos();
      List<Publicacion> publicaciones = repositorioPublicaciones.buscarTodos();


//      modelo.put("mascotasRegistradas", MapperToViewModel.mascotasToViewModel(
//          mascotas.subList(Math.max(mascotas.size() - 3, 0), mascotas.size())));
//
//      modelo.put("mascotasPerdidas",
//          MapperToViewModel.mascotasEncontradasToViewModel(
//              publicaciones.stream().map(p -> p.getMascotaEncontrada()).collect(Collectors.toList()).subList(Math.max(publicaciones.size() - 3, 0), publicaciones.size())));
      return new ModelAndView(modelo, "index.html.hbs");

  }
}
