package com.example.crudusuario.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.crudusuario.model.Proyecto;
import com.example.crudusuario.model.Usuario;
import com.example.crudusuario.service.ProyectoService;
import com.example.crudusuario.service.UsuarioService;

/**
 * Controlador para gestionar proyectos.
 */
@Controller
@RequestMapping("/proyectos")
public class ProyectoController {
    private final ProyectoService proyectoService;
    private final UsuarioService usuarioService;

    /**
     * Inyección de dependencias.
     */
    public ProyectoController(ProyectoService proyectoService, UsuarioService usuarioService) {
        this.proyectoService = proyectoService;
        this.usuarioService = usuarioService;
    }

    /**
     * Muestra la lista de proyectos.
     * @param model Modelo de la vista.
     * @return Página de listado de proyectos.
     */
    @GetMapping
    public String listarProyectos(Model model) {
        List<Proyecto> proyectos = proyectoService.listarProyectos();
        model.addAttribute("proyectos", proyectos);
        return "proyectos/index";
    }

    /**
     * Muestra el formulario para crear un nuevo proyecto.
     * @param model Modelo de la vista.
     * @return Página del formulario de creación.
     */
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "proyectos/crear";
    }

    /**
     * Guarda un nuevo proyecto en la base de datos.
     * @param proyecto Datos del proyecto.
     * @return Redirección a la lista de proyectos.
     */
    @PostMapping("/guardar")
    public String guardarProyecto(@ModelAttribute Proyecto proyecto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuario = usuarioService.buscarPorUsername(username);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        proyecto.setUsuario(usuario); // Asignar el usuario antes de guardar
        proyectoService.guardarProyecto(proyecto);

        return "redirect:/proyectos";
    }

    /**
     * Muestra el formulario para editar un proyecto existente.
     * @param id ID del proyecto.
     * @param model Modelo de la vista.
     * @return Página del formulario de edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Proyecto proyecto = proyectoService.obtenerProyectoPorId(id);
        model.addAttribute("proyecto", proyecto);
        return "proyectos/editar";
    }

    /**
     * Actualiza un proyecto en la base de datos.
     * @param id ID del proyecto.
     * @param proyecto Datos actualizados.
     * @return Redirección a la lista de proyectos.
     */
    @PostMapping("/actualizar/{id}")
    public String actualizarProyecto(@PathVariable Long id, @ModelAttribute Proyecto proyecto) {
        proyectoService.actualizarProyecto(id, proyecto);
        return "redirect:/proyectos";
    }

    /**
     * Elimina un proyecto.
     * @param id ID del proyecto.
     * @return Redirección a la lista de proyectos.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable Long id) {
        proyectoService.eliminarProyecto(id);
        return "redirect:/proyectos";
    }
}
