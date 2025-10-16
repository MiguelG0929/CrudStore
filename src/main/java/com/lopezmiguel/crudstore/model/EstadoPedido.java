package com.lopezmiguel.crudstore.model;

public enum EstadoPedido {

    PENDIENTE,      // Pedido creado pero no pagado
    PAGADO,         // Pedido pagado, preparando envío
    ENVIADO,        // Pedido enviado al cliente
    ENTREGADO,      // Pedido entregado satisfactoriamente
    CANCELADO       // Pedido cancelado por cliente/admin

}
