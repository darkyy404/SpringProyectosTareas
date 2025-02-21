package com.example.crudusuario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    /**
     * Muestra la vista de login.
     */
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    /**
     * Muestra el dashboard del administrador.
     */
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
}
