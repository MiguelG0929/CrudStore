package com.lopezmiguel.crudstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCreacion;
    private Double total;

    public Carrito()
    {

    }

    public Carrito(Long id, LocalDateTime fechaCreacion, Double total) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    //Getters and Setters de relaciones
    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    //Un Cliente tiene un solo carrito y un carrito pertenece a un solo cliente
    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    //Un carrito puede tener muchos items, pero cada item esta en solo un carrito
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ItemCarrito> items = new ArrayList<>();



}
