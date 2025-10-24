package com.lopezmiguel.crudstore.dto;

public class ItemCarritoDTO {

    private Long id;
    private Integer cantidad;
    private Double precioEnMomento;
    private Double subtotal;
    private ProductoDTO producto;

    // Constructores
    public ItemCarritoDTO() {}

    public ItemCarritoDTO(Long id, Integer cantidad, Double precioEnMomento, Double subtotal, ProductoDTO producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioEnMomento = precioEnMomento;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecioEnMomento() { return precioEnMomento; }
    public void setPrecioEnMomento(Double precioEnMomento) { this.precioEnMomento = precioEnMomento; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public ProductoDTO getProducto() { return producto; }
    public void setProducto(ProductoDTO producto) { this.producto = producto; }
}
