package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.ProductoNormal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad ProductoNormal.
 */
@Repository
public interface IProductoNormalRepository extends JpaRepository<ProductoNormal,Integer> {

    /**
     * Verifica si existe un producto normal con el nombre especificado, sin importar mayúsculas o minúsculas.
     * @param nombre El nombre del producto normal a buscar.
     * @return Verdadero si existe un producto normal con el nombre especificado, falso en caso contrario.
     */
    boolean existsProductoNormalByNombreLikeIgnoreCase(String nombre);

    /**
     * Obtiene todos los productos normales con visibilidad activa.
     * @return Lista de productos normales con visibilidad activa.
     */
    List<ProductoNormal> getAllByVisibilidadTrue();

    /**
     * Obtiene todos los productos normales con visibilidad inactiva.
     * @return Lista de productos normales con visibilidad inactiva.
     */
    List<ProductoNormal> getAllByVisibilidadFalse();

    /**
     * Busca productos normales cuyo stock actual sea menor que el mínimo.
     * @return Lista de productos normales con stock actual menor al mínimo.
     */
    @Query("SELECT prod FROM ProductoNormal prod WHERE prod.stockActual < prod.stockMin")
    List<ProductoNormal> findProductoNormalByStockActualMenorAlMinimo();

    /**
     * Busca productos normales cuyo stock actual sea mayor que el máximo.
     * @return Lista de productos normales con stock actual mayor al máximo.
     */
    @Query("SELECT prod FROM ProductoNormal prod WHERE prod.stockActual > prod.stockMax")
    List<ProductoNormal> findProductoNormalByStockActualMayorAlMaximo();

    /**
     * Busca productos normales cuyo stock actual esté entre el mínimo y el máximo.
     * @return Lista de productos normales con stock actual entre el mínimo y el máximo.
     */
    @Query("SELECT prod FROM ProductoNormal prod WHERE prod.stockActual >= prod.stockMin AND prod.stockActual <= prod.stockMax")
    List<ProductoNormal> findProductoNormalByStockActualEntreMinimoYMaximo();

    // Método para contar productos normales por categoría
    @Query("SELECT COUNT(p) FROM ProductoNormal p WHERE p.categoria.id = :categoriaId")
    Long countByCategoriaId( Integer categoriaId);

    List<ProductoNormal> getAllByCategoriaId(Integer id);
}
