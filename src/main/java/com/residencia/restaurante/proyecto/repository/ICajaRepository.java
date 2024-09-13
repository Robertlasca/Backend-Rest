package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad Caja.
 */
@Repository
public interface ICajaRepository extends JpaRepository<Caja,Integer> {

    /**
     * Obtiene todas las cajas con visibilidad verdadera.
     * @return Lista de cajas con visibilidad verdadera.
     */
    List<Caja> getAllByVisibilidadTrue();

    /**
     * Obtiene todas las cajas con visibilidad falsa.
     * @return Lista de cajas con visibilidad falsa.
     */
    List<Caja> getAllByVisibilidadFalse();

    /**
     * Verifica si existe una caja con el nombre especificado (ignorando mayúsculas y minúsculas).
     * @param nombre El nombre de la caja a buscar.
     * @return true si existe una caja con el nombre especificado, false de lo contrario.
     */
    boolean existsCajaByNombreLikeIgnoreCase(String nombre);
}
