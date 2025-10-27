package com.lopezmiguel.crudstore.services.impl;

// Importaciones necesarias para los DTO, modelo, repositorio y servicio
import com.lopezmiguel.crudstore.dto.ClienteDTO;
import com.lopezmiguel.crudstore.dto.ClienteRegistroDTO;
import com.lopezmiguel.crudstore.dto.ClienteLoginDTO;
import com.lopezmiguel.crudstore.model.Cliente;
import com.lopezmiguel.crudstore.repository.ClienteRepository;


// Importaciones de Spring para la inyección de dependencias y paginación
import com.lopezmiguel.crudstore.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// Importaciones de utilidades de tiempo y colecciones
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Marca esta clase como un servicio gestionado por Spring
@Service
public class ClienteServiceImpl implements ClienteService {

    // Inyección automática del repositorio de clientes
    @Autowired
    private ClienteRepository clienteRepository;

    // Convierte un objeto Cliente (entidad) a un ClienteDTO (para enviar al frontend)
    private ClienteDTO convertToDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getFechaNacimiento(),
                cliente.getFechaRegistro()
        );
    }

    // Convierte un ClienteRegistroDTO (datos del registro) a una entidad Cliente
    private Cliente convertToEntity(ClienteRegistroDTO clienteRegistroDTO) {
        Cliente cliente = new Cliente(); // Crea una nueva instancia del cliente
        cliente.setNombre(clienteRegistroDTO.getNombre()); // Asigna el nombre
        cliente.setApellido(clienteRegistroDTO.getApellido()); // Asigna el apellido
        cliente.setEmail(clienteRegistroDTO.getEmail()); // Asigna el email
        // Aquí normalmente se encriptaría la contraseña antes de guardarla
        cliente.setTelefono(clienteRegistroDTO.getTelefono()); // Asigna el teléfono
        cliente.setDireccion(clienteRegistroDTO.getDireccion()); // Asigna la dirección
        cliente.setFechaNacimiento(clienteRegistroDTO.getFechaNacimiento()); // Asigna la fecha de nacimiento
        cliente.setFechaRegistro(LocalDateTime.now()); // Registra la fecha actual como fecha de registro
        return cliente; // Devuelve el objeto cliente
    }

    // Obtiene todos los clientes y los convierte a DTO
    @Override
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll() // Obtiene todos los clientes
                .stream() // Crea un flujo de datos
                .map(this::convertToDTO) // Convierte cada cliente a DTO
                .collect(Collectors.toList()); // Devuelve la lista final
    }

    // Obtiene clientes con paginación (por ejemplo, 10 por página)
    @Override
    public Page<ClienteDTO> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable) // Busca clientes paginados
                .map(this::convertToDTO); // Convierte los resultados a DTO
    }

    // Busca un cliente por su ID
    @Override
    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id) // Busca por ID
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id)); // Si no existe, lanza excepción
        return convertToDTO(cliente); // Devuelve el cliente en formato DTO
    }

    // Busca un cliente por su email
    @Override
    public ClienteDTO findByEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email) // Busca por email
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + email)); // Lanza error si no lo encuentra
        return convertToDTO(cliente); // Devuelve el cliente como DTO
    }

    // Crea un nuevo cliente
    @Override
    public ClienteDTO create(ClienteRegistroDTO clienteRegistroDTO) {
        // Verifica si ya existe un cliente con el mismo email
        if (clienteRepository.existsByEmail(clienteRegistroDTO.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con el email: " + clienteRegistroDTO.getEmail());
        }

        Cliente cliente = convertToEntity(clienteRegistroDTO); // Convierte el DTO a entidad
        Cliente clienteGuardado = clienteRepository.save(cliente); // Guarda en la base de datos
        return convertToDTO(clienteGuardado); // Devuelve el cliente guardado como DTO
    }

    // Actualiza los datos de un cliente existente
    @Override
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id) // Busca el cliente por ID
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id)); // Lanza error si no existe

        // Actualiza solo los campos no nulos (que fueron enviados)
        if (clienteDTO.getNombre() != null) clienteExistente.setNombre(clienteDTO.getNombre());
        if (clienteDTO.getApellido() != null) clienteExistente.setApellido(clienteDTO.getApellido());
        if (clienteDTO.getTelefono() != null) clienteExistente.setTelefono(clienteDTO.getTelefono());
        if (clienteDTO.getDireccion() != null) clienteExistente.setDireccion(clienteDTO.getDireccion());
        if (clienteDTO.getFechaNacimiento() != null) clienteExistente.setFechaNacimiento(clienteDTO.getFechaNacimiento());

        Cliente clienteActualizado = clienteRepository.save(clienteExistente); // Guarda los cambios
        return convertToDTO(clienteActualizado); // Devuelve el cliente actualizado
    }

    // Elimina un cliente por su ID
    @Override
    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id) // Busca el cliente
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id)); // Si no existe, lanza error
        clienteRepository.delete(cliente); // Elimina el cliente
    }

    // Permite el inicio de sesión del cliente
    @Override
    public ClienteDTO login(ClienteLoginDTO clienteLoginDTO) {
        Cliente cliente = clienteRepository.findByEmail(clienteLoginDTO.getEmail()) // Busca cliente por email
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas")); // Lanza error si no lo encuentra

        // En un caso real, aquí se comprobaría la contraseña encriptada con un passwordEncoder
        // if (!passwordEncoder.matches(clienteLoginDTO.getPassword(), cliente.getPassword())) {
        //     throw new RuntimeException("Credenciales inválidas");
        // }

        return convertToDTO(cliente); // Devuelve los datos del cliente logueado
    }

    // Verifica si existe un cliente con el email indicado
    @Override
    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    // Busca clientes cuyo nombre contenga cierto texto (ignorando mayúsculas)
    @Override
    public List<ClienteDTO> findByNombreContaining(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Busca clientes cuyo apellido contenga cierto texto (ignorando mayúsculas)
    @Override
    public List<ClienteDTO> findByApellidoContaining(String apellido) {
        return clienteRepository.findByApellidoContainingIgnoreCase(apellido)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Busca clientes registrados entre dos fechas dadas
    @Override
    public List<ClienteDTO> findByFechaRegistroBetween(String start, String end) {
        LocalDateTime startDate = LocalDateTime.parse(start); // Convierte la fecha inicial
        LocalDateTime endDate = LocalDateTime.parse(end); // Convierte la fecha final

        return clienteRepository.findByFechaRegistroBetween(startDate, endDate) // Busca entre las fechas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Activa la cuenta de un cliente (futura implementación)
    @Override
    public ClienteDTO activarCuenta(Long id) {
        // Aquí se implementaría la lógica de activar la cuenta
        return findById(id); // Por ahora solo devuelve el cliente
    }

    // Desactiva la cuenta de un cliente (futura implementación)
    @Override
    public ClienteDTO desactivarCuenta(Long id) {
        // Aquí se implementaría la lógica de desactivar la cuenta
        return findById(id); // Por ahora solo devuelve el cliente
    }
}