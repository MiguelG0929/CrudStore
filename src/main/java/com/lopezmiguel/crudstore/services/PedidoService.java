package com.lopezmiguel.crudstore.services;

import com.lopezmiguel.crudstore.dto.PedidoCreateDTO;
import com.lopezmiguel.crudstore.dto.PedidoDTO;
import com.lopezmiguel.crudstore.dto.PedidoUpdateDTO;
import com.lopezmiguel.crudstore.model.EstadoPedido;

import java.util.List;

public interface PedidoService {

    // CRUD básico
    List<PedidoDTO> findAll(); //Devuelve todos los pedidos
    PedidoDTO findById(Long id); //Busca un pedido por su ID
    PedidoDTO findByNumPedido(String numPedido); //Busca un pedido por su número único
    PedidoDTO create(PedidoCreateDTO pedidoCreateDTO, Long clienteId); //Crea un nuevo pedido para un cliente
    PedidoDTO update(Long id, PedidoUpdateDTO pedidoUpdateDTO); //Actualiza un pedido existente
    void cancelarPedido(Long id); //Cancela un pedido por ID

    // Búsquedas por cliente
    List<PedidoDTO> findByClienteId(Long clienteId); //Obtiene todos los pedidos de un cliente
    List<PedidoDTO> findByClienteIdAndEstado(Long clienteId, EstadoPedido estado); //Filtra los pedidos del cliente por estado

    // Búsquedas para administración
    List<PedidoDTO> findByEstado(EstadoPedido estado); //Obtiene todos los pedidos en un estado concreto
    List<PedidoDTO> findPedidosPendientesEnvio(); //Devuelve los pedidos que aún no se han enviado
    List<PedidoDTO> findByFechaPedidoBetween(String start, String end); //Devuelve pedidos dentro de un rango de fechas
    List<PedidoDTO> findByMetodoPago(String metodoPago); //Busca pedidos según el metodo de pago

    // Seguimiento
    PedidoDTO findByEmailAndNumPedido(String email, String numPedido); //Permite a un cliente consultar su pedido usando email y número de pedido

    // Gestión de estados
    PedidoDTO marcarComoPagado(Long id); //Cambia el estado a "PAGADO"
    PedidoDTO marcarComoEnviado(Long id);//Cambia el estado a "ENVIADO"
    PedidoDTO marcarComoEntregado(Long id);//Cambia el estado a "ENTREGADO"

    // Reportes y estadísticas
    Long countPedidosEnPeriodo(String start, String end); //Cuenta cuántos pedidos se realizaron en un periodo
    Double calcularVentasTotalesEnPeriodo(String start, String end); //Calcula el total de ventas (dinero generado) en un periodo

}
