package com.lopezmiguel.crudstore.services;

import com.lopezmiguel.crudstore.dto.ClienteDTO;
import com.lopezmiguel.crudstore.dto.ClienteLoginDTO;
import com.lopezmiguel.crudstore.dto.ClienteRegistroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClienteService {
    // CRUD básico
    List<ClienteDTO> findAll(); //devuelve todos los clientes como lista
    Page<ClienteDTO> findAll(Pageable pageable); //devuelve clientes con paginación
    ClienteDTO findById(Long id); //busca un cliente por su ID.
    ClienteDTO findByEmail(String email); //busca un cliente por su correo electrónico.
    ClienteDTO create(ClienteRegistroDTO clienteRegistroDTO); //registra un nuevo cliente
    ClienteDTO update(Long id, ClienteDTO clienteDTO);//actualiza los datos de un cliente existente.
    void delete(Long id);

    // Autenticación
    ClienteDTO login(ClienteLoginDTO clienteLoginDTO); //valida si el email y la contraseña coinciden con un cliente existente.
    boolean existsByEmail(String email); //comprueba si un correo ya está registrado

    // Búsquedas
    List<ClienteDTO> findByNombreContaining(String nombre); //busca clientes cuyo nombre contiene cierta cadena
    List<ClienteDTO> findByApellidoContaining(String apellido); //similar pero para el apellido
    List<ClienteDTO> findByFechaRegistroBetween(String start, String end); //busca clientes registrados entre dos fechas.

    // Gestión de cuenta
    ClienteDTO activarCuenta(Long id); //cambia un campo (por ejemplo, activo = true).
    ClienteDTO desactivarCuenta(Long id); //lo marca como inactivo (activo = false).


}
