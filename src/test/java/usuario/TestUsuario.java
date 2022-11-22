package usuario;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seguridadDeContraseñas.*;

import java.util.ArrayList;
import java.util.List;

public class TestUsuario {

  @DisplayName("Si la contraseña es insegura, crear usuario arroja excepcion")
  @Test
  void siLaContrasenanNoEsSeguraArrojaExcepcion()
  {
    Assertions.assertThrows(ContrasenaInseguraException.class,
          () -> crearUsuario("MeFaltaSimbol0").crearUsuarioNormal());

    Assertions.assertThrows(ContrasenaInseguraException.class,
          () -> crearUsuario("12345678910Aa!").crearUsuarioAdministrador());

    Assertions.assertThrows(ContrasenaInseguraException.class,
      () -> crearUsuario("aaabbbbR!3").crearUsuarioNormal());

    Assertions.assertThrows(ContrasenaInseguraException.class,
    () -> crearUsuario("aaabbbbR!3").crearUsuarioAdministrador());
  }


  @DisplayName("Si password esta en top 10.000 OWASP no valida")
  @Test
  void siLaContrasenaEstaEnElTop10000PeoresContrasenasNoEsValida()
  {
    RestriccionesPassword top10000Contrasenas = new PeoresContrasenas();

    Assertions.assertFalse(top10000Contrasenas.cumpleRestriccion("123456"));
    Assertions.assertFalse(top10000Contrasenas.cumpleRestriccion("qwerty"));
    Assertions.assertFalse(top10000Contrasenas.cumpleRestriccion("admin"));
  }

  @DisplayName("Si password no esta en top 10.000 OWASP, valida")
  @Test
  void siLaContrasenaNoEstaEnElTop10000PeoresContrasenasEsValida()
  {
    RestriccionesPassword top10000Contrasenas = new PeoresContrasenas();

    Assertions.assertTrue(top10000Contrasenas.cumpleRestriccion("CosmeFulanito"));
    Assertions.assertTrue(top10000Contrasenas.cumpleRestriccion("Anashe"));
    Assertions.assertTrue(top10000Contrasenas.cumpleRestriccion("LosBorbotones"));
  }

  @DisplayName("Si password no coincide con expresiones regulares no valida")
  @Test
  void siLaContrasenaNoCumpleConLasCondicionesDeExpresionesRegularesNoValida() {
      RestriccionesPassword expresionRegular = new ExpresionRegular();

    Assertions.assertFalse(expresionRegular.cumpleRestriccion("Menos!8"));
    Assertions.assertFalse(expresionRegular.cumpleRestriccion("sinmayuscul4!"));
    Assertions.assertFalse(expresionRegular.cumpleRestriccion("SINMINUSCUL4!"));
    Assertions.assertFalse(expresionRegular.cumpleRestriccion("sinDigito!"));
    Assertions.assertFalse(expresionRegular.cumpleRestriccion("sinCaracterEspeci4l"));
  }

  @DisplayName("Si password cumple con digito Mayuscula " +
      "minuscula minimo 8 y caracter especial, valida")
  @Test
  void siLaContrasenaCumpleConLasCondicionesDeExpresionesRegularesValida() {
    RestriccionesPassword expresionRegular = new ExpresionRegular();

    Assertions.assertTrue(expresionRegular.cumpleRestriccion("Menos!82"));
    Assertions.assertTrue(expresionRegular.cumpleRestriccion("sinmayuscul4!S"));
    Assertions.assertTrue(expresionRegular.cumpleRestriccion("SINMINUsCUL4!"));
    Assertions.assertTrue(expresionRegular.cumpleRestriccion("sinDigito!4"));
    Assertions.assertTrue(expresionRegular.cumpleRestriccion("sinCaracterEsp$ci4l"));
  }

  @DisplayName("Si password tiene al menos 3 caracteres secuenciales consecutivos, no valida")
  @Test
  void siLaContrasenaTieneAlMenos3CaracteresIgualesConsecutivosNoValida() {
    RestriccionesPassword caracteresSecuenciales = new CaracterSecuencial();

   Assertions.assertFalse(caracteresSecuenciales.cumpleRestriccion("abc"));
   Assertions.assertFalse(caracteresSecuenciales.cumpleRestriccion("12345"));
   Assertions.assertFalse(caracteresSecuenciales.cumpleRestriccion("asFe345pRob"));
  }

  @DisplayName("Si password no tiene al menos 3 caracteres secuenciales seguidos, valida")
  @Test
  void siLaContrasenaNoTieneAlMenos3CaracteresSecuencialesSeguidosValida() {
    RestriccionesPassword caracteresSecuenciales = new CaracterSecuencial();

    Assertions.assertTrue(caracteresSecuenciales.cumpleRestriccion("RicardoFort"));
    Assertions.assertTrue(caracteresSecuenciales.cumpleRestriccion("elcomandante"));
    Assertions.assertTrue(caracteresSecuenciales.cumpleRestriccion("validAndo"));
  }

  @DisplayName("Si password tiene al menos 3 caracteres iguales consecutivos no valida")
  @Test
  void siLaContrasenaTieneAlMenos3CaracteresConsecutivosIgualesNoValida() {
    RestriccionesPassword caracteresIguales = new CaracterRepetido();

    Assertions.assertFalse(caracteresIguales.cumpleRestriccion("111"));
    Assertions.assertFalse(caracteresIguales.cumpleRestriccion("AAAAAAAA"));
    Assertions.assertFalse(caracteresIguales.cumpleRestriccion("!!!!!!!!!!!!!!!!!!!"));
  }


  @DisplayName("Si password no tiene caracteres iguales consecutivos 3 o mas veces, valida")
  @Test
  void siLaContrasenaNoTiene3CaracteresIgualesConsecutivosValida() {
    RestriccionesPassword caracteresIguales = new CaracterRepetido();

    Assertions.assertTrue(caracteresIguales.cumpleRestriccion("aa"));
    Assertions.assertTrue(caracteresIguales.cumpleRestriccion("aZ9"));
    Assertions.assertTrue(caracteresIguales.cumpleRestriccion("1HolaProba!ndo"));
    Assertions.assertTrue(caracteresIguales.cumpleRestriccion("soyvalid0"));
  }

  private CreadorUsuarios crearUsuario(String password) {
    List<RestriccionesPassword> reglas = new ArrayList<>();

    reglas.add(new PeoresContrasenas());
    reglas.add(new ExpresionRegular());
    reglas.add(new CaracterRepetido());
    reglas.add(new CaracterSecuencial());

    return new CreadorUsuarios("usuario",password, reglas);
  }







}
