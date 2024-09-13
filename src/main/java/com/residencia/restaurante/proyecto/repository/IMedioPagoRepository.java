package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.MedioPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad MedioPago.
 */
@Repository
public interface IMedioPagoRepository extends JpaRepository<MedioPago,Integer> {

    /**
     * Obtiene todos los medios de pago con disponibilidad activa.
     * @return Lista de medios de pago con disponibilidad activa.
     */
    List<MedioPago> getAllByDisponibilidadTrue();

    /**
     * Obtiene todos los medios de pago con disponibilidad inactiva.
     * @return Lista de medios de pago con disponibilidad inactiva.
     */
    List<MedioPago> getAllByDisponibilidadFalse();

    /**
     * Verifica si existe un medio de pago con el nombre especificado, sin importar mayúsculas o minúsculas.
     * @param nombre El nombre del medio de pago a buscar.
     * @return Verdadero si existe un medio de pago con el nombre especificado, falso en caso contrario.
     */
    boolean existsMedioPagoByNombreLikeIgnoreCase(String nombre);
}
