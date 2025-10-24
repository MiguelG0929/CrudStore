package com.lopezmiguel.crudstore.dto;

public class AdminRegistroDTO {

    private String username;
    private String password;
    private String email;
    private String rol;

    // Constructores
    public AdminRegistroDTO() {}

    public AdminRegistroDTO(String username, String password, String email, String rol) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = rol;
    }

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}