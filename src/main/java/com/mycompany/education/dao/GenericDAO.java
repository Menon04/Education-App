package com.mycompany.education.dao;

import java.util.List;

import com.mycompany.education.exceptions.CadastroException;

public interface GenericDAO<T, ID>{
    public void create(T entity) throws CadastroException;
    public T findById(ID id);
    public List<T> findAll();
    public void update(T entity);
    public void delete(T entity);
}