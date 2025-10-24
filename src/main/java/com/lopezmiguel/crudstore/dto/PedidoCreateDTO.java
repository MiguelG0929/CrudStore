package com.lopezmiguel.crudstore.dto;

import java.util.ArrayList;
import java.util.List;

public class PedidoCreateDTO {
    private String direccionEnvio;
    private String metodoPago;
    private List<DetallePedidoCreateDTO> detalles = new ArrayList<>();

    // Constructores
    public PedidoCreateDTO() {}

    public PedidoCreateDTO(String direccionEnvio, String metodoPago, List<DetallePedidoCreateDTO> detalles) {
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
    }

    // Getters y Setters
    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public List<DetallePedidoCreateDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoCreateDTO> detalles) { this.detalles = detalles; }
}