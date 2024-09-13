package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Venta_MedioPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVenta_MedioPagoRepository extends JpaRepository<Venta_MedioPago,Integer> {
    List<Venta_MedioPago> getAllByVenta_Id(Integer id);
}
