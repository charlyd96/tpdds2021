package controllers;

import Repositorios.RepositorioDuenios;
import Repositorios.RepositorioUsuarios;
import modelo.*;
import notificaciones.ContactoMail;
import notificaciones.ContactoSMS;
import notificaciones.EmailSenderService;
import notificaciones.SMSSenderService;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import seguridadDeContraseñas.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.CreadorUsuarios;
import usuario.Usuario;
import java.time.LocalDate;
import java.util.*;

public class UsuarioController extends SessionController implements WithGlobalEntityManager, TransactionalOps {

  RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios();

  public ModelAndView getFormularioRegistrarUsuario(Request request, Response response) {
    return new ModelAndView(null, "crear-usuario.html.hbs");
  }

  public ModelAndView getFormularioLogin(Request request, Response response) {

    if (this.usuarioLoggeado(request)) {
      response.redirect("/");
      return null;
    }

    return new ModelAndView(this.modeloUsuarioLoggeado(request), "login.html.hbs");
  }

  public ModelAndView login(Request request, Response response) {
    try {
      Usuario usuario = repositorioUsuarios.buscarPorUsuarioYContrasenia(
          request.queryParams("usuario"),
          request.queryParams("usuarioContrasenia"));
      request.session().attribute("user_id", usuario.getId());
      request.session().attribute("es_admin", usuario.esAdministrador());
      if (request.session().attribute("loginRedirect") != null) {
        response.redirect(request.session().attribute("loginRedirect"));
      } else {
        response.redirect("/");
      }
      return null;
    } catch (NoSuchElementException e) {
      Map<String, Object> modelo = new HashMap<>();
      modelo.put("fallaInicioSesion", true);
      return new ModelAndView(modelo, "login.html.hbs");
    }
  }

  public ModelAndView logout(Request request, Response response) {
    request.session().removeAttribute("user_id");
    request.session().removeAttribute("es_admin");
    request.session().removeAttribute("loginRedirect");
    response.redirect("/");
      return null;
  }

  public ModelAndView crearUsuario(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
      RepositorioDuenios repositorioDuenios = new RepositorioDuenios();
      String nombre = request.queryParams("usuarioNombre");
      String usuario = request.queryParams("usuario");
      String password = request.queryParams("usuarioContrasenia");
      LocalDate usuarioNacimiento = LocalDate.parse(request.queryParams("usuarioNacimiento"));

      if (repositorioUsuarios.existeUsuario(usuario)) {
        modelo.put("yaExisteUsuario", true);
        return new ModelAndView(modelo, "crear-usuario.html.hbs");
      }

      CreadorUsuarios creadorUsuario = new CreadorUsuarios(usuario, password, getReglas());

      try {
        creadorUsuario.crearUsuarioNormal();
      } catch (ContrasenaInseguraException e) {

        modelo.put("errorContraseña", true);
        return new ModelAndView(modelo, "crear-usuario.html.hbs");
      }

      Usuario usuarioFinal = creadorUsuario.crearUsuarioNormal();

      Persona persona = new Persona(
          nombre,
          usuarioNacimiento,
          TipoDocumento.values()[Integer.parseInt(request.queryParams("usuarioTipoIdentificacion"))],
          request.queryParams("usuarioNroIdentificacion"),
          new Ubicacion(null, null, "usuarioDireccion"),
          new ContactoMail(nombre, request.queryParams("usuarioEmail"), new EmailSenderService())
      );

      DuenioMascota duenio = new DuenioMascota(persona, usuarioFinal);
      withTransaction(() -> {
        repositorioDuenios.agregar(duenio);
      });

      return login(request, response);

  }

  private List<RestriccionesPassword> getReglas() {
    return Arrays.asList(new PeoresContrasenas(),
        new ExpresionRegular(),
        new CaracterSecuencial(),
        new CaracterRepetido());
  }

}


