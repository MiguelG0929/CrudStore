package com.lopezmiguel.crudstore.controller;

// Importación de las clases necesarias
import com.lopezmiguel.crudstore.dto.ProductoDTO; // DTO para mostrar productos
import com.lopezmiguel.crudstore.dto.ProductoCreateDTO; // DTO para crear productos
import com.lopezmiguel.crudstore.dto.ProductoUpdateDTO; // DTO para actualizar productos
import com.lopezmiguel.crudstore.services.ProductoService;  // Servicio con la lógica de negocio
import org.springframework.beans.factory.annotation.Autowired; // Inyección automática de dependencias
import org.springframework.http.ResponseEntity; // Representa respuestas HTTP
import org.springframework.web.bind.annotation.*; // Anotaciones REST (Get, Post, etc.)
import java.util.List; // Uso de listas

//Controlador REST para manejar las peticiones HTTP de productos
@RestController
@RequestMapping("/api/productos") // Ruta base para todos los endpoints de esta clase
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen (útil para Postman o frontends)
public class ProductoController {

    @Autowired // Inyecta automáticamente el servicio ProductoService
    private ProductoService productoService; // Objeto que contiene la lógica de negocio

    // GET - Obtener todos los productos
    @GetMapping // Responde a GET /api/productos
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        List<ProductoDTO> productos = productoService.findAll(); // Llama al servicio para obtener todos
        return ResponseEntity.ok(productos); // Devuelve la lista con código 200 OK
    }

    // GET - Obtener producto por ID
    @GetMapping("/{id}") // Responde a GET /api/productos/{id}
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        ProductoDTO producto = productoService.findById(id); // Busca producto por su ID
        return ResponseEntity.ok(producto); // Devuelve el producto encontrado
    }

    // GET - Obtener producto por SKU
    @GetMapping("/sku/{sku}") // Responde a GET /api/productos/sku/{sku}
    public ResponseEntity<ProductoDTO> getProductoBySku(@PathVariable String sku) {
        ProductoDTO producto = productoService.findBySku(sku); // Busca producto por su SKU
        return ResponseEntity.ok(producto); // Devuelve el producto encontrado

    }

    // POST - Crear nuevo producto
    @PostMapping // Responde a POST /api/productos
    public ResponseEntity<ProductoDTO> createProducto(@RequestBody ProductoCreateDTO productoCreateDTO) {
        ProductoDTO nuevoProducto = productoService.create(productoCreateDTO); // Crea un nuevo producto
        return ResponseEntity.ok(nuevoProducto); // Devuelve el producto creado con 200 OK
    }

    // PUT - Actualizar producto
    @PutMapping("/{id}") // Responde a PUT /api/productos/{id}
    public ResponseEntity<ProductoDTO> updateProducto(
            @PathVariable Long id, // ID del producto a actualizar (de la URL)
            @RequestBody ProductoUpdateDTO productoUpdateDTO) { // Nuevos datos enviados en el cuerpo
        ProductoDTO productoActualizado = productoService.update(id, productoUpdateDTO); // Actualiza el producto
        return ResponseEntity.ok(productoActualizado); // Devuelve el producto actualizado
    }

    // DELETE - Eliminar producto (soft delete)
    @DeleteMapping("/{id}") // Responde a DELETE /api/productos/{id}
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        productoService.delete(id); // Marca el producto como eliminado
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content (sin cuerpo)
    }

    // GET - Obtener productos activos
    @GetMapping("/activos") // Responde a GET /api/productos/activos
    public ResponseEntity<List<ProductoDTO>> getProductosActivos() {
        List<ProductoDTO> productos = productoService.findByActivoTrue(); // Busca productos activos
        return ResponseEntity.ok(productos); // Devuelve la lista de activos
    }

    // GET - Obtener productos por categoría
    @GetMapping("/categoria/{categoria}") // Responde a GET /api/productos/categoria/{categoria}
    public ResponseEntity<List<ProductoDTO>> getProductosByCategoria(@PathVariable String categoria) {
        List<ProductoDTO> productos = productoService.findByCategoria(categoria); // Busca productos por categoría
        return ResponseEntity.ok(productos); // Devuelve la lista encontrada
    }

    // GET - Búsqueda avanzada por texto
    @GetMapping("/buscar") // Responde a GET /api/productos/buscar?query=...
    public ResponseEntity<List<ProductoDTO>> buscarProductos(@RequestParam String query) {
        List<ProductoDTO> productos = productoService.busquedaAvanzada(query); // Busca productos por texto
        return ResponseEntity.ok(productos); // Devuelve los resultados
    }

    // GET - Obtener productos por rango de precio
    @GetMapping("/precio") // Responde a GET /api/productos/precio?min=...&max=...
    public ResponseEntity<List<ProductoDTO>> getProductosByPrecioRange(
            @RequestParam Double min, // Precio mínimo
            @RequestParam Double max) { // Precio máximo
        List<ProductoDTO> productos = productoService.findByPrecioBetween(min, max); // Busca en el rango
        return ResponseEntity.ok(productos); // Devuelve la lista filtrada
    }

    // GET - Obtener productos con stock bajo
    @GetMapping("/stock-bajo") // Responde a GET /api/productos/stock-bajo?stockMinimo=...
    public ResponseEntity<List<ProductoDTO>> getProductosStockBajo(@RequestParam Integer stockMinimo) {
        List<ProductoDTO> productos = productoService.findProductosConStockBajo(stockMinimo); // Busca productos con poco stock
        return ResponseEntity.ok(productos); // Devuelve la lista
    }
}
