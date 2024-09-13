package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.CategoriaDTO;
import com.residencia.restaurante.proyecto.dto.CategoriaMateriaPrimaDTO;
import com.residencia.restaurante.proyecto.entity.Categoria;
import com.residencia.restaurante.proyecto.entity.CategoriaMateriaPrima;
import com.residencia.restaurante.proyecto.entity.MateriaPrima;
import com.residencia.restaurante.proyecto.repository.ICategoriaMateriaPrimaRepository;
import com.residencia.restaurante.proyecto.repository.IMateriaPrimaRepository;
import com.residencia.restaurante.proyecto.service.ICategoriaMateriaPrimaService;
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
public class CategoriaMateriaPrimaServiceImpl implements ICategoriaMateriaPrimaService {
    @Autowired
    ICategoriaMateriaPrimaRepository categoriaMateriaPrimaRepository;

    @Autowired
    IMateriaPrimaRepository materiaPrimaRepository;

    @Autowired
    JwtFilter jfilter;


    /**
     * Obtiene una lista de todas las categorías activas de materia prima.
     * @return Lista de categorías activas con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoriasActivas() {
        try {
            if(jfilter.esAdministrador() && jfilter.esComandero()){
            List<CategoriaMateriaPrimaDTO> categoriaConEstado = new ArrayList<>();
            for (CategoriaMateriaPrima categoria : categoriaMateriaPrimaRepository.getAllByVisibilidadTrue()) {
                CategoriaMateriaPrimaDTO categoriaDTO= new CategoriaMateriaPrimaDTO();
                categoriaDTO.setCategoriaMateriaPrima(categoria);
                categoriaDTO.setEstado("No visible");
                categoriaDTO.setCantidadMaterias(materiaPrimaRepository.countByCategoriaId(categoria.getId()));


                categoriaConEstado.add(categoriaDTO);
            }
            return new ResponseEntity<List<CategoriaMateriaPrimaDTO>>(categoriaConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<CategoriaMateriaPrimaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<CategoriaMateriaPrimaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene una lista de todas las categorías de materia prima no activas.
     * @return Lista de categorías no activas con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoriasNoActivas() {
        try {
            if(jfilter.esAdministrador() && jfilter.esComandero()){
            List<CategoriaMateriaPrimaDTO> categoriaConEstado = new ArrayList<>();
            for (CategoriaMateriaPrima categoria : categoriaMateriaPrimaRepository.getAllByVisibilidadFalse()) {
                CategoriaMateriaPrimaDTO categoriaDTO= new CategoriaMateriaPrimaDTO();
                categoriaDTO.setCategoriaMateriaPrima(categoria);
                categoriaDTO.setEstado("No visible");
                categoriaDTO.setCantidadMaterias(materiaPrimaRepository.countByCategoriaId(categoria.getId()));


                categoriaConEstado.add(categoriaDTO);
            }
            return new ResponseEntity<List<CategoriaMateriaPrimaDTO>>(categoriaConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<CategoriaMateriaPrimaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<CategoriaMateriaPrimaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene una lista de todas las categorías de materia prima registradas.
     * @return Lista de categorías con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategorias() {
        try {
            if(jfilter.esAdministrador() && jfilter.esComandero()){
            List<CategoriaMateriaPrimaDTO> categoriaConEstado = new ArrayList<>();
            for (CategoriaMateriaPrima categoria : categoriaMateriaPrimaRepository.findAll()) {
                CategoriaMateriaPrimaDTO categoriaDTO= new CategoriaMateriaPrimaDTO();
                categoriaDTO.setCategoriaMateriaPrima(categoria);
                if(categoria.isVisibilidad()){
                    categoriaDTO.setEstado("Visible");
                }else{
                    categoriaDTO.setEstado("No visible");
                }
                categoriaDTO.setCantidadMaterias(materiaPrimaRepository.countByCategoriaId(categoria.getId()));

                categoriaConEstado.add(categoriaDTO);
            }
            return new ResponseEntity<List<CategoriaMateriaPrimaDTO>>(categoriaConEstado, HttpStatus.OK);

            }

            return new ResponseEntity<List<CategoriaMateriaPrimaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<CategoriaMateriaPrimaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Cambia el estado de una categoría de materia prima (activo/inactivo) según el ID proporcionado en el mapa de datos.
     * @param objetoMap Mapa de datos que contiene el ID de la categoría y el nuevo estado.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador() ) {
                if (objetoMap.containsKey("id") && objetoMap.containsKey("visibilidad")) {
                    Optional<CategoriaMateriaPrima> categoriaOptional = categoriaMateriaPrimaRepository.findById(Integer.parseInt(objetoMap.get("id")));
                    if (!categoriaOptional.isEmpty()) {
                        CategoriaMateriaPrima categoria = categoriaOptional.get();
                        if (objetoMap.get("visibilidad").equalsIgnoreCase("false")) {
                            categoria.setVisibilidad(false);

                            List<MateriaPrima> materiaPrimaList = materiaPrimaRepository.getAllByCategoriaMateriaPrima_Id(Integer.parseInt(objetoMap.get("id")));
                            Optional<CategoriaMateriaPrima> categoriaOptional1 = categoriaMateriaPrimaRepository.findCategoriaByNombreLikeIgnoreCase("Sin categoría.");

                            if (categoriaOptional1.isPresent()) {
                                CategoriaMateriaPrima categoriaMateriaPrima = categoriaOptional1.get();
                                if (!materiaPrimaList.isEmpty()) {
                                    for (MateriaPrima materiaPrima : materiaPrimaList) {
                                        materiaPrima.setCategoriaMateriaPrima(categoriaMateriaPrima);
                                        materiaPrimaRepository.save(materiaPrima);
                                    }
                                }

                            }
                        } else {
                            categoria.setVisibilidad(true);
                        }

                        categoriaMateriaPrimaRepository.save(categoria);

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
     * Agrega una nueva categoría de materia prima utilizando los datos proporcionados en el mapa.
     * @param objetoMap Mapa de datos que contiene la información de la nueva categoría.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()) {

            if(validarCategoriaMateriaPrimaMap(objetoMap,false)){
                if(!categoriaMateriaPrimaExistente(objetoMap)){
                    categoriaMateriaPrimaRepository.save(obtenerCategoriaDesdeMap(objetoMap,false));
                    return Utils.getResponseEntity("Categoría guardada exitosamente.",HttpStatus.OK);
                }
                return Utils.getResponseEntity("Esta categoría ya existe.",HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }

            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Actualiza los datos de una categoría de materia prima existente utilizando los datos proporcionados en el mapa.
     * @param objetoMap Mapa de datos que contiene la información actualizada de la categoría.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()){


            if(validarCategoriaMateriaPrimaMap(objetoMap,true)){
                Optional<CategoriaMateriaPrima> optional=categoriaMateriaPrimaRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(!optional.isEmpty()){
                    if(optional.get().getNombre().equalsIgnoreCase(objetoMap.get("nombre"))){
                        categoriaMateriaPrimaRepository.save(obtenerCategoriaDesdeMap(objetoMap,true));
                        return Utils.getResponseEntity("Categoría actualizada",HttpStatus.OK);
                    }else{
                        if(!categoriaMateriaPrimaExistente(objetoMap)){
                            categoriaMateriaPrimaRepository.save(obtenerCategoriaDesdeMap(objetoMap,true));
                            return Utils.getResponseEntity("Categoría actualizada",HttpStatus.OK);
                        }
                        return Utils.getResponseEntity("No puedes asignarle este nombre.",HttpStatus.BAD_REQUEST);
                    }

                }
                return Utils.getResponseEntity("No existe la Categoría.",HttpStatus.BAD_REQUEST);
            }

            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene los datos de una categoría de materia prima específica según su ID.
     * @param id ID de la categoría de materia prima.
     * @return Datos de la categoría solicitada con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<CategoriaMateriaPrima> obtenerCategoriasId(Integer id) {
        try {
            if(jfilter.esAdministrador()){


            Optional<CategoriaMateriaPrima> categoriaOptional=categoriaMateriaPrimaRepository.findById(id);

            if(categoriaOptional.isPresent()){
                CategoriaMateriaPrima categoria= categoriaOptional.get();
                return new ResponseEntity<CategoriaMateriaPrima>(categoria,HttpStatus.OK);
            }
            return new ResponseEntity<CategoriaMateriaPrima>(new CategoriaMateriaPrima(),HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<CategoriaMateriaPrima>(new CategoriaMateriaPrima(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<CategoriaMateriaPrima>(new CategoriaMateriaPrima(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private CategoriaMateriaPrima obtenerCategoriaDesdeMap(Map<String, String> objetoMap, boolean esAgregado) {
        CategoriaMateriaPrima categoria= new CategoriaMateriaPrima();
        boolean disponibidad=true;
        boolean estado=true;

        if(esAgregado){
            Optional<CategoriaMateriaPrima> categoriaOptional= categoriaMateriaPrimaRepository.findById(Integer.parseInt(objetoMap.get("id")));

            categoria.setId(Integer.parseInt(objetoMap.get("id")));
            categoriaOptional.ifPresent(value -> categoria.setVisibilidad(value.isVisibilidad()));

        }else {
            categoria.setVisibilidad(true);
        }

        categoria.setNombre(objetoMap.get("nombre"));
        return categoria;
    }
    //Se valida una caja Existente mediante el nombre
    private boolean categoriaMateriaPrimaExistente(Map<String, String> objetoMap) {
        return categoriaMateriaPrimaRepository.existsCategoriaMateriaPrimaByNombreLikeIgnoreCase(objetoMap.get("nombre"));
    }
    //Se valida que el json contenga las llaves
    private boolean validarCategoriaMateriaPrimaMap(Map<String, String> objetoMap, boolean validarId) {
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
