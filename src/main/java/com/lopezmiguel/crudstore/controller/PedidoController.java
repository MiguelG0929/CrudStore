package com.lopezmiguel.crudstore.controller;

import com.lopezmiguel.crudstore.dto.PedidoDTO;
import com.lopezmiguel.crudstore.dto.PedidoCreateDTO;
import com.lopezmiguel.crudstore.dto.PedidoUpdateDTO;
import com.lopezmiguel.crudstore.model.EstadoPedido;

import com.lopezmiguel.crudstore.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // GET - Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> getAllPedidos() {
        List<PedidoDTO> pedidos = pedidoService.findAll();
        return ResponseEntity.ok(pedidos);
    }

    // GET - Obtener pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> getPedidoById(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.findById(id);
        return ResponseEntity.ok(pedido);
    }

    // GET - Obtener pedido por número
    @GetMapping("/numero/{numPedido}")
    public ResponseEntity<PedidoDTO> getPedidoByNumero(@PathVariable String numPedido) {
        PedidoDTO pedido = pedidoService.findByNumPedido(numPedido);
        return ResponseEntity.ok(pedido);
    }

    // POST - Crear nuevo pedido
    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<PedidoDTO> crearPedido(
            @PathVariable Long clienteId,
            @RequestBody PedidoCreateDTO pedidoCreateDTO) {
        PedidoDTO nuevoPedido = pedidoService.create(pedidoCreateDTO, clienteId);
        return ResponseEntity.ok(nuevoPedido);
    }

    // PUT - Actualizar pedido
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> updatePedido(
            @PathVariable Long id,
            @RequestBody PedidoUpdateDTO pedidoUpdateDTO) {
        PedidoDTO pedidoActualizado = pedidoService.update(id, pedidoUpdateDTO);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // DELETE - Cancelar pedido
    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }

    // GET - Pedidos por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoDTO>> getPedidosByCliente(@PathVariable Long clienteId) {
        List<PedidoDTO> pedidos = pedidoService.findByClienteId(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    // GET - Pedidos por cliente y estado
    @GetMapping("/cliente/{clienteId}/estado/{estado}")
    public ResponseEntity<List<PedidoDTO>> getPedidosByClienteAndEstado(
            @PathVariable Long clienteId,
            @PathVariable EstadoPedido estado) {
        List<PedidoDTO> pedidos = pedidoService.findByClienteIdAndEstado(clienteId, estado);
        return ResponseEntity.ok(pedidos);
    }

    // GET - Pedidos por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PedidoDTO>> getPedidosByEstado(@PathVariable EstadoPedido estado) {
        List<PedidoDTO> pedidos = pedidoService.findByEstado(estado);
        return ResponseEntity.ok(pedidos);
    }

    // GET - Pedidos pendientes de envío
    @GetMapping("/pendientes-envio")
    public ResponseEntity<List<PedidoDTO>> getPedidosPendientesEnvio() {
        List<PedidoDTO> pedidos = pedidoService.findPedidosPendientesEnvio();
        return ResponseEntity.ok(pedidos);
    }

    // GET - Seguimiento de pedido
    @GetMapping("/seguimiento")
    public ResponseEntity<PedidoDTO> seguimientoPedido(
            @RequestParam String email,
            @RequestParam String numPedido) {
        PedidoDTO pedido = pedidoService.findByEmailAndNumPedido(email, numPedido);
        return ResponseEntity.ok(pedido);
    }

    // PUT - Marcar como pagado
    @PutMapping("/{id}/pagar")
    public ResponseEntity<PedidoDTO> marcarComoPagado(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.marcarComoPagado(id);
        return ResponseEntity.ok(pedido);
    }

    // PUT - Marcar como enviado
    @PutMapping("/{id}/enviar")
    public ResponseEntity<PedidoDTO> marcarComoEnviado(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.marcarComoEnviado(id);
        return ResponseEntity.ok(pedido);
    }

    // PUT - Marcar como entregado
    @PutMapping("/{id}/entregar")
    public ResponseEntity<PedidoDTO> marcarComoEntregado(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.marcarComoEntregado(id);
        return ResponseEntity.ok(pedido);
    }

    // GET - Estadísticas: contar pedidos en periodo
    @GetMapping("/estadisticas/contar")
    public ResponseEntity<Long> contarPedidosEnPeriodo(
            @RequestParam String start,
            @RequestParam String end) {
        Long cantidad = pedidoService.countPedidosEnPeriodo(start, end);
        return ResponseEntity.ok(cantidad);
    }

    // GET - Estadísticas: ventas totales en periodo
    @GetMapping("/estadisticas/ventas")
    public ResponseEntity<Double> calcularVentasEnPeriodo(
            @RequestParam String start,
            @RequestParam String end) {
        Double ventas = pedidoService.calcularVentasTotalesEnPeriodo(start, end);
        return ResponseEntity.ok(ventas);
    }
}