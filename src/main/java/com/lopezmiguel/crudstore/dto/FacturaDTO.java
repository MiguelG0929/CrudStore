package com.lopezmiguel.crudstore.dto;

import com.lopezmiguel.crudstore.model.EstadoFactura;

import java.time.LocalDateTime;

public class FacturaDTO {private Long id;
    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private Double subtotal;
    private Double impuestos;
    private Double total;
    private EstadoFactura estado;
    private PedidoDTO pedido;

    // Constructores
    public FacturaDTO() {}

    public FacturaDTO(Long id, String numeroFactura, LocalDateTime fechaEmision, Double subtotal,
                      Double impuestos, Double total, EstadoFactura estado, PedidoDTO pedido) {
        this.id = id;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.total = total;
        this.estado = estado;
        this.pedido = pedido;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getImpuestos() { return impuestos; }
    public void setImpuestos(Double impuestos) { this.impuestos = impuestos; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public PedidoDTO getPedido() { return pedido; }
    public void setPedido(PedidoDTO pedido) { this.pedido = pedido; }
}
