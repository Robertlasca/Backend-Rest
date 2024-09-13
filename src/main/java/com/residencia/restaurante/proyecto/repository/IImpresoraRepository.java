package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Impresora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Impresora.
 */
@Repository
public interface IImpresoraRepository extends JpaRepository<Impresora,Integer> {

    /**
     * Obtiene todas las impresoras con estado verdadero.
     * @return Lista de impresoras con estado verdadero.
     */
    List<Impresora> getAllByEstadoTrue();

    /**
     * Obtiene todas las impresoras con estado falso.
     * @return Lista de impresoras con estado falso.
     */
    List<Impresora> getAllByEstadoFalse();

    /**
     * Obtiene la impresora marcada como por defecto, si existe.
     * @return La impresora marcada como por defecto, si existe.
     */
    Optional<Impresora> getImpresoraByPorDefectoTrue();

    /**
     * Verifica si existe una impresora con el nombre especificado (ignorando mayúsculas y minúsculas).
     * @param nombre El nombre de la impresora a buscar.
     * @return true si existe una impresora con el nombre especificado, false de lo contrario.
     */
    boolean existsImpresoraByNombreLikeIgnoreCase(String nombre);
}
