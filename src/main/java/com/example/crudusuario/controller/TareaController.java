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

    public TareaController(TareaService tareaService, ProyectoService proyectoService) {
        this.tareaService = tareaService;
        this.proyectoService = proyectoService;
    }

    /**
     * Muestra la lista de todas las tareas junto con sus proyectos.
     */
    @GetMapping("/todas")
    public String listarTodasLasTareas(Model model) {
        List<Tarea> tareas = tareaService.listarTodasLasTareas();

        if (tareas == null || tareas.isEmpty()) {
            model.addAttribute("mensaje", "No hay tareas registradas.");
        }

        model.addAttribute("tareas", tareas);

        return "tareas/listado_general";
    }


    /**
     * Muestra la lista de tareas de un proyecto específico.
     */
    @GetMapping("/proyecto/{proyectoId}")
    public String listarTareasPorProyecto(@PathVariable Long proyectoId, Model model) {
        Proyecto proyecto = proyectoService.obtenerProyectoPorId(proyectoId);
    
        if (proyecto == null) {
            model.addAttribute("error", "El proyecto con ID " + proyectoId + " no existe o ha sido eliminado.");
            return "tareas/index";
        }
    
        List<Tarea> tareas = tareaService.listarTareasPorProyecto(proyectoId);
    
        if (tareas == null || tareas.isEmpty()) {
            model.addAttribute("mensaje", "No hay tareas registradas en los proyectos.");
        }
    
        model.addAttribute("tareas", tareas);
        model.addAttribute("proyecto", proyecto);
    
        return "tareas/index";
    }
    

    /**
     * Muestra el formulario para crear una nueva tarea dentro de un proyecto.
     */
    @GetMapping("/crear/{proyectoId}")
    public String mostrarFormularioCreacion(@PathVariable Long proyectoId, Model model) {
        Proyecto proyecto = proyectoService.obtenerProyectoPorId(proyectoId);
        if (proyecto == null) {
            return "redirect:/proyectos";
        }

        model.addAttribute("tarea", new Tarea());
        model.addAttribute("proyectoId", proyectoId);
        return "tareas/crear";
    }

    /**
     * Guarda una nueva tarea en la base de datos.
     */
    @PostMapping("/guardar")
    public String guardarTarea(@ModelAttribute Tarea tarea, @RequestParam Long proyectoId) {
        Proyecto proyecto = proyectoService.obtenerProyectoPorId(proyectoId);
        if (proyecto == null) {
            return "redirect:/proyectos";
        }

        tarea.setProyecto(proyecto);
        tareaService.guardarTarea(tarea);

        return "redirect:/tareas/proyecto/" + proyectoId;
    }

    /**
     * Muestra el formulario para editar una tarea existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.obtenerTareaPorId(id);
        if (tarea == null) {
            return "redirect:/tareas";
        }

        model.addAttribute("tarea", tarea);
        model.addAttribute("proyecto", tarea.getProyecto());
        return "tareas/editar";
    }

    /**
     * Actualiza una tarea en la base de datos.
     */
    @PostMapping("/actualizar/{id}")
    public String actualizarTarea(@PathVariable Long id, @ModelAttribute Tarea tarea) {
        Tarea tareaExistente = tareaService.obtenerTareaPorId(id);
        if (tareaExistente == null || tareaExistente.getProyecto() == null) {
            return "redirect:/tareas";
        }

        tarea.setProyecto(tareaExistente.getProyecto());
        tareaService.actualizarTarea(id, tarea);

        return "redirect:/tareas/proyecto/" + tareaExistente.getProyecto().getId();
    }

    /**
     * Elimina una tarea.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id) {
        Tarea tarea = tareaService.obtenerTareaPorId(id);
        if (tarea != null && tarea.getProyecto() != null) {
            Long proyectoId = tarea.getProyecto().getId();
            tareaService.eliminarTarea(id);
            return "redirect:/tareas/proyecto/" + proyectoId;
        }
        return "redirect:/proyectos";
    }
}
