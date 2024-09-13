package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Almacen;
import com.residencia.restaurante.proyecto.entity.Cocina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Almacen.
 */
@Repository
public interface IAlmacenRepository extends JpaRepository<Almacen,Integer> {

    /**
     * Obtiene todos los almacenes con visibilidad activa.
     * @return Lista de almacenes activos.
     */
    List<Almacen> getAllByVisibilidadTrue();

    /**
     * Obtiene todos los almacenes con visibilidad inactiva.
     * @return Lista de almacenes inactivos.
     */
    List<Almacen> getAllByVisibilidadFalse();

    /**
     * Verifica si existe un almacén con un nombre dado, ignorando mayúsculas y minúsculas.
     * @param nombre Nombre del almacén a buscar.
     * @return True si existe un almacén con el nombre dado, false en caso contrario.
     */
    boolean existsAlmacenByNombreLikeIgnoreCase(String nombre);

    /**
     * Busca un almacén por el ID de la cocina asociada.
     * @param id ID de la cocina asociada al almacén.
     * @return Almacén encontrado (si existe) o null.
     */
    Optional<Almacen> findAlmacenByCocina_Id(Integer id);
}
