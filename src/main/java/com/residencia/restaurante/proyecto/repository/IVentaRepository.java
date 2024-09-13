package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IVentaRepository extends JpaRepository<Venta,Integer> {
    List<Venta> getAllByArqueo_Caja_Nombre(String caja);

    @Query("SELECT v FROM Venta v WHERE DATE(v.fechaHoraConsolidacion) = :fecha")
    List<Venta> findVentasPorDia(LocalDate fecha);

    @Query("SELECT v FROM Venta v WHERE YEAR(v.fechaHoraConsolidacion) = :anio AND MONTH(v.fechaHoraConsolidacion) = :mes")
    List<Venta> findVentasPorMes( int anio, int mes);

    @Query("SELECT v FROM Venta v WHERE YEAR(v.fechaHoraConsolidacion) = :anio")
    List<Venta> findVentasPorAno(int anio);

    @Query("SELECT v FROM Venta v WHERE v.fechaHoraConsolidacion >= :startDate AND v.fechaHoraConsolidacion < :endDate")
    List<Venta> findByFechaBetween(LocalDate startDate, LocalDate endDate);



}
