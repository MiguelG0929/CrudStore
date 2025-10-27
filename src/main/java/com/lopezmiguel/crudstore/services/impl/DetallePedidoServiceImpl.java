package com.lopezmiguel.crudstore.services.impl;

// Importaciones de DTOs y entidades
import com.lopezmiguel.crudstore.dto.DetallePedidoDTO; // DTO para detalles de pedido
import com.lopezmiguel.crudstore.dto.ProductoDTO; // DTO para producto dentro del detalle
import com.lopezmiguel.crudstore.model.DetallePedido; // Entidad DetallePedido
import com.lopezmiguel.crudstore.repository.DetallePedidoRepository; // Repositorio para DetallePedido
import com.lopezmiguel.crudstore.services.DetallePedidoService; // Interfaz que implementa esta clase

//Importaciones de Spring y utilidades
import org.springframework.beans.factory.annotation.Autowired; // Para inyección automática
import org.springframework.stereotype.Service; // Marca la clase como servicio gestionado por Spring

import java.time.LocalDateTime; // Para parseo de fechas
import java.util.List; // Interfaz List
import java.util.stream.Collectors; // Para transformar colecciones con streams

@Service // Declara esta clase como servicio de Spring
public class DetallePedidoServiceImpl implements DetallePedidoService { // Implementa la interfaz DetallePedidoService

    @Autowired // Inyecta automáticamente el repositorio
    private DetallePedidoRepository detallePedidoRepository; // Repositorio para operaciones sobre DetallePedido

    // Conversión Entity → DTO
    private DetallePedidoDTO convertToDTO(DetallePedido detalle) { // Método que transforma DetallePedido a DetallePedidoDTO
        ProductoDTO productoDTO = new ProductoDTO( // Construye el DTO del producto asociado al detalle
                detalle.getProducto().getId(), // ID del producto
                detalle.getProducto().getNombre(), // Nombre del producto
                detalle.getProducto().getDescripcion(), // Descripción del producto
                detalle.getProducto().getPrecio(), // Precio actual del producto
                detalle.getProducto().getStock(), // Stock del producto
                detalle.getProducto().getColor(), // Color del producto
                detalle.getProducto().getCategoria(), // Categoría del producto
                detalle.getProducto().getSku(), // SKU del producto
                detalle.getProducto().getActivo(), // Estado activo/inactivo del producto
                detalle.getProducto().getFechaCreacion(), // Fecha de creación del producto
                detalle.getProducto().getFechaActualizacion() // Fecha de última actualización del producto
        );

        return new DetallePedidoDTO( // Devuelve el DetallePedidoDTO con datos del detalle y producto
                detalle.getId(), // ID del detalle
                detalle.getCantidad(), // Cantidad pedida
                detalle.getPrecioUnitario(), // Precio unitario al crear el detalle
                detalle.getSubtotal(), // Subtotal (precio * cantidad)
                productoDTO // DTO del producto incluido en el detalle
        );
    }

    @Override
    public List<DetallePedidoDTO> findAll() { // Devuelve todos los detalles de pedido
        return detallePedidoRepository.findAll() // Recupera todas las entidades DetallePedido
                .stream() // Convierte la lista a Stream
                .map(this::convertToDTO) // Mapea cada entidad a su DTO
                .collect(Collectors.toList()); // Recolecta y devuelve una lista de DTOs
    }

    @Override
    public DetallePedidoDTO findById(Long id) { // Busca un detalle por su ID
        DetallePedido detalle = detallePedidoRepository.findById(id) // Intenta recuperar la entidad
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado con id: " + id)); // Lanza excepción si no existe
        return convertToDTO(detalle); // Convierte y devuelve el DTO
    }

    @Override
    public List<DetallePedidoDTO> findByPedidoId(Long pedidoId) { // Devuelve detalles asociados a un pedido
        return detallePedidoRepository.findByPedidoId(pedidoId) // Busca por pedidoId en el repositorio
                .stream() // Stream de resultados
                .map(this::convertToDTO) // Convierte cada entidad a DTO
                .collect(Collectors.toList()); // Devuelve la lista de DTOs
    }

    @Override
    public List<DetallePedidoDTO> findByProductoId(Long productoId) { // Devuelve detalles que contienen un producto específico
        return detallePedidoRepository.findByProductoId(productoId) // Busca por productoId
                .stream() // Stream de resultados
                .map(this::convertToDTO) // Convierte cada entidad a DTO
                .collect(Collectors.toList()); // Recolecta y devuelve la lista
    }

    @Override
    public List<Object[]> findProductosMasVendidos(String start, String end) { // Reporte de productos más vendidos en un rango
        LocalDateTime startDate = LocalDateTime.parse(start); // Convierte cadena start a LocalDateTime
        LocalDateTime endDate = LocalDateTime.parse(end); // Convierte cadena end a LocalDateTime

        return detallePedidoRepository.findProductosMasVendidos(startDate, endDate); // Llama al repo para obtener resultados agregados
    }

    @Override
    public Double calcularTotalPedido(Long pedidoId) { // Calcula el total de un pedido por su ID
        Double total = detallePedidoRepository.calcularTotalPedido(pedidoId); // Recupera la suma de subtotales desde el repositorio
        return total != null ? total : 0.0; // Si es null devuelve 0.0, si no devuelve el total
    }

    @Override
    public Long countByProductoId(Long productoId) { // Cuenta cuántos detalles incluyen un producto
        return detallePedidoRepository.findByProductoId(productoId) // Recupera lista de detalles por productoId
                .stream() // Convierte a stream
                .count(); // Cuenta los elementos y devuelve el total (Long)
    }
}
