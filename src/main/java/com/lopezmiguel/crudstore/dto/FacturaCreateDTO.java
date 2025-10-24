package com.lopezmiguel.crudstore.dto;

public class FacturaCreateDTO {
    private Long pedidoId;
    private Double impuestos;

    // Constructores
    public FacturaCreateDTO() {}

    public FacturaCreateDTO(Long pedidoId, Double impuestos) {
        this.pedidoId = pedidoId;
        this.impuestos = impuestos;
    }

    // Getters y Setters
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public Double getImpuestos() { return impuestos; }
    public void setImpuestos(Double impuestos) { this.impuestos = impuestos; }
}
