package controllers;

import Repositorios.RepositorioCaracteristicas;
import caracteristica.Caracteristica;
import caracteristica.CaracteristicaConValores;
import caracteristica.CaracteristicaLibre;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.MapperToViewModel;

import java.util.*;

public class AdministradorController extends SessionController implements WithGlobalEntityManager, TransactionalOps {

  RepositorioCaracteristicas repositorioCaracteristicas = new RepositorioCaracteristicas();


  public ModelAndView getCaracteristicas(Request request, Response response) {

    if (!esAdministrador(request)){
      response.redirect("/");
    }
    Map<String, Object> modelo = modeloUsuarioLoggeado(request);
    modelo.put("caracteristicasConValores", MapperToViewModel.caracteristicasToViewModel(repositorioCaracteristicas.getCaracteristicasConValores()));
    modelo.put("caracteristicasLibres", repositorioCaracteristicas.getCaracteristicasLibres());

    return new ModelAndView(modelo, "caracteristicas.html.hbs");
  }

  public ModelAndView getFormularioEditarCaracteristica(Request request, Response response) {

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("creacion", false);
    boolean conValores = false;

    try {
      Caracteristica caracteristica = repositorioCaracteristicas.buscar(Integer.parseInt(request.params(":id")));
      modelo.put("caracteristica", caracteristica);
      conValores = caracteristica.getClass() == CaracteristicaConValores.class;
    } catch (Exception e) {
      //TODO: retornar vista de "error al obtener la característica"
    }

      modelo.put("titulo", "Editar característica" + (conValores ? " con valores" : " libre"));
      modelo.put("conValores", conValores);
      return new ModelAndView(modelo, "editar-caracteristica.html.hbs");
  }

  public ModelAndView getFormularioCrearCaracteristica(boolean conValores, Request request, Response response) {
    Map<String, Object> modelo = modeloUsuarioLoggeado(request);
    modelo.put("titulo", "Crear característica " + (conValores ? " con valores" : " libre"));
    modelo.put("creacion", true);
    modelo.put("conValores", conValores);
    return new ModelAndView(modelo, "editar-caracteristica.html.hbs");
  }


  public ModelAndView crearCaracteristica(Request request, Response response) {

    String descripcion = request.queryParams("descripcion");
    String valorPosible = request.queryParams("valorPosible");
    Caracteristica caracteristica;

    if (valorPosible != null) {
      caracteristica = new CaracteristicaConValores(descripcion, Arrays.asList(valorPosible));
    } else {
      caracteristica = new CaracteristicaLibre(descripcion);
    }

    withTransaction(() -> {
      repositorioCaracteristicas.agregar(caracteristica);
    });
    response.redirect("/caracteristicas");

    return null;
  }

  public ModelAndView editarCaracteristica(Request request, Response response) {

    Caracteristica caracteristica = repositorioCaracteristicas
        .buscarCaracteristicaPorId(Integer.parseInt(request.params(":id")));

    boolean conValores = caracteristica.getClass() == CaracteristicaConValores.class;

    caracteristica.setDescripcion(request.queryParams("descripcion"));

    if (conValores) {
      String nuevoValor = request.queryParams("valorPosible");
      if (!nuevoValor.isEmpty())
      ((CaracteristicaConValores) caracteristica).agregarValor(nuevoValor);
    }

    withTransaction(() -> {
      repositorioCaracteristicas.modificar(caracteristica);
    });

    response.redirect("/caracteristicas/" + request.params(":id"));

    return null;
  }

  public ModelAndView eliminarCaracteristica(Request request, Response response) {

    withTransaction(() -> {
      repositorioCaracteristicas.eliminar(repositorioCaracteristicas.buscar(Integer.parseInt(request.params(":id"))));
    });
    response.redirect("/caracteristicas");
    return null;
  }

}
