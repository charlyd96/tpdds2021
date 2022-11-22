package usuario;

import static java.util.Objects.requireNonNull;
import caracteristica.Caracteristica;
import Repositorios.RepositorioCaracteristicas;

import javax.persistence.*;

@Entity
public class Usuario  {

  @Id
  @GeneratedValue
  @Column(name = "id_usuario")
  private Long id;

  public Long getId() {
    return id;
  }

  private String username;
  private String password;
  @Enumerated(EnumType.STRING)
  private UsuarioRol rol;

  public Usuario(String username, String password, UsuarioRol rol) {
    this.username = username;
    this.password = password;
    this.rol = rol;
  }

  public Usuario() {

  }
  public void agregarCaracteristica(Caracteristica nuevaCaracteristica) {

    if (!rol.equals(UsuarioRol.ADMINISTRADOR)) {
      throw new PermisosInsuficientesException(
          "No tiene permisos para agregar nuevas caracteristicas");
    }
    new RepositorioCaracteristicas().agregar(requireNonNull(nuevaCaracteristica,
            "NULL no es un valor valido para agregar a una caracteristica."));
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  public boolean esAdministrador() {
    return rol == UsuarioRol.ADMINISTRADOR;
  }
}
