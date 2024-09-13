package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.DetalleOrdenMenu;
import com.residencia.restaurante.proyecto.entity.DetalleOrden_ProductoNormal;
import com.residencia.restaurante.proyecto.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDetalleOrden_ProductoNormalRepository extends JpaRepository<DetalleOrden_ProductoNormal,Integer> {
    List<DetalleOrden_ProductoNormal> getAllByOrden(Orden orden);
}
