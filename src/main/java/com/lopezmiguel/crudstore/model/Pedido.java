package com.lopezmiguel.crudstore.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numPedido;  //numero unico
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaEntrega;

    @Enumerated(EnumType.STRING)  // Esto guarda "PENDIENTE", "PAGADO" etc en la BD
    private EstadoPedido estado;

    private String direccionEnvio;
    private String metodoPago;
    private Double total;


    public Pedido()
    {

    }

    public Pedido(Long id, String numPedido, LocalDateTime fechaPedido, LocalDateTime fechaEnvio, LocalDateTime fechaEntrega, EstadoPedido estado, String direccionEnvio, String metodoPago, Double total) {
        this.id = id;
        this.numPedido = numPedido;
        this.fechaPedido = fechaPedido;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    //Muchos pedidos pertenecen a solo un Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    //Un Pedido contiene muchos prodcutos(detalles), pere cada detalle pertenece a solo un Pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles = new ArrayList<>();

    //Un Pedido tiene una sola Factura y una Factura corresponde a un solo Pedido
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Factura factura;


}
