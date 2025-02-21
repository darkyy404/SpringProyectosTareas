package com.example.crudusuario.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Clase que representa la entidad Tarea en la base de datos.
 */
@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único de la tarea (autoincremental)

    @Column(nullable = false)
    private String titulo; // Título de la tarea (obligatorio)

    private String descripcion; // Descripción opcional de la tarea

    private LocalDate fechaLimite; // Fecha límite opcional para completar la tarea

    @Enumerated(EnumType.STRING)
    private EstadoTarea estado; // Estado de la tarea: Pendiente, En Curso, Completada

    /**
     * Relación N:1 con la entidad Proyecto.
     * Cada tarea está asociada a un único proyecto.
     */
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false) // Llave foránea en la BD
    private Proyecto proyecto;

    // Constructor vacío requerido por JPA
    public Tarea() {}

    // Constructor con parámetros
    public Tarea(String titulo, String descripcion, LocalDate fechaLimite, EstadoTarea estado, Proyecto proyecto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.estado = estado;
        this.proyecto = proyecto;
    }

    // Métodos Getter y Setter
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public EstadoTarea getEstado() { return estado; }
    public Proyecto getProyecto() { return proyecto; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
    public void setEstado(EstadoTarea estado) { this.estado = estado; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }
}
