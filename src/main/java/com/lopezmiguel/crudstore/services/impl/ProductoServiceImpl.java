// Define el paquete donde se encuentra la clase
package com.lopezmiguel.crudstore.services.impl;

// Importa las clases DTO (objetos que se usan para transferir datos)
import com.lopezmiguel.crudstore.dto.ProductoDTO;
import com.lopezmiguel.crudstore.dto.ProductoCreateDTO;
import com.lopezmiguel.crudstore.dto.ProductoUpdateDTO;

// Importa la entidad Producto (representa la tabla en la base de datos)
import com.lopezmiguel.crudstore.model.Producto;

// Importa el repositorio que se comunica con la base de datos
import com.lopezmiguel.crudstore.repository.ProductoRepository;

// Importa la interfaz del servicio (contrato que esta clase implementa)


// Importaciones de Spring necesarias para la lógica del servicio
import com.lopezmiguel.crudstore.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// Librerías Java estándar
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Marca esta clase como un servicio manejado por Spring (componente de lógica de negocio)
@Service
public class ProductoServiceImpl implements ProductoService{

    // Inyección automática del repositorio de productos
    @Autowired
    private ProductoRepository productoRepository;

    // --- MÉTODOS PRIVADOS DE CONVERSIÓN ---

    // Convierte un objeto Producto (entidad) en un ProductoDTO (para enviar al cliente)
    private ProductoDTO convertToDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getColor(),
                producto.getCategoria(),
                producto.getSku(),
                producto.getActivo(),
                producto.getFechaCreacion(),
                producto.getFechaActualizacion()
        );
    }

    // Convierte un ProductoCreateDTO (datos que vienen al crear un producto)
    // en una entidad Producto lista para guardar en la base de datos
    private Producto convertToEntity(ProductoCreateDTO productoCreateDTO) {
        Producto producto = new Producto(); // Crea una nueva instancia vacía
        producto.setNombre(productoCreateDTO.getNombre());
        producto.setDescripcion(productoCreateDTO.getDescripcion());
        producto.setPrecio(productoCreateDTO.getPrecio());
        // Si el stock viene nulo, lo inicializa a 0
        producto.setStock(productoCreateDTO.getStock() != null ? productoCreateDTO.getStock() : 0);
        producto.setColor(productoCreateDTO.getColor());
        producto.setCategoria(productoCreateDTO.getCategoria());
        producto.setSku(productoCreateDTO.getSku());
        producto.setActivo(true); // Los productos nuevos se marcan como activos
        producto.setFechaCreacion(LocalDateTime.now()); // Fecha actual del sistema
        producto.setFechaActualizacion(LocalDateTime.now()); // Igual al crear
        return producto;
    }

    // --- MÉTODOS PRINCIPALES DEL SERVICIO ---

    // Devuelve todos los productos en formato DTO (sin paginación)
    @Override
    public List<ProductoDTO> findAll() {
        return productoRepository.findAll()
                .stream() // Convierte la lista a stream
                .map(this::convertToDTO) // Transforma cada entidad en DTO
                .collect(Collectors.toList()); // Devuelve una lista
    }

    // Devuelve productos con paginación (para mostrar por páginas)
    @Override
    public Page<ProductoDTO> findAll(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(this::convertToDTO); // Convierte cada resultado en DTO
    }

    // Busca un producto por su ID, lanza error si no existe
    @Override
    public ProductoDTO findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        return convertToDTO(producto);
    }

    // Busca un producto por su SKU, lanza error si no existe
    @Override
    public ProductoDTO findBySku(String sku) {
        Producto producto = productoRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con SKU: " + sku));
        return convertToDTO(producto);
    }

    // Crea un nuevo producto
    @Override
    public ProductoDTO create(ProductoCreateDTO productoCreateDTO) {
        // Primero verifica si ya existe un producto con ese SKU
        if (productoRepository.findBySku(productoCreateDTO.getSku()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con el SKU: " + productoCreateDTO.getSku());
        }

        // Convierte el DTO a entidad y guarda en base de datos
        Producto producto = convertToEntity(productoCreateDTO);
        Producto productoGuardado = productoRepository.save(producto);
        return convertToDTO(productoGuardado);
    }

    // Actualiza los datos de un producto existente
    @Override
    public ProductoDTO update(Long id, ProductoUpdateDTO productoUpdateDTO) {
        // Busca el producto, lanza error si no existe
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        // Solo actualiza los campos que vienen con datos no nulos
        if (productoUpdateDTO.getNombre() != null) productoExistente.setNombre(productoUpdateDTO.getNombre());
        if (productoUpdateDTO.getDescripcion() != null) productoExistente.setDescripcion(productoUpdateDTO.getDescripcion());
        if (productoUpdateDTO.getPrecio() != null) productoExistente.setPrecio(productoUpdateDTO.getPrecio());
        if (productoUpdateDTO.getStock() != null) productoExistente.setStock(productoUpdateDTO.getStock());
        if (productoUpdateDTO.getColor() != null) productoExistente.setColor(productoUpdateDTO.getColor());
        if (productoUpdateDTO.getCategoria() != null) productoExistente.setCategoria(productoUpdateDTO.getCategoria());
        if (productoUpdateDTO.getActivo() != null) productoExistente.setActivo(productoUpdateDTO.getActivo());

        // Actualiza la fecha de modificación
        productoExistente.setFechaActualizacion(LocalDateTime.now());

        // Guarda los cambios en base de datos y devuelve el DTO actualizado
        Producto productoActualizado = productoRepository.save(productoExistente);
        return convertToDTO(productoActualizado);
    }

    // Elimina un producto (soft delete = desactivar sin borrar)
    @Override
    public void delete(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        // Marca el producto como inactivo y actualiza la fecha
        producto.setActivo(false);
        producto.setFechaActualizacion(LocalDateTime.now());
        productoRepository.save(producto);
    }

    // Devuelve todos los productos activos (no eliminados)
    @Override
    public List<ProductoDTO> findByActivoTrue() {
        return productoRepository.findByActivoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Devuelve productos filtrados por categoría
    @Override
    public List<ProductoDTO> findByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Realiza una búsqueda avanzada (ej. por nombre o descripción)
    @Override
    public List<ProductoDTO> busquedaAvanzada(String query) {
        return productoRepository.busquedaAvanzada(query)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Filtra productos dentro de un rango de precios
    @Override
    public List<ProductoDTO> findByPrecioBetween(Double min, Double max) {
        return productoRepository.findByPrecioBetween(min, max)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Filtra productos por categoría y rango de precios
    @Override
    public List<ProductoDTO> findByCategoriaAndPrecioBetween(String categoria, Double min, Double max) {
        return productoRepository.findByCategoriaAndPrecioBetween(categoria, min, max)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Devuelve productos cuyo stock es menor que un valor mínimo
    @Override
    public List<ProductoDTO> findProductosConStockBajo(Integer stockMinimo) {
        return productoRepository.findProductosConStockBajo(stockMinimo)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Actualiza únicamente el stock de un producto
    @Override
    public ProductoDTO actualizarStock(Long id, Integer nuevoStock) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        producto.setStock(nuevoStock);
        producto.setFechaActualizacion(LocalDateTime.now());

        Producto productoActualizado = productoRepository.save(producto);
        return convertToDTO(productoActualizado);
    }

    // Devuelve los productos activos ordenados por precio ascendente
    @Override
    public List<ProductoDTO> findByActivoTrueOrderByPrecioAsc() {
        return productoRepository.findByActivoTrueOrderByPrecioAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Devuelve los productos activos ordenados por precio descendente
    @Override
    public List<ProductoDTO> findByActivoTrueOrderByPrecioDesc() {
        return productoRepository.findByActivoTrueOrderByPrecioDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Devuelve los productos activos ordenados por fecha de creación más reciente
    @Override
    public List<ProductoDTO> findByActivoTrueOrderByFechaCreacionDesc() {
        return productoRepository.findByActivoTrueOrderByFechaCreacionDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
