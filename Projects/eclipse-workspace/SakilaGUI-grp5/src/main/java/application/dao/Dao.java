package application.dao;

import java.util.List;

import javax.persistence.PersistenceException;

public interface Dao<T> {
    List<T> getAll() throws PersistenceException;

    // T getBy(int id) throws PersistenceException;

    void create(T object) throws PersistenceException;

    void update(T object) throws PersistenceException;

    void delete(T object) throws PersistenceException;
}
