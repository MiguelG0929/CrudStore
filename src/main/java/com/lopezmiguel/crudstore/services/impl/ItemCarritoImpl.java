package com.lopezmiguel.crudstore.services.impl;
// Importaciones necesarias
import com.lopezmiguel.crudstore.dto.ItemCarritoDTO; // DTO para transferir datos del ítem del carrito
import com.lopezmiguel.crudstore.dto.ActualizarItemCarritoDTO; // DTO para actualizar cantidad del ítem
import com.lopezmiguel.crudstore.dto.ProductoDTO; // DTO del producto asociado al ítem
import com.lopezmiguel.crudstore.model.ItemCarrito; // Entidad del ítem del carrito
import com.lopezmiguel.crudstore.repository.ItemCarritoRepository; // Repositorio para acceso a la base de datos
import com.lopezmiguel.crudstore.services.ItemCarritoService;// Interfaz del servicio que se implementa
import org.springframework.beans.factory.annotation.Autowired; // Permite la inyección de dependencias
import org.springframework.stereotype.Service; // Indica que esta clase es un servicio de Spring
import java.util.List; // Importa la interfaz List
import java.util.stream.Collectors; // Permite usar Streams para transformar listas


@Service // Marca la clase como un servicio gestionado por Spring
public class ItemCarritoImpl implements ItemCarritoService {

    @Autowired // Inyección automática del repositorio
    private ItemCarritoRepository itemCarritoRepository; // Permite acceder a la base de datos de ítems del carrito

    // Metodo auxiliar para convertir una entidad ItemCarrito a su DTO
    private ItemCarritoDTO convertToDTO(ItemCarrito item) {

        // Crea un objeto ProductoDTO a partir del producto contenido en el ítem
        ProductoDTO productoDTO = new ProductoDTO(
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getProducto().getDescripcion(),
                item.getProducto().getPrecio(),
                item.getProducto().getStock(),
                item.getProducto().getColor(),
                item.getProducto().getCategoria(),
                item.getProducto().getSku(),
                item.getProducto().getActivo(),
                item.getProducto().getFechaCreacion(),
                item.getProducto().getFechaActualizacion()
        );

        // Devuelve un nuevo ItemCarritoDTO con los datos del ítem y su producto
        return new ItemCarritoDTO(
                item.getId(),
                item.getCantidad(),
                item.getPrecioEnMomento(),
                item.getSubtotal(),
                productoDTO
        );
    }

    @Override // Implementación del método definido en la interfaz
    public List<ItemCarritoDTO> findByCarritoId(Long carritoId) {
        // Busca todos los ítems que pertenecen al carrito con el ID dado,
        // los convierte a DTO y los devuelve en una lista
        return itemCarritoRepository.findByCarritoId(carritoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemCarritoDTO findById(Long id) {
        // Busca un ítem por su ID o lanza excepción si no existe
        ItemCarrito item = itemCarritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de carrito no encontrado con id: " + id));
        // Convierte la entidad a DTO y la devuelve
        return convertToDTO(item);
    }

    @Override
    public ItemCarritoDTO findByCarritoIdAndProductoId(Long carritoId, Long productoId) {
        // Busca un ítem por el ID del carrito y el ID del producto
        ItemCarrito item = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));
        // Convierte la entidad encontrada a DTO
        return convertToDTO(item);
    }

    @Override
    public ItemCarritoDTO updateCantidad(Long id, ActualizarItemCarritoDTO actualizarDTO)
    {
        // Busca el ítem a actualizar por su ID
        ItemCarrito item = itemCarritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de carrito no encontrado con id: " + id));

        // Verifica si la cantidad solicitada excede el stock disponible
        if (actualizarDTO.getCantidad() > item.getProducto().getStock()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + item.getProducto().getStock());
        }

        // Verifica que la cantidad sea mayor que cero
        if (actualizarDTO.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        // Actualiza la cantidad del ítem y recalcula su subtotal
        item.setCantidad(actualizarDTO.getCantidad());
        item.setSubtotal(item.getPrecioEnMomento() * actualizarDTO.getCantidad());

        // Guarda el ítem actualizado en la base de datos
        ItemCarrito itemActualizado = itemCarritoRepository.save(item);
        // Devuelve el ítem actualizado como DTO
        return convertToDTO(itemActualizado);
    }

    @Override
    public void delete(Long id) {
        // Busca el ítem a eliminar
        ItemCarrito item = itemCarritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de carrito no encontrado con id: " + id));
        // Elimina el ítem de la base de datos
        itemCarritoRepository.delete(item);
    }

    @Override
    public void deleteByCarritoId(Long carritoId) {
        // Elimina todos los ítems pertenecientes al carrito con el ID dado
        itemCarritoRepository.deleteByCarritoId(carritoId);
    }

    @Override
    public Double calcularSubtotalItem(Long itemId) {
        // Busca el ítem por ID o lanza excepción si no existe
        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item de carrito no encontrado con id: " + itemId));
        // Devuelve el subtotal del ítem
        return item.getSubtotal();
    }

    @Override
    public boolean existsByCarritoIdAndProductoId(Long carritoId, Long productoId) {
        // Verifica si existe un ítem con ese carrito y producto
        return itemCarritoRepository.existsByCarritoIdAndProductoId(carritoId, productoId);
    }

    @Override
    public List<ItemCarritoDTO> findAll() {
        // Devuelve todos los ítems del carrito existentes en la base,
        // convertidos a DTO mediante un Stream
        return itemCarritoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
