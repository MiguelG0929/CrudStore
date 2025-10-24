package com.lopezmiguel.crudstore.dto;

public class ProductoUpdateDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String color;
    private String categoria;
    private Boolean activo;

    // Constructores
    public ProductoUpdateDTO() {}

    public ProductoUpdateDTO(String nombre, String descripcion, Double precio, Integer stock,
                             String color, String categoria, Boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.color = color;
        this.categoria = categoria;
        this.activo = activo;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}