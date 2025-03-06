package com.example.crudusuario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.crudusuario.model.Proyecto;
import com.example.crudusuario.repository.ProyectoRepository;

/**
 * Servicio para la gestión de proyectos con validaciones.
 */
@Service
public class ProyectoService {
    private final ProyectoRepository proyectoRepository;

    /**
     * Inyección de dependencia del repositorio.
     */
    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    /**
     * Listar todos los proyectos.
     * @return Lista de proyectos.
     */
    public List<Proyecto> listarProyectos() {
        return proyectoRepository.findAll();
    }

    //  MÉTODO PARA OBTENER PROYECTOS POR USUARIO
    public List<Proyecto> obtenerProyectosPorUsuario(String username) {
        return proyectoRepository.findByUsuario_Username(username);
    }

    /**
     * Obtener un proyecto por su ID.
     * @param id Identificador del proyecto.
     * @return Proyecto encontrado.
     */
    public Proyecto obtenerProyectoPorId(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
    }

    /**
     * Guardar un nuevo proyecto o actualizar uno existente con validaciones.
     * @param proyecto Proyecto a guardar.
     */
    public void guardarProyecto(Proyecto proyecto) {
        if (proyecto.getNombre() == null || proyecto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proyecto no puede estar vacío.");
        }
        if (proyecto.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }
        proyectoRepository.save(proyecto);
    }

    /**
     * Actualizar un proyecto existente con validaciones.
     * @param id Identificador del proyecto.
     * @param proyectoActualizado Datos actualizados del proyecto.
     */
    public void actualizarProyecto(Long id, Proyecto proyectoActualizado) {
        Proyecto proyecto = obtenerProyectoPorId(id);
        if (proyectoActualizado.getNombre() == null || proyectoActualizado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proyecto no puede estar vacío.");
        }
        proyecto.setNombre(proyectoActualizado.getNombre());
        proyecto.setDescripcion(proyectoActualizado.getDescripcion());
        proyecto.setFechaInicio(proyectoActualizado.getFechaInicio());
        proyecto.setEstado(proyectoActualizado.getEstado());
        proyectoRepository.save(proyecto);
    }

    /**
     * Eliminar un proyecto por su ID solo si no tiene tareas asociadas.
     * @param id Identificador del proyecto a eliminar.
     */
    public void eliminarProyecto(Long id) {
        Proyecto proyecto = obtenerProyectoPorId(id);
        if (proyecto.getTareas() != null && !proyecto.getTareas().isEmpty()) {
            throw new RuntimeException("No se puede eliminar un proyecto con tareas asignadas.");
        }
        proyectoRepository.deleteById(id);
    }
}
