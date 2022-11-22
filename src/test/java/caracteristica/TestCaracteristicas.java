package caracteristica;

import Repositorios.RepositorioCaracteristicas;
import modelo.*;
import notificaciones.ContactoSMS;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import seguridadDeContraseñas.*;
import usuario.*;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestCaracteristicas extends AbstractPersistenceTest implements WithGlobalEntityManager {

    @BeforeEach
    public void antes() {
        this.beginTransaction();
    }

    @AfterEach
    public void despues() {
        this.rollbackTransaction(); // Comentar si se le quiere pegar a la DB
   // this.commitTransaction(); // descomentar si se le quiere pegar a la DB
    }

    // setup inicial de caracteristicas
    List<RestriccionesPassword> reglas = Arrays.asList( new PeoresContrasenas(),
        new ExpresionRegular(),
        new CaracterSecuencial(),
        new CaracterRepetido());

    Usuario usuarioAdmin = new CreadorUsuarios("usuario", "Pa$$word1", reglas)
        .crearUsuarioAdministrador();

    Usuario usuarioNormal = new CreadorUsuarios("usuario", "Pa$$word1" , reglas)
        .crearUsuarioNormal();

    CaracteristicaConValores castrado = new CaracteristicaConValores("¿Está castrado?",
        Arrays.asList("SI","NO"));

    CaracteristicaConValores colorPrincipal = new CaracteristicaConValores("¿De que color es su pelo?",
        Arrays.asList("NEGRO","BLANCO","MARRON","GRIS","DORADO"));

    CaracteristicaConValores vacunado = new CaracteristicaConValores("¿Está vacunado?",
        Arrays.asList("SI","NO"));

    CaracteristicaConValores tamano = new CaracteristicaConValores("Tamano",
        Arrays.asList("GRANDE","MEDIANO","PEQUENO"));

    CaracteristicaLibre estadoDeAnimo = new CaracteristicaLibre("Estado de animo");
    Usuario usser = new Usuario("usser" , "pass",UsuarioRol.NORMAL);

    Persona facu = new Persona("Facundo Pugliese" ,
        LocalDate.of(1994,2,25),
        TipoDocumento.DNI ,
        "38071803",
        null,
        new ContactoSMS("Facundo","1544110793",null));

    DuenioMascota duenoGenerico = new DuenioMascota(facu, usser);

    RepositorioCaracteristicas repo = new RepositorioCaracteristicas();


    @DisplayName("un admin puede crear categorias y agregarlas a la lista de categorias definidas")
    @Test
    void unUsuarioAdminCreaUnaCaracteristicaOK() {
        usuarioAdmin.agregarCaracteristica(new CaracteristicaConValores("Es Jugueton?",
            Arrays.asList("SI","NO","UN POCO")));
        usuarioAdmin.agregarCaracteristica(castrado);
        usuarioAdmin.agregarCaracteristica(tamano);


        Assertions.assertTrue(repo.getCaracteristicas().contains(castrado));
        Assertions.assertTrue(repo.getCaracteristicas().contains(tamano));
        Assertions.assertEquals(repo.getCaracteristicas().size(),3);
    }

    @DisplayName("un usuario normal no puede crear ni agregar nuevas caracteristicas")
    @Test
    void unUsuarioNormalNoPuedeAgregarCaracteristicas(){

        Assertions.assertThrows(RuntimeException.class,
            () -> usuarioNormal.agregarCaracteristica(new CaracteristicaConValores("Es Jugueton?",
            Arrays.asList("SI","NO","UN POCO"))));
    }

    @Test
    void mascotaNoPuedeTenerValoresNullComoParametrosPrincipales() {

        Assertions.assertThrows(NullPointerException.class,
            () -> new Mascota(TipoMascota.PERRO,null,"Mortadela", TamanioMascota.GRANDE,LocalDate.of(2018, 6,5),
                Sexo.FEMENINO,"realmente parece una mortadela", null , duenoGenerico));
    }


    @DisplayName("Puedo cargar categorias dinamicamente y asignarle valor")
    @Test
    void puedoCrearCaracteristicasDinamicamenteYSetearlasPosteriormente() {

        usuarioAdmin.agregarCaracteristica(tamano);
        Mascota mascota = cargarCaracteristicaMascota(new Atributo(tamano,"GRANDE"));

        Assertions.assertTrue(mascota.getAtributos()
                   .stream()
                   .map(Atributo::getTipo)
                   .anyMatch(tipo -> tipo.equals(tamano)));
    }

    @DisplayName("Puedo crear una caracteristica libre y me valida cualquier valor seteado")
    @Test
    void unaCaracteristicaLibreMeValidaCualquierValorYNoRompe(){

        Mascota mascota = cargarCaracteristicaMascota(new Atributo(estadoDeAnimo,"SE LO ENCONTRO CHOCHO, PROWLLING FOR BITCHIES"));

        Assertions.assertTrue(mascota
            .getAtributos()
            .stream()
            .map(Atributo::getTipo)
            .anyMatch(tipo -> tipo.equals(estadoDeAnimo))
        );
    }

    @Test
    void unaCaracteristicaLibreMeValidaCualquierValorYRompeConNull(){
        Assertions.assertThrows(NullPointerException.class,
            () -> cargarCaracteristicaMascota(new Atributo(estadoDeAnimo,null)));
    }

    private Mascota cargarCaracteristicaMascota(Atributo atributo){
       Mascota mascota = new Mascota(TipoMascota.PERRO,"Carlos","carlito", TamanioMascota.GRANDE,LocalDate.of(2017, 4,5),
            Sexo.MASCULINO,"marca calle, blanco y negro", null,duenoGenerico);
       mascota.agregarAtributo(atributo);

       return mascota;
    }


}
