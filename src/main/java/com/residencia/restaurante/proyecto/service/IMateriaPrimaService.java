package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.AlmacenDTO;
import com.residencia.restaurante.proyecto.dto.MateriaPrimaDTO;
import com.residencia.restaurante.proyecto.entity.MateriaPrima;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de materias primas en el restaurante.
 */
public interface IMateriaPrimaService {

    /**
     * Obtiene todas las materias primas activas.
     * @return ResponseEntity con la lista de materias primas activas.
     */
    ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimasActivas();

    /**
     * Obtiene todas las materias primas no activas.
     * @return ResponseEntity con la lista de materias primas no activas.
     */
    ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimasNoActivas();

    /**
     * Obtiene todas las materias primas.
     * @return ResponseEntity con la lista de todas las materias primas.
     */
    ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimas();

    /**
     * Cambia el estado de una materia prima.
     * @param objetoMap Mapa que contiene la información necesaria para cambiar el estado.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> cambiarEstado(Map<String,String> objetoMap);

    /**
     * Agrega una nueva materia prima.
     * @param objetoMap Mapa que contiene la información necesaria para agregar la materia prima.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    /**
     * Actualiza una materia prima existente.
     * @param objetoMap Mapa que contiene la información necesaria para actualizar la materia prima.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    /**
     * Obtiene una materia prima por su ID.
     * @param id El ID de la materia prima.
     * @return ResponseEntity con la materia prima correspondiente al ID.
     */
    ResponseEntity<MateriaPrimaDTO> obtenerMateriaPrimaId(Integer id);

    ResponseEntity<String> agregarMateria(String nombre, int idCategoria, int idUsuario, String unidadMedida, double costoUnitario, String inventario, MultipartFile file);

    ResponseEntity<String> actualizarMateria(int id, String nombre, int idCategoria, double costoUnitario, MultipartFile file);

    ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimasIdCategoria(Integer id);

    ResponseEntity<Integer> calcularTotalMateriasPrimas();

    ResponseEntity<String> editarStockMM(Map<String, String> objetoMap);

    ResponseEntity<String> eliminarInventario(Integer id);

    ResponseEntity<List<AlmacenDTO>> listarAlmacenesPorIdMateriaPrima(Integer id);

    ResponseEntity<String> agregarInventario(Map<String, String> objetoMap);
}
