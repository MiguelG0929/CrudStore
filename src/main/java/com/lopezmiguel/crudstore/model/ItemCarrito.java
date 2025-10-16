package com.lopezmiguel.crudstore.model;

import jakarta.persistence.*;

@Entity
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    private Double precioEnMomento;
    private Double subtotal;

    public ItemCarrito()
    {

    }

    public ItemCarrito(Long id, Integer cantidad, Double precioEnMomento, Double subtotal, Carrito carrito, Producto producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioEnMomento = precioEnMomento;
        this.subtotal = subtotal;
        this.carrito = carrito;
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioEnMomento() {
        return precioEnMomento;
    }

    public void setPrecioEnMomento(Double precioEnMomento) {
        this.precioEnMomento = precioEnMomento;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    //Muchos item del Carrito pertencen a solo un Carrito
    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    //Muchos item del carrito referencian a un solo Producto
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;



}
