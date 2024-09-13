package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.AreaServicio_Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAreaServicio_MesaRepository extends JpaRepository<AreaServicio_Mesa,Integer> {
}
