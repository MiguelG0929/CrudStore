package com.lopezmiguel.crudstore.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private Double subtotal;
    private Double impuestos;
    private Double total;

    @Enumerated(EnumType.STRING)    //Config como se guarda Para BD
    private EstadoFactura estado;  //Declaracion de la variable

    public Factura()
    {

    }

    public Factura(Long id, String numeroFactura, LocalDateTime fechaEmision, Double subtotal, Double impuestos, Double total, EstadoFactura estado) {
        this.id = id;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.total = total;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Double impuestos) {
        this.impuestos = impuestos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    //Una Factura corresponde a un solo Pedido y un Pedido genera una sola factura
    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
