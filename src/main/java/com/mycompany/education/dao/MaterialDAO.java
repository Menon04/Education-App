package com.mycompany.education.dao;

import com.mycompany.education.models.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialDAO implements GenericDAO<Material, Long> {
  private final Map<Long, Material> materiaisMap = new HashMap<>();
  private Long currentId = 1L;

  public void create(Material material) {
    material = new Material(currentId, material.titulo(), material.conteudo(), material.dataPublicacao());
    materiaisMap.put(currentId, material);
    currentId++;
  }

  public Material findById(Long id) {
    return materiaisMap.get(id);
  }

  public List<Material> findAll() {
    return new ArrayList<>(materiaisMap.values());
  }

  public void update(Material material) {
    if (materiaisMap.containsKey(material.id())) {
      materiaisMap.put(material.id(), material);
    }
  }

  public void delete(Material material) {
    materiaisMap.remove(material.id());
  }
}