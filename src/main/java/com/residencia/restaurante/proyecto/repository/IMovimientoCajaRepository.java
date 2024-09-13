package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.MovimientosCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMovimientoCajaRepository extends JpaRepository<MovimientosCaja,Integer> {
    List<MovimientosCaja> findAllByTipoMovimientoEqualsIgnoreCase(String tipo);

    List<MovimientosCaja> findAllByArqueo_Id(Integer id);
}
