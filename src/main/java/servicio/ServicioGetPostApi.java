package servicio;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ServicioGetPostApi {
  private final String URLAPI = "https://api.refugiosdds.com.ar/api";
  private final String RESOURCE_HOGARES = "hogares";
  private final String RESOURCE_USUARIOS = "usuarios";
  private final String EMAIL_REGISTRACION = "patitasAlRescate@gmail.com.ar";
  private final String TOKEN = "b6XaeYpx9CViSTu0yZDtQOZUWVLVhbJ565NyRPvQwbTD1PVbfTEJw6FQ3yFP";

  private final int PRIMER_PAGINA_HOGARES = 1;

  public String conectandoALaAPIyToken( ){

      Client restClient = Client.create();
      WebResource webResource = restClient.resource(URLAPI).path(RESOURCE_USUARIOS);
      ClientResponse resp = webResource.type("application/json")
          .post(ClientResponse.class, "{\"email\":" + "\"" + EMAIL_REGISTRACION + "\"}");
      validacionConexion(resp);
      PostResponseBody postResponseBody = resp.getEntity(PostResponseBody.class);
      return postResponseBody.getToken(); //Aca deberia haber un setter a TOKEN

  }


  public List<Hogar> respuestaDeHogaresApi(){
    Client restClient = Client.create();
    List<Hogar> repoDeHogares = new ArrayList<>();
    int pagina = PRIMER_PAGINA_HOGARES;
    WebResource webResource = restClient.resource(URLAPI).path(RESOURCE_HOGARES);
    ClientResponse resp = getClientResponse(pagina, webResource);
    do{
      validacionConexion(resp);
      GetResponseBody output = resp.getEntity(GetResponseBody.class);
      repoDeHogares = Stream.concat(repoDeHogares.stream(), output.getHogares().stream()).collect(Collectors.toList());
      pagina++;
      } while ((resp = getClientResponse(pagina, webResource)).getStatus()==200);

    return repoDeHogares;
  }

  private ClientResponse getClientResponse(int pagina, WebResource webResource) {
    ClientResponse resp;
    resp = webResource.queryParam("offset",Integer.toString(pagina))
        .header("Content-Type", "application/json;charset=UTF-8")
        .header("Authorization", "Bearer " + TOKEN)
        .get(ClientResponse.class);
    return resp;
  }


  public void validacionConexion(ClientResponse resp)
    {
      if (resp.getStatus() != 200)
      {
        throw new APIInvalidResponse("Email ya en uso/No autorizado");
      }
    }
}








