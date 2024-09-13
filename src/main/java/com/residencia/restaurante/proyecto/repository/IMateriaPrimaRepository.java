package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.MateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad MateriaPrima.
 */
@Repository
public interface IMateriaPrimaRepository extends JpaRepository<MateriaPrima,Integer> {

    /**
     * Obtiene todas las materias primas con visibilidad activa.
     * @return Lista de materias primas con visibilidad activa.
     */
    List<MateriaPrima> getAllByVisibilidadTrue();

    /**
     * Obtiene todas las materias primas con visibilidad inactiva.
     * @return Lista de materias primas con visibilidad inactiva.
     */
    List<MateriaPrima> getAllByVisibilidadFalse();

    /**
     * Verifica si existe una materia prima con el nombre especificado, sin importar mayúsculas o minúsculas.
     * @param nombre El nombre de la materia prima a buscar.
     * @return Verdadero si existe una materia prima con el nombre especificado, falso en caso contrario.
     */
    boolean existsMateriaPrimaByNombreLikeIgnoreCase(String nombre);

    @Query("SELECT COUNT(p) FROM MateriaPrima p WHERE p.categoriaMateriaPrima.id = :categoriaId")
    int countByCategoriaId( Integer categoriaId);

    List<MateriaPrima> getAllByCategoriaMateriaPrima_Id(Integer id);

}
