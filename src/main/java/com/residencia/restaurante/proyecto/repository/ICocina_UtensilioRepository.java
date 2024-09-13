package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Cocina_Utensilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICocina_UtensilioRepository extends JpaRepository<Cocina_Utensilio,Integer> {
    List<Cocina_Utensilio> findAllByCocina_Id(Integer id);
}
