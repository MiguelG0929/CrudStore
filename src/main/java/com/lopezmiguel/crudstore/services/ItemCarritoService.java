package com.lopezmiguel.crudstore.services;

import com.lopezmiguel.crudstore.dto.ActualizarItemCarritoDTO;
import com.lopezmiguel.crudstore.dto.ItemCarritoDTO;

import java.util.List;

public interface ItemCarritoService {

    // Gestión de items
    List<ItemCarritoDTO> findByCarritoId(Long carritoId); //Devuelve todos los ítems (productos) que pertenecen a un carrito específico, identificado por su carritoId
    ItemCarritoDTO findById(Long id); //Busca y devuelve un ítem específico del carrito por su id.
    ItemCarritoDTO findByCarritoIdAndProductoId(Long carritoId, Long productoId); //Busca un ítem concreto en función del ID del carrito y del producto
    ItemCarritoDTO updateCantidad(Long id, ActualizarItemCarritoDTO actualizarDTO); //Permite modificar la cantidad de un producto dentro del carrito
    void delete(Long id); //Elimina un ítem específico del carrito
    void deleteByCarritoId(Long carritoId); //Elimina todos los ítems de un carrito determinado

    // Cálculos
    Double calcularSubtotalItem(Long itemId); //Calcula el subtotal de un ítems
    boolean existsByCarritoIdAndProductoId(Long carritoId, Long productoId); //Verifica si un producto ya existe en el carrito para evitar duplicarlo

    // Para administración
    List<ItemCarritoDTO> findAll(); //Devuelve una lista con todos los ítems de todos los carritos

}
