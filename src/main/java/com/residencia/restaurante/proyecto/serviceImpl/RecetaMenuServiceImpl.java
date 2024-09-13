package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.entity.*;
import com.residencia.restaurante.proyecto.repository.*;
import com.residencia.restaurante.proyecto.service.IMenuService;
import com.residencia.restaurante.proyecto.service.IRecetaMenuService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class RecetaMenuServiceImpl implements IRecetaMenuService {

    @Autowired
    private IMenuRepository menuRepository;
    @Autowired
    private IMateriaPrima_MenuRepository materiaPrimaMenuRepository;
    @Autowired
    private IProductoTerminado_MenuRepository productoTerminadoMenuRepository;

    @Autowired
    private IProductoTerminadoRepository productoTerminadoRepository;
    @Autowired
    private IMateriaPrimaRepository materiaPrimaRepository;
    @Autowired
            private IInventarioRepository inventarioRepository;

    @Autowired
    private IDetalleOrden_MenuRepository detalleOrdenMenuRepository;

@Autowired
        MenuServiceImpl menuServiceImpl;

    @Override
    public ResponseEntity<String> eliminarIngrediente(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("id") && objetoMap.containsKey("esIngrediente")){
                Integer id= Integer.parseInt(objetoMap.get("id"));
                if(objetoMap.get("esIngrediente").equalsIgnoreCase("true")){
                    Optional<MateriaPrima_Menu> materiaPrimaMenuOptional= materiaPrimaMenuRepository.findById(id);
                    if(materiaPrimaMenuOptional.isPresent()){
                        MateriaPrima_Menu materiaPrimaMenu=materiaPrimaMenuOptional.get();
                        Integer idMenu= materiaPrimaMenu.getMenu().getId();
                        if(detalleOrdenMenuRepository.existsByMenuIdAndEstadoNotIn(idMenu)){
                            return Utils.getResponseEntity("No puedes eliminar el ingrediente, ya que el platillo esta en preparación.",HttpStatus.BAD_REQUEST);
                        }


                        materiaPrimaMenuRepository.delete(materiaPrimaMenu);

                        Optional<Menu> optionalMenu=menuRepository.findById(idMenu);
                        if(optionalMenu.isPresent()){
                            Menu menu= optionalMenu.get();
                            menu.setCostoProduccionDirecto(menuServiceImpl.calcularCostoTotalMenu(menu.getId()));
                            menuRepository.save(menu);
                        }


                        return Utils.getResponseEntity("Ingrediente eliminado correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("No existe un registro.", HttpStatus.BAD_REQUEST);

                }else {
                    Optional<ProductoTerminado_Menu> productoTerminadoMenuOptional= productoTerminadoMenuRepository.findById(id);
                    if(productoTerminadoMenuOptional.isPresent()){
                        ProductoTerminado_Menu productoTerminadoMenu= productoTerminadoMenuOptional.get();
                        Menu menu=productoTerminadoMenu.getMenu();
                        productoTerminadoMenuRepository.delete(productoTerminadoMenu);
                        menu.setCostoProduccionDirecto(menuServiceImpl.calcularCostoTotalMenu(menu.getId()));
                        menuRepository.save(menu);
                        return Utils.getResponseEntity("Ingrediente eliminado correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("No existe un registro.", HttpStatus.BAD_REQUEST);

                }

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editarIngredienteReceta(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("id") && objetoMap.containsKey("esIngrediente") && objetoMap.containsKey("cantidad")){
                Integer id= Integer.parseInt(objetoMap.get("id"));
                if(objetoMap.get("esIngrediente").equalsIgnoreCase("true")){
                    Optional<MateriaPrima_Menu> materiaPrimaMenuOptional= materiaPrimaMenuRepository.findById(id);
                    if(materiaPrimaMenuOptional.isPresent()){
                        MateriaPrima_Menu materiaPrimaMenu=materiaPrimaMenuOptional.get();
                        Integer idMenu= materiaPrimaMenu.getMenu().getId();
                        if(detalleOrdenMenuRepository.existsByMenuIdAndEstadoNotIn(idMenu)){
                            return Utils.getResponseEntity("No puedes editar la cantidad el ingrediente, ya que el platillo esta en preparación.",HttpStatus.BAD_REQUEST);
                        }
                        materiaPrimaMenu.setCantidad(Double.parseDouble(objetoMap.get("cantidad")));
                        Menu menu= materiaPrimaMenu.getMenu();
                        materiaPrimaMenuRepository.save(materiaPrimaMenu);

                        menu.setCostoProduccionDirecto(menuServiceImpl.calcularCostoTotalMenu(menu.getId()));
                        menuRepository.save(menu);
                        return Utils.getResponseEntity("Receta editada correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("No existe un registro.", HttpStatus.BAD_REQUEST);

                }else {
                    Optional<ProductoTerminado_Menu> productoTerminadoMenuOptional= productoTerminadoMenuRepository.findById(id);
                    if(productoTerminadoMenuOptional.isPresent()){
                        ProductoTerminado_Menu productoTerminadoMenu= productoTerminadoMenuOptional.get();
                        productoTerminadoMenu.setCantidad(Double.parseDouble(objetoMap.get("cantidad")));
                        Menu menu=productoTerminadoMenu.getMenu();
                        productoTerminadoMenuRepository.save(productoTerminadoMenu);
                        menu.setCostoProduccionDirecto(menuServiceImpl.calcularCostoTotalMenu(menu.getId()));
                        menuRepository.save(menu);
                        return Utils.getResponseEntity("Receta editada  correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("No existe un registro.", HttpStatus.BAD_REQUEST);

                }

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregarIngredienteReceta(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("idMenu") && objetoMap.containsKey("idProducto") && objetoMap.containsKey("esIngrediente") && objetoMap.containsKey("cantidad")){
                Integer id= Integer.parseInt(objetoMap.get("idProducto"));
                Integer idMenu=Integer.parseInt(objetoMap.get("idMenu"));
                Optional<Menu> menuOptional=menuRepository.findById(idMenu);
                if(menuOptional.isPresent()){
                    Menu menu=menuOptional.get();


                if(objetoMap.get("esIngrediente").equalsIgnoreCase("true")){
                    Optional<Inventario> materiaPrimaMenuOptional= inventarioRepository.findById(id);
                    if(materiaPrimaMenuOptional.isPresent()){
                        Inventario materiaPrima= materiaPrimaMenuOptional.get();
                        MateriaPrima_Menu materiaPrimaMenu=new MateriaPrima_Menu();
                        Integer idMenua= materiaPrimaMenu.getMenu().getId();
                        if(detalleOrdenMenuRepository.existsByMenuIdAndEstadoNotIn(idMenua)){
                            return Utils.getResponseEntity("No puedes agregar el ingrediente, ya que el platillo esta en preparación.",HttpStatus.BAD_REQUEST);
                        }
                        materiaPrimaMenu.setMenu(menu);
                        materiaPrimaMenu.setInventario(materiaPrima);
                        materiaPrimaMenu.setCantidad(Double.parseDouble(objetoMap.get("cantidad")));

                        materiaPrimaMenuRepository.save(materiaPrimaMenu);

                        menu.setCostoProduccionDirecto(menuServiceImpl.calcularCostoTotalMenu(menu.getId()));
                        menuRepository.save(menu);
                        return Utils.getResponseEntity("Ingrediente agregado a la receta correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("No existe un registro.", HttpStatus.BAD_REQUEST);

                }else {
                    Optional<ProductoTerminado> productoTerminadoMenuOptional= productoTerminadoRepository.findById(id);
                    if(productoTerminadoMenuOptional.isPresent()){
                        ProductoTerminado_Menu productoTerminadoMenu= new ProductoTerminado_Menu();
                        ProductoTerminado productoTerminado= productoTerminadoMenuOptional.get();
                        productoTerminadoMenu.setMenu(menu);
                        productoTerminadoMenu.setProductoTerminado(productoTerminado);
                        productoTerminadoMenu.setCantidad(Double.parseDouble(objetoMap.get("cantidad")));

                        productoTerminadoMenuRepository.save(productoTerminadoMenu);
                        menu.setCostoProduccionDirecto(menuServiceImpl.calcularCostoTotalMenu(menu.getId()));
                        menuRepository.save(menu);
                        return Utils.getResponseEntity("Ingrediente agregado a la receta correctamente  correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("No existe un registro.", HttpStatus.BAD_REQUEST);

                }
            }

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
