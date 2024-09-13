package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.AreaServicio;
import com.residencia.restaurante.proyecto.entity.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAreaServicioRepository extends JpaRepository<AreaServicio,Integer> {

    List<AreaServicio> getAllByDisponibilidadTrue();


    List<AreaServicio> getAllByDisponibilidadFalse();

    boolean existsAreaServiciosByNombreLikeIgnoreCase(String nombre);
}
