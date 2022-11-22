package caracteristica;

import javax.persistence.*;


@Entity
@Table(name = "Atributos")
public class Atributo  {

  @Id
  @GeneratedValue
  @Column(name = "id_atributo")
  private Long id;

  public Long getId() {
    return id;
  }

  @ManyToOne (cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
  @JoinColumn(name = "caracteristica_id", referencedColumnName = "id_caracteristica")
  private Caracteristica tipo;

  @Column(name = "valor_atributo")
  private String valorCaracteristica;


  public Atributo(Caracteristica caracteristica, String valorCaracteristica) {
    caracteristica.validar(valorCaracteristica);
    this.tipo = caracteristica;
    this.valorCaracteristica = valorCaracteristica;
  }

  public Atributo() {

  }

  public Caracteristica getTipo() {
    return tipo;
  }

  public String getValorCaracteristica() {
    return valorCaracteristica;
  }

}
