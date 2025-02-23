package com.example.crudusuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crudusuario.model.Tarea;

/**
 * Repositorio JPA para la entidad Tarea.
 * Permite manejar las tareas asociadas a un proyecto.
 */
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    /**
     * Encuentra todas las tareas asociadas a un proyecto específico.
     * @param proyectoId ID del proyecto al que pertenecen las tareas.
     * @return Lista de tareas del proyecto.
     */
    List<Tarea> findByProyectoId(Long proyectoId);
    List<Tarea> findAll();

}
