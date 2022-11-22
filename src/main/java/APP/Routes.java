package APP;

import static spark.Spark.get;

import controllers.*;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.servlet.SparkApplication;

public class Routes implements SparkApplication {

  public static void main(String[] args) {
    new Routes().init();

  }

  @Override
  public void init() {
    Spark.staticFileLocation("/public");
    HomeController homeController = new HomeController();
    MascotaController mascotaController = new MascotaController();
    UsuarioController usuarioController = new UsuarioController();
    MascotaEncontradaController mascotaEncontradaController = new MascotaEncontradaController();
    AdministradorController administradorController = new AdministradorController();

    HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();

    Spark.get("/", (request, response) -> {
      response.redirect("/home");
      return null;
    });

    Spark.get("/home", homeController::getHome, engine);

    Spark.post("/registrar-mascota/datos", mascotaController::getFormularioRegistrarMascota, engine);

    Spark.get("/registrar-mascota", mascotaController::getFormularioCaracteristicas, engine);

    Spark.get("/mascotas-encontradas", mascotaEncontradaController::getFormularioMascotaEncontrada, engine);

    Spark.get("/mascotas-encontradas/:id", mascotaEncontradaController::getMascotaEncontrada, engine);

    Spark.post("/mascotas-encontradas/crear-publicacion", mascotaEncontradaController::crearPublicacion, engine);

    Spark.post("/mascotas-encontradas/reportar-mascota-con-chapita", mascotaEncontradaController::reportarMascotaConChapita, engine);

    Spark.get("/mascotas/:id", mascotaController::verMascota, engine);

    Spark.get("/mascotas", mascotaController::verMascotas, engine);

    Spark.post("/mascotas", mascotaController::crearMascota, engine);

    Spark.get("/crear-usuario", usuarioController::getFormularioRegistrarUsuario, engine);

    Spark.post("/usuarios", usuarioController::crearUsuario, engine);

    Spark.get("/login", usuarioController::getFormularioLogin, engine);

    Spark.post("/login", usuarioController::login, engine);

    Spark.get("/logout", usuarioController::logout, engine);

    Spark.get("/caracteristicas", administradorController::getCaracteristicas, engine);

    Spark.get("/caracteristicas/con-valores-nueva", (request, response) ->
        administradorController.getFormularioCrearCaracteristica(true, request, response), engine);

    Spark.get("/caracteristicas/libre-nueva", (request, response) ->
        administradorController.getFormularioCrearCaracteristica(false, request, response), engine);

    Spark.get("/caracteristicas/:id", administradorController::getFormularioEditarCaracteristica, engine);

    Spark.post("/caracteristicas", administradorController::crearCaracteristica, engine);

    Spark.post("/caracteristicas/:id", administradorController::editarCaracteristica, engine);
    //TODO: verificar por qué no funciona con PUT y sacar el "modificar" de la URL

    Spark.delete("/caracteristicas/:id", administradorController::eliminarCaracteristica, engine);
  }

}
