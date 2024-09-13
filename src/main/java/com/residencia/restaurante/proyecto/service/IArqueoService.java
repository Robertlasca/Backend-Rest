package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.entity.Arqueo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de arqueos en el restaurante.
 */
public interface IArqueoService {

    /**
     * Abre un nuevo arqueo.
     * @param objetoMap Mapa que contiene la información necesaria para abrir el arqueo.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> abrirArqueo(@RequestBody(required = true) Map<String,String> objetoMap);

    /**
     * Cierra un arqueo existente.
     * @param objetoMap Mapa que contiene la información necesaria para cerrar el arqueo.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> cerrarArqueo(@RequestBody(required = true) Map<String,String> objetoMap);

    /**
     * Obtiene todos los arqueos registrados.
     * @return ResponseEntity con la lista de arqueos.
     */
    ResponseEntity<List<Arqueo>> obtenerArqueos();

    /**
     * Obtiene todos los arqueos activos.
     * @return ResponseEntity con la lista de arqueos activos.
     */
    ResponseEntity<List<Arqueo>> obtenerArqueosActivos();

    /**
     * Obtiene los arqueos realizados por un empleado específico.
     * @param id El ID del empleado.
     * @return ResponseEntity con la lista de arqueos realizados por el empleado.
     */
    ResponseEntity<List<Arqueo>> obtenerArqueoXEmpleado(@PathVariable Integer id);

    /**
     * Obtiene un arqueo específico por su ID.
     * @param id El ID del arqueo.
     * @return ResponseEntity con el arqueo obtenido.
     */
    public ResponseEntity<Arqueo> obtenerArqueoId(Integer id);
}
