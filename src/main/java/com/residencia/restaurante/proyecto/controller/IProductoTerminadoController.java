package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.IngredienteProductoTerminado;
import com.residencia.restaurante.proyecto.dto.ProductoTerminadoDto;
import com.residencia.restaurante.proyecto.dto.RecetaDTO;
import com.residencia.restaurante.proyecto.entity.ProductoTerminado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/productoTerminado")
public interface IProductoTerminadoController {

    @GetMapping(path = "/obtenerActivos")
    ResponseEntity<List<ProductoTerminadoDto>> obtenerActivos();

    @GetMapping(path = "/obtenerNoActivos")
    ResponseEntity<List<ProductoTerminadoDto>> obtenerNoActivos();

    @GetMapping(path = "/obtener")
    ResponseEntity<List<ProductoTerminadoDto>> obtenerTodos();

    @GetMapping(path = "/obtenerPorCategoria/{id}")
    ResponseEntity<List<ProductoTerminadoDto>> obtenerTodosPorCategoria(Integer id);

    @GetMapping(path = "/obtenerTotal")
    ResponseEntity<Integer> obtenerTotalProductos();

    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestParam("nombre") String nombre,
                                   @RequestParam("unidadMedida") String unidadMedida,
                                   @RequestParam("descripcion") String descripcion,
                                   @RequestParam("stockMax") double stockMax,
                                   @RequestParam("stockMin") double stockMin,
                                   @RequestParam(value = "img",required = false) MultipartFile file,
                                          @RequestParam("idCategoria") int idCategoria,
                                          @RequestPart("materias")String materias
                                          );

    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(
            @RequestParam("id") Integer id,@RequestParam("nombre") String nombre,
                                   @RequestParam("descripcion") String descripcion,
                                   @RequestParam("stockMax") double stockMax,
                                   @RequestParam("stockMin") double stockMin,
                                   @RequestParam(value = "img",required = false) MultipartFile file,
                                   @RequestParam("idCategoria") int idCategoria
    );

    @GetMapping(path = "/obtenerMateriaPrima/{id}")
    ResponseEntity<IngredienteProductoTerminado> obtenerMateriaPrimaId(@PathVariable Integer id);

    @GetMapping(path = "/obtenerProductoTerminado/{id}")
    ResponseEntity<ProductoTerminado> obtenerProductoTerminado(@PathVariable Integer id);

    @GetMapping(path = "/obtenerReceta/{id}")
    ResponseEntity<List<RecetaDTO>> obtenerRecetaId(@PathVariable Integer id);

    @PostMapping(path = "/eliminar/{id}")
    ResponseEntity<String> eliminar(@PathVariable Integer id);

    @PostMapping(path = "/preparacionDiaria")
    ResponseEntity<String> preparacionDiaria(@RequestBody(required = true) Map<String, String> objetoMap);

    @GetMapping(path = "/validarStock")
    ResponseEntity<String> validarStock(@RequestBody(required = true) Map<String, String> objetoMap);

    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String, String> objetoMap);





}
