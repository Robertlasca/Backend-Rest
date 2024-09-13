package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Cocina;
import com.residencia.restaurante.proyecto.entity.Utensilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad Utensilio.
 */
@Repository
public interface IUtensilioRepository extends JpaRepository<Utensilio,Integer> {

    /**
     * Obtiene todos los utensilios asociados a una cocina especificada.
     * @param cocina La cocina de la que se desean obtener los utensilios.
     * @return Lista de utensilios asociados a la cocina especificada.
     */
    //List<Utensilio> getAllByCocina(Cocina cocina);
}
