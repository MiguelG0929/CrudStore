package com.lopezmiguel.crudstore.services.impl;

// Importaciones necesarias para DTOs, modelos, repositorios y utilidades
import com.lopezmiguel.crudstore.dto.CarritoDTO;
import com.lopezmiguel.crudstore.dto.CarritoResponseDTO;
import com.lopezmiguel.crudstore.dto.ItemCarritoRequestDTO;
import com.lopezmiguel.crudstore.dto.ItemCarritoDTO;
import com.lopezmiguel.crudstore.dto.ProductoDTO;
import com.lopezmiguel.crudstore.model.Carrito;
import com.lopezmiguel.crudstore.model.Cliente;
import com.lopezmiguel.crudstore.model.ItemCarrito;
import com.lopezmiguel.crudstore.model.Producto;
import com.lopezmiguel.crudstore.repository.CarritoRepository;
import com.lopezmiguel.crudstore.repository.ClienteRepository;
import com.lopezmiguel.crudstore.repository.ItemCarritoRepository;
import com.lopezmiguel.crudstore.repository.ProductoRepository;

import com.lopezmiguel.crudstore.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service // Indica que es un servicio gestionado por Spring
@Transactional // Garantiza que las operaciones se ejecuten dentro de una transacción
public class CarritoServiceImpl implements CarritoService { // Implementa la interfaz CarritoService

    @Autowired // Inyección automática de dependencias
    private CarritoRepository carritoRepository; // Repositorio de carritos

    @Autowired
    private ClienteRepository clienteRepository; // Repositorio de clientes

    @Autowired
    private ProductoRepository productoRepository; // Repositorio de productos

    @Autowired
    private ItemCarritoRepository itemCarritoRepository; // Repositorio de items del carrito

    // Convierte un objeto Carrito a CarritoDTO
    private CarritoDTO convertToDTO(Carrito carrito) {
        // Convierte los items del carrito a DTOs
        List<ItemCarritoDTO> itemsDTO = carrito.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());

        // Crea el objeto DTO con la información del carrito
        return new CarritoDTO(
                carrito.getId(),
                carrito.getFechaCreacion(),
                carrito.getTotal(),
                itemsDTO
        );
    }

    // Convierte un objeto ItemCarrito a ItemCarritoDTO
    private ItemCarritoDTO convertItemToDTO(ItemCarrito item) {
        // Crea un DTO del producto asociado al item
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

        // Devuelve el DTO del item con su producto
        return new ItemCarritoDTO(
                item.getId(),
                item.getCantidad(),
                item.getPrecioEnMomento(),
                item.getSubtotal(),
                productoDTO
        );
    }

    @Override
    public CarritoDTO findCarritoByClienteId(Long clienteId) {
        // Busca el carrito asociado a un cliente
        Carrito carrito = carritoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado para el cliente id: " + clienteId));
        return convertToDTO(carrito); // Convierte y devuelve el carrito como DTO
    }

    @Override
    public CarritoDTO createCarritoForCliente(Long clienteId) {
        // Busca el cliente por ID
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        // Si ya tiene carrito, lanza error
        if (carritoRepository.findByClienteId(clienteId).isPresent()) {
            throw new RuntimeException("El cliente ya tiene un carrito activo");
        }

        // Crea un nuevo carrito vacío
        Carrito carrito = new Carrito();
        carrito.setCliente(cliente);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setTotal(0.0);

        // Guarda el carrito en la base de datos
        Carrito carritoGuardado = carritoRepository.save(carrito);
        return convertToDTO(carritoGuardado); // Devuelve el carrito creado como DTO
    }

    @Override
    public void deleteCarrito(Long carritoId) {
        // Busca el carrito por ID
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con id: " + carritoId));
        carritoRepository.delete(carrito); // Elimina el carrito
    }

    @Override
    public void vaciarCarrito(Long carritoId) {
        // Busca el carrito
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con id: " + carritoId));

        // Elimina todos los items asociados al carrito
        itemCarritoRepository.deleteByCarritoId(carritoId);

        // Reinicia el total a 0
        carrito.setTotal(0.0);
        carritoRepository.save(carrito); // Guarda los cambios
    }

    @Override
    public CarritoDTO agregarItem(Long carritoId, ItemCarritoRequestDTO itemRequest) {
        // Busca el carrito por ID
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con id: " + carritoId));

        // Busca el producto por ID
        Producto producto = productoRepository.findById(itemRequest.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + itemRequest.getProductoId()));

        // Verifica si hay stock suficiente
        if (producto.getStock() < itemRequest.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        // Busca si el producto ya está en el carrito
        ItemCarrito itemExistente = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, itemRequest.getProductoId())
                .orElse(null);

        if (itemExistente != null) {
            // Si ya existe, actualiza la cantidad y subtotal
            itemExistente.setCantidad(itemExistente.getCantidad() + itemRequest.getCantidad());
            itemExistente.setSubtotal(itemExistente.getPrecioEnMomento() * itemExistente.getCantidad());
            itemCarritoRepository.save(itemExistente);
        } else {
            // Si no existe, crea un nuevo item
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(itemRequest.getCantidad());
            nuevoItem.setPrecioEnMomento(producto.getPrecio());
            nuevoItem.setSubtotal(producto.getPrecio() * itemRequest.getCantidad());

            itemCarritoRepository.save(nuevoItem);
        }

        // Actualiza el total del carrito
        actualizarTotalCarrito(carritoId);

        // Devuelve el carrito actualizado
        return findById(carritoId);
    }


    @Override
    public CarritoDTO actualizarCantidadItem(Long carritoId, Long itemId, Integer cantidad) {
        // Busca el item por ID
        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con id: " + itemId));

        // Verifica stock disponible
        if (item.getProducto().getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + item.getProducto().getStock());
        }

        // Actualiza la cantidad y subtotal
        item.setCantidad(cantidad);
        item.setSubtotal(item.getPrecioEnMomento() * cantidad);
        itemCarritoRepository.save(item);

        // Recalcula el total del carrito
        actualizarTotalCarrito(carritoId);

        return findById(carritoId); // Devuelve el carrito actualizado
    }

    @Override
    public CarritoDTO eliminarItem(Long carritoId, Long itemId) {
        // Busca el item por ID
        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con id: " + itemId));

        // Elimina el item del carrito
        itemCarritoRepository.delete(item);

        // Recalcula el total del carrito
        actualizarTotalCarrito(carritoId);

        return findById(carritoId); // Devuelve el carrito actualizado
    }


    @Override
    public Double calcularTotalCarrito(Long carritoId) {
        // Calcula el total sumando todos los subtotales
        Double total = itemCarritoRepository.calcularTotalCarrito(carritoId);
        return total != null ? total : 0.0; // Devuelve 0 si es nulo
    }

    @Override
    public Integer contarItemsEnCarrito(Long carritoId) {
        // Obtiene todos los items del carrito
        List<ItemCarrito> items = itemCarritoRepository.findByCarritoId(carritoId);
        // Suma la cantidad total de unidades
        return items.stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }

    @Override
    public boolean existeItemEnCarrito(Long carritoId, Long productoId) {
        // Verifica si un producto ya está en el carrito
        return itemCarritoRepository.existsByCarritoIdAndProductoId(carritoId, productoId);
    }

    @Override
    public List<CarritoDTO> findAll() {
        // Devuelve todos los carritos como lista de DTOs
        return carritoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CarritoDTO findById(Long id) {
        // Busca un carrito por su ID
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con id: " + id));
        return convertToDTO(carrito); // Devuelve el carrito como DTO
    }

    //Metodo auxiliar que actualiza el total de un carrito
    private void actualizarTotalCarrito(Long carritoId) {
        // Calcula el nuevo total
        Double total = calcularTotalCarrito(carritoId);
        // Busca el carrito
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con id: " + carritoId));
        // Actualiza el total
        carrito.setTotal(total);
        carritoRepository.save(carrito); // Guarda los cambios
    }
}



