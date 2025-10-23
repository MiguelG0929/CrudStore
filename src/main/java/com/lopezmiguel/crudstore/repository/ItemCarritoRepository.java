package com.lopezmiguel.crudstore.repository;

import com.lopezmiguel.crudstore.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long>
{
    List<ItemCarrito> findByCarritoId(Long carritoId); //Devuelve todos los productos que hay dentro de un carrito específico.
    Optional<ItemCarrito> findByCarritoIdAndProductoId(Long carritoId, Long productoId); //Busca un producto concreto dentro de un carrito.

    // Para actualizar cantidades
    @Modifying
    @Query("UPDATE ItemCarrito ic SET ic.cantidad = :cantidad, ic.subtotal = ic.precioEnMomento * :cantidad WHERE ic.id = :itemId")
    void actualizarCantidad(@Param("itemId") Long itemId, @Param("cantidad") Integer cantidad);
    /*Esta consulta actualiza la cantidad de un producto dentro del carrito
    y recalcula el subtotal (precio × cantidad).*/

    // Eliminar todos los items de un carrito
    @Modifying //se usa cuando una consulta modifica datos (por ejemplo, un UPDATE o DELETE)
    @Query("DELETE FROM ItemCarrito ic WHERE ic.carrito.id = :carritoId")
    void deleteByCarritoId(@Param("carritoId") Long carritoId);

    // Calcular total del carrito
    @Query("SELECT SUM(ic.subtotal) FROM ItemCarrito ic WHERE ic.carrito.id = :carritoId")
    Double calcularTotalCarrito(@Param("carritoId") Long carritoId);

    // Verificar si un producto ya está en el carrito
    Boolean existsByCarritoIdAndProductoId(Long carritoId, Long productoId);

}
