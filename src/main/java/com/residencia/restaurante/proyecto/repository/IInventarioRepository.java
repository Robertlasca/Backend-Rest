package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Almacen;
import com.residencia.restaurante.proyecto.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Inventario.
 */
@Repository
public interface IInventarioRepository extends JpaRepository<Inventario,Integer> {

    /**
     * Encuentra todos los registros de inventario asociados con un almacén específico.
     * @param id El ID del almacén.
     * @return Lista de registros de inventario asociados con el almacén específico.
     */
    List<Inventario> getAllByAlmacen_Id(Integer id);
    List<Inventario> getAllByMateriaPrima_Id(Integer id);
    boolean existsByAlmacen_Id(Integer id);
    boolean existsByMateriaPrima_Id(Integer id);

    /**
     * Encuentra un registro de inventario por ID de almacén y ID de materia prima.
     * @param idAlmacen El ID del almacén.
     * @param idMateriaPrima El ID de la materia prima.
     * @return Un registro de inventario si existe para el almacén y la materia prima especificados.
     */
    Optional<Inventario> findInventarioByAlmacen_IdAndMateriaPrima_Id(Integer idAlmacen, Integer idMateriaPrima);

    /**
     * Encuentra todos los registros de inventario asociados con un almacén específico.
     * @param id El ID del almacén.
     * @return Lista de registros de inventario asociados con el almacén específico.
     */
    List<Inventario> findAllByAlmacenId(Integer id);

    /**
     * Encuentra todos los registros de inventario con stock actual menor que el stock mínimo.
     * @return Lista de registros de inventario con stock actual menor que el stock mínimo.
     */
    @Query("SELECT inv FROM Inventario inv WHERE inv.stockActual < inv.stockMin")
    List<Inventario> findInventarioByStockActualMenorAlMinimo();

    /**
     * Encuentra todos los registros de inventario con stock actual mayor que el stock máximo.
     * @return Lista de registros de inventario con stock actual mayor que el stock máximo.
     */
    @Query("SELECT inv FROM Inventario inv WHERE inv.stockActual > inv.stockMax")
    List<Inventario> findInventarioByStockActualMayorAlMaximo();

    /**
     * Encuentra todos los registros de inventario con stock actual entre el stock mínimo y máximo.
     * @return Lista de registros de inventario con stock actual entre el stock mínimo y máximo.
     */
    @Query("SELECT inv FROM Inventario inv WHERE inv.stockActual >= inv.stockMin AND inv.stockActual <= inv.stockMax")
    List<Inventario> findInventarioByStockActualEntreMinimoYMaximo();

    @Query("SELECT a FROM Almacen a WHERE a.id NOT IN (SELECT i.almacen.id FROM Inventario i WHERE i.materiaPrima.id = :materiaPrimaId) AND a.visibilidad=true")
    List<Almacen> findAlmacenesNotRelatedToMateriaPrima(Integer materiaPrimaId);

    @Query("SELECT i FROM Inventario i WHERE i.almacen.id = :almacenId AND i.stockActual < i.stockMin")
    List<Inventario> inventariosInsuficientePorAlmacen(Integer almacenId);
}
