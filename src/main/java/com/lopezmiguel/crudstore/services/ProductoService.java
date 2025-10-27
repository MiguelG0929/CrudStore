package com.lopezmiguel.crudstore.services;

import com.lopezmiguel.crudstore.dto.ProductoCreateDTO;
import com.lopezmiguel.crudstore.dto.ProductoDTO;
import com.lopezmiguel.crudstore.dto.ProductoUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; //Spring Data para paginación estilo "Amazon"

import java.util.List;

//Define que metodos existen, como si fuese un menu
public interface ProductoService {

    // CRUD básico
    List<ProductoDTO> findAll();   //devuelve todos los productos
    Page<ProductoDTO> findAll(Pageable pageable); //devuelve todos los productos paginados
    ProductoDTO findById(Long id);   //Busca un producto por ID
    ProductoDTO findBySku(String sku); //Busca por su codigo SKU
    ProductoDTO create(ProductoCreateDTO productoCreateDTO); //Crea un producto nuevo
    ProductoDTO update(Long id, ProductoUpdateDTO productoUpdateDTO); //Cambiar el precio
    void delete(Long id);   //Elimina o desactiva un producto

    // Búsquedas y filtros segun distintos criterios
    List<ProductoDTO> findByActivoTrue();  //Solo productos activos
    List<ProductoDTO> findByCategoria(String categoria); //buscar por categoria
    List<ProductoDTO> busquedaAvanzada(String query); //busca por palabra clave
    List<ProductoDTO> findByPrecioBetween(Double min, Double max); //productos entre dos precios
    List<ProductoDTO> findByCategoriaAndPrecioBetween(String categoria, Double min, Double max); //combina ambos filtros

    // Gestión de stock
    List<ProductoDTO> findProductosConStockBajo(Integer stockMinimo); //devuelve los que tienen poco stock
    ProductoDTO actualizarStock(Long id, Integer nuevoStock);  //cambia la cantidad disponible de un producto.

    // Ordenamientos
    List<ProductoDTO> findByActivoTrueOrderByPrecioAsc(); //Por precio ascendente (de menor a mayor).
    List<ProductoDTO> findByActivoTrueOrderByPrecioDesc(); //Por precio descendente.
    List<ProductoDTO> findByActivoTrueOrderByFechaCreacionDesc(); //Por fecha de creación (los más nuevos primero).

}
