package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.AlmacenDTO;
import com.residencia.restaurante.proyecto.dto.MateriaPrimaDTO;
import com.residencia.restaurante.proyecto.entity.Almacen;
import com.residencia.restaurante.proyecto.entity.MateriaPrima;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Interfaz del controlador para la gestión de materias primas en el restaurante.
 * Proporciona endpoints para obtener información sobre materias primas activas e inactivas,
 * así como para la creación, actualización y cambio de estado de las materias primas.
 */
@RequestMapping("/materiaPrima")
public interface IMateriaPrimaController {
    /**
     * Obtiene una lista de todas las materias primas activas en el sistema.
     *
     * @return ResponseEntity con la lista de materias primas activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/materiasPrimasActivas")
    ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimasActivas();
    /**
     * Obtiene una lista de todas las materias primas que actualmente no están activas en el sistema.
     *
     * @return ResponseEntity con la lista de materias primas no activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/materiasPrimasNoActivas")
    ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimasNoActivas();

    /**
     * Obtiene una lista de todas las materias primas registradas en el sistema, independientemente de su estado.
     *
     * @return ResponseEntity con la lista completa de materias primas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerMateriasPrimas")
    ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimas();

    @GetMapping(path = "/obtenerMateriasPrimasXCategoria/{id}")
    ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimasIdCategoria(@PathVariable Integer id);


    /**
     * Cambia el estado de una materia prima específica en el sistema. La información necesaria
     * para identificar la materia prima y el nuevo estado se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cambiar el estado de la materia prima.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Agrega una nueva materia prima al sistema con los datos proporcionados.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para crear una nueva materia prima.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String, String> objetoMap);
    /**
     * Actualiza los datos de una materia prima existente en el sistema. La información para la actualización
     * se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para actualizar la materia prima.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String, String> objetoMap);
    @GetMapping(path = "/listarAlmacenesPorIdMateriaPrima/{id}")
    ResponseEntity<List<AlmacenDTO>> listarAlmacenesPorIdMateriaPrima(@PathVariable Integer id);

    @PostMapping(path = "/agregarInventario")
    ResponseEntity<String> agregarInventario(@RequestBody(required = true) Map<String,String> objetoMap);
    /**
     * Obtiene los detalles de una materia prima específica por su identificador.
     *
     * @param id El identificador único de la materia prima a consultar.
     * @return ResponseEntity con los detalles de la materia prima solicitada y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerMateriaPrima/{id}")
    ResponseEntity<MateriaPrimaDTO> obtenerMateriaPrimaId(@PathVariable Integer id);

    @PostMapping(path = "/agregarMateriaPrima")
    ResponseEntity<String> agregarMateria(@RequestParam("nombre") String nombre,
                                   @RequestParam("idCategoria") int idCategoria,
                                   @RequestParam("idUsuario") int idUsuario,
                                   @RequestParam("unidadMedida") String unidadMedida,
                                   @RequestParam("costoUnitario") double costoUnitario,
                                   @RequestPart("inventario")String inventario,
                                   @RequestParam(value = "img",required = false) MultipartFile file);

    @PostMapping(path = "/actualizarMateriaPrima")
    ResponseEntity<String> actualizarMateria(@RequestParam("id") int id,
            @RequestParam("nombre") String nombre,
                                          @RequestParam("idCategoria") int idCategoria,
                                          @RequestParam("costoUnitario") double costoUnitario,
                                          @RequestParam(value = "img",required = false) MultipartFile file);

    @GetMapping(path = "/totalMateriasPrimas")
    ResponseEntity<Integer> calcularTotalMateriasPrimas();

    @PostMapping(path = "/editarStockMM")
    ResponseEntity<String> editarStockMM(@RequestBody(required = true) Map<String,String> objetoMap);

    @PostMapping(path = "/eliminarInventario/{id}")
    ResponseEntity<String> eliminarInventario(@PathVariable(required = true)  Integer id);

}
