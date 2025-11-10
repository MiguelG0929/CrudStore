package com.lopezmiguel.crudstore.controller;

import com.lopezmiguel.crudstore.dto.ClienteDTO;
import com.lopezmiguel.crudstore.dto.ClienteRegistroDTO;
import com.lopezmiguel.crudstore.dto.ClienteLoginDTO;

import com.lopezmiguel.crudstore.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/clientes") // Ruta base para todas las peticiones relacionadas con clientes
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen (útil para Postman o frontends)
public class ClienteController {

    @Autowired // Inyección automática del servicio ClienteService
    private ClienteService clienteService;

    // GET - Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        // Llama al servicio para obtener todos los clientes
        List<ClienteDTO> clientes = clienteService.findAll();
        // Devuelve la lista con estado HTTP 200 OK
        return ResponseEntity.ok(clientes);
    }

    // GET - Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        // Busca cliente según su ID
        ClienteDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    // GET - Obtener cliente por email
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteDTO> getClienteByEmail(@PathVariable String email) {
        // Busca cliente según su email
        ClienteDTO cliente = clienteService.findByEmail(email);
        return ResponseEntity.ok(cliente);
    }

    // POST - Registrar nuevo cliente
    @PostMapping("/auth/register")
    public ResponseEntity<ClienteDTO> registrarCliente(@RequestBody ClienteRegistroDTO clienteRegistroDTO) {
        // Crea un nuevo cliente con los datos recibidos
        ClienteDTO nuevoCliente = clienteService.create(clienteRegistroDTO);
        return ResponseEntity.ok(nuevoCliente);
    }

    // POST - Login de cliente
    @PostMapping("/auth/login")
    public ResponseEntity<ClienteDTO> loginCliente(@RequestBody ClienteLoginDTO clienteLoginDTO) {
        // Verifica las credenciales del cliente
        ClienteDTO cliente = clienteService.login(clienteLoginDTO);
        return ResponseEntity.ok(cliente);
    }

    // PUT - Actualizar cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(
            @PathVariable Long id,
            @RequestBody ClienteDTO clienteDTO) {
        // Actualiza los datos del cliente según su ID
        ClienteDTO clienteActualizado = clienteService.update(id, clienteDTO);
        return ResponseEntity.ok(clienteActualizado);
    }

    // DELETE - Eliminar cliente (borrado lógico o físico según el servicio)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        // Llama al servicio para eliminar al cliente
        clienteService.delete(id);
        // Devuelve respuesta vacía con estado 204 No Content
        return ResponseEntity.noContent().build();
    }

    // GET - Verificar si un email ya está registrado
    @GetMapping("/existe-email/{email}")
    public ResponseEntity<Boolean> existeEmail(@PathVariable String email) {
        // Devuelve true si el email ya existe en la base de datos
        boolean existe = clienteService.existsByEmail(email);
        return ResponseEntity.ok(existe);
    }

    // GET - Buscar clientes por nombre
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<ClienteDTO>> buscarPorNombre(@RequestParam String nombre) {
        // Busca todos los clientes cuyo nombre contenga el texto dado
        List<ClienteDTO> clientes = clienteService.findByNombreContaining(nombre);
        return ResponseEntity.ok(clientes);
    }

    // GET - Buscar clientes por apellido
    @GetMapping("/buscar/apellido")
    public ResponseEntity<List<ClienteDTO>> buscarPorApellido(@RequestParam String apellido) {
        // Busca todos los clientes cuyo apellido contenga el texto dado
        List<ClienteDTO> clientes = clienteService.findByApellidoContaining(apellido);
        return ResponseEntity.ok(clientes);
    }
}
