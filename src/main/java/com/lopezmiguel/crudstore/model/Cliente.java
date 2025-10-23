package com.lopezmiguel.crudstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)  //no puede existir cliente sin nombre
    private String nombre;

    private String apellido;

    @Column(unique = true, nullable = false)  //no pueden haber dos client con el mismo email NO vacio/nulo
    private String email;

    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaRegistro;


    public Cliente()
    {

    }


    public Cliente(Long id, String nombre, String apellido, String email, String telefono, String direccion, LocalDate fechaNacimiento, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;

    }

    //Getters and Setters de Relaciones
    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    //Un Cliente puede hacer muchos pedidos, pero cada pedido es de un solo cliente
    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Pedido> pedidos = new ArrayList<>(); //Evitar NullPointerException por ello se inicializa
    /*Se usa @JsonIgnore para evitar bucles infinitos al convertir las relaciones en JSON.
    Sin esta anotación, al enviar datos al frontend, Spring intentaría convertir ambas partes
    de la relación (por ejemplo Cliente → Pedido → Cliente → Pedido...), causando un error o recursión.
    Además, evita enviar datos innecesarios al frontend si no son requeridos.
    En caso de necesitar estos datos más adelante, se puede usar DTOs o @JsonManagedReference/@JsonBackReference.
    @JsonIgnore */

    //Un cliente tiene un Carrito y un Carrito tiene un cliente
    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    private Carrito carrito;

}
