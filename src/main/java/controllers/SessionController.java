package controllers;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

abstract public class SessionController {


  public boolean usuarioLoggeado(Request request) {
    return (request.session().attribute("user_id") != null);
  }


  public boolean esAdministrador(Request request) {
    return ( (Boolean) request.session().attribute("es_admin") != null && (Boolean) request.session().attribute("es_admin") );
  }

  public Map<String, Object> modeloUsuarioLoggeado(Request request) {
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("sesionIniciada", usuarioLoggeado(request));
    modelo.put("es_admin", esAdministrador(request));
    return modelo;
  }

}
