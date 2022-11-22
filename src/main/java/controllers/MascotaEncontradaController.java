package controllers;

import Repositorios.*;
import modelo.*;
import notificaciones.Contacto;
import notificaciones.ContactoSMS;
import notificaciones.SMSSenderService;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.FileHandler;
import utils.MapperToViewModel;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MascotaEncontradaController extends SessionController implements WithGlobalEntityManager, TransactionalOps {

  private RepositorioMascotas repositorioMascotas = new RepositorioMascotas();
  private RepositorioPublicaciones repositorioPublicaciones = new RepositorioPublicaciones();
  private RepositorioMascotasEncontradas repositorioMascotasEncontradas = new RepositorioMascotasEncontradas();
  private RepositorioAsociaciones repoAsoc = new RepositorioAsociaciones();
  public ModelAndView crearPublicacion(Request request, Response response) throws ServletException, IOException {



    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("images"));

    Ubicacion lugarDeRescate = new Ubicacion(null,null,request.queryParams("lugarDeRescate") ) ;
    LocalDate fechaDeRescate = LocalDate.parse(request.queryParams("fechaDeRescate"));
    TamanioMascota mascotaTamanio = TamanioMascota.values()[Integer.parseInt(request.queryParams("mascotaTamanio"))];
    TipoMascota mascotaTipo = TipoMascota.values()[Integer.parseInt(request.queryParams("mascotaTipo"))];
    String mascotaDescripcion = request.queryParams("mascotaDescripcion");
    List<String> pathFotos = FileHandler.saveFile(request);

    Persona rescatista = getDatosRescatista(request);


    MascotaEncontrada mascota = new MascotaEncontrada(mascotaTipo, pathFotos,mascotaDescripcion, fechaDeRescate,
        lugarDeRescate, rescatista, mascotaTamanio, null );

    Publicacion publicacionALiberar = new Publicacion(mascota);

    withTransaction(() -> {
      repositorioPublicaciones.agregar(publicacionALiberar);
    });

    // TODO aca debe derivar a la vista de la publicación nueva con el redirect no carga de vuelta la foto.
   response.redirect("/");
   return null;
  }

  public ModelAndView reportarMascotaConChapita(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    Persona rescatista = getDatosRescatista(request);
    String mascota_id = request.queryParams("codigoChapita");
    if (repositorioMascotas.buscar(Integer.parseInt(mascota_id))  != null ){
      MascotaEncontrada mascota = new MascotaEncontrada(null, null,null, null,
          null, rescatista, null, repositorioMascotas.buscar(Integer.parseInt(mascota_id)));


//      // TODO ¿Deberia pegar en la DB la mascotaEncontrada? En caso que sea así, hay que sumar un repo MascotaEncontrada?
//      BuilderMascotaEncontrada buildMascota =  new BuilderMascotaEncontrada(new RepositorioAsociaciones());
//      buildMascota.setMascota(repositorioMascotas.buscar(Integer.parseInt(mascota_id)));
//      buildMascota.setRescatista(rescatista);
//
//
//      //TODO La ubicacion deberia ser obtenida desde la UI como latitud y longitud
//      buildMascota.setLugarEncontrado(new Ubicacion(-34.6083, -58.3712, ""));
//
//      buildMascota.generarMascotaConChapitaYasociarla();
      modelo.put("seDaAvisoADuenio", true);
      return new ModelAndView(modelo, "mascota-encontrada.html.hbs");

      // TODO aca deberia haber un metodo que le avise al dueño, como es solo mirada rescatista, no lo colocamos.


    } else {
      modelo.put("mascotaNoExistenteDB", true);
      return new ModelAndView(modelo, "mascota-encontrada.html.hbs");
    }
  }

  public ModelAndView getFormularioMascotaEncontrada(Request request, Response response) {
    if (usuarioLoggeado(request)) {
      return new ModelAndView(modeloUsuarioLoggeado(request), "mascota-encontrada.html.hbs");
    } else {
      return new ModelAndView(null, "mascota-encontrada.html.hbs");
    }
  }

  public ModelAndView getMascotaEncontrada(Request request, Response response) throws IOException {
    Map<String, Object> modelo = new HashMap<>();

    MascotaEncontrada mascotaEncontrada = repositorioMascotasEncontradas.buscar(Integer.parseInt(request.params("id")));
    Publicacion publicacion = repositorioPublicaciones.buscarPublicacionPorMascotaId(Long.parseLong(request.params("id")));

    modelo.put("fotos", MapperToViewModel.convertirFotosABase64(mascotaEncontrada.getPathFotos()));
    modelo.put("mascota", mascotaEncontrada);
    modelo.put("titulo", "Ver publicación");
    modelo.put("publicacion", true);
    return new ModelAndView(modelo,"ver-mascota.html.hbs");
  }

  private Persona getDatosRescatista(Request request) {
    String nombreApellido = request.queryParams("nombreYApellido");
    LocalDate fechaNacimiento = LocalDate.parse(request.queryParams("fechaNacimiento"));
    TipoDocumento usuarioTipoIdentificacion  = TipoDocumento.values()[Integer.parseInt(request.queryParams("usuarioTipoIdentificacion"))];
    String numeroDeIdentificacion = request.queryParams("numeroDeIdentificacion");
    Ubicacion ubicacionRescatista = new Ubicacion(null, null, "direccion");
    Contacto contactoRescatista = new ContactoSMS(nombreApellido, request.queryParams("telefono"), new SMSSenderService());
  return new Persona(nombreApellido,fechaNacimiento,usuarioTipoIdentificacion,numeroDeIdentificacion,ubicacionRescatista,contactoRescatista);
  }


}
