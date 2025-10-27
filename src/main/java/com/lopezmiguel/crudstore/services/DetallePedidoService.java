package com.lopezmiguel.crudstore.services;

import com.lopezmiguel.crudstore.dto.DetallePedidoDTO;

import java.util.List;

public interface DetallePedidoService {

    // Consultas básicas
    List<DetallePedidoDTO> findAll(); //Devuelve todos los detalles de pedidos existentes en la base de datos
    DetallePedidoDTO findById(Long id); //Busca un detalle de pedido específico por su ID
    List<DetallePedidoDTO> findByPedidoId(Long pedidoId); //Devuelve todos los detalles asociados a un pedido concreto
    List<DetallePedidoDTO> findByProductoId(Long productoId); //Devuelve todos los pedidos en los que aparece un producto específico.

    // Reportes y estadísticas
    List<Object[]> findProductosMasVendidos(String start, String end); //Devuelve una lista con los productos más vendidos en un rango de fechas.
    Double calcularTotalPedido(Long pedidoId); //alcula el importe total de un pedido sumando los subtotales de cada detalle (precio × cantidad)

    // Para administración
    Long countByProductoId(Long productoId); //Devuelve el número total de veces que se ha vendido un producto

}
