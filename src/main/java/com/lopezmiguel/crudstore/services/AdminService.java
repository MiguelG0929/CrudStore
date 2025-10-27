package com.lopezmiguel.crudstore.services;

import com.lopezmiguel.crudstore.dto.AdminDTO;
import com.lopezmiguel.crudstore.dto.AdminLoginDTO;
import com.lopezmiguel.crudstore.dto.AdminRegistroDTO;

import java.util.List;

public interface AdminService {
    // CRUD básico
    List<AdminDTO> findAll(); //devuelve todos los administradores registrados
    AdminDTO findById(Long id); //busca un admin por su ID
    AdminDTO findByUsername(String username); //busca por nombre de usuario
    AdminDTO findByEmail(String email); //busca por correo electrónico
    AdminDTO create(AdminRegistroDTO adminRegistroDTO); //crea un nuevo administrador usando los datos del DTO de registro
    AdminDTO update(Long id, AdminDTO adminDTO); //actualiza la información de un administrador existente
    void delete(Long id); //elimina un administrador por su ID.

    // Autenticación
    AdminDTO login(AdminLoginDTO adminLoginDTO); //valida usuario y contraseña. Devuelve un AdminDTO si el login es correcto
    boolean existsByUsername(String username); //comprueba si ya existe un admin con ese nombre de usuario
    boolean existsByEmail(String email); //igual, pero con el correo electrónico

    // Gestión de estado
    //Permiten activar o desactivar cuentas de administrador
    AdminDTO activarAdmin(Long id);
    AdminDTO desactivarAdmin(Long id);


    // Verificaciones de seguridad
    //Comprueba si un administrador está activo antes de permitirle realizar acciones
    boolean isAdminActivo(String username);
}