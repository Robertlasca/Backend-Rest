package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden,Integer> {
    Optional<Orden> findOrdensByMesaId(Integer id);

    @Query("SELECT MAX(o.folio) FROM Orden o")
    Integer findMaxFolio();

    @Query("SELECT o FROM Orden o WHERE o.fechaHoraApertura >= :startOfDay AND o.fechaHoraApertura < :endOfDay ORDER BY o.fechaHoraApertura DESC")
    List<Orden> findOrdenesDelDia(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT COUNT(o) > 0 FROM Orden o WHERE o.mesa.areaServicio.id = :areaServicioId AND o.estado NOT IN ('Terminada', 'Cancelado')")
    boolean existsByAreaServicioIdAndEstadoNotIn(Integer areaServicioId);

    @Query("SELECT COUNT(o) > 0 FROM Orden o WHERE o.caja.id = :cajaId AND o.estado NOT IN ('Terminada', 'Cancelado')")
    boolean existsByCajaIdAndEstadoNotIn(Integer cajaId);
}
