package Repositorios.factories;

import Repositorios.RepositorioAsociaciones;
import modelo.Asociacion;

public class FactoryRepositorioAsociaciones {
  private static RepositorioAsociaciones repo;

  static {
    repo = null;
  }

  public static RepositorioAsociaciones get() {
    if (repo == null) {
        repo = new RepositorioAsociaciones();
      }
    return repo;
    }
  }
