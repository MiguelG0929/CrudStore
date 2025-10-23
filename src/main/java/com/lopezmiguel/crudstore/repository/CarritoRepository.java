package com.lopezmiguel.crudstore.repository;

import com.lopezmiguel.crudstore.model.Carrito;
import com.lopezmiguel.crudstore.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long>
{
    //El Optional se usa porque podria no existir un carrito
    Optional<Carrito> findByClienteId(Long clienteId); //busca el carrito de un cliente usando su id
    Optional<Carrito> findByCliente(Cliente cliente); //busca el carrito pasando el objeto Cliente

    @Query("SELECT c FROM Carrito c WHERE c.cliente.id = :clienteId")
    Optional<Carrito> findCarritoConItems(@Param("clienteId") Long clienteId); //consulta personalizada que busca el carrito con sus productos

    void deleteByClienteId(Long clienteId); //Elimina el carrito del cliente indicado
}
