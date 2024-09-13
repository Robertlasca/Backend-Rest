package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.ArqueoSaldos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad ArqueoSaldos.
 */
@Repository
public interface IArqueoSaldosRepository extends JpaRepository<ArqueoSaldos,Integer> {

    /**
     * Busca un registro de ArqueoSaldos por el ID del arqueo y el ID del medio de pago.
     * @param arqueo ID del arqueo.
     * @param medioPago ID del medio de pago.
     * @return Un objeto Optional que puede contener el ArqueoSaldos si se encuentra, o un objeto vacío si no se encuentra.
     */
    Optional<ArqueoSaldos> findArqueoSaldosByArqueo_IdAndMedioPago_Id(Integer arqueo,Integer medioPago);

    /**
     * Busca todos los registros de ArqueoSaldos asociados a un arqueo específico.
     * @param id ID del arqueo.
     * @return Lista de ArqueoSaldos asociados al arqueo.
     */
    List<ArqueoSaldos> findAllByArqueo_Id(Integer id);
}
