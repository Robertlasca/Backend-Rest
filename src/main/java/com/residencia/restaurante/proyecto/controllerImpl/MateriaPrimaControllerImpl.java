package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IMateriaPrimaController;
import com.residencia.restaurante.proyecto.dto.AlmacenDTO;
import com.residencia.restaurante.proyecto.dto.MateriaPrimaDTO;
import com.residencia.restaurante.proyecto.entity.Almacen;
import com.residencia.restaurante.proyecto.entity.MateriaPrima;
import com.residencia.restaurante.proyecto.repository.IInventarioRepository;
import com.residencia.restaurante.proyecto.service.IMateriaPrimaService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Implementación del controlador para la gestión de materias primas en el restaurante.
 */
@RestController
public class MateriaPrimaControllerImpl implements IMateriaPrimaController {

    @Autowired
    IMateriaPrimaService materiaPrimaService;
    @Autowired
    IInventarioRepository inventarioRepository;
    /**
     * Obtiene la lista de materias primas activas.
     *
     * @return ResponseEntity con la lista de materias primas activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimasActivas() {
        try {
            return materiaPrimaService.obtenerMateriasPrimasActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene la lista de materias primas no activas.
     *
     * @return ResponseEntity con la lista de materias primas no activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimasNoActivas() {
        try {
            return materiaPrimaService.obtenerMateriasPrimasNoActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene la lista completa de materias primas.
     *
     * @return ResponseEntity con la lista completa de materias primas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimas() {
        try {
            return materiaPrimaService.obtenerMateriasPrimas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MateriaPrimaDTO>> obtenerMateriasPrimasIdCategoria(Integer id) {
        try {
            return materiaPrimaService.obtenerMateriasPrimasIdCategoria(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cambia el estado de una materia prima.
     *
     * @param objetoMap Mapa que contiene los datos necesarios para cambiar el estado de la materia prima.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return materiaPrimaService.cambiarEstado(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega una nueva materia prima.
     *
     * @param objetoMap Mapa que contiene los datos de la nueva materia prima.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return materiaPrimaService.agregar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Actualiza una materia prima existente.
     *
     * @param objetoMap Mapa que contiene los datos actualizados de la materia prima.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return materiaPrimaService.actualizar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<AlmacenDTO>> listarAlmacenesPorIdMateriaPrima(Integer id) {
        try {
            return materiaPrimaService.listarAlmacenesPorIdMateriaPrima(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<AlmacenDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregarInventario(Map<String, String> objetoMap) {
        try {
            return materiaPrimaService.agregarInventario(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Obtiene una materia prima por su ID.
     *
     * @param id El ID único de la materia prima.
     * @return ResponseEntity con la materia prima solicitada y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<MateriaPrimaDTO> obtenerMateriaPrimaId(Integer id) {
        try {
            return materiaPrimaService.obtenerMateriaPrimaId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new MateriaPrimaDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregarMateria(String nombre, int idCategoria, int idUsuario, String unidadMedida, double costoUnitario, String inventario, MultipartFile file) {
        try {
            return materiaPrimaService.agregarMateria(nombre,idCategoria,idUsuario,unidadMedida,costoUnitario,inventario,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizarMateria(int id, String nombre, int idCategoria, double costoUnitario, MultipartFile file) {
        try {
            return materiaPrimaService.actualizarMateria(id,nombre,idCategoria,costoUnitario,file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Integer> calcularTotalMateriasPrimas() {
        try {
            return materiaPrimaService.calcularTotalMateriasPrimas();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(0,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editarStockMM(Map<String, String> objetoMap) {
        try {
            return materiaPrimaService.editarStockMM(objetoMap);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> eliminarInventario(Integer id) {
        try {
            return materiaPrimaService.eliminarInventario(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
