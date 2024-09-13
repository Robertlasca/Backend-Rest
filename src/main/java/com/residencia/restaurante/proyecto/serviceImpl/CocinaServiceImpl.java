package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.EstacionDTO;
import com.residencia.restaurante.proyecto.dto.InventarioDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.Cocina;
import com.residencia.restaurante.proyecto.entity.Impresora;
import com.residencia.restaurante.proyecto.entity.Inventario;
import com.residencia.restaurante.proyecto.repository.ICocinaRepository;
import com.residencia.restaurante.proyecto.repository.IDetalleOrden_MenuRepository;
import com.residencia.restaurante.proyecto.repository.IImpresoraRepository;
import com.residencia.restaurante.proyecto.repository.IMenuRepository;
import com.residencia.restaurante.proyecto.service.ICocinaService;
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
public class CocinaServiceImpl implements ICocinaService {

    @Autowired
    JwtFilter jfilter;

    @Autowired
    ICocinaRepository cocinaRepository;

    @Autowired
    IImpresoraRepository impresoraRepository;

    @Autowired
    IDetalleOrden_MenuRepository detalleOrdenMenuRepository;

    @Autowired
    IMenuRepository menuRepository;
    /**
     * Obtiene una lista de todas las cocinas activas.
     * @return Lista de cocinas activas con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<EstacionDTO>> obtenerCocinasActivas() {
        try {
            if (jfilter.esCocinero() && jfilter.esAdministrador()){
                List<EstacionDTO> estacionesConEstado = new ArrayList<>();
            for (Cocina cocina : cocinaRepository.getAllByVisibilidadTrue()) {
                EstacionDTO estacionDTO = new EstacionDTO();
                estacionDTO.setCocina(cocina);
                estacionDTO.setEstado("Visible");
                estacionesConEstado.add(estacionDTO);
            }
            return new ResponseEntity<List<EstacionDTO>>(estacionesConEstado, HttpStatus.OK);

        }
            return new ResponseEntity<List<EstacionDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<EstacionDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene una lista de todas las cocinas no activas.
     * @return Lista de cocinas no activas con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<EstacionDTO>> obtenerCocinasNoActivas() {
        try {
            if (jfilter.esAdministrador() || jfilter.esComandero()) {
                List<EstacionDTO> estacionesConEstado = new ArrayList<>();
                for (Cocina cocina : cocinaRepository.getAllByVisibilidadFalse()) {
                    EstacionDTO estacionDTO = new EstacionDTO();
                    estacionDTO.setCocina(cocina);
                    estacionDTO.setEstado("No visible");
                    estacionesConEstado.add(estacionDTO);
                }
                return new ResponseEntity<List<EstacionDTO>>(estacionesConEstado, HttpStatus.OK);

            }
            return new ResponseEntity<List<EstacionDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<List<EstacionDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
    /**
     * Obtiene una lista de todas las cocinas registradas.
     * @return Lista de cocinas con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<EstacionDTO>> obtenerCocinas() {
        try {
            if (jfilter.esAdministrador() || jfilter.esComandero()) {
                List<EstacionDTO> estacionesConEstado = new ArrayList<>();
                for (Cocina cocina : cocinaRepository.findAll()) {
                    EstacionDTO estacionDTO = new EstacionDTO();
                    estacionDTO.setCocina(cocina);
                    if (cocina.isVisibilidad()) {
                        estacionDTO.setEstado("Visible");
                    } else {
                        estacionDTO.setEstado("No visible");
                    }

                    estacionesConEstado.add(estacionDTO);
                }
                return new ResponseEntity<List<EstacionDTO>>(estacionesConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<EstacionDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<EstacionDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Cambia el estado de una cocina (activo/inactivo) según el ID proporcionado en el mapa de datos.
     * @param objetoMap Mapa de datos que contiene el ID de la cocina y el nuevo estado.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()) {
                if (objetoMap.containsKey("id") && objetoMap.containsKey("visibilidad")) {
                    Optional<Cocina> cocinaOptional = cocinaRepository.findById(Integer.parseInt(objetoMap.get("id")));
                    if (!cocinaOptional.isEmpty()) {
                        Cocina cocina = cocinaOptional.get();
                        if (objetoMap.get("visibilidad").equalsIgnoreCase("false")) {
                            if (detalleOrdenMenuRepository.existsByMenuCocinaIdAndEstadoNotIn(Integer.parseInt(objetoMap.get("id")))) {
                                return Utils.getResponseEntity("No puedes cambiar el estado de la cocina ya que existen platillos en preparación en la cocina.", HttpStatus.BAD_REQUEST);
                            }

                            if (menuRepository.existsByCocinaIdAndMenuVisibilidadIsTrue(Integer.parseInt(objetoMap.get("id")))) {
                                return Utils.getResponseEntity("No puedes cambiar el estado de la cocina ya que existen platillos asociados a la cocina.", HttpStatus.BAD_REQUEST);
                            }


                            cocina.setVisibilidad(false);
                        } else {
                            cocina.setVisibilidad(true);
                        }

                        cocinaRepository.save(cocina);

                        return Utils.getResponseEntity("El estado de la cocina ha sido cambiado.", HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("La cocina no existe.", HttpStatus.BAD_REQUEST);

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
     * Agrega una nueva cocina utilizando los datos proporcionados en el mapa.
     * @param objetoMap Mapa de datos que contiene la información de la nueva cocina.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()) {
            if(validarCocinaMap(objetoMap,false)){
                if(!cocinaExistente(objetoMap)){
                    cocinaRepository.save(obtenerCocinaDesdeMap(objetoMap,false));
                    return Utils.getResponseEntity("Cocina guardada exitosamente.",HttpStatus.OK);
                }
                return Utils.getResponseEntity("Esta cocina ya existe.",HttpStatus.BAD_REQUEST);

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
     * Actualiza los datos de una cocina existente utilizando los datos proporcionados en el mapa.
     * @param objetoMap Mapa de datos que contiene la información actualizada de la cocina.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()) {
            if(validarCocinaMap(objetoMap,true)){
                Optional<Cocina> optional=cocinaRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(!optional.isEmpty()){
                    if(optional.get().getNombre().equalsIgnoreCase(objetoMap.get("nombre"))){
                        cocinaRepository.save(obtenerCocinaDesdeMap(objetoMap,true));
                        return Utils.getResponseEntity("Cocina actualizada",HttpStatus.OK);
                    }else{
                        if(!cocinaExistente(objetoMap)){
                            cocinaRepository.save(obtenerCocinaDesdeMap(objetoMap,true));
                            return Utils.getResponseEntity("Cocina actualizada",HttpStatus.OK);
                        }
                        return Utils.getResponseEntity("No puedes asignarle este nombre.",HttpStatus.BAD_REQUEST);
                    }

                }
                return Utils.getResponseEntity("No existe la cocina.",HttpStatus.BAD_REQUEST);
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
     * Obtiene los datos de una cocina específica según su ID.
     * @param id ID de la cocina.
     * @return Datos de la cocina solicitada con estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Cocina> obtenerCocinasId(Integer id) {
        try {
            if(jfilter.esAdministrador()) {
            Optional<Cocina> optionalCocina=cocinaRepository.findById(id);

            if(optionalCocina.isPresent()){
                Cocina cocina= optionalCocina.get();
                return new ResponseEntity<Cocina>(cocina,HttpStatus.OK);
            }
            return new ResponseEntity<Cocina>(new Cocina(),HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Cocina>(new Cocina(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new Cocina(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Cocina>> obtenerCocinasActivasSinAlmacen() {
        try {
            if(jfilter.esAdministrador()|| jfilter.esEncargadoAlmacen() || jfilter.esCocinero()) {
                return new ResponseEntity<List<Cocina>>(cocinaRepository.getCocinasNoAsociadas(), HttpStatus.OK);
            }
            return new ResponseEntity<List<Cocina>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Cocina>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Cocina obtenerCocinaDesdeMap(Map<String, String> objetoMap, boolean esAgregado) {
        Cocina cocina= new Cocina();
        boolean disponibidad=true;
        boolean estado=true;


        if(esAgregado){
            Optional<Cocina> cocinaOptional= cocinaRepository.findById(Integer.parseInt(objetoMap.get("id")));

            cocina.setId(Integer.parseInt(objetoMap.get("id")));
            cocinaOptional.ifPresent(value -> cocina.setVisibilidad(value.isVisibilidad()));

        }else {
            cocina.setVisibilidad(true);
        }

        Optional<Impresora> impresoraOptional= impresoraRepository.findById(Integer.parseInt(objetoMap.get("idImpresora")));
        if(impresoraOptional.isPresent()){
            Impresora impresora= impresoraOptional.get();
            cocina.setImpresora(impresora);

            cocinaRepository.save(cocina);
        }

        cocina.setNombre(objetoMap.get("nombre"));
        cocina.setDescripcion(objetoMap.get("descripcion"));
        return cocina;
    }
    //Se valida una caja Existente mediante el nombre
    private boolean cocinaExistente(Map<String, String> objetoMap) {
        return cocinaRepository.existsCocinaByNombreLikeIgnoreCase(objetoMap.get("nombre"));
    }
    //Se valida que el json contenga las llaves
    private boolean validarCocinaMap(Map<String, String> objetoMap, boolean validarId) {
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
