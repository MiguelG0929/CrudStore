package com.lopezmiguel.crudstore.repository;

import com.lopezmiguel.crudstore.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //le dice a Spring que esta interfaz es un Repositorio de datos y debe ser gestionada como bean
public interface AdminRepository extends JpaRepository<Admin, Long>
{
    Optional<Admin> findByUsername(String username); //buscar admin por nombre de usuario
    Optional<Admin> findByEmail(String email); //buscar admin por correo
    Boolean existsByUsername(String username); //comprobar si un nombre ya existe
    Boolean existsByEmail(String email); //comprobar si un correo ya existe
    Optional<Admin> findByUsernameAndActivoTrue(String username); //solo devolvera el admin si este esta activo

}
