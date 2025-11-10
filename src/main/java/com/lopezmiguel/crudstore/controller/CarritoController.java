package com.lopezmiguel.crudstore.controller;

//Imports de DTOs y servicios utilizados por el controlador
import com.lopezmiguel.crudstore.dto.CarritoDTO; // DTO que representa un carrito
import com.lopezmiguel.crudstore.dto.ItemCarritoRequestDTO; // DTO para añadir un item
import com.lopezmiguel.crudstore.dto.ActualizarItemCarritoDTO; // DTO para actualizar cantidad
import com.lopezmiguel.crudstore.services.CarritoService; // Servicio con la lógica de carritos
import org.springframework.beans.factory.annotation.Autowired; // Inyección de dependencias
import org.springframework.http.ResponseEntity; // Representación de respuestas HTTP
import org.springframework.web.bind.annotation.*; // Anotaciones REST (Get, Post, etc.)

import java.util.List; // Uso de listas

@RestController // Marca la clase como un controlador REST que devuelve JSON
@RequestMapping("/api/carritos") // Ruta base para todos los endpoints de carritos
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen (temporal/desarrollo)

public class CarritoController {

    @Autowired // Inyecta automáticamente la implementación de CarritoService
    private CarritoService carritoService; // Servicio que contiene la lógica de negocio

    // GET - Obtener carrito por cliente ID
    @GetMapping("/cliente/{clienteId}") // GET /api/carritos/cliente/{clienteId}
    public ResponseEntity<CarritoDTO> getCarritoByClienteId(@PathVariable Long clienteId) {
        CarritoDTO carrito = carritoService.findCarritoByClienteId(clienteId); // Obtiene carrito por cliente
        return ResponseEntity.ok(carrito); // Devuelve 200 OK con el carrito
    }

    // POST - Crear carrito para cliente
    @PostMapping("/cliente/{clienteId}") // POST /api/carritos/cliente/{clienteId}
    public ResponseEntity<CarritoDTO> createCarritoForCliente(@PathVariable Long clienteId) {
        CarritoDTO carrito = carritoService.createCarritoForCliente(clienteId); // Crea un carrito nuevo para el cliente
        return ResponseEntity.ok(carrito); // Devuelve el carrito creado
    }

    // POST - Agregar item al carrito
    @PostMapping("/{carritoId}/items") // POST /api/carritos/{carritoId}/items
    public ResponseEntity<CarritoDTO> agregarItemAlCarrito(
            @PathVariable Long carritoId, // ID del carrito al que añadir
            @RequestBody ItemCarritoRequestDTO itemRequest) { // Datos del item a añadir
        CarritoDTO carrito = carritoService.agregarItem(carritoId, itemRequest); // Añade el item y devuelve el carrito actualizado
        return ResponseEntity.ok(carrito); // Devuelve 200 OK con el carrito actualizado
    }

    // PUT - Actualizar cantidad de item en carrito
    @PutMapping("/{carritoId}/items/{itemId}") // PUT /api/carritos/{carritoId}/items/{itemId}
    public ResponseEntity<CarritoDTO> actualizarCantidadItem(
            @PathVariable Long carritoId, // ID del carrito
            @PathVariable Long itemId, // ID del item dentro del carrito
            @RequestBody ActualizarItemCarritoDTO actualizarDTO) { // DTO con la nueva cantidad
        CarritoDTO carrito = carritoService.actualizarCantidadItem(carritoId, itemId, actualizarDTO.getCantidad()); // Actualiza la cantidad
        return ResponseEntity.ok(carrito); // Devuelve carrito actualizado
    }

    // DELETE - Eliminar item del carrito
    @DeleteMapping("/{carritoId}/items/{itemId}") // DELETE /api/carritos/{carritoId}/items/{itemId}
    public ResponseEntity<CarritoDTO> eliminarItemDelCarrito(
            @PathVariable Long carritoId, // ID del carrito
            @PathVariable Long itemId) { // ID del item a eliminar
        CarritoDTO carrito = carritoService.eliminarItem(carritoId, itemId); // Elimina el item y devuelve carrito actualizado
        return ResponseEntity.ok(carrito); // Devuelve carrito actualizado
    }

    // DELETE - Vaciar carrito completo
    @DeleteMapping("/{carritoId}/vaciar") // DELETE /api/carritos/{carritoId}/vaciar
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long carritoId) { // ID del carrito a vaciar
        carritoService.vaciarCarrito(carritoId); // Elimina todos los items del carrito
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }

    // GET - Calcular total del carrito
    @GetMapping("/{carritoId}/total") // GET /api/carritos/{carritoId}/total
    public ResponseEntity<Double> getTotalCarrito(@PathVariable Long carritoId) { // ID del carrito
        Double total = carritoService.calcularTotalCarrito(carritoId); // Calcula el total (suma precio*cantidad)
        return ResponseEntity.ok(total); // Devuelve el total como número
    }

    // GET - Contar items en carrito
    @GetMapping("/{carritoId}/contar-items") // GET /api/carritos/{carritoId}/contar-items
    public ResponseEntity<Integer> contarItemsEnCarrito(@PathVariable Long carritoId) { // ID del carrito
        Integer cantidad = carritoService.contarItemsEnCarrito(carritoId); // Cuenta la cantidad de items distintos o totales según implementación
        return ResponseEntity.ok(cantidad); // Devuelve el número de items
    }

    // GET - Verificar si producto está en carrito
    @GetMapping("/{carritoId}/existe-producto/{productoId}") // GET /api/carritos/{carritoId}/existe-producto/{productoId}
    public ResponseEntity<Boolean> existeProductoEnCarrito(
            @PathVariable Long carritoId, // ID del carrito
            @PathVariable Long productoId) { // ID del producto a verificar
        boolean existe = carritoService.existeItemEnCarrito(carritoId, productoId); // Comprueba existencia del producto en el carrito
        return ResponseEntity.ok(existe); // Devuelve true o false
    }

    // GET - Todos los carritos (para admin)
    @GetMapping // GET /api/carritos
    public ResponseEntity<List<CarritoDTO>> getAllCarritos() {
        List<CarritoDTO> carritos = carritoService.findAll(); // Obtiene todos los carritos
        return ResponseEntity.ok(carritos); // Devuelve la lista de carritos
    } /*Aun no se como implementarlo*/


}