package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Inventario;
import com.residencia.restaurante.proyecto.entity.ProductoTerminado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoTerminadoRepository extends JpaRepository<ProductoTerminado,Integer> {
    boolean existsByNombreLikeIgnoreCase(String nombre);

    List<ProductoTerminado> getAllByVisibilidadTrue();

    List<ProductoTerminado> getAllByVisibilidadFalse();



    /**
     * Encuentra todos los registros de inventario con stock actual menor que el stock mínimo.
     * @return Lista de registros de inventario con stock actual menor que el stock mínimo.
     */
    @Query("SELECT inv FROM ProductoTerminado inv WHERE inv.stockActual < inv.stockMin AND inv.visibilidad=true")
    List<ProductoTerminado> findProductoTerminadoByStockActualMenorAlMinimo();

    /**
     * Encuentra todos los registros de inventario con stock actual mayor que el stock máximo.
     * @return Lista de registros de inventario con stock actual mayor que el stock máximo.
     */
    @Query("SELECT inv FROM ProductoTerminado inv WHERE inv.stockActual > inv.stockMax AND inv.visibilidad=true")
    List<ProductoTerminado> findProductoTerminadoByStockActualMayorAlMaximo();

    /**
     * Encuentra todos los registros de inventario con stock actual entre el stock mínimo y máximo.
     * @return Lista de registros de inventario con stock actual entre el stock mínimo y máximo.
     */
    @Query("SELECT inv FROM ProductoTerminado inv WHERE inv.stockActual >= inv.stockMin AND inv.stockActual <= inv.stockMax AND inv.visibilidad=true")
    List<ProductoTerminado> findProductoTerminadoByStockActualEntreMinimoYMaximo();

    // Método para contar productos terminados por categoría
    @Query("SELECT COUNT(p) FROM ProductoTerminado p WHERE p.categoria.id = :categoriaId")
    Long countByCategoriaId(Integer categoriaId);

    List<ProductoTerminado> getAllByCategoriaId(Integer id);
}
