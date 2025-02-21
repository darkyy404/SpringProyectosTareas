package com.example.crudusuario.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.crudusuario.model.Tarea;
import com.example.crudusuario.model.Proyecto;
import com.example.crudusuario.model.EstadoTarea;
import com.example.crudusuario.repository.TareaRepository;
import com.example.crudusuario.repository.ProyectoRepository;

/**
 * Servicio para la gestión de tareas con validaciones.
 */
@Service
public class TareaService {
    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository;

    /**
     * Inyección de dependencias del repositorio de tareas y proyectos.
     */
    public TareaService(TareaRepository tareaRepository, ProyectoRepository proyectoRepository) {
        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
    }

    /**
     * Listar todas las tareas.
     * @return Lista de tareas.
     */
    public List<Tarea> listarTareas() {
        return tareaRepository.findAll();
    }

    /**
     * Listar todas las tareas asociadas a un proyecto específico.
     * @param proyectoId ID del proyecto.
     * @return Lista de tareas del proyecto.
     */
    public List<Tarea> listarTareasPorProyecto(Long proyectoId) {
        return tareaRepository.findByProyectoId(proyectoId);
    }

    /**
     * Obtener una tarea por su ID.
     * @param id Identificador de la tarea.
     * @return Tarea encontrada.
     */
    public Tarea obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + id));
    }

    /**
     * Guardar una nueva tarea o actualizar una existente con validaciones.
     * @param tarea Tarea a guardar.
     */
    public void guardarTarea(Tarea tarea) {
        if (tarea.getTitulo() == null || tarea.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea no puede estar vacío.");
        }
        if (tarea.getProyecto() == null || tarea.getProyecto().getId() == null) {
            throw new IllegalArgumentException("La tarea debe estar asociada a un proyecto válido.");
        }

        // Verificar si el proyecto asociado existe
        Proyecto proyecto = proyectoRepository.findById(tarea.getProyecto().getId())
                .orElseThrow(() -> new RuntimeException("Proyecto asociado no encontrado con ID: " + tarea.getProyecto().getId()));

        tarea.setProyecto(proyecto);
        tareaRepository.save(tarea);
    }

    /**
     * Actualizar una tarea existente con validaciones.
     * @param id Identificador de la tarea.
     * @param tareaActualizada Datos actualizados de la tarea.
     */
    public void actualizarTarea(Long id, Tarea tareaActualizada) {
        Tarea tarea = obtenerTareaPorId(id);
        if (tareaActualizada.getTitulo() == null || tareaActualizada.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea no puede estar vacío.");
        }
        tarea.setTitulo(tareaActualizada.getTitulo());
        tarea.setDescripcion(tareaActualizada.getDescripcion());
        tarea.setFechaLimite(tareaActualizada.getFechaLimite());
        tarea.setEstado(tareaActualizada.getEstado());
        tareaRepository.save(tarea);
    }

    /**
     * Eliminar una tarea solo si está en estado "Pendiente".
     * @param id Identificador de la tarea a eliminar.
     */
    public void eliminarTarea(Long id) {
        Tarea tarea = obtenerTareaPorId(id);
        if (tarea.getEstado() == EstadoTarea.EN_CURSO || tarea.getEstado() == EstadoTarea.COMPLETADA) {
            throw new RuntimeException("No se puede eliminar una tarea en curso o completada.");
        }
        tareaRepository.deleteById(id);
    }
}
