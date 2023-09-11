package com.eshop.bt.to;

import java.sql.SQLException;
import java.util.List;


public interface ServiceDAO<T,U> {
	T create(U t) throws SQLException;

    List<T> findAll() throws SQLException;

    T find(long id) throws SQLException;

    T update(long id,U t) throws SQLException;

    T delete(long id) throws SQLException;
}
