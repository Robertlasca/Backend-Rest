package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.entity.ArqueoSaldos;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Interfaz para el servicio de gestión de saldos de arqueo.
 */
public interface IArqueoSaldosService {

    /**
     * Obtiene los saldos de arqueo asociados a un arqueo específico.
     * @param id El ID del arqueo del que se desean obtener los saldos.
     * @return ResponseEntity con la lista de saldos de arqueo obtenidos.
     */
    ResponseEntity<List<ArqueoSaldos>> obtenerArqueoSaldosId(Integer id);
}
