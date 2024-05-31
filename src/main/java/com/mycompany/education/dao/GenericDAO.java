package com.mycompany.education.dao;

import java.util.List;

public interface GenericDAO<T, ID> {
    public void create(T entity);
    public T findById(ID id);
    public List<T> findAll();
    public void update(T entity);
    public void delete(T entity);
}