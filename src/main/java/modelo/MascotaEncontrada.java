package modelo;

import org.uqbarproject.jpa.java8.extras.convert.LocalDateConverter;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = "Mascotas_Encontradas")
public class MascotaEncontrada  {

  @Id
  @GeneratedValue
  @Column(name = "id_mascota_encontrada")
  private Long id;

  public Long getId() {
    return id;
  }

  @Enumerated(EnumType.STRING)
  private TipoMascota tipo;

  @ElementCollection
  @CollectionTable(name = "fotos_mascotas_encontradas" , joinColumns = @JoinColumn(name = "mascota_encontrada_id",nullable = true) )
  private List<String> pathFotos;

  private String descripcion;

  @Column(name = "fecha_rescate")
  @Convert(converter = LocalDateConverter.class)
  private LocalDate fechaRescate;

  @Embedded
  private Ubicacion lugarEncontrado;

  //TODO: Se modifica el CascadeType a PERSIST y la relacion a ManyToOne, esto debido a que si es un usuario logeado en el sistema tomaremos sus datos de la DB y si borramos a la mascota
  // no queremos borrar a la persona, esa validacion deberiamos hacerla en el controller
  @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinColumn(name = "persona_id")
  private Persona rescatista;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado")
  private EstadoMascotaRescatada estadoMascotaRescatada;

  @Enumerated(EnumType.STRING)
  @Column (name = "tamanio")
  private TamanioMascota tamanioMascota;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "mascota_id")
  private Mascota mascota;


  public MascotaEncontrada(TipoMascota tipo, List<String> pathFotos,
                           String descripcion, LocalDate fechaRescate,
                           Ubicacion lugarEncontrado, Persona rescatista,
                           TamanioMascota tamanioMascota, Mascota mascota) {
    this.tipo = tipo;
    this.pathFotos = pathFotos;
    this.descripcion = descripcion;
    this.fechaRescate = fechaRescate;
    this.lugarEncontrado = lugarEncontrado;
    this.rescatista = rescatista;
    this.tamanioMascota = tamanioMascota;
    this.mascota = mascota;
    estadoMascotaRescatada = EstadoMascotaRescatada.EN_TRANSITO;
  }

  public MascotaEncontrada() {

  }

  public String getDescripcionFisica() {return descripcion;}

  public LocalDate getFechaRescate() {
    return fechaRescate;
  }

  public TipoMascota getTipo() {
    return tipo;
  }

  public TamanioMascota getTamanioMascota() {
    return tamanioMascota;
  }

  public Ubicacion getLugarEncontrado() {
    return lugarEncontrado;
  }


  public Persona getRescatista() {
    return rescatista;
  }

  public Ubicacion getCasaRescatista() {
    return this.rescatista.getDireccion();
  }

  public boolean esPequenia() {
    return tamanioMascota == TamanioMascota.CHIQUITA;

  }

  public Mascota getMascota() {
    return mascota;
  }

  public void reclamada() {
    this.estadoMascotaRescatada = EstadoMascotaRescatada.RECLAMADA;
  }

  public void recuperadaPorElDuenio() {
    this.estadoMascotaRescatada = EstadoMascotaRescatada.RECUPERADA;
  }

  public EstadoMascotaRescatada getEstadoMascotaRescatada() {
    return estadoMascotaRescatada;
  }

  public boolean cumpleCaracteristicasHogar(List<String> listaCaracteristicasHogar) {
    if (mascota == null) {
      return false;
    } else {
      return listaCaracteristicasHogar.stream().allMatch(this::cumpleCaracteristica);
    }
  }

  private boolean cumpleCaracteristica(String caracteristica) {
    return mascota.getAtributos().stream().anyMatch(carac ->
        carac.getTipo().getDescripcion().equalsIgnoreCase(caracteristica)
            && carac.getValorCaracteristica().equalsIgnoreCase("SI"));
  }

  public String getPrimeraFoto() {
    return this.pathFotos.get(0);
  }

  public List<String> getPathFotos() {
    return pathFotos;
  }

  public String getDescripcion() {
    return descripcion;
  }
}
