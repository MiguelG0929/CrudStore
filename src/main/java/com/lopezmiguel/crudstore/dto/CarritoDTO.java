package com.lopezmiguel.crudstore.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarritoDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private Double total;
    private List<ItemCarritoDTO> items = new ArrayList<>();

    // Constructores
    public CarritoDTO() {}

    public CarritoDTO(Long id, LocalDateTime fechaCreacion, Double total, List<ItemCarritoDTO> items) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.total = total;
        this.items = items != null ? items : new ArrayList<>();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public List<ItemCarritoDTO> getItems() { return items; }
    public void setItems(List<ItemCarritoDTO> items) { this.items = items; }
}
