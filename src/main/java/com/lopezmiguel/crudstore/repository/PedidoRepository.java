package com.lopezmiguel.crudstore.repository;

import com.lopezmiguel.crudstore.model.EstadoPedido;
import com.lopezmiguel.crudstore.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>
{
    Optional<Pedido> findByNumPedido(String numPedido);  //Busca un pedido por su número único.
    List<Pedido> findByClienteId(Long clienteId);        //Devuelve todos los pedidos de un cliente usando su id
    List<Pedido> findByEstado(EstadoPedido estado);      //Devuelve todos los pedidos que tengan un estado específico

    //----Filtros para administrador----
    List<Pedido> findByFechaPedidoBetween(LocalDateTime start, LocalDateTime end); //Devuelve pedidos que se hicieron entre dos fechas, útil para reportes.
    List<Pedido> findByMetodoPago(String metodoPago);                              //Devuelve pedidos según el metodo de pago usados

    //----Pedidos por cliente con estado específico----
    List<Pedido> findByClienteIdAndEstado(Long clienteId, EstadoPedido estado);    //Devuelve los pedidos de un cliente solo si tienen un estado determinado.

    //----Búsqueda para seguimiento----
    @Query("SELECT p FROM Pedido p WHERE p.cliente.email = :email AND p.numPedido = :numPedido")
    Optional<Pedido> findByEmailAndNumPedido(@Param("email") String email, @Param("numPedido") String numPedido);
    //permite a un cliente verificar su pedido usando email + número de pedido.

    //----Estadísticas y reportes----
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.fechaPedido BETWEEN :start AND :end")
    Long countPedidosEnPeriodo(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    //Cuenta cuántos pedidos se hicieron en un periodo de tiempo.

    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.fechaPedido BETWEEN :start AND :end AND p.estado = 'ENTREGADO'")
    Double calcularVentasTotalesEnPeriodo(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    //Calcula el total de ventas de pedidos entregados en un periodo.

    //Pedidos pendientes de envío
    @Query("SELECT p FROM Pedido p WHERE p.estado IN :estados ORDER BY p.fechaPedido ASC")
    List<Pedido> findPedidosPendientesEnvio(@Param("estados") List<EstadoPedido> estados);
    //Devuelve los pedidos que todavía no se han enviado (PAGADO o PREPARACION)
    //Ordenados por fecha, los más antiguos primero, para priorizar el envío.
}
