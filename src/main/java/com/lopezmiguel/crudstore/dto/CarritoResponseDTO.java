package com.lopezmiguel.crudstore.dto;

import java.util.ArrayList;
import java.util.List;

public class CarritoResponseDTO {
        private Long id;
        private Double total;
        private List<ItemCarritoDTO> items = new ArrayList<>();

        // Constructores
        public CarritoResponseDTO() {}

        public CarritoResponseDTO(Long id, Double total, List<ItemCarritoDTO> items) {
            this.id = id;
            this.total = total;
            this.items = items != null ? items : new ArrayList<>();
        }

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Double getTotal() { return total; }
        public void setTotal(Double total) { this.total = total; }

        public List<ItemCarritoDTO> getItems() { return items; }
        public void setItems(List<ItemCarritoDTO> items) { this.items = items; }
    }
