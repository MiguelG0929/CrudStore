package com.lopezmiguel.crudstore.dto;

import com.lopezmiguel.crudstore.model.EstadoPedido;

import java.time.LocalDateTime;

public class PedidoUpdateDTO {

    private EstadoPedido estado;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaEntrega;

    // Constructores
    public PedidoUpdateDTO() {}

    public PedidoUpdateDTO(EstadoPedido estado, LocalDateTime fechaEnvio, LocalDateTime fechaEntrega) {
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntrega = fechaEntrega;
    }

    // Getters y Setters
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }
}