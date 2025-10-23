package com.lopezmiguel.crudstore.repository;

import com.lopezmiguel.crudstore.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long>
{
    List<DetallePedido> findByPedidoId(Long pedidoId);
    List<DetallePedido> findByProductoId(Long productoId);

    // Para reportes de productos más vendidos
    @Query("SELECT dp.producto, SUM(dp.cantidad) as totalVendido " +
            "FROM DetallePedido dp " +
            "JOIN dp.pedido p " +
            "WHERE p.estado = 'ENTREGADO' AND p.fechaPedido BETWEEN :start AND :end " +
            "GROUP BY dp.producto " +
            "ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidos(@Param("start") java.time.LocalDateTime start,
                                            @Param("end") java.time.LocalDateTime end);

    /*Mira todos los pedidos entregados (los que ya se completaron) y suma cuántas unidades se vendieron de cada producto
    Solo tiene en cuenta los pedidos dentro del rango de fechas que le indiques
    y al final devolvera una lista ordenada de mayor a menor mostrando primero los productos que más se vendieron.*/



    // Calcular total de un pedido
    @Query("SELECT SUM(dp.subtotal) FROM DetallePedido dp WHERE dp.pedido.id = :pedidoId")
    Double calcularTotalPedido(@Param("pedidoId") Long pedidoId);

    /*Suma todos los subtotales (precio × cantidad) de los productos dentro del pedido indicado.
    Devuelve el total final del pedido en formato Double.*/
}
