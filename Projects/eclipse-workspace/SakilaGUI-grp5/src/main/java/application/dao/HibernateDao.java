package application.dao;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import application.core.DaoFactory;

public class HibernateDao<T> implements Dao<T> {

    private Class<T> clazz;
    Session session;

    public HibernateDao(Class<T> clazz) {
        this.clazz = clazz;
        session = DaoFactory.getInstance().getSession();
    }

    public List<T> getAll() throws PersistenceException {
        if (session.getTransaction().isActive()) {
            session.getTransaction().commit();
        }
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> rootEntry = query.from(clazz);
        CriteriaQuery<T> all = query.select(rootEntry);
        TypedQuery<T> allQuery = session.createQuery(all);
        List<T> data = allQuery.getResultList();
        session.getTransaction().commit();
        return data;
    }

    // public T getBy(int id) throws PersistenceException {
    // return session.get(clazz, id);
    // }

    public void create(T object) throws PersistenceException {
        if (session.getTransaction().isActive()) {
            session.getTransaction().commit();
        }
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
    }

    public void update(T object) throws PersistenceException {
        if (session.getTransaction().isActive()) {
            session.getTransaction().commit();
        }
        session.beginTransaction();
        session.update(object);
        session.getTransaction().commit();
    }

    public void delete(T object) throws PersistenceException {
        if (session.getTransaction().isActive()) {
            session.getTransaction().commit();
        }
        session.beginTransaction();
        session.delete(object);
        session.getTransaction().commit();
    }
}