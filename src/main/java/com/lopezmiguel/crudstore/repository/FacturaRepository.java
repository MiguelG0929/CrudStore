package com.lopezmiguel.crudstore.repository;

import com.lopezmiguel.crudstore.model.EstadoFactura;
import com.lopezmiguel.crudstore.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long>
{
    Optional<Factura> findByNumeroFactura(String numeroFactura);  //Busca una factura concreta por su número (por ejemplo, "F-00123").
    Optional<Factura> findByPedidoId(Long pedidoId); //Busca la factura asociada a un pedido concreto.
    List<Factura> findByEstado(EstadoFactura estado); //Devuelve todas las facturas que tengan un estado específico.

    // Facturas por cliente
    @Query("SELECT f FROM Factura f JOIN f.pedido p WHERE p.cliente.id = :clienteId")
    List<Factura> findByClienteId(@Param("clienteId") Long clienteId);
    /*Busca todas las facturas de un cliente específico.
    * Se une (JOIN) con la tabla de pedidos para obtener las facturas según el cliente.*/

    // Facturas en un período
    List<Factura> findByFechaEmisionBetween(LocalDateTime start, LocalDateTime end); //Devuelve las facturas emitidas entre dos fechas.

    // Generar número de factura automático
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(f.numeroFactura, 3) AS Long)), 0) FROM Factura f WHERE f.numeroFactura LIKE 'F-%'")
    Long findMaxNumeroFactura();
    /*Busca el número más alto de factura existente (por ejemplo, si la última fue F-102, devuelve 102).
    Esto sirve para generar la siguiente factura automáticamente (F-103).*/



    // Reporte de facturación
    @Query("SELECT SUM(f.total) FROM Factura f WHERE f.fechaEmision BETWEEN :start AND :end AND f.estado = 'PAGADA'")
    Double calcularFacturacionTotal(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    /*Suma el total de todas las facturas pagadas en el periodo de fechas indicado.
    Es ideal para obtener cuánto dinero ingresó la tienda en un mes o un año.*/

}
