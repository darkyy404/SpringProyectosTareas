package com.example.crudusuario.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Clase que representa la entidad Proyecto en la base de datos.
 */
@Entity
@Table(name = "proyectos")
public class Proyecto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del proyecto (autoincremental)

    @Column(nullable = false)
    private String nombre; // Nombre del proyecto (obligatorio)

    private String descripcion; // Descripción opcional del proyecto

    @Column(nullable = false)
    private LocalDate fechaInicio; // Fecha de inicio del proyecto (obligatoria)

    @Enumerated(EnumType.STRING)
    private EstadoProyecto estado; // Estado del proyecto: Activo, En Progreso, Finalizado

    /**
     * Relación N:1 con la entidad Usuario.
     * Un usuario puede tener varios proyectos, pero un proyecto pertenece a un solo usuario. Futura funcionalidad
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) // Llave foránea en la BD
    private Usuario usuario;

    /**
     * Relación 1:N con la entidad Tarea.
     * Un proyecto puede tener muchas tareas asociadas.
     * - `cascade = CascadeType.ALL`: Si se elimina un proyecto, se eliminan sus tareas.
     * - `orphanRemoval = true`: Si una tarea deja de pertenecer a un proyecto, se elimina.
     */
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarea> tareas;

    // Constructor vacío requerido por JPA
    public Proyecto() {}

    // Constructor con parámetros
    public Proyecto(String nombre, String descripcion, LocalDate fechaInicio, EstadoProyecto estado, Usuario usuario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
        this.usuario = usuario;
    }

    // Métodos Getter y Setter
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public EstadoProyecto getEstado() { return estado; }
    public Usuario getUsuario() { return usuario; }
    public List<Tarea> getTareas() { return tareas; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setEstado(EstadoProyecto estado) { this.estado = estado; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setTareas(List<Tarea> tareas) { this.tareas = tareas; }
}

/**
 * Enumeración que define los posibles estados de un proyecto.
 */
enum EstadoProyecto {
    ACTIVO, EN_PROGRESO, FINALIZADO
}
