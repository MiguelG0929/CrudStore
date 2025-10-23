package com.lopezmiguel.crudstore.repository;

import com.lopezmiguel.crudstore.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email); //treame el cliente cuyo email sea el que le paso
    Boolean existsByEmail(String email);         //comprueba si existe un cliente con ese correo electronico

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))") //Busca clientes cuyo nombre contenga cierta palabra o parte del texto, sin importar mayúsculas o minúsculas.
    List<Cliente> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))") //lo mismo que el anterior pero buscando por apellido
    List<Cliente> findByApellidoContainingIgnoreCase(@Param("apellido") String apellido);

    List<Cliente> findByFechaRegistroBetween(java.time.LocalDateTime start, java.time.LocalDateTime end); //busca todos los clientes que se registraron entre dos fechas

}
