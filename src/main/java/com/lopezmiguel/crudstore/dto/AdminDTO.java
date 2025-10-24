package com.lopezmiguel.crudstore.dto;

import java.time.LocalDateTime;

public class AdminDTO {

    private Long id;
    private String username;
    private String email;
    private String rol;
    private Boolean activo;
    private LocalDateTime fechaRegistro;

    // Constructores
    public AdminDTO() {}

    public AdminDTO(Long id, String username, String email, String rol, Boolean activo, LocalDateTime fechaRegistro) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}