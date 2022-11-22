package seguridadDeContraseñas;

public class CaracterSecuencial implements RestriccionesPassword {

  public boolean cumpleRestriccion(String password) {
    return !verificar3Consecutivos(password.toCharArray());
  }

  private boolean verificar3Consecutivos(char[] s) {
    int l = s.length;

    for (int i = 2; i < l; i++) {
      if (s[i] - s[i - 1] == 1 && s[i - 1] - s[i - 2] == 1) {
        return true;
      }
    }
    return false;
  }

}
