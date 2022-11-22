package usuario;

import static java.util.Objects.requireNonNull;

import java.util.List;

import modelo.Asociacion;
import seguridadDeContraseñas.*;


public class CreadorUsuarios {
  private String username;
  private String password;
  private List<RestriccionesPassword> reglas;

  public CreadorUsuarios(String username, String password, List<RestriccionesPassword> reglas) {
    validarReglasNoVacias(reglas);
    this.username = requireNonNull(username, "Especificar un username");
    this.password = requireNonNull(password, "Especifiar una password");
    this.reglas = reglas;
  }

  public Usuario crearUsuarioAdministrador() {
    validarPassword(password);
    return new Usuario(username, password, UsuarioRol.ADMINISTRADOR);
  }

  public Usuario crearUsuarioNormal() {
    validarPassword(password);
    return new Usuario(username, password, UsuarioRol.NORMAL);
  }

  public Usuario crearUsuarioVoluntario() {
    validarPassword(password);
    return new Usuario(username, password, UsuarioRol.VOLUNTARIO);
  }

  private void validarPassword(String pass) {
    if (!reglas
        .stream()
        .allMatch(restriccion -> restriccion.cumpleRestriccion(pass))) {
      throw new ContrasenaInseguraException("La contrasena no cumple con las normas de seguridad");
    }
  }

  private void validarReglasNoVacias(List<RestriccionesPassword> reglas) {
    if (reglas.isEmpty()) {
      throw new SinValidacionDePasswordException("Se debe ingresar al menos "
          + "una restriccion para la contraseña.");
    }
  }
}


