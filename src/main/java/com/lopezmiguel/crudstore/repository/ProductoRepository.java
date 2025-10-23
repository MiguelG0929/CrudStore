package com.lopezmiguel.crudstore.repository;

import com.lopezmiguel.crudstore.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>
{
    //----Metodos basicos para realizar el filtrado----

    List<Producto> findByActivoTrue();  //Devuelve todos los productos que están activos
    List<Producto> findByCategoria(String categoria); //Devuelve todos los productos de una categoría específica.
    Optional<Producto> findBySku(String sku); //Busca un producto por su SKU (un identificador único) y devuelve Optional porque podría no existir
    List<Producto> findByActivoTrueAndStockGreaterThan(Integer stock); //Devuelve productos activos y que tengan stock mayor que el número dado.


    //----Busquedas Avanzas----

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +  //Busca productos activos que contengan la palabra query en: nombre, categoria y descripcion ignora mayus y minus
            "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.categoria) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Producto> busquedaAvanzada(@Param("query") String query);


    //----Filtros por precio----

    List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);    //Devuelve productos cuyo precio esté entre dos valores.
    List<Producto> findByPrecioLessThanEqual(Double precioMax);
    List<Producto> findByPrecioGreaterThanEqual(Double precioMin);
    //Productos hasta un precio máximo o desde un precio mínimo.


    //----Filtros combinados categoria + precio----

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.categoria = :categoria AND p.precio BETWEEN :precioMin AND :precioMax")
    List<Producto> findByCategoriaAndPrecioBetween(@Param("categoria") String categoria,
                                                   @Param("precioMin") Double precioMin,
                                                   @Param("precioMax") Double precioMax);
    /*Devuelve solo productos activos, que pertenezacan a una categoria especifica y cuyo precio
    * este dentro de un rango ejemplo: ropa entre 10 y 50*/




    //----Productos con stock bajo (para alertas)----

    @Query("SELECT p FROM Producto p WHERE p.stock <= :stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo(@Param("stockMinimo") Integer stockMinimo);
    //Devuelve productos activos que tengan pocas unidades en stock, útil para generar alertas.



    // Productos por color
    List<Producto> findByColorIgnoreCase(String color);
    //Devuelve productos cuyo color coincida, sin importar mayúsculas/minúsculas.



    // Ordenamientos
    List<Producto> findByActivoTrueOrderByPrecioAsc();
    List<Producto> findByActivoTrueOrderByPrecioDesc();
    List<Producto> findByActivoTrueOrderByFechaCreacionDesc();

    /*Devuelve Productos ordenados por: ascendente, descendente y fecha de creación*/
}
