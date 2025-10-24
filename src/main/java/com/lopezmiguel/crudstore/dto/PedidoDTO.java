package com.lopezmiguel.crudstore.dto;

import com.lopezmiguel.crudstore.model.EstadoPedido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {

    private Long id;
    private String numPedido;
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaEntrega;
    private EstadoPedido estado;
    private String direccionEnvio;
    private String metodoPago;
    private Double total;
    private List<DetallePedidoDTO> detalles = new ArrayList<>();
    private ClienteDTO cliente;

    // Constructores
    public PedidoDTO() {}

    public PedidoDTO(Long id, String numPedido, LocalDateTime fechaPedido, LocalDateTime fechaEnvio,
                     LocalDateTime fechaEntrega, EstadoPedido estado, String direccionEnvio,
                     String metodoPago, Double total, List<DetallePedidoDTO> detalles, ClienteDTO cliente) {
        this.id = id;
        this.numPedido = numPedido;
        this.fechaPedido = fechaPedido;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
        this.total = total;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        this.cliente = cliente;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumPedido() { return numPedido; }
    public void setNumPedido(String numPedido) { this.numPedido = numPedido; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoDTO> detalles) { this.detalles = detalles; }

    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }
}
