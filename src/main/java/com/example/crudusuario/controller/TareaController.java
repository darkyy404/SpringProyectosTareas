package com.example.crudusuario.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.crudusuario.model.Proyecto;
import com.example.crudusuario.model.Tarea;
import com.example.crudusuario.service.ProyectoService;
import com.example.crudusuario.service.TareaService;

/**
 * Controlador para gestionar tareas dentro de proyectos.
 */
@Controller
@RequestMapping("/tareas")
public class TareaController {
    private final TareaService tareaService;
    private final ProyectoService proyectoService;

    /**
     * Inyección de dependencias.
     */
    public TareaController(TareaService tareaService, ProyectoService proyectoService) {
        this.tareaService = tareaService;
        this.proyectoService = proyectoService;
    }

    /**
     * Muestra la lista de tareas de un proyecto.
     * @param proyectoId ID del proyecto.
     * @param model Modelo de la vista.
     * @return Página de listado de tareas.
     */
    @GetMapping("/proyecto/{proyectoId}")
    public String listarTareasPorProyecto(@PathVariable Long proyectoId, Model model) {
        List<Tarea> tareas = tareaService.listarTareasPorProyecto(proyectoId);
        Proyecto proyecto = proyectoService.obtenerProyectoPorId(proyectoId);
        model.addAttribute("tareas", tareas);
        model.addAttribute("proyecto", proyecto);
        return "tarea/index";
    }

    /**
     * Muestra el formulario para crear una nueva tarea dentro de un proyecto.
     * @param proyectoId ID del proyecto.
     * @param model Modelo de la vista.
     * @return Página del formulario de creación de tareas.
     */
    @GetMapping("/crear/{proyectoId}")
    public String mostrarFormularioCreacion(@PathVariable Long proyectoId, Model model) {
        model.addAttribute("tarea", new Tarea());
        model.addAttribute("proyectoId", proyectoId);
        return "tareas/crear";
    }

    /**
     * Guarda una nueva tarea en la base de datos.
     * @param tarea Datos de la tarea.
     * @param proyectoId ID del proyecto asociado.
     * @return Redirección a la lista de tareas del proyecto.
     */
    @PostMapping
    public String guardarTarea(@ModelAttribute Tarea tarea, @RequestParam Long proyectoId) {
        Proyecto proyecto = proyectoService.obtenerProyectoPorId(proyectoId);
        if (proyecto != null) {
            tarea.setProyecto(proyecto);
            tareaService.guardarTarea(tarea);
        }
        return "redirect:/tareas/proyecto/" + proyectoId;
    }

    /**
     * Muestra el formulario para editar una tarea existente.
     * @param id ID de la tarea.
     * @param model Modelo de la vista.
     * @return Página del formulario de edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.obtenerTareaPorId(id);
        model.addAttribute("tarea", tarea);
        return "tareas/editar";
    }

    /**
     * Actualiza una tarea en la base de datos.
     * @param id ID de la tarea.
     * @param tarea Datos actualizados.
     * @return Redirección a la lista de tareas del proyecto.
     */
    @PostMapping("/actualizar/{id}")
    public String actualizarTarea(@PathVariable Long id, @ModelAttribute Tarea tarea) {
        tareaService.actualizarTarea(id, tarea);
        return "redirect:/tareas/proyecto/" + tarea.getProyecto().getId();
    }

    /**
     * Elimina una tarea.
     * @param id ID de la tarea.
     * @return Redirección a la lista de tareas del proyecto.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id) {
        Tarea tarea = tareaService.obtenerTareaPorId(id);
        Long proyectoId = tarea.getProyecto().getId();
        tareaService.eliminarTarea(id);
        return "redirect:/tareas/proyecto/" + proyectoId;
    }
}
