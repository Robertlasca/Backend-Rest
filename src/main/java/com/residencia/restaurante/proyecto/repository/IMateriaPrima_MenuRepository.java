package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Inventario;
import com.residencia.restaurante.proyecto.entity.MateriaPrima_Menu;
import com.residencia.restaurante.proyecto.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMateriaPrima_MenuRepository extends JpaRepository<MateriaPrima_Menu,Integer> {
    List<MateriaPrima_Menu> getAllByMenu(Menu menu);

    @Query("SELECT SUM(mpm.cantidad * mp.costoUnitario) FROM MateriaPrima_Menu mpm " +
            "JOIN mpm.inventario i " +
            "JOIN i.materiaPrima mp " +
            "WHERE mpm.menu.id = :menuId")
    Double calcularCostoIngredientesPorMenu(Integer menuId);

    List<MateriaPrima_Menu> findByMenuId(Integer idProducto);

    List<MateriaPrima_Menu> getAllByInventario(Inventario inventario);
}
