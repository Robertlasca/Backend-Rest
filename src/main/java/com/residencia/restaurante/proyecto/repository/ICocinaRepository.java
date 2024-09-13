package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Cocina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad Cocina.
 */
@Repository
public interface ICocinaRepository extends JpaRepository<Cocina,Integer> {

    /**
     * Obtiene todas las cocinas con visibilidad verdadera.
     * @return Lista de cocinas con visibilidad verdadera.
     */
    List<Cocina> getAllByVisibilidadTrue();

    /**
     * Obtiene todas las cocinas con visibilidad falsa.
     * @return Lista de cocinas con visibilidad falsa.
     */
    List<Cocina> getAllByVisibilidadFalse();

    /**
     * Verifica si existe una cocina con el nombre especificado (ignorando mayúsculas y minúsculas).
     * @param nombre El nombre de la cocina a buscar.
     * @return true si existe una cocina con el nombre especificado, false de lo contrario.
     */
    boolean existsCocinaByNombreLikeIgnoreCase(String nombre);

    @Query("SELECT c FROM Cocina c WHERE c NOT IN (SELECT a.cocina FROM Almacen a) AND c.visibilidad = true")
    List<Cocina> getCocinasNoAsociadas();
}
