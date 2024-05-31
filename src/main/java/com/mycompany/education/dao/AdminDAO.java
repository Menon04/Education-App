package com.mycompany.education.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.education.models.Admin;

public class AdminDAO implements GenericDAO<Admin, Long> {
  private final Map<Long, Admin> datastore = new HashMap<>();
  private Long currentId = 1L;

  @Override
  public void create(Admin entity) {
    entity = new Admin(currentId, entity.email(), entity.senha());
    datastore.put(currentId, entity);
    currentId++;
  }

  @Override
  public Admin findById(Long id) {
    return datastore.get(id);
  }

  @Override
  public List<Admin> findAll() {
    return new ArrayList<>(datastore.values());
  }

  @Override
  public void update(Admin entity) {
    if (datastore.containsKey(entity.id())) {
      datastore.put(entity.id(), entity);
    }
  }

  @Override
  public void delete(Admin entity) {
    datastore.remove(entity.id());
  }
}
