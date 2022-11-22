package servicio;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TestConexionApi {

  private ServicioGetPostApi apiMock;

  @BeforeEach
  void initMock() {
    apiMock = mock(ServicioGetPostApi.class);
  }

  @Test
  void conectoYpidoElTokenOk() {

    apiMock.conectandoALaAPIyToken();
    verify(apiMock, times(1)).conectandoALaAPIyToken();
  }

  @Test
  void siElPostParaObtenerElTokenFallaLanzoUnaExcepcion() {

    when(apiMock.conectandoALaAPIyToken()).thenThrow(APIInvalidResponse.class);

    Assertions.assertThrows(APIInvalidResponse.class, () -> apiMock.conectandoALaAPIyToken());
    verify(apiMock, times(1)).conectandoALaAPIyToken();
  }

  @Test
  void conectoYPidoLaListaDeHogares() {

    apiMock.respuestaDeHogaresApi();
    verify(apiMock, times(1)).respuestaDeHogaresApi();
  }

  @Test
  void siElGetHogaresFallaLanzoUnaExcepcion() {

    when(apiMock.respuestaDeHogaresApi()).thenThrow(APIInvalidResponse.class);

    Assertions.assertThrows(APIInvalidResponse.class, () -> apiMock.respuestaDeHogaresApi());
    verify(apiMock, times(1)).respuestaDeHogaresApi();
  }

//  @Test
//  void siLePegoALaAPIRealObtengoLaListaDeLos40HogaresCargados() {
//    ServicioGetPostApi servAPI = new ServicioGetPostApi();
//
//    List<Hogar> hogares = servAPI.respuestaDeHogaresApi();
//    Assertions.assertEquals(40,hogares.size());
//  }
}

