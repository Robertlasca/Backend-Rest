package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.entity.ArqueoSaldos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
/**
 * Interfaz del controlador para la gestión de saldos en los arqueos.
 * Define endpoints para consultar los saldos asociados a un arqueo específico.
 */
@RequestMapping(path = "/arqueosSaldos")
public interface IArqueoSaldosController {
    /**
     * Obtiene una lista de todos los saldos relacionados con un arqueo específico, identificado por su ID.
     *
     * @param id El identificador del arqueo para el cual se buscan los saldos.
     * @return ResponseEntity que contiene la lista de los saldos asociados al arqueo y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerArqueoSaldos/{id}")
    public ResponseEntity<List<ArqueoSaldos>> obtenerArqueoSaldosId(@PathVariable Integer id);


}
