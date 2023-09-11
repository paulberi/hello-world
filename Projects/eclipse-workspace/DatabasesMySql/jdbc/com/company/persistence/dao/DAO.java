package com.company.persistence.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {

    int create(T t) throws SQLException;

    List<T> findAll() throws SQLException;

    T find(Object id) throws SQLException;

    int update(T t) throws SQLException;

    int delete(T t) throws SQLException;

}
