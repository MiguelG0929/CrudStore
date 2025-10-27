package com.lopezmiguel.crudstore.services;

import com.lopezmiguel.crudstore.dto.CarritoDTO;
import com.lopezmiguel.crudstore.dto.ItemCarritoRequestDTO;

import java.util.List;

public interface CarritoService {

    // Gestión de carritos
    CarritoDTO findCarritoByClienteId(Long clienteId);//Busca el carrito perteneciente a un cliente en específico, usando su ID
    CarritoDTO createCarritoForCliente(Long clienteId); //Crea un nuevo carrito vacío para un cliente, normalmente cuando se registra o cuando todavía no tiene uno
    void deleteCarrito(Long carritoId); //Elimina un carrito completo, normalmente por motivos administrativos o de limpieza del sistema.
    void vaciarCarrito(Long carritoId); //Quita todos los productos del carrito, pero sin eliminar el carrito en sí

    // Gestión de items en carrito
    CarritoDTO agregarItem(Long carritoId, ItemCarritoRequestDTO itemRequest); //Agrega un nuevo producto al carrito.
    CarritoDTO actualizarCantidadItem(Long carritoId, Long itemId, Integer cantidad); //Permite modificar la cantidad de un producto ya agregado al carrito.
    CarritoDTO eliminarItem(Long carritoId, Long itemId); //Elimina un producto específico del carrito (por su itemId).

    // Cálculos y consultas
    Double calcularTotalCarrito(Long carritoId); //Calcula el precio total del carrito sumando los precios de todos los productos multiplicados por sus cantidades
    Integer contarItemsEnCarrito(Long carritoId); //Devuelve el número total de productos diferentes en el carrito
    boolean existeItemEnCarrito(Long carritoId, Long productoId); //Comprueba si un determinado producto ya está dentro del carrito

    // Para administración
    List<CarritoDTO> findAll(); //Devuelve todos los carritos existentes en el sistema
    CarritoDTO findById(Long id); //Busca un carrito por su ID único
}
