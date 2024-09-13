package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.DetalleOrdenMenu;
import com.residencia.restaurante.proyecto.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDetalleOrden_MenuRepository extends JpaRepository<DetalleOrdenMenu,Integer> {
    List<DetalleOrdenMenu> getAllByOrden(Orden orden);
    List<DetalleOrdenMenu> getAllByEstadoEqualsIgnoreCase(String estado);

    Optional<DetalleOrdenMenu> findDetalleOrdenMenuByOrden_IdAndMenu_Id(Integer orden, Integer menu);

    @Query("SELECT COUNT(d) > 0 FROM DetalleOrdenMenu d WHERE d.menu.cocina.id = :cocinaId AND d.estado NOT IN ('Terminada', 'Cancelado')")
    boolean existsByMenuCocinaIdAndEstadoNotIn(Integer cocinaId);

    @Query("SELECT COUNT(d) > 0 FROM DetalleOrdenMenu d WHERE d.menu.id = :menuId AND d.estado NOT IN ('Terminada', 'Cancelado')")
    boolean existsByMenuIdAndEstadoNotIn(Integer menuId);
}
