package com.lopezmiguel.crudstore.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity  //Marca Entidad persistente -> la Class se Mapeara en la BD
public class Producto {

    @Id //indica que es este campo es la clave primaria en la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) //le dice a JPA que este id se genera automaticamente en la BD
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String color;
    private String categoria;
    private String sku; //Código unico que identifica dicho producto
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Producto()
    {

    }

    public Producto(Long id, String nombre, String descripcion, Double precio, Integer stock, String color, String categoria, String sku, Boolean activo, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.color = color;
        this.categoria = categoria;
        this.sku = sku;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }


    //Un Productos puede aparecer en muchos detalles pedidos
    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detallePedidos = new ArrayList<>();

    //Un producto puede estar en muchos carritos, pero cada item del carrito referencia a solo un Producto
    @OneToMany(mappedBy = "producto")
    private List<ItemCarrito> itemsCarrito = new ArrayList<>();



}
