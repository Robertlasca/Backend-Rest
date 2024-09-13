package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Inventario;
import com.residencia.restaurante.proyecto.entity.MateriaPrima_ProductoTerminado;
import com.residencia.restaurante.proyecto.entity.ProductoTerminado;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMateriaPrima_ProductoTerminadoRepository extends JpaRepository<MateriaPrima_ProductoTerminado,Integer> {

    List<MateriaPrima_ProductoTerminado> getAllByProductoTerminado(ProductoTerminado productoTerminado);

    @Query("SELECT SUM(mpp.cantidad * mp.costoUnitario) FROM MateriaPrima_ProductoTerminado mpp " +
            "JOIN mpp.inventario i " +
            "JOIN i.materiaPrima mp " +
            "WHERE mpp.productoTerminado.id = :productoTerminadoId")
    Double calcularCostoProduccionPorProductoTerminado(Integer productoTerminadoId);


    List<MateriaPrima_ProductoTerminado> findAllByProductoTerminado(ProductoTerminado producto);

    List<MateriaPrima_ProductoTerminado> getAllByInventario(Inventario inventario);
}
