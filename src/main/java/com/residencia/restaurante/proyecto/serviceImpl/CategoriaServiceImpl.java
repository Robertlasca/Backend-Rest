package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.AlmacenDTO;
import com.residencia.restaurante.proyecto.dto.CategoriaDTO;
import com.residencia.restaurante.proyecto.entity.*;
import com.residencia.restaurante.proyecto.repository.ICategoriaRepository;
import com.residencia.restaurante.proyecto.repository.IMenuRepository;
import com.residencia.restaurante.proyecto.repository.IProductoNormalRepository;
import com.residencia.restaurante.proyecto.repository.IProductoTerminadoRepository;
import com.residencia.restaurante.proyecto.service.ICategoriaService;
import com.residencia.restaurante.security.JWT.JwtFilter;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements ICategoriaService {
    @Autowired
    ICategoriaRepository categoriaRepository;

    @Autowired
    IMenuRepository menuRepository;
    @Autowired
    IProductoNormalRepository productoNormalRepository;

    @Autowired
    IProductoTerminadoRepository productoTerminadoRepository;

    @Autowired
    JwtFilter jfilter;
    /**
     * Obtiene una lista de todas las categorías activas.
     * @return Lista de categorías activas con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriasActivas() {
        try {
            if(jfilter.esAdministrador() && jfilter.esComandero()) {
                List<CategoriaDTO> categoriaConEstado = new ArrayList<>();
                for (Categoria categoria : categoriaRepository.getAllByVisibilidadTrue()) {
                    CategoriaDTO categoriaDTO = new CategoriaDTO();
                    categoriaDTO.setCategoria(categoria);
                    categoriaDTO.setEstado("Visible");

                    categoriaConEstado.add(categoriaDTO);
                }
                return new ResponseEntity<List<CategoriaDTO>>(categoriaConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene una lista de todas las categorías no activas.
     * @return Lista de categorías no activas con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriasNoActivas() {
        try {
            if(jfilter.esAdministrador() && jfilter.esComandero()) {
                List<CategoriaDTO> categoriaConEstado = new ArrayList<>();
                for (Categoria categoria : categoriaRepository.getAllByVisibilidadFalse()) {
                    CategoriaDTO categoriaDTO = new CategoriaDTO();
                    categoriaDTO.setCategoria(categoria);

                    categoriaDTO.setEstado("No visible");


                    categoriaConEstado.add(categoriaDTO);
                }
                return new ResponseEntity<List<CategoriaDTO>>(categoriaConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene una lista de todas las categorías registradas.
     * @return Lista de categorías con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategorias() {
        try {
            if(jfilter.esAdministrador() && jfilter.esComandero()) {
                List<CategoriaDTO> categoriaConEstado = new ArrayList<>();
                for (Categoria categoria : categoriaRepository.findAll()) {
                    CategoriaDTO categoriaDTO = new CategoriaDTO();
                    categoriaDTO.setCategoria(categoria);
                    if (categoria.isVisibilidad()) {
                        categoriaDTO.setEstado("Visible");
                    } else {
                        categoriaDTO.setEstado("No visible");
                    }

                    categoriaConEstado.add(categoriaDTO);
                }
                return new ResponseEntity<List<CategoriaDTO>>(categoriaConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Cambia el estado de una categoría (activo/inactivo) según el ID proporcionado en el mapa de datos.
     * @param objetoMap Mapa de datos que contiene el ID de la categoría y el nuevo estado.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()) {
                if (objetoMap.containsKey("id") && objetoMap.containsKey("visibilidad")) {
                    Optional<Categoria> categoriaOptional = categoriaRepository.findById(Integer.parseInt(objetoMap.get("id")));
                    if (!categoriaOptional.isEmpty()) {
                        Categoria categoria = categoriaOptional.get();
                        categoria.setNombre(categoria.getNombre());
                        categoria.setPertenece(categoria.getPertenece());
                        if (objetoMap.get("visibilidad").equalsIgnoreCase("false")) {
                            categoria.setVisibilidad(false);
                            List<Menu> menuList = menuRepository.getAllByCategoriaId(categoria.getId());
                            List<ProductoNormal> productoNormalList = productoNormalRepository.getAllByCategoriaId(categoria.getId());
                            List<ProductoTerminado> productoTerminadoList = productoTerminadoRepository.getAllByCategoriaId(categoria.getId());
                            Optional<Categoria> categoriaOptional1 = categoriaRepository.findCategoriaByNombreLikeIgnoreCase("Sin categoría.");

                            if (categoriaOptional1.isPresent()) {
                                Categoria categoria1 = categoriaOptional1.get();
                                if (!menuList.isEmpty()) {
                                    for (Menu menu : menuList) {
                                        menu.setCategoria(categoria1);
                                        menuRepository.save(menu);
                                    }
                                }

                                if (!productoNormalList.isEmpty()) {
                                    for (ProductoNormal productoNormal : productoNormalList) {
                                        productoNormal.setCategoria(categoria1);
                                        productoNormalRepository.save(productoNormal);
                                    }

                                }

                                if (!productoTerminadoList.isEmpty()) {
                                    for (ProductoTerminado productoTerminado : productoTerminadoList) {
                                        productoTerminado.setCategoria(categoria1);
                                        productoTerminadoRepository.save(productoTerminado);
                                    }

                                }

                            }

                        } else {
                            categoria.setVisibilidad(true);
                        }

                        categoriaRepository.save(categoria);

                        return Utils.getResponseEntity("El estado de la categoría ha sido cambiado.", HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("La categoría no existe.", HttpStatus.BAD_REQUEST);

                }

                return Utils.getResponseEntity(Constantes.INVALID_DATA, HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Agrega una nueva categoría utilizando los datos proporcionados en el mapa.
     * @param objetoMap Mapa de datos que contiene la información de la nueva categoría.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()) {
                if (validarCategoriaMap(objetoMap, false)) {
                    if (!categoriaRepository.existsCategoriaByNombreLikeIgnoreCaseAndPerteneceLikeIgnoreCase(objetoMap.get("nombre"), objetoMap.get("pertenece"))) {
                        categoriaRepository.save(obtenerCategoriaDesdeMap(objetoMap, false));
                        return Utils.getResponseEntity("Categoría guardada exitosamente.", HttpStatus.OK);
                    }
                    return Utils.getResponseEntity("Esta categoría ya existe.", HttpStatus.BAD_REQUEST);

                }
                return Utils.getResponseEntity(Constantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Actualiza los datos de una categoría existente utilizando los datos proporcionados en el mapa.
     * @param objetoMap Mapa de datos que contiene la información actualizada de la categoría.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()) {
                if (validarCategoriaMap(objetoMap, true)) {
                    Optional<Categoria> optional = categoriaRepository.findById(Integer.parseInt(objetoMap.get("id")));
                    if (!optional.isEmpty()) {
                        if (optional.get().getNombre().equalsIgnoreCase(objetoMap.get("nombre"))) {
                            categoriaRepository.save(obtenerCategoriaDesdeMap(objetoMap, true));
                            return Utils.getResponseEntity("Categoría actualizada", HttpStatus.OK);
                        } else {
                            if (!categoriaExistente(objetoMap)) {
                                categoriaRepository.save(obtenerCategoriaDesdeMap(objetoMap, true));
                                return Utils.getResponseEntity("Categoría actualizada", HttpStatus.OK);
                            }
                            return Utils.getResponseEntity("No puedes asignarle este nombre por que ya esta registrado.", HttpStatus.BAD_REQUEST);
                        }

                    }
                    return Utils.getResponseEntity("No existe la Categoría.", HttpStatus.BAD_REQUEST);
                }

                return Utils.getResponseEntity(Constantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene los datos de una categoría específica según su ID.
     * @param id ID de la categoría.
     * @return Datos de la categoría solicitada con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Categoria> obtenerCategoriasId(Integer id) {
        try {

            if (jfilter.esAdministrador()){
                Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);

            if (categoriaOptional.isPresent()) {
                Categoria categoria = categoriaOptional.get();
                return new ResponseEntity<>(categoria, HttpStatus.OK);
            }
            return new ResponseEntity<>(new Categoria(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Categoria>(new Categoria(),HttpStatus.UNAUTHORIZED);


        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new Categoria(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriasMenu() {
        try {
            if (jfilter.esAdministrador() || jfilter.esCocinero() || jfilter.esComandero()){
            List<CategoriaDTO> categoriaConEstado = new ArrayList<>();
            for (Categoria categoria : categoriaRepository.getAllByPerteneceEqualsIgnoreCase("M")) {
                CategoriaDTO categoriaDTO= new CategoriaDTO();
                int cantidadTerminado= Math.toIntExact(productoTerminadoRepository.countByCategoriaId(categoria.getId()));
                int cantidadNormal= Math.toIntExact(productoNormalRepository.countByCategoriaId(categoria.getId()));
                int cantidadMenu= Math.toIntExact(menuRepository.countByCategoriaId(categoria.getId()));
                categoriaDTO.setCantidadProductos(cantidadTerminado+cantidadNormal+cantidadMenu);
                categoriaDTO.setCategoria(categoria);
                if(categoria.isVisibilidad()){
                    categoriaDTO.setEstado("Visible");
                }else {
                    categoriaDTO.setEstado("No visible");
                }


                categoriaConEstado.add(categoriaDTO);
            }
            return new ResponseEntity<List<CategoriaDTO>>(categoriaConEstado, HttpStatus.OK);

            }
            return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriasProductoTerminado() {
        try {
            if (jfilter.esAdministrador() || jfilter.esCocinero() || jfilter.esComandero()) {
                List<CategoriaDTO> categoriaConEstado = new ArrayList<>();
                for (Categoria categoria : categoriaRepository.getAllByPerteneceEqualsIgnoreCase("T")) {
                    CategoriaDTO categoriaDTO = new CategoriaDTO();
                    int cantidadTerminado = Math.toIntExact(productoTerminadoRepository.countByCategoriaId(categoria.getId()));
                    int cantidadNormal = Math.toIntExact(productoNormalRepository.countByCategoriaId(categoria.getId()));
                    int cantidadMenu = Math.toIntExact(menuRepository.countByCategoriaId(categoria.getId()));
                    categoriaDTO.setCantidadProductos(cantidadTerminado + cantidadNormal + cantidadMenu);
                    categoriaDTO.setCategoria(categoria);
                    if (categoria.isVisibilidad()) {
                        categoriaDTO.setEstado("Visible");
                    } else {
                        categoriaDTO.setEstado("No visible");
                    }

                    categoriaConEstado.add(categoriaDTO);
                }
                return new ResponseEntity<List<CategoriaDTO>>(categoriaConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriasProductoNormal() {
        try {
            if (jfilter.esAdministrador() || jfilter.esCocinero() || jfilter.esComandero()) {
                List<CategoriaDTO> categoriaConEstado = new ArrayList<>();
                for (Categoria categoria : categoriaRepository.getAllByPerteneceEqualsIgnoreCase("N")) {
                    CategoriaDTO categoriaDTO = new CategoriaDTO();
                    int cantidadTerminado = Math.toIntExact(productoTerminadoRepository.countByCategoriaId(categoria.getId()));
                    int cantidadNormal = Math.toIntExact(productoNormalRepository.countByCategoriaId(categoria.getId()));
                    int cantidadMenu = Math.toIntExact(menuRepository.countByCategoriaId(categoria.getId()));
                    categoriaDTO.setCantidadProductos(cantidadTerminado + cantidadNormal + cantidadMenu);
                    categoriaDTO.setCategoria(categoria);
                    if (categoria.isVisibilidad()) {
                        categoriaDTO.setEstado("Visible");
                    } else {
                        categoriaDTO.setEstado("No visible");
                    }

                    categoriaConEstado.add(categoriaDTO);
                }
                return new ResponseEntity<List<CategoriaDTO>>(categoriaConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<CategoriaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Categoria obtenerCategoriaDesdeMap(Map<String, String> objetoMap, boolean esAgregado) {
        Categoria categoria= new Categoria();
        boolean disponibidad=true;
        boolean estado=true;

        if(esAgregado){
            Optional<Categoria> categoriaOptional= categoriaRepository.findById(Integer.parseInt(objetoMap.get("id")));

            categoria.setId(Integer.parseInt(objetoMap.get("id")));
            categoriaOptional.ifPresent(value -> categoria.setVisibilidad(value.isVisibilidad()));
            categoriaOptional.ifPresent(value -> categoria.setPertenece(value.getPertenece()));

        }else {
            categoria.setVisibilidad(true);
            categoria.setPertenece(objetoMap.get("pertenece"));
        }

        categoria.setNombre(objetoMap.get("nombre"));
        return categoria;
    }
    //Se valida una caja Existente mediante el nombre
    private boolean categoriaExistente(Map<String, String> objetoMap) {
        return categoriaRepository.existsCategoriaByNombreLikeIgnoreCase(objetoMap.get("nombre"));
    }
    //Se valida que el json contenga las llaves
    private boolean validarCategoriaMap(Map<String, String> objetoMap, boolean validarId) {
        if(objetoMap.containsKey("nombre")){
            if(objetoMap.containsKey("id") && validarId){
                return true;
            } else if (!validarId) {
                return true;
            }
        }
        return false;
    }
}
