package com.mycompany.education.services;

import com.mycompany.education.dao.MaterialDAO;
import com.mycompany.education.models.Material;
import java.util.List;

public class MaterialService {
    private final MaterialDAO materialDAO;

    public MaterialService() {
        this.materialDAO = new MaterialDAO();
    }

    public List<Material> findAllMaterialsByStudent(Long studentId) {
        return materialDAO.findAllMaterialsByStudent(studentId);
    }
}
