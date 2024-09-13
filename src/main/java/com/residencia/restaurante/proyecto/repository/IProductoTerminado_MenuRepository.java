package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Menu;
import com.residencia.restaurante.proyecto.entity.ProductoTerminado;
import com.residencia.restaurante.proyecto.entity.ProductoTerminado_Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoTerminado_MenuRepository extends JpaRepository<ProductoTerminado_Menu,Integer> {
    List<ProductoTerminado_Menu> getAllByMenu(Menu menu);


    List<ProductoTerminado_Menu> findAllByMenuId(Integer menuId);

    List<ProductoTerminado_Menu> getAllByProductoTerminado(ProductoTerminado productoTerminado);

    @Query("SELECT COUNT(ptm) > 0 FROM ProductoTerminado_Menu ptm WHERE ptm.productoTerminado.id = :productoTerminadoId AND ptm.menu.visibilidad = true")
    boolean existsByProductoTerminadoIdAndMenuVisibilidadIsTrue(Integer productoTerminadoId);
}
