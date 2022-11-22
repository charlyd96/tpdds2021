package Repositorios;

import org.joda.time.Interval;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class Repositorio<T> implements WithGlobalEntityManager {

  private Class<T> type;

  public Repositorio(Class<T> type) {
  this.type = type;
  }

  public void agregar(Object unObjeto) {
    entityManager().persist(unObjeto);
    entityManager().flush();
  }

  public void modificar(Object unObjeto) {
    entityManager().persist(unObjeto);
    entityManager().flush();
  }

  public void eliminar(Object unObjeto) {
    entityManager().remove(unObjeto);
    entityManager().flush();
  }

  public List<T> buscarTodos() {
    CriteriaBuilder builder = entityManager().getCriteriaBuilder();
    CriteriaQuery<T> critera = builder.createQuery(type);
    critera.from(type);
    List<T> entities =  entityManager().createQuery(critera).getResultList();
    return entities;
  }

  public T buscar(int id) {
    return  entityManager().find(type, (long) id);
  }

}
