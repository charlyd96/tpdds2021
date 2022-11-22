package Repositorios;

import com.sun.org.apache.xpath.internal.operations.Bool;
import usuario.Usuario;

public class RepositorioUsuarios extends Repositorio<Usuario> {

  public RepositorioUsuarios() {
    super(Usuario.class);
  }

  public Usuario buscarPorUsuarioYContrasenia(String username, String password) {
    return buscarTodos().stream()
        .filter(u -> u.getPassword().equals(password) && u.getUsername().equals(username)).findFirst().get();
  }

  public Boolean existeUsuario(String username) {
    return buscarTodos().stream().anyMatch(u -> u.getUsername().equals(username));
  }
}
