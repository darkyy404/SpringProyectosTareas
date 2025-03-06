package com.example.crudusuario.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.crudusuario.model.Proyecto;
import com.example.crudusuario.service.ProyectoService;
import com.example.crudusuario.service.UsuarioService;

/**
 * Controlador para gestionar proyectos.
 */
@Controller //Indica que esta clase es un controlador de Spring MVC
@RequestMapping("/proyectos") //Indica que las rutas de este controlador empiezan con /proyectos
public class ProyectoController {
    // Dependencias del servicio de proyectos
    private final ProyectoService proyectoService;

    /**
     * Constructor con Inyección de dependencias
     */
    public ProyectoController(ProyectoService proyectoService, UsuarioService usuarioService) {
        this.proyectoService = proyectoService;
    }

    /**
     * Muestra la lista de proyectos
     */
    @GetMapping // Indica que este método maneja peticiones GET
    public String listarProyectos(Model model) {
        List<Proyecto> proyectos = proyectoService.listarProyectos(); // Obtiene la lista de proyectos desde el servicio
        model.addAttribute("proyectos", proyectos); // Agrega los proyectos al modelo para la vista
        return "proyectos/index"; // Devuelve la vista en la carpeta proyectos
    }

    /**
     * Muestra el formulario para crear un nuevo proyecto
     */
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("proyecto", new Proyecto()); // Se pasa un nuevo objeto Proyecto vacío a la vista
        return "proyectos/crear";
    }

    /**
     * Maneja la solicitud POST para guardar un nuevo proyecto en la base de datos
     */
    @PostMapping("/guardar")
    public String guardarProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoService.guardarProyecto(proyecto);  // Guarda el proyecto en la base de datos
        return "redirect:/proyectos";  // Redirige a la lista de proyectos
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
        model.addAttribute("proyecto", proyecto);// Agrega el proyecto al modelo
        return "proyectos/editar";
    }

    /**
     * Maneja la solicitud POST para actualizar un proyecto existente en la base de datos.
     */
    @PostMapping("/actualizar/{id}")
    public String actualizarProyecto(@PathVariable Long id, @ModelAttribute Proyecto proyecto) {
        proyectoService.actualizarProyecto(id, proyecto);  
        return "redirect:/proyectos";  // Redirige a la página de listado de proyectos.
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
