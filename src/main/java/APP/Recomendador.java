package APP;

import Repositorios.RepositorioAsociaciones;
import adopcion.GeneradorRecomendacionesAdopcion;
import modelo.*;

import java.util.List;

/*
  Para programar el crontab se deberia de ejecutar las siguientes lineas en la consola luego de compilar el programa con el comando mvn package

  crontab -e
  0 0 8 ? * FRI * java -jar ~/TPA-Grupo14/target/ejercicio-1.0-SNAPSHOT-jar-with-dependencies.jar

  Quedara programada la ejecucion de este main todos los viernes a las 8AM

 */


public class Recomendador {

  public static void main (String[] args){
    List<Asociacion> repo = inicializarRepo();

    GeneradorRecomendacionesAdopcion generadorRecomendacionesAdopcion = new GeneradorRecomendacionesAdopcion(repo);
    generadorRecomendacionesAdopcion.generarRecomendacion();


  }

  private static List<Asociacion> inicializarRepo() {
    RepositorioAsociaciones repoAsociaciones = new RepositorioAsociaciones();
    return repoAsociaciones.buscarTodos();
    // TODO conectarse a la DB en siguiente iteracion
  }

}