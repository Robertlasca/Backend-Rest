package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Categoria.
 */
@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria,Integer> {

    /**
     * Obtiene todas las categorías con visibilidad verdadera.
     * @return Lista de categorías con visibilidad verdadera.
     */
    List<Categoria> getAllByVisibilidadTrue();

    /**
     * Obtiene todas las categorías con visibilidad falsa.
     * @return Lista de categorías con visibilidad falsa.
     */
    List<Categoria> getAllByVisibilidadFalse();

    List<Categoria> getAllByVisibilidadTrueAndPerteneceEqualsIgnoreCase(String pertenece);
    List<Categoria> getAllByPerteneceEqualsIgnoreCase(String pertenece);

    /**
     * Verifica si existe una categoría con el nombre especificado (ignorando mayúsculas y minúsculas).
     * @param nombre El nombre de la categoría a buscar.
     * @return true si existe una categoría con el nombre especificado, false de lo contrario.
     */
    boolean existsCategoriaByNombreLikeIgnoreCase(String nombre);

    Optional<Categoria> findCategoriaByNombreLikeIgnoreCase(String nombre);

    boolean existsCategoriaByNombreLikeIgnoreCaseAndPerteneceLikeIgnoreCase(String nombre, String pertenece);

}
