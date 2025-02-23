package com.example.crudusuario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.crudusuario.model.EstadoTarea;
import com.example.crudusuario.model.Proyecto;
import com.example.crudusuario.model.Tarea;
import com.example.crudusuario.repository.ProyectoRepository;
import com.example.crudusuario.repository.TareaRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Servicio para la gestión de tareas con validaciones y control de estado.
 */
@Service
public class TareaService {
    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository;

    public TareaService(TareaRepository tareaRepository, ProyectoRepository proyectoRepository) {
        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
    }

    /**
     * Listar todas las tareas en la base de datos.
     */
    public List<Tarea> listarTodasLasTareas() {
        return tareaRepository.findAll();
    }

    /**
     * Listar tareas por proyecto.
     */
    public List<Tarea> listarTareasPorProyecto(Long proyectoId) {
        List<Tarea> tareas = tareaRepository.findByProyectoId(proyectoId);
        return (tareas != null) ? tareas : List.of(); // Retorna una lista vacía en caso de null
    }

    /**
     * Obtener una tarea por su ID.
     */
    public Tarea obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarea no encontrada con ID: " + id));
    }

    /**
     * Guardar una nueva tarea con validaciones.
     */
    public void guardarTarea(Tarea tarea) {
        if (tarea.getTitulo() == null || tarea.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea no puede estar vacío.");
        }

        if (tarea.getProyecto() == null || tarea.getProyecto().getId() == null) {
            throw new IllegalArgumentException("La tarea debe estar asociada a un proyecto válido.");
        }

        Long proyectoId = tarea.getProyecto().getId();
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        tarea.setProyecto(proyecto);
        tareaRepository.save(tarea);
    }

    /**
     * Actualizar una tarea existente con validaciones.
     */
    public void actualizarTarea(Long id, Tarea tareaActualizada) {
        Tarea tareaExistente = obtenerTareaPorId(id);

        if (tareaActualizada.getTitulo() == null || tareaActualizada.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea no puede estar vacío.");
        }

        tareaActualizada.setProyecto(tareaExistente.getProyecto()); // Mantener el mismo proyecto

        tareaExistente.setTitulo(tareaActualizada.getTitulo());
        tareaExistente.setDescripcion(tareaActualizada.getDescripcion());
        tareaExistente.setFechaLimite(tareaActualizada.getFechaLimite());
        tareaExistente.setEstado(tareaActualizada.getEstado());

        tareaRepository.save(tareaExistente);
    }

    /**
     * Eliminar una tarea si está en estado "Pendiente".
     */
    public void eliminarTarea(Long id) {
        Tarea tarea = obtenerTareaPorId(id);

        if (tarea.getEstado() == EstadoTarea.EN_CURSO || tarea.getEstado() == EstadoTarea.COMPLETADA) {
            throw new IllegalStateException("No se puede eliminar una tarea en curso o completada.");
        }

        tareaRepository.deleteById(id);
    }
}
