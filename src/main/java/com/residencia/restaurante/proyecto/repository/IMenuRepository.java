package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMenuRepository extends JpaRepository<Menu,Integer> {

    @Query("SELECT COUNT(m) > 0 FROM Menu m WHERE m.cocina.id = :cocinaId AND m.visibilidad = true")
    boolean existsByCocinaIdAndMenuVisibilidadIsTrue(Integer cocinaId);
    boolean existsByNombreLikeIgnoreCase(String nombre);

    List<Menu> getAllByVisibilidadTrue();

    // Método para contar menús por categoría
    @Query("SELECT COUNT(m) FROM Menu m WHERE m.categoria.id = :categoriaId")
    Long countByCategoriaId(Integer categoriaId);

    List<Menu> getAllByVisibilidadFalse();
    List<Menu> getAllByCategoriaId(Integer id);
}
