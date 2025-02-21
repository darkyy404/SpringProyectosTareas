package com.example.crudusuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.crudusuario.model.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    // 🔹 Método para buscar proyectos según el usuario
    List<Proyecto> findByUsuario_Username(String username);
}