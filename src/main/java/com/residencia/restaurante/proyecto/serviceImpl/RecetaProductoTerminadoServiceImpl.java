package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.entity.*;
import com.residencia.restaurante.proyecto.repository.*;
import com.residencia.restaurante.proyecto.service.IRecetaProductoTerminadoService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecetaProductoTerminadoServiceImpl implements IRecetaProductoTerminadoService {
    @Autowired
    IMateriaPrima_ProductoTerminadoRepository materiaPrimaProductoTerminadoRepository;

    @Autowired
    IProductoTerminado_MenuRepository productoTerminadoMenuRepository;
    @Autowired
    IProductoTerminadoRepository productoTerminadoRepository;

    @Autowired
    IMenuRepository menuRepository;

    @Autowired
            private IMateriaPrimaRepository materiaPrimaRepository;
    @Autowired
            private IInventarioRepository inventarioRepository;

    @Autowired
            private IDetalleOrden_MenuRepository detalleOrdenMenuRepository;

    MenuServiceImpl menuServiceImp;
    @Override
    public ResponseEntity<String> eliminarIngrediente(Integer id) {
        try {
            Optional<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminadoOptional= materiaPrimaProductoTerminadoRepository.findById(id);
            if(materiaPrimaProductoTerminadoOptional.isPresent()){

                MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado=materiaPrimaProductoTerminadoOptional.get();
                Integer idProducto= materiaPrimaProductoTerminado.getProductoTerminado().getId();
                Optional<ProductoTerminado> productoTerminadoOptional= productoTerminadoRepository.findById(idProducto);

                if(productoTerminadoOptional.isPresent()){
                    ProductoTerminado productoTerminado= productoTerminadoOptional.get();
                    List<ProductoTerminado_Menu> productoTerminadoMenusa= productoTerminadoMenuRepository.getAllByProductoTerminado(productoTerminado);

                    if(!productoTerminadoMenusa.isEmpty()){
                        for (ProductoTerminado_Menu productoMenu:productoTerminadoMenusa) {
                            Menu menu= productoMenu.getMenu();
                            if(detalleOrdenMenuRepository.existsByMenuIdAndEstadoNotIn(menu.getId())){
                                return Utils.getResponseEntity("No puedes eliminar el ingrediente, ya que el platillo al que pertenece el producto terminado esta en preparación.",HttpStatus.BAD_REQUEST);
                            }

                        }

                    }



                    materiaPrimaProductoTerminadoRepository.delete(materiaPrimaProductoTerminado);
                    List<ProductoTerminado_Menu> productoTerminadoMenus= productoTerminadoMenuRepository.getAllByProductoTerminado(productoTerminado);

                    if(!productoTerminadoMenus.isEmpty()){
                        for (ProductoTerminado_Menu productoMenu:productoTerminadoMenus) {
                            Menu menu= productoMenu.getMenu();
                            menu.setCostoProduccionDirecto(menuServiceImp.calcularCostoTotalMenu(menu.getId()));
                            menuRepository.save(menu);
                        }

                    }
                    return Utils.getResponseEntity("Ingrediente eliminado correctamente.",HttpStatus.OK);
                }

            }
            return Utils.getResponseEntity("No existe el registro.",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editarIngredienteReceta(Map<String, String> objetoMap) {
        try {

            if(objetoMap.containsKey("id") && objetoMap.containsKey("cantidad")){
                Integer id= Integer.parseInt(objetoMap.get("id"));
                Optional<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminadoOptional= materiaPrimaProductoTerminadoRepository.findById(id);
                if(materiaPrimaProductoTerminadoOptional.isPresent()){
                    MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado= materiaPrimaProductoTerminadoOptional.get();

                    List<ProductoTerminado_Menu> productoTerminadoMenusa= productoTerminadoMenuRepository.getAllByProductoTerminado(materiaPrimaProductoTerminado.getProductoTerminado());

                    if(!productoTerminadoMenusa.isEmpty()){
                        for (ProductoTerminado_Menu productoMenu:productoTerminadoMenusa) {
                            Menu menu= productoMenu.getMenu();
                            if(detalleOrdenMenuRepository.existsByMenuIdAndEstadoNotIn(menu.getId())){
                                return Utils.getResponseEntity("No puedes editar la cantidad del ingrediente, ya que el platillo al que pertenece el producto terminado esta en preparación.",HttpStatus.BAD_REQUEST);
                            }

                        }

                    }

                    materiaPrimaProductoTerminado.setCantidad(Double.parseDouble(objetoMap.get("cantidad")));
                    materiaPrimaProductoTerminadoRepository.save(materiaPrimaProductoTerminado);

                    Integer idProducto= materiaPrimaProductoTerminado.getProductoTerminado().getId();
                    Optional<ProductoTerminado> productoTerminadoOptional= productoTerminadoRepository.findById(idProducto);

                    if(productoTerminadoOptional.isPresent()){

                        ProductoTerminado productoTerminado= productoTerminadoOptional.get();
                        List<ProductoTerminado_Menu> productoTerminadoMenus= productoTerminadoMenuRepository.getAllByProductoTerminado(productoTerminado);

                        if(!productoTerminadoMenus.isEmpty()){
                            for (ProductoTerminado_Menu productoMenu:productoTerminadoMenus) {
                                Menu menu= productoMenu.getMenu();
                                menu.setCostoProduccionDirecto(menuServiceImp.calcularCostoTotalMenu(menu.getId()));
                                menuRepository.save(menu);
                            }
                            return Utils.getResponseEntity("Receta actualizada correctamente.",HttpStatus.OK);

                        }
                        return Utils.getResponseEntity("Receta actualizada correctamente.",HttpStatus.OK);
                    }

                    return Utils.getResponseEntity("Receta actualizada correctamente.",HttpStatus.OK);

                }
                return Utils.getResponseEntity("No existe el registro.",HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregarIngredienteReceta(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("idMateria") && objetoMap.containsKey("idProducto") && objetoMap.containsKey("cantidad")){
                Optional<Inventario> materiaPrimaOptional= inventarioRepository.findById(Integer.parseInt(objetoMap.get("idMateria")));
                Optional<ProductoTerminado> productoTerminadoOptional=productoTerminadoRepository.findById(Integer.parseInt(objetoMap.get("idProducto")));
                if(materiaPrimaOptional.isPresent() && productoTerminadoOptional.isPresent()){
                    MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado= new MateriaPrima_ProductoTerminado();

                    List<ProductoTerminado_Menu> productoTerminadoMenusa= productoTerminadoMenuRepository.getAllByProductoTerminado(materiaPrimaProductoTerminado.getProductoTerminado());

                    if(!productoTerminadoMenusa.isEmpty()){
                        for (ProductoTerminado_Menu productoMenu:productoTerminadoMenusa) {
                            Menu menu= productoMenu.getMenu();
                            if(detalleOrdenMenuRepository.existsByMenuIdAndEstadoNotIn(menu.getId())){
                                return Utils.getResponseEntity("No puedes agregar el ingrediente, ya que el platillo al que pertenece el producto terminado esta en preparación.",HttpStatus.BAD_REQUEST);
                            }

                        }

                    }
                    materiaPrimaProductoTerminado.setInventario(materiaPrimaOptional.get());
                    materiaPrimaProductoTerminado.setProductoTerminado(productoTerminadoOptional.get());
                    materiaPrimaProductoTerminado.setCantidad(Double.parseDouble(objetoMap.get("cantidad")));
                    materiaPrimaProductoTerminadoRepository.save(materiaPrimaProductoTerminado);

                    ProductoTerminado productoTerminado= productoTerminadoOptional.get();
                    List<ProductoTerminado_Menu> productoTerminadoMenus= productoTerminadoMenuRepository.getAllByProductoTerminado(productoTerminado);

                    if(!productoTerminadoMenus.isEmpty()){
                        for (ProductoTerminado_Menu productoMenu:productoTerminadoMenus) {
                            Menu menu= productoMenu.getMenu();
                            menu.setCostoProduccionDirecto(menuServiceImp.calcularCostoTotalMenu(menu.getId()));
                            menuRepository.save(menu);
                        }
                        return Utils.getResponseEntity("Ingrediente agregado correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("Ingrediente agregado correctamente.",HttpStatus.OK);


                }
                return Utils.getResponseEntity("No existen los registros.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Inventario>> obtenerMateriasXCProducto(Integer id) {
        try {
            Optional<ProductoTerminado> productoTerminadoOptional = productoTerminadoRepository.findById(id);
            List<Inventario> inventarioList = new ArrayList<>();
            if (productoTerminadoOptional.isPresent()) {
                List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados = materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminadoOptional.get());
                if (!materiaPrimaProductoTerminados.isEmpty()) {
                    Set<Long> inventarioIdsEnUso = new HashSet<>();
                    for (MateriaPrima_ProductoTerminado mppt : materiaPrimaProductoTerminados) {
                        inventarioIdsEnUso.add(Long.valueOf(mppt.getInventario().getId()));
                    }

                    Inventario inventario = materiaPrimaProductoTerminados.get(0).getInventario();
                    List<Inventario> inventarios = inventarioRepository.getAllByAlmacen_Id(inventario.getAlmacen().getId());
                    for (Inventario inv : inventarios) {
                        if (!inventarioIdsEnUso.contains(inv.getId())) {

                            inventarioList.add(inv);
                        }
                    }
                }
                return new ResponseEntity<List<Inventario>>(inventarioList,HttpStatus.OK);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Inventario>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
