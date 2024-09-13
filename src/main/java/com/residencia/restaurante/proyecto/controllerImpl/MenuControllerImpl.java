package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IMenuController;
import com.residencia.restaurante.proyecto.dto.IngredienteProductoTerminado;
import com.residencia.restaurante.proyecto.dto.MenuDTO;
import com.residencia.restaurante.proyecto.dto.RecetaDTO;
import com.residencia.restaurante.proyecto.entity.Menu;
import com.residencia.restaurante.proyecto.entity.ProductoTerminado;
import com.residencia.restaurante.proyecto.service.IMenuService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MenuControllerImpl implements IMenuController {
    @Autowired
    private IMenuService menuService;
    @Override
    public ResponseEntity<List<MenuDTO>> obtenerActivos() {
        try {
            return menuService.obtenerActivos();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MenuDTO>> obtenerNoActivos() {
        try {
            return menuService.obtenerNoActivos();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MenuDTO>> obtenerTodos() {
        try {
            return menuService.obtenerTodos();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MenuDTO>> obtenerTodosPorCategoria(Integer id) {
        try {
            return menuService.obtenerTodosPorCategoria(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Integer> obtenerTotalMenu() {
        try {
            return menuService.obtenerTotalMenu();

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(0,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregar(String nombre, String descripcion, double costoProduccion, double margenGanancia, double precioVenta, MultipartFile file, int idCategoria, String receta) {
        try {
            return menuService.agregar(nombre,descripcion,costoProduccion,margenGanancia,precioVenta,file,idCategoria,receta);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Menu> agregarMenu(String nombre, String descripcion, double precioVenta, MultipartFile file, int idCategoria,int idCocina) {
        try {
            return menuService.agregarMenu(nombre,descripcion,precioVenta,file,idCategoria,idCocina);

        }catch (Exception e){
         e.printStackTrace();
        }
        return new ResponseEntity<Menu>(new Menu(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> crearReceta(Map<String, String> objetoMap) {
        try {
            return menuService.crearReceta(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizar(Integer id, String nombre, String descripcion, double margenGanancia, double precioVenta, MultipartFile file, int idCategoria,int idCocina) {
        try {
            return menuService.actualizar(id,nombre,descripcion,margenGanancia,precioVenta,file,idCategoria,idCocina);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<IngredienteProductoTerminado> obtenerMateriaPrimaId(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<ProductoTerminado> obtenerProductoTerminado(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<RecetaDTO>> obtenerRecetaId(Integer id) {
        try {
            return menuService.obtenerRecetaId(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<RecetaDTO>> obtenerIngredientesOProductosTerminadosIdCocina(Integer id) {
        try {
            return menuService.obtenerIngredientesOProductosTerminadosIdCocina(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<RecetaDTO> obtenerIngredientesOProductosTerminadosXId(Map<String, String> objetoMap) {
        try {
            return menuService.obtenerIngredientesOProductosTerminadosXId(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new RecetaDTO(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return menuService.cambiarEstado(objetoMap);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<RecetaDTO>> obtenerRecetasUnicasPorCocinaYMenu(Integer idMenu) {
        try {
            return menuService.obtenerRecetasUnicasPorCocinaYMenu(idMenu);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
