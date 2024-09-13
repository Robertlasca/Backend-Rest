package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.IngredienteProductoTerminado;
import com.residencia.restaurante.proyecto.dto.MenuDTO;
import com.residencia.restaurante.proyecto.dto.ProductoTerminadoDto;
import com.residencia.restaurante.proyecto.dto.RecetaDTO;
import com.residencia.restaurante.proyecto.entity.Menu;
import com.residencia.restaurante.proyecto.entity.ProductoTerminado;
import com.residencia.restaurante.proyecto.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/menu")
public interface IMenuController {

    @GetMapping(path = "/obtenerActivos")
    ResponseEntity<List<MenuDTO>> obtenerActivos();

    @GetMapping(path = "/obtenerNoActivos")
    ResponseEntity<List<MenuDTO>> obtenerNoActivos();

    @GetMapping(path = "/obtener")
    ResponseEntity<List<MenuDTO>> obtenerTodos();
    @GetMapping(path = "/obtenerPorCategoria/{id}")
    ResponseEntity<List<MenuDTO>> obtenerTodosPorCategoria(@PathVariable Integer id);

    @GetMapping(path = "/obtenerTotalMenu")
    ResponseEntity<Integer> obtenerTotalMenu();

    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestParam("nombre") String nombre,
                                   @RequestParam("descripcion") String descripcion,
                                   @RequestParam("costoProduccion") double costoProduccion,
                                   @RequestParam("margenGanancia") double margenGanancia,
                                   @RequestParam("precioVenta") double precioVenta,
                                   @RequestParam(value = "img",required = false) MultipartFile file,
                                   @RequestParam("idCategoria") int idCategoria,
                                   @RequestPart("receta")String receta
    );

    @PostMapping(path = "/agregarMenu")
    ResponseEntity<Menu> agregarMenu(@RequestParam("nombre") String nombre,
                                     @RequestParam("descripcion") String descripcion,
                                     @RequestParam("precioVenta") double precioVenta,
                                     @RequestParam(value = "img",required = false) MultipartFile file,
                                     @RequestParam("idCategoria") int idCategoria,
                                     @RequestParam("idCocina") int idCocina
    );

    @PostMapping(path = "/crearReceta")
    ResponseEntity<String> crearReceta(@RequestBody(required = true) Map<String, String> objetoMap);

    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(
            @RequestParam("id") Integer id,
            @RequestParam("nombre") String nombre,
                                   @RequestParam("descripcion") String descripcion,
                                   @RequestParam("margenGanancia") double margenGanancia,
                                   @RequestParam("precioVenta") double precioVenta,
                                   @RequestParam(value = "img",required = false) MultipartFile file,
                                   @RequestParam("idCategoria") int idCategoria,
            @RequestParam("idCocina") int idCocina
    );

    @GetMapping(path = "/obtenerMateriaPrima/{id}")
    ResponseEntity<IngredienteProductoTerminado> obtenerMateriaPrimaId(@PathVariable Integer id);

    @GetMapping(path = "/obtenerProductoTerminado/{id}")
    ResponseEntity<ProductoTerminado> obtenerProductoTerminado(@PathVariable Integer id);

    @GetMapping(path = "/obtenerReceta/{id}")
    ResponseEntity<List<RecetaDTO>> obtenerRecetaId(@PathVariable Integer id);
//Este método obtiene todos los ingredientes o productos terminados que esten relacionados con una cocina y que solo se preparan con sus recursos asignados.
    @GetMapping(path = "/obtenerIngredientes/{id}")
    ResponseEntity<List<RecetaDTO>> obtenerIngredientesOProductosTerminadosIdCocina(@PathVariable Integer id);
//Este método se obtiene
    @GetMapping(path = "/obtenerIngredienteOProducto")
    ResponseEntity<RecetaDTO> obtenerIngredientesOProductosTerminadosXId(@RequestBody(required = true) Map<String, String> objetoMap);

    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String, String> objetoMap);
    @GetMapping(path = "/obtenerIngredientesAgregar/{idMenu}")
    ResponseEntity<List<RecetaDTO>> obtenerRecetasUnicasPorCocinaYMenu(@PathVariable Integer idMenu);

}
