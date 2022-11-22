package modelo;

import static java.util.Objects.requireNonNull;

import caracteristica.Atributo;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import org.uqbarproject.jpa.java8.extras.convert.LocalDateConverter;


@Entity
@Table(name = "Mascotas")
public class Mascota  {

  @Id
  @GeneratedValue
  @Column(name = "id_mascota")
  private Long id;

  public Long getId() {
    return id;
  }

  @Enumerated(EnumType.STRING)
  private TipoMascota tipo;

  private String nombre;

  private String apodo;


  @Column(name = "fecha_nacimiento")
  @Convert(converter = LocalDateConverter.class)
  private LocalDate fechaNacimiento;

  @Enumerated(EnumType.STRING)
  private Sexo sexo;

  private String descripcionFisica;

  @ElementCollection
  @CollectionTable(name = "fotos_mascotas" , joinColumns = @JoinColumn(name = "mascota_id"))
  private List<String> pathFotos = new ArrayList<>();

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "mascota_id")
  private Set<Atributo> atributos = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "duenio_id")
  private DuenioMascota duenio;

  @Enumerated(EnumType.STRING)
  private TamanioMascota tamanio;

  public Mascota() {

  }

  public Mascota(TipoMascota tipo, String nombre, String apodo, TamanioMascota tamanioMascota,
                 LocalDate fechaNacimiento, Sexo sexo, String descripcionFisica,
                 List<String> pathFoto, DuenioMascota duenio) {


    this.tipo = requireNonNull(tipo, "Debe especificarse el tipo");
    this.nombre = requireNonNull(nombre, "Debe especificarse el nombre");
    this.apodo = apodo;
    this.fechaNacimiento = requireNonNull(fechaNacimiento, "Debe especificarse "
        + "la fecha de nacimiento aproximada");
    this.sexo = requireNonNull(sexo, "Debe especificarse el sexo");
    this.descripcionFisica = requireNonNull(descripcionFisica, "Debe especificarse "
        + "la descripcion fisica");
    this.pathFotos = pathFoto;
    this.duenio = requireNonNull(duenio, "Debe especificarse un dueño para crear una mascota");
    this.tamanio = requireNonNull(tamanioMascota,
        "Tenes que especificar un tamaño para la mascota");

  }

  public TipoMascota getTipo() {
    return tipo;
  }

  public void agregarAtributo(Atributo atributo) {
    this.atributos.add(atributo);
  }

  public Set<Atributo> getAtributos() {
    return atributos;
  }

  public void setAtributos(List<Atributo> atributos) {
    //TODO: ver de agregar los atributos en el constructor y modificar los test. ¿O dejarlo así?
    this.atributos.addAll(atributos);
  }

  public int calcularEdad() {
    return Period
        .between(fechaNacimiento, LocalDate.now())
        .getYears();
  }

  public TamanioMascota getTamanio() {
    return tamanio;
  }

  public DuenioMascota getDuenio() {
    return this.duenio;
  }

  public String getNombreDuenio() { return this.duenio.getDuenio().getNombreApellido(); }

  public String getPrimeraFoto() {
    return this.pathFotos.get(0);
  }

  public String getNombre() {
    return nombre;
  }

  public String getApodo() {
    return apodo;
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public Sexo getSexo() {
    return sexo;
  }

  public List<String> getPathFotos() {
    return pathFotos;
  }

  public String getDescripcionFisica() {
    return descripcionFisica;
  }
}
