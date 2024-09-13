package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.MovimientoInventarioDTO;
import com.residencia.restaurante.proyecto.entity.Inventario;
import com.residencia.restaurante.proyecto.entity.MateriaPrima;
import com.residencia.restaurante.proyecto.dto.InventarioDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de inventario de materias primas en el restaurante.
 */
public interface IInventarioService {

    /**
     * Lista las materias primas por su estado de stock.
     * @return ResponseEntity con la lista de inventario de materias primas.
     */
    ResponseEntity<List<InventarioDTO>> listarPorStock();

    /**
     * Agrega una nueva entrada al inventario.
     * @param objetoMap Mapa que contiene la información necesaria para agregar la entrada.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    /**
     * Realiza un descuento en el inventario de una materia prima.
     * @param objetoMap Mapa que contiene la información necesaria para realizar el descuento.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> descontar(Map<String,String> objetoMap);

    /**
     * Transfiere una materia prima entre almacenes.
     * @param objetoMap Mapa que contiene la información necesaria para realizar la transferencia.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> transferir(Map<String,String> objetoMap);

    /**
     * Lista las materias primas por almacenes.
     * @param objetoMap Mapa que contiene la información necesaria para filtrar las materias primas por almacenes.
     * @return ResponseEntity con la lista de materias primas por almacenes.
     */
    ResponseEntity<List<MateriaPrima>> listarPorAlmacenes(Map<String,String> objetoMap);

    /**
     * Lista las materias primas por un almacén específico.
     * @param id El ID del almacén.
     * @return ResponseEntity con la lista de materias primas del almacén.
     */
    ResponseEntity<List<MateriaPrima>> listarPorAlmacen(Integer id);

    /**
     * Obtiene los datos para realizar una transferencia.
     * @param objetoMap Mapa que contiene la información necesaria para obtener los datos de transferencia.
     * @return ResponseEntity con los datos de transferencia.
     */
    ResponseEntity<Map<String,String>> obtenerDatosTransferencia(Map<String,String> objetoMap);

    /**
     * Obtiene los datos necesarios para realizar una operación en el inventario.
     * @param objetoMap Mapa que contiene la información necesaria para obtener los datos del inventario.
     * @return ResponseEntity con los datos del inventario.
     */
    ResponseEntity<Map<String,String>> obtenerDatos(Map<String,String> objetoMap);

    ResponseEntity<List<Inventario>> obtenerMateriasXCocinaID(Integer id);

    ResponseEntity<List<MovimientoInventarioDTO>> listarMovimientos();
}
