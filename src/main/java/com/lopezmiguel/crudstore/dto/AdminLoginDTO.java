package com.lopezmiguel.crudstore.dto;

public class AdminLoginDTO {
    private String username;
    private String password;

    // Constructores
    public AdminLoginDTO() {}

    public AdminLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
