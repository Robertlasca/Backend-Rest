package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Categoria;
import com.residencia.restaurante.proyecto.entity.CategoriaMateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad CategoriaMateriaPrima.
 */
@Repository
public interface ICategoriaMateriaPrimaRepository extends JpaRepository<CategoriaMateriaPrima,Integer> {

    /**
     * Obtiene todas las categorías de materia prima con visibilidad verdadera.
     * @return Lista de categorías de materia prima con visibilidad verdadera.
     */
    List<CategoriaMateriaPrima> getAllByVisibilidadTrue();

    /**
     * Obtiene todas las categorías de materia prima con visibilidad falsa.
     * @return Lista de categorías de materia prima con visibilidad falsa.
     */
    List<CategoriaMateriaPrima> getAllByVisibilidadFalse();

    /**
     * Verifica si existe una categoría de materia prima con el nombre especificado (ignorando mayúsculas y minúsculas).
     * @param nombre El nombre de la categoría de materia prima a buscar.
     * @return true si existe una categoría de materia prima con el nombre especificado, false de lo contrario.
     */
    boolean existsCategoriaMateriaPrimaByNombreLikeIgnoreCase(String nombre);

    Optional<CategoriaMateriaPrima> findCategoriaByNombreLikeIgnoreCase(String s);
}
