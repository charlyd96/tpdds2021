package servicio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import modelo.MascotaEncontrada;
import modelo.TipoMascota;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "nombre",
    "ubicacion",
    "telefono",
    "admisiones",
    "capacidad",
    "lugares_disponibles",
    "patio",
    "caracteristicas"
})

public class Hogar {

  @JsonProperty("id")
  private String id;
  @JsonProperty("nombre")
  private String nombre;
  @JsonProperty("ubicacion")
  private UbicacionHogarAPI ubicacionHogarAPI;
  @JsonProperty("telefono")
  private String telefono;
  @JsonProperty("admisiones")
  private Admisiones admisiones;
  @JsonProperty("capacidad")
  private Integer capacidad;
  @JsonProperty("lugares_disponibles")
  private Integer lugaresDisponibles;
  @JsonProperty("patio")
  private Boolean patio;
  @JsonProperty("caracteristicas")
  private List<String> caracteristicasAceptadas = null;


  public Hogar() {
  }


  public Hogar(String id, String nombre, UbicacionHogarAPI ubicacionHogarAPI, String telefono, Admisiones admisiones, Integer capacidad,
               Integer lugaresDisponibles, Boolean patio, List<String> caracteristicasAceptadas) {
    super();
    this.id = id;
    this.nombre = nombre;
    this.ubicacionHogarAPI = ubicacionHogarAPI;
    this.telefono = telefono;
    this.admisiones = admisiones;
    this.capacidad = capacidad;
    this.lugaresDisponibles = lugaresDisponibles;
    this.patio = patio;
    this.caracteristicasAceptadas = caracteristicasAceptadas;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("nombre")
  public String getNombre() {
    return nombre;
  }

  @JsonProperty("nombre")
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  @JsonProperty("ubicacion")
  public UbicacionHogarAPI getUbicacion() {
    return ubicacionHogarAPI;
  }

  @JsonProperty("ubicacion")
  public void setUbicacion(UbicacionHogarAPI ubicacionHogarAPI) {
    this.ubicacionHogarAPI = ubicacionHogarAPI;
  }

  @JsonProperty("telefono")
  public String getTelefono() {
    return telefono;
  }

  @JsonProperty("telefono")
  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  @JsonProperty("admisiones")
  public Admisiones getAdmisiones() {
    return admisiones;
  }

  @JsonProperty("admisiones")
  public void setAdmisiones(Admisiones admisiones) {
    this.admisiones = admisiones;
  }

  @JsonProperty("capacidad")
  public Integer getCapacidad() {
    return capacidad;
  }

  @JsonProperty("capacidad")
  public void setCapacidad(Integer capacidad) {
    this.capacidad = capacidad;
  }

  @JsonProperty("lugares_disponibles")
  public Integer getLugaresDisponibles() {
    return lugaresDisponibles;
  }

  @JsonProperty("lugares_disponibles")
  public void setLugaresDisponibles(Integer lugaresDisponibles) {
    this.lugaresDisponibles = lugaresDisponibles;
  }

  @JsonProperty("patio")
  public Boolean getPatio() {
    return patio;
  }

  @JsonProperty("patio")
  public void setPatio(Boolean patio) {
    this.patio = patio;
  }

  @JsonProperty("caracteristicas")
  public List<String> getCaracteristicasAceptadas() {
    return caracteristicasAceptadas;
  }

  @JsonProperty("caracteristicas")
  public void setCaracteristicasAceptadas(List<String> caracteristicasAceptadas) {
    this.caracteristicasAceptadas = caracteristicasAceptadas;
  }


  public boolean aceptoMascota(MascotaEncontrada mascota) {
    return hayLugaresDisponibles() && aceptaElTipoDeMascota(mascota)
        && aceptaTamanioMascotaPatio(mascota) && cumpleCaracteristicas(mascota);
  }

  private boolean aceptaElTipoDeMascota(MascotaEncontrada mascota) {
    return ((mascota.getTipo() == TipoMascota.PERRO && this.admisiones.getPerros())
        || (mascota.getTipo() == TipoMascota.GATO && this.admisiones.getGatos()));
  }

  private boolean aceptaTamanioMascotaPatio(MascotaEncontrada mascota) {
    return ((this.patio)
        || mascota.esPequenia());
  }

  private boolean cumpleCaracteristicas(MascotaEncontrada mascota) {
    if (this.caracteristicasAceptadas.size() != 0 ) {
      return mascota.cumpleCaracteristicasHogar(caracteristicasAceptadas);
    } else
      return true;
  }

  public boolean hayLugaresDisponibles() {
    return (this.lugaresDisponibles > 0);
  }
}