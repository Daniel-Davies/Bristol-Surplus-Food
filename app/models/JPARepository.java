package models;


import ch.qos.logback.core.db.dialect.SybaseSqlAnywhereDialect;
import com.google.inject.Inject;
import play.db.jpa.JPAApi;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaUpdate;
import java.util.List;

public class JPARepository<T> {

    @Inject
    protected JPAApi jpaApi;

    public void insert(T o){
        jpaApi.em().persist(o);
    }

    public T findById(Class<T> tClass, int id){
        T result = jpaApi.em().find(tClass, id);
        if (result == null) {
            throw new EntityNotFoundException(
                    "Can't find entity of type " + tClass+ " for ID " + id+"\n");
        }
        return result;
    }

    protected T findByAttribute(Class<T> tClass, String attribute, Object value){

        // prelude to load safe selectors
        CriteriaBuilder b = jpaApi.em().getCriteriaBuilder();
        CriteriaQuery<T> query = b.createQuery(tClass);
        Root<T> root = query.from(tClass);

        // corresponds to SELECT * FROM tClassTable WHERE attribute = value;
        query.select(root);
        query.where(b.equal(root.get(attribute), value));

        T result = jpaApi.em().createQuery(query).getSingleResult();

        if (result == null) {
            throw new EntityNotFoundException(
                    "Can't find entity of type " + tClass + " for attribute " + attribute + " with value: " + value);
        }
        return result;
    }

    public void remove(T o){
        jpaApi.em().remove(o);
    }

    protected int update(Class<T> tClass, int id, String attribute, Object newValue) {
        CriteriaBuilder b = jpaApi.em().getCriteriaBuilder();

        // create update
        CriteriaUpdate<T> updateQuery = b.createCriteriaUpdate(tClass);

        // set the root class
        Root root = updateQuery.from(tClass);
        updateQuery.where(b.equal(root.get("id"), id));

        // set update and where clause
        updateQuery.set(attribute, newValue);

        // perform update and return the number of entities updated
        return jpaApi.em().createQuery(updateQuery).executeUpdate();
    }


    public List<T> listAll(Class<T> tClass) {

        // prelude to load safe selectors
        CriteriaBuilder b = jpaApi.em().getCriteriaBuilder();
        CriteriaQuery<T> query = b.createQuery(tClass);
        Root<T> root = query.from(tClass);

        // corresponds to SELECT * FROM tClassTable ORDER BY orderAttributeName;
        query.select(root);
        List<T> result = jpaApi.em().createQuery(query).getResultList();
        return result;
    }

}
