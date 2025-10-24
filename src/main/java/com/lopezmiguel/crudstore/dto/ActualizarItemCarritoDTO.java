package com.lopezmiguel.crudstore.dto;

public class ActualizarItemCarritoDTO {

    private Integer cantidad;

    // Constructores
    public ActualizarItemCarritoDTO() {}

    public ActualizarItemCarritoDTO(Integer cantidad) {
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}