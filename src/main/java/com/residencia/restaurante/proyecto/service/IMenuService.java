package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.IngredienteProductoTerminado;
import com.residencia.restaurante.proyecto.dto.MenuDTO;
import com.residencia.restaurante.proyecto.dto.RecetaDTO;
import com.residencia.restaurante.proyecto.entity.Menu;
import com.residencia.restaurante.proyecto.entity.ProductoTerminado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IMenuService {
    ResponseEntity<List<MenuDTO>> obtenerActivos();

    ResponseEntity<List<MenuDTO>> obtenerNoActivos();

    ResponseEntity<List<MenuDTO>> obtenerTodos();


    ResponseEntity<String> agregar(String nombre,
                                   String descripcion,
                                   double costoProduccion,
                                   double margenGanancia,
                                   double precioVenta,
                                    MultipartFile file,
                                    int idCategoria,
                                   String receta
    );

    ResponseEntity<IngredienteProductoTerminado> obtenerMateriaPrimaId(@PathVariable Integer id);


    ResponseEntity<ProductoTerminado> obtenerProductoTerminado(@PathVariable Integer id);

    ResponseEntity<List<RecetaDTO>> obtenerRecetaId(@PathVariable Integer id);

    ResponseEntity<List<RecetaDTO>> obtenerIngredientesOProductosTerminadosIdCocina(@PathVariable Integer id);

    ResponseEntity<RecetaDTO> obtenerIngredientesOProductosTerminadosXId(Map<String, String> objetoMap);

    ResponseEntity<String> actualizar(Integer id, String nombre, String descripcion, double margenGanancia, double precioVenta, MultipartFile file, int idCategoria,int idCocina);

    ResponseEntity<Menu> agregarMenu(String nombre, String descripcion, double precioVenta, MultipartFile file, int idCategoria,int idCocina);

    ResponseEntity<String> crearReceta(Map<String, String> objetoMap);

    ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap);

    ResponseEntity<List<RecetaDTO>> obtenerRecetasUnicasPorCocinaYMenu(Integer idMenu);

    ResponseEntity<List<MenuDTO>> obtenerTodosPorCategoria(Integer id);

    ResponseEntity<Integer> obtenerTotalMenu();
}
