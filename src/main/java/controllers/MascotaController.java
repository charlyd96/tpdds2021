package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.xml.ws.BindingType;

import Repositorios.RepositorioCaracteristicas;
import Repositorios.RepositorioDuenios;
import Repositorios.RepositorioMascotas;
import caracteristica.Atributo;
import caracteristica.Caracteristica;
import modelo.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.FileHandler;
import utils.MapperToViewModel;

@BindingType("http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class MascotaController extends SessionController implements WithGlobalEntityManager, TransactionalOps {

  private RepositorioCaracteristicas repositorioCaracteristicas = new RepositorioCaracteristicas();
  private RepositorioDuenios repositorioDuenios = new RepositorioDuenios();
  private RepositorioMascotas repositorioMascotas = new RepositorioMascotas();

  public ModelAndView getFormularioRegistrarMascota(Request request, Response response) {
    if (usuarioLoggeado(request)) {
      Map<String, Object> modelo = modeloUsuarioLoggeado(request);

      List<String> idsCaracteristicas = Arrays.asList(request.queryParamsValues("selected"));

      String valoresCaracteristicas = String.join(";", idsCaracteristicas.stream()
          .map(i -> i + ":" + request.queryParams("caracteristica-"+i) ).collect(Collectors.toList()));

      modelo.put("caracteristicas", valoresCaracteristicas);

      return new ModelAndView(modelo, "registrar-mascota.html.hbs");
    } else {
      Map<String, Object> modelo = new HashMap<>();
      modelo.put("desdeRegistrarMascota", true);
      request.session().attribute("loginRedirect", request.pathInfo());
      return new ModelAndView(modelo, "login.html.hbs");
    }
  }

  public ModelAndView crearMascota(Request request, Response response) throws ServletException, IOException {

    //request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("images"));

    String caracteristicasJoined = request.queryParams("caracteristicas");
    String mascotaNombre = request.queryParams("mascotaNombre");
    String mascotaApodo = request.queryParams("mascotaApodo");
    String mascotaDescripcion = request.queryParams("mascotaDescripcion");
    Sexo mascotaSexo = Sexo.values()[Integer.parseInt(request.queryParams("mascotaSexo"))];
    TamanioMascota mascotaTamanio = TamanioMascota.values()[Integer.parseInt(request.queryParams("mascotaTamanio"))];
    TipoMascota mascotaTipo = TipoMascota.values()[Integer.parseInt(request.queryParams("mascotaTipo"))];
    LocalDate mascotaNacimiento = LocalDate.parse(request.queryParams("mascotaNacimiento"));

    List<Atributo> atributosMascota = new ArrayList<>();

    List<String> caracteristicas = Arrays.asList(caracteristicasJoined.split(";"));

    caracteristicas.forEach(c -> {
      int id = Integer.parseInt(c.split(":")[0]);
      String valor = c.split(":")[1];
      Caracteristica caracteristica = repositorioCaracteristicas.buscarCaracteristicaPorId(id);
      atributosMascota.add(new Atributo(caracteristica, valor));
    });

    //List<String> pathFotos = FileHandler.saveFile(request);

    Long usuarioId2= request.session().attribute("user_id");
    DuenioMascota duenio = repositorioDuenios.buscarPorUsuarioId(usuarioId2);

    Mascota mascota = new Mascota(mascotaTipo, mascotaNombre, mascotaApodo, mascotaTamanio,
        LocalDate.now(), mascotaSexo, mascotaDescripcion, null, duenio);

    mascota.setAtributos(atributosMascota);

    withTransaction(() -> {
      repositorioMascotas.agregar(mascota);
    });

    response.redirect("/mascota/"+ mascota.getId());
    return null;
  }

  public ModelAndView verMascota(Request request, Response response) throws IOException, InterruptedException {


    Map<String, Object> modelo = this.modeloUsuarioLoggeado(request);
    modelo.put("titulo", "Ver mascota");
    Mascota mascota = repositorioMascotas.buscar(Integer.parseInt(request.params("id")));

    modelo.put("fotos", MapperToViewModel.convertirFotosABase64(mascota.getPathFotos()));
    modelo.put("mascota", mascota);

    return new ModelAndView(modelo, "ver-mascota.html.hbs");
  }

  public ModelAndView verMascotas(Request request, Response response) throws IOException, InterruptedException {


    Map<String, Object> modelo = new HashMap<>();

    try {
    modelo = this.modeloUsuarioLoggeado(request);

    modelo.put("titulo", "Ver mascota");
    Long id_usuario = request.session().attribute("user_id");

    DuenioMascota duenio = repositorioDuenios.buscarPorUsuarioId(id_usuario);
    List<Mascota> mascotas = repositorioMascotas.buscarTodos();
    mascotas = mascotas.stream().filter(mascota -> mascota.getDuenio().getId().equals(duenio.getId())).collect(Collectors.toList());
    //modelo.put("fotos", MapperToViewModel.convertirFotosABase64(mascota.getPathFotos()));
    modelo.put("mascotas", MapperToViewModel.mascotasToViewModel(mascotas));

    return new ModelAndView(modelo, "ver-mis-mascotas.html.hbs");
    }
    catch (Exception e) {
      modelo.put("excepcion", e);
      modelo.put("errorMessage", e.getMessage());
      return new ModelAndView(modelo, "error.html.hbs");
    }
  }

  public ModelAndView getFormularioCaracteristicas(Request request, Response response) {
    if (usuarioLoggeado(request)) {
      Map<String, Object> modelo = modeloUsuarioLoggeado(request);

      String search = request.queryParams("search");

      modelo.put("caracteristicaConValores", repositorioCaracteristicas
          .filtrarPorDescripcion(repositorioCaracteristicas.getCaracteristicasConValores(), search));

      modelo.put("caracteristicaLibre", repositorioCaracteristicas
          .filtrarPorDescripcion(repositorioCaracteristicas.getCaracteristicasLibres(), search));

      return new ModelAndView(modelo, "registrar-mascota-caracteristicas.html.hbs");
    }
    else {
      Map<String, Object> modelo = new HashMap<>();
      modelo.put("desdeRegistrarMascota", true);
      request.session().attribute("loginRedirect", request.pathInfo());
      return new ModelAndView(modelo, "login.html.hbs");
    }
  }
}
