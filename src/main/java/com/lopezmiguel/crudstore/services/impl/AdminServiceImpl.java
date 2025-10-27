package com.lopezmiguel.crudstore.services.impl;

import com.lopezmiguel.crudstore.dto.AdminDTO; // Importa el DTO principal del administrador
import com.lopezmiguel.crudstore.dto.AdminLoginDTO; // Importa el DTO usado para el login
import com.lopezmiguel.crudstore.dto.AdminRegistroDTO; // Importa el DTO usado para el registro
import com.lopezmiguel.crudstore.model.Admin; // Importa la entidad Admin
import com.lopezmiguel.crudstore.repository.AdminRepository; // Importa el repositorio de administradores
import com.lopezmiguel.crudstore.services.AdminService;// Importa la interfaz que esta clase implementa
import org.springframework.beans.factory.annotation.Autowired; // Permite la inyección automática de dependencias
import org.springframework.stereotype.Service; // Marca la clase como un servicio de Spring

import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas
import java.util.List; // Importa la interfaz List
import java.util.stream.Collectors; // Importa Collectors para transformar listas

@Service // Anotación que registra la clase como un servicio de Spring
public class AdminServiceImpl implements AdminService { // Clase que implementa la interfaz AdminService

    @Autowired // Inyección automática del repositorio
    private AdminRepository adminRepository; // Repositorio para interactuar con la base de datos

    // Conversión de entidad Admin a DTO
    private AdminDTO convertToDTO(Admin admin) {
        return new AdminDTO( // Crea un nuevo objeto AdminDTO
                admin.getId(), // Asigna el ID del admin
                admin.getUsername(), // Asigna el nombre de usuario
                admin.getEmail(), // Asigna el correo electrónico
                admin.getRol(), // Asigna el rol del admin
                admin.getActivo(), // Asigna el estado activo/inactivo
                admin.getFechaRegistro() // Asigna la fecha de registro
        );
    }

    // Conversión de DTO de registro a entidad Admin
    private Admin convertToEntity(AdminRegistroDTO adminRegistroDTO) {
        Admin admin = new Admin(); // Crea un nuevo objeto Admin
        admin.setUsername(adminRegistroDTO.getUsername()); // Asigna el nombre de usuario
        // En un caso real, aquí se encriptaría la contraseña
        admin.setEmail(adminRegistroDTO.getEmail()); // Asigna el correo electrónico
        admin.setRol(adminRegistroDTO.getRol() != null ? adminRegistroDTO.getRol() : "ADMIN"); // Asigna el rol o ADMIN por defecto
        admin.setActivo(true); // Marca el admin como activo al crearse
        admin.setFechaRegistro(LocalDateTime.now()); // Asigna la fecha actual como fecha de registro
        return admin; // Devuelve la entidad completa
    }

    @Override
    public List<AdminDTO> findAll() { // Devuelve una lista de todos los administradores
        return adminRepository.findAll() // Busca todos los registros en la BD
                .stream() // Convierte la lista en un flujo
                .map(this::convertToDTO) // Convierte cada Admin en un AdminDTO
                .collect(Collectors.toList()); // Devuelve la lista de DTOs
    }

    @Override
    public AdminDTO findById(Long id) { // Busca un admin por su ID
        Admin admin = adminRepository.findById(id) // Busca el admin en la BD
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + id)); // Lanza error si no existe
        return convertToDTO(admin); // Devuelve el admin convertido a DTO
    }

    @Override
    public AdminDTO findByUsername(String username) { // Busca un admin por su nombre de usuario
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con username: " + username));
        return convertToDTO(admin); // Devuelve el DTO correspondiente
    }

    @Override
    public AdminDTO findByEmail(String email) { // Busca un admin por correo electrónico
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con email: " + email));
        return convertToDTO(admin);
    }

    @Override
    public AdminDTO create(AdminRegistroDTO adminRegistroDTO) { // Crea un nuevo administrador
        // Verifica que no exista un admin con el mismo username
        if (adminRepository.existsByUsername(adminRegistroDTO.getUsername())) {
            throw new RuntimeException("Ya existe un administrador con el username: " + adminRegistroDTO.getUsername());
        }

        // Verifica que no exista un admin con el mismo email
        if (adminRepository.existsByEmail(adminRegistroDTO.getEmail())) {
            throw new RuntimeException("Ya existe un administrador con el email: " + adminRegistroDTO.getEmail());
        }

        Admin admin = convertToEntity(adminRegistroDTO); // Convierte el DTO en entidad
        Admin adminGuardado = adminRepository.save(admin); // Guarda el admin en la BD
        return convertToDTO(adminGuardado); // Devuelve el DTO del admin creado
    }

    @Override
    public AdminDTO update(Long id, AdminDTO adminDTO) { // Actualiza un admin existente
        Admin adminExistente = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + id));

        // Si el email ha cambiado, se verifica que no esté en uso
        if (adminDTO.getEmail() != null && !adminDTO.getEmail().equals(adminExistente.getEmail())) {
            if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
                throw new RuntimeException("Ya existe un administrador con el email: " + adminDTO.getEmail());
            }
            adminExistente.setEmail(adminDTO.getEmail()); // Actualiza el email
        }

        if (adminDTO.getRol() != null) { // Actualiza el rol si se proporciona
            adminExistente.setRol(adminDTO.getRol());
        }
        if (adminDTO.getActivo() != null) { // Actualiza el estado activo/inactivo
            adminExistente.setActivo(adminDTO.getActivo());
        }

        Admin adminActualizado = adminRepository.save(adminExistente); // Guarda los cambios
        return convertToDTO(adminActualizado); // Devuelve el DTO actualizado
    }

    @Override
    public void delete(Long id) { // Elimina un administrador por su ID
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + id));
        adminRepository.delete(admin); // Elimina el registro
    }

    @Override
    public AdminDTO login(AdminLoginDTO adminLoginDTO) { // Valida el login de un administrador
        Admin admin = adminRepository.findByUsername(adminLoginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // Verifica si el admin está activo
        if (!admin.getActivo()) {
            throw new RuntimeException("La cuenta de administrador está desactivada");
        }

        // En un caso real, aquí se comprobaría la contraseña encriptada
        // if (!passwordEncoder.matches(adminLoginDTO.getPassword(), admin.getPassword())) {
        //     throw new RuntimeException("Credenciales inválidas");
        // }

        return convertToDTO(admin); // Devuelve el DTO del admin autenticado
    }

    @Override
    public boolean existsByUsername(String username) { // Comprueba si existe un admin por username
        return adminRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) { // Comprueba si existe un admin por email
        return adminRepository.existsByEmail(email);
    }

    @Override
    public AdminDTO activarAdmin(Long id) { // Activa una cuenta de admin
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + id));
        admin.setActivo(true); // Marca como activo
        Admin adminActualizado = adminRepository.save(admin); // Guarda los cambios
        return convertToDTO(adminActualizado); // Devuelve el DTO actualizado
    }

    @Override
    public AdminDTO desactivarAdmin(Long id) { // Desactiva una cuenta de admin
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + id));
        admin.setActivo(false); // Marca como inactivo
        Admin adminActualizado = adminRepository.save(admin); // Guarda los cambios
        return convertToDTO(adminActualizado); // Devuelve el DTO actualizado
    }

    @Override
    public boolean isAdminActivo(String username) { // Comprueba si un admin está activo
        return adminRepository.findByUsernameAndActivoTrue(username).isPresent(); // Devuelve true si está activo
    }
}
