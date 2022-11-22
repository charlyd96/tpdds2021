package Repositorios.factories;

import Repositorios.Repositorio;

import java.util.HashMap;

public class FactoryRepositorio {
  private static HashMap<String, Repositorio> repos;

  static {
    repos = new HashMap<>();
  }
}
