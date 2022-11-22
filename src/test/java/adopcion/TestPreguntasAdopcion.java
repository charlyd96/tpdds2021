package adopcion;

import Repositorios.RepositorioAsociaciones;
import Repositorios.RepositorioPreguntas;
import modelo.*;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestPreguntasAdopcion extends AbstractPersistenceTest implements WithGlobalEntityManager {

  PreguntaAdopcion patio = new PreguntaAdopcion("Patio",
      "¿Necesitas que el adoptante tenga patio?",
      "¿Tenés patio?", Arrays.asList("SI","NO"));
  PreguntaAdopcion seLlevaBienConOtrasMascotas = new PreguntaAdopcion("Convivencia",
      "¿Tu mascota se lleva bien con otras mascotas?",
      "Tenes otras mascotas?", Arrays.asList("SI", "NO"));
  PreguntaAdopcion  color = new PreguntaAdopcion("Color",
      "¿De que de color es su pelaje",
      "De que color te gustaria que sea su pelo",
      Arrays.asList("MARRON","NEGRO","BLANCO","MACHITAS","GRIS"));
  PreguntaAdopcion amigableConChicos = new PreguntaAdopcion("ninos",
      "¿Es amigable con chicos pequenos?",
      "¿Tenes hijos pequenos?", Arrays.asList("SI","NO"));
  PreguntaAdopcion vacunado = new PreguntaAdopcion("vacunado",
      "¿Esta castrado?",
      "¿La mascota tiene que estar castrada", Arrays.asList("SI","NO"));

  Asociacion asociacionDeFlores ;
  Asociacion asociacionDePosadas;
  RepositorioAsociaciones repoAsociaciones = new RepositorioAsociaciones();
  RepositorioPreguntas repoPreguntas;

  @BeforeEach
  void initTests(){
    asociacionDeFlores = unaAsociacionEnBajoFlores();
    asociacionDePosadas = unaAsociacionEnPosadas();
    repoAsociaciones.agregar(asociacionDeFlores);
    repoAsociaciones.agregar(asociacionDePosadas);
    cargarRepoPreguntas();
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction(); // Comentar si se le quiere pegar a la DB
//    this.commitTransaction(); // descomentar si se le quiere pegar a la DB
  }


  @DisplayName("Puedo borrar preguntas globales ya cargadas")
  @Test
  void puedoBorrarPreguntasDeAdopcionGlobales() {
    Assertions.assertTrue(repoPreguntas.getPreguntas().contains(amigableConChicos));
    repoPreguntas.eliminar(amigableConChicos);
    Assertions.assertFalse(repoPreguntas.getPreguntas().contains(amigableConChicos));
  }

  @DisplayName("Cargo dos preguntas en una asociacion")
  @Test
  void puedoCargarPreguntasEnUnaAsociacion(){
    Assertions.assertEquals(0, asociacionDePosadas.getPreguntasAdopciones().size());
    asociacionDePosadas.agregarPreguntaAdopcion(vacunado);
    asociacionDePosadas.agregarPreguntaAdopcion(color);
    Assertions.assertEquals(2, asociacionDePosadas.getPreguntasAdopciones().size());
  }

  @Test
  void puedoObtenerElTotalDePreguntasCargadasEnCadaAsociacion() {
    asociacionDePosadas.agregarPreguntaAdopcion(vacunado);
    asociacionDePosadas.agregarPreguntaAdopcion(color);
    asociacionDePosadas.agregarPreguntaAdopcion(patio);
    asociacionDePosadas.agregarPreguntaAdopcion(seLlevaBienConOtrasMascotas);
    Assertions.assertEquals(4,preguntasTotalesAdopcion(asociacionDePosadas).size());
    Assertions.assertEquals(0,preguntasTotalesAdopcion(asociacionDeFlores).size());
  }

  @Test
  void puedoObtenerLaPreguntaPatioDelRepoPreguntasConUnaQuery() {
    repoPreguntas.agregar(patio);
    List<PreguntaAdopcion> patioPersistido = entityManager()
        .createQuery("From PreguntaAdopcion where identificador= :identificador")
        .setParameter("identificador", patio.getIdentificador())
        .getResultList();
    Assertions.assertEquals(patio.getIdentificador(), patioPersistido.get(0).getIdentificador());
  }

  @DisplayName("Puedo obtener todas las preguntas cargadas en el repo de preguntas globales")
  @Test
  void puedoObtenerTodasLasPreguntasDelRepoPreguntas() {
    List<PreguntaAdopcion> rp = repoPreguntas.getPreguntas();
    Assertions.assertEquals(5, rp.size());
    Assertions.assertTrue(rp.contains(seLlevaBienConOtrasMascotas));
  }

  private void cargarRepoPreguntas() {
    repoPreguntas = new RepositorioPreguntas();
    repoPreguntas.agregar(patio);
    repoPreguntas.agregar(seLlevaBienConOtrasMascotas);
    repoPreguntas.agregar(vacunado);
    repoPreguntas.agregar(color);
    repoPreguntas.agregar(amigableConChicos);
  }

  private Asociacion unaAsociacionEnBajoFlores() {
    return new Asociacion(new Ubicacion(-34.63334, -58.46668, ""));
  }

  private Asociacion unaAsociacionEnPosadas() {
    return new Asociacion(new Ubicacion(-27.36708, -55.89608, ""));
  }

  private List<PreguntaAdopcion> preguntasTotalesAdopcion(Asociacion asociacion) {
    return asociacion.getPreguntasAdopciones()
        .stream()
        .collect(Collectors.toList());
  }
}
