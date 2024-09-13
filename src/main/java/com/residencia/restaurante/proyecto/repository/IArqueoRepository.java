package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Arqueo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Arqueo.
 */
@Repository
public interface IArqueoRepository extends JpaRepository<Arqueo,Integer> {

    /**
     * Verifica si existe un arqueo activo asociado a una caja específica.
     * @param id ID de la caja.
     * @return True si existe un arqueo activo asociado a la caja, false en caso contrario.
     */
    boolean existsArqueoByEstadoArqueoTrueAndCaja_Id(Integer id);

    Optional<Arqueo> findArqueoByEstadoArqueoTrueAndCaja_Id(Integer id);

    /**
     * Busca todos los arqueos activos.
     * @return Lista de arqueos activos.
     */
    List<Arqueo> findArqueoByEstadoArqueoTrue();

    /**
     * Busca todos los arqueos activos asociados a un usuario específico.
     * @param id ID del usuario.
     * @return Lista de arqueos activos asociados al usuario.
     */
    List<Arqueo> findArqueoByUsuarioIdAndEstadoArqueoTrue(Integer id);

    @Query("SELECT COUNT(a) > 0 FROM Arqueo a WHERE a.estadoArqueo = true AND a.caja.impresora.id = :impresoraId")
    boolean existsByEstadoArqueoIsTrueAndCajaImpresoraId(Integer impresoraId);

}
