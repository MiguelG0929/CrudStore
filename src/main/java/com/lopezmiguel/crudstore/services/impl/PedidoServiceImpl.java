package com.lopezmiguel.crudstore.services.impl;

// Importaciones necesarias
import com.lopezmiguel.crudstore.dto.*; // Importa los DTOs (para transferir datos)
import com.lopezmiguel.crudstore.model.*; // Importa las clases del modelo (Pedido, Cliente, Producto...)
import com.lopezmiguel.crudstore.repository.*; // Importa los repositorios para acceder a la base de datos
import com.lopezmiguel.crudstore.services.PedidoService; // Importa la interfaz que esta clase implementa
import org.springframework.beans.factory.annotation.Autowired; // Para inyección automática de dependencias
import org.springframework.stereotype.Service; // Marca esta clase como un servicio de Spring
import org.springframework.transaction.annotation.Transactional; // Maneja transacciones de base de datos

import java.time.LocalDateTime; // Para manejar fechas y horas
import java.util.ArrayList; // Lista dinámica
import java.util.List; // Interfaz para listas
import java.util.stream.Collectors; // Para usar stream y mapear listas

@Service // Indica que esta clase es un servicio de negocio
@Transactional // Garantiza que las operaciones de BD sean transaccionales
public class PedidoServiceImpl implements PedidoService { // Implementa la interfaz PedidoService

    @Autowired
    private PedidoRepository pedidoRepository; // Acceso a la tabla de pedidos

    @Autowired
    private ClienteRepository clienteRepository; // Acceso a la tabla de clientes

    @Autowired
    private ProductoRepository productoRepository; // Acceso a la tabla de productos

    @Autowired
    private CarritoRepository carritoRepository; // Acceso a la tabla de carritos

    @Autowired
    private ItemCarritoRepository itemCarritoRepository; // Acceso a los items del carrito

    // Convierte un objeto Pedido a un PedidoDTO
    private PedidoDTO convertToDTO(Pedido pedido) {
        // Convierte los detalles del pedido a DTO
        List<DetallePedidoDTO> detallesDTO = pedido.getDetalles().stream()
                .map(this::convertDetalleToDTO)
                .collect(Collectors.toList());

        // Convierte los datos del cliente a DTO
        ClienteDTO clienteDTO = new ClienteDTO(
                pedido.getCliente().getId(),
                pedido.getCliente().getNombre(),
                pedido.getCliente().getApellido(),
                pedido.getCliente().getEmail(),
                pedido.getCliente().getTelefono(),
                pedido.getCliente().getDireccion(),
                pedido.getCliente().getFechaNacimiento(),
                pedido.getCliente().getFechaRegistro()
        );

        // Crea el PedidoDTO final
        return new PedidoDTO(
                pedido.getId(),
                pedido.getNumPedido(),
                pedido.getFechaPedido(),
                pedido.getFechaEnvio(),
                pedido.getFechaEntrega(),
                pedido.getEstado(),
                pedido.getDireccionEnvio(),
                pedido.getMetodoPago(),
                pedido.getTotal(),
                detallesDTO,
                clienteDTO
        );
    }

    // Convierte un DetallePedido a DetallePedidoDTO
    private DetallePedidoDTO convertDetalleToDTO(DetallePedido detalle) {
        // Convierte el producto del detalle a DTO
        ProductoDTO productoDTO = new ProductoDTO(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getProducto().getDescripcion(),
                detalle.getProducto().getPrecio(),
                detalle.getProducto().getStock(),
                detalle.getProducto().getColor(),
                detalle.getProducto().getCategoria(),
                detalle.getProducto().getSku(),
                detalle.getProducto().getActivo(),
                detalle.getProducto().getFechaCreacion(),
                detalle.getProducto().getFechaActualizacion()
        );

        // Devuelve el detalle convertido
        return new DetallePedidoDTO(
                detalle.getId(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal(),
                productoDTO
        );
    }

    // Genera un número de pedido único usando fecha y hora
    private String generarNumeroPedido() {
        LocalDateTime now = LocalDateTime.now(); // Obtiene la hora actual
        String timestamp = String.valueOf(now.getYear()) +
                String.format("%02d", now.getMonthValue()) +
                String.format("%02d", now.getDayOfMonth()) +
                String.format("%02d", now.getHour()) +
                String.format("%02d", now.getMinute()) +
                String.format("%02d", now.getSecond());
        return "PED-" + timestamp; // Retorna un código único
    }

    @Override
    public List<PedidoDTO> findAll() {
        // Devuelve todos los pedidos convertidos a DTO
        return pedidoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PedidoDTO findById(Long id) {
        // Busca un pedido por ID o lanza error si no existe
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        return convertToDTO(pedido); // Convierte y devuelve
    }

    @Override
    public PedidoDTO findByNumPedido(String numPedido) {
        // Busca un pedido por su número
        Pedido pedido = pedidoRepository.findByNumPedido(numPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con número: " + numPedido));
        return convertToDTO(pedido);
    }

    @Override
    public PedidoDTO create(PedidoCreateDTO pedidoCreateDTO, Long clienteId) {
        // Busca al cliente por su ID
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        // Crea el pedido base
        Pedido pedido = new Pedido();
        pedido.setNumPedido(generarNumeroPedido()); // Asigna número único
        pedido.setCliente(cliente); // Asocia cliente
        pedido.setFechaPedido(LocalDateTime.now()); // Fecha actual
        pedido.setEstado(EstadoPedido.PENDIENTE); // Estado inicial
        pedido.setDireccionEnvio(pedidoCreateDTO.getDireccionEnvio());
        pedido.setMetodoPago(pedidoCreateDTO.getMetodoPago());
        pedido.setTotal(0.0); // Total inicial

        // Lista para los detalles del pedido
        List<DetallePedido> detalles = new ArrayList<>();
        double totalPedido = 0.0;

        // Recorre los detalles recibidos
        for (DetallePedidoCreateDTO detalleCreateDTO : pedidoCreateDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleCreateDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + detalleCreateDTO.getProductoId()));

            // Verifica si hay suficiente stock
            if (producto.getStock() < detalleCreateDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() +
                        ". Disponible: " + producto.getStock());
            }

            // Resta el stock vendido
            producto.setStock(producto.getStock() - detalleCreateDTO.getCantidad());
            productoRepository.save(producto); // Guarda el nuevo stock

            // Crea un detalle del pedido
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleCreateDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * detalleCreateDTO.getCantidad());

            detalles.add(detalle); // Añade el detalle a la lista
            totalPedido += detalle.getSubtotal(); //Suma el total
        }

        // Asigna los detalles y el total final
        pedido.setDetalles(detalles);
        pedido.setTotal(totalPedido);

        // Vacía el carrito del cliente
        vaciarCarritoCliente(clienteId);

        // Guarda el pedido en BD y devuelve su DTO
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return convertToDTO(pedidoGuardado);
    }

    @Override
    public PedidoDTO update(Long id, PedidoUpdateDTO pedidoUpdateDTO) {
        // Busca el pedido
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        // Actualiza solo los campos permitidos
        if (pedidoUpdateDTO.getEstado() != null) pedido.setEstado(pedidoUpdateDTO.getEstado());
        if (pedidoUpdateDTO.getFechaEnvio() != null) pedido.setFechaEnvio(pedidoUpdateDTO.getFechaEnvio());
        if (pedidoUpdateDTO.getFechaEntrega() != null) pedido.setFechaEntrega(pedidoUpdateDTO.getFechaEntrega());

        // Guarda cambios y devuelve DTO
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        return convertToDTO(pedidoActualizado);
    }

    @Override
    public void cancelarPedido(Long id) {
        // Busca el pedido
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        // No permite cancelar si ya fue enviado o entregado
        if (pedido.getEstado() == EstadoPedido.ENVIADO || pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new RuntimeException("No se puede cancelar un pedido que ya ha sido enviado o entregado");
        }

        // Devuelve el stock de los productos
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        // Marca el pedido como cancelado
        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    @Override
    public List<PedidoDTO> findByClienteId(Long clienteId) {
        // Devuelve pedidos del cliente
        return pedidoRepository.findByClienteId(clienteId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PedidoDTO> findByClienteIdAndEstado(Long clienteId, EstadoPedido estado) {
        // Devuelve pedidos del cliente por estado
        return pedidoRepository.findByClienteIdAndEstado(clienteId, estado)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PedidoDTO> findByEstado(EstadoPedido estado) {
        // Devuelve pedidos según estado
        return pedidoRepository.findByEstado(estado)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PedidoDTO> findPedidosPendientesEnvio() {
        // Crea lista de estados pendientes
        List<EstadoPedido> estadosPendientes = List.of(EstadoPedido.PAGADO, EstadoPedido.PENDIENTE);
        // Devuelve pedidos que esperan envío
        return pedidoRepository.findPedidosPendientesEnvio(estadosPendientes)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PedidoDTO> findByFechaPedidoBetween(String start, String end) {
        // Convierte las fechas a LocalDateTime
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        // Busca pedidos en ese rango
        return pedidoRepository.findByFechaPedidoBetween(startDate, endDate)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PedidoDTO> findByMetodoPago(String metodoPago) {
        // Filtra pedidos por método de pago
        return pedidoRepository.findByMetodoPago(metodoPago)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public PedidoDTO findByEmailAndNumPedido(String email, String numPedido) {
        // Busca pedido por email y número
        Pedido pedido = pedidoRepository.findByEmailAndNumPedido(email, numPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return convertToDTO(pedido);
    }

    @Override
    public PedidoDTO marcarComoPagado(Long id) {
        // Marca pedido como pagado
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        pedido.setEstado(EstadoPedido.PAGADO);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        return convertToDTO(pedidoActualizado);
    }

    @Override
    public PedidoDTO marcarComoEnviado(Long id) {
        // Marca pedido como enviado
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        pedido.setEstado(EstadoPedido.ENVIADO);
        pedido.setFechaEnvio(LocalDateTime.now());
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        return convertToDTO(pedidoActualizado);
    }

    @Override
    public PedidoDTO marcarComoEntregado(Long id) {
        // Marca pedido como entregado
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        pedido.setEstado(EstadoPedido.ENTREGADO);
        pedido.setFechaEntrega(LocalDateTime.now());
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        return convertToDTO(pedidoActualizado);
    }

    @Override
    public Long countPedidosEnPeriodo(String start, String end) {
        // Cuenta pedidos entre dos fechas
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return pedidoRepository.countPedidosEnPeriodo(startDate, endDate);
    }

    @Override
    public Double calcularVentasTotalesEnPeriodo(String start, String end) {
        // Calcula ventas totales en un periodo
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        Double total = pedidoRepository.calcularVentasTotalesEnPeriodo(startDate, endDate);
        return total != null ? total : 0.0; // Si no hay ventas, devuelve 0
    }

    // Vacía el carrito de un cliente tras generar pedido
    private void vaciarCarritoCliente(Long clienteId) {
        Carrito carrito = carritoRepository.findByClienteId(clienteId).orElse(null);
        if (carrito != null) {
            itemCarritoRepository.deleteByCarritoId(carrito.getId()); // Elimina los ítems
            carrito.setTotal(0.0); // Reinicia total
            carritoRepository.save(carrito); // Guarda cambios
        }
    }
}