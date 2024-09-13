package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.EstacionDTO;
import com.residencia.restaurante.proyecto.dto.MedioPagoDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.Cocina;
import com.residencia.restaurante.proyecto.entity.MedioPago;
import com.residencia.restaurante.proyecto.repository.IMedioPagoRepository;
import com.residencia.restaurante.proyecto.service.IMedioPagoService;
import com.residencia.restaurante.security.JWT.JwtFilter;
import com.residencia.restaurante.security.utils.Utils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class MedioPagoServiceImpl implements IMedioPagoService {
    @Autowired
    IMedioPagoRepository medioPagoRepository;

    @Autowired
    JwtFilter jwtFilter;
    /**
     * Obtiene todos los medios de pago activos.
     * @return ResponseEntity<List<MedioPago>> Lista de medios de pago activos.
     */
    @Override
    public ResponseEntity<List<MedioPagoDTO>> obtenerMedioPagoActivas() {
        try {
            if(jwtFilter.esAdministrador()){


            List<MedioPagoDTO> mediosPagoConEstado = new ArrayList<>();
            for (MedioPago medioPago : medioPagoRepository.getAllByDisponibilidadTrue()) {
                MedioPagoDTO medioPagoDTO= new MedioPagoDTO();
                medioPagoDTO.setMedioPago(medioPago);
                medioPagoDTO.setEstado("Visible");

                mediosPagoConEstado.add(medioPagoDTO);
            }
            return new ResponseEntity<List<MedioPagoDTO>>(mediosPagoConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<MedioPagoDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<MedioPagoDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene todos los medios de pago no activos.
     * @return ResponseEntity<List<MedioPago>> Lista de medios de pago no activos.
     */
    @Override
    public ResponseEntity<List<MedioPagoDTO>> obtenerMedioPagoNoActivas() {
        try {
            if(jwtFilter.esAdministrador()){
            List<MedioPagoDTO> mediosPagoConEstado = new ArrayList<>();
            for (MedioPago medioPago : medioPagoRepository.getAllByDisponibilidadFalse()) {
                MedioPagoDTO medioPagoDTO= new MedioPagoDTO();
                medioPagoDTO.setMedioPago(medioPago);
                medioPagoDTO.setEstado("No visible");


                mediosPagoConEstado.add(medioPagoDTO);
            }
            return new ResponseEntity<List<MedioPagoDTO>>(mediosPagoConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<MedioPagoDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<MedioPagoDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene todos los medios de pago.
     * @return ResponseEntity<List<MedioPago>> Lista de todos los medios de pago.
     */
    @Override
    public ResponseEntity<List<MedioPagoDTO>> obtenerMedioPago() {
        try {
            if(jwtFilter.esAdministrador()){
            List<MedioPagoDTO> mediosPagoConEstado = new ArrayList<>();
            for (MedioPago medioPago : medioPagoRepository.findAll()) {
                MedioPagoDTO medioPagoDTO= new MedioPagoDTO();
                medioPagoDTO.setMedioPago(medioPago);
                if(medioPago.isDisponibilidad()){
                    medioPagoDTO.setEstado("Visible");
                }else{
                    medioPagoDTO.setEstado("No visible");
                }

                mediosPagoConEstado.add(medioPagoDTO);
            }
            return new ResponseEntity<List<MedioPagoDTO>>(mediosPagoConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<MedioPagoDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<MedioPagoDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Cambia el estado de disponibilidad de un medio de pago.
     * @param objetoMap Un mapa de datos con la información del medio de pago y su nuevo estado.
     * @return ResponseEntity<String> Respuesta que indica el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            if(jwtFilter.esAdministrador()) {
                if (objetoMap.containsKey("id") && objetoMap.containsKey("visibilidad")) {
                    Optional<MedioPago> medioPagoOptional = medioPagoRepository.findById(Integer.parseInt(objetoMap.get("id")));
                    if (!medioPagoOptional.isEmpty()) {
                        MedioPago medioPago = medioPagoOptional.get();
                        if (objetoMap.get("visibilidad").equalsIgnoreCase("false")) {
                            medioPago.setDisponibilidad(false);
                        } else {
                            medioPago.setDisponibilidad(true);
                        }

                        medioPagoRepository.save(medioPago);

                        return Utils.getResponseEntity("La visibilidad del medio de pago ha sido cambiada.", HttpStatus.OK);
                    }
                    return Utils.getResponseEntity("El medio de pago no existe.", HttpStatus.BAD_REQUEST);
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
     * Agrega un nuevo medio de pago.
     * @param objetoMap Un mapa de datos con la información del nuevo medio de pago.
     * @return ResponseEntity<String> Respuesta que indica el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            if(jwtFilter.esAdministrador()) {

            if(validarMedioPagoMap(objetoMap,false)){
                if(!medioPagoExistente(objetoMap)){
                    medioPagoRepository.save(obtenerMedioPagoDesdeMap(objetoMap,false));
                    return Utils.getResponseEntity("Medio de pago guardado exitosamente.",HttpStatus.OK);
                }
                return Utils.getResponseEntity("Este medio de pago ya existe.",HttpStatus.BAD_REQUEST);

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
     * Actualiza un medio de pago existente.
     * @param objetoMap Un mapa de datos con la información actualizada del medio de pago.
     * @return ResponseEntity<String> Respuesta que indica el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            if(jwtFilter.esAdministrador()) {
            if(validarMedioPagoMap(objetoMap,true)){
                Optional<MedioPago> optional=medioPagoRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(!optional.isEmpty()){
                    if(optional.get().getNombre().equalsIgnoreCase(objetoMap.get("nombre"))){
                        medioPagoRepository.save(obtenerMedioPagoDesdeMap(objetoMap,true));
                        return Utils.getResponseEntity("Medio de pago actualizado.",HttpStatus.OK);
                    }else{
                        if(!medioPagoExistente(objetoMap)){
                            medioPagoRepository.save(obtenerMedioPagoDesdeMap(objetoMap,true));
                            return Utils.getResponseEntity("Medio de pago actualizado",HttpStatus.OK);
                        }
                        return Utils.getResponseEntity("No puedes asignarle este nombre.",HttpStatus.BAD_REQUEST);
                    }

                }
                return Utils.getResponseEntity("No existe el medio de pago.",HttpStatus.BAD_REQUEST);
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
     * Obtiene los datos de un medio de pago específico.
     * @param id El ID del medio de pago.
     * @return ResponseEntity<MedioPago> Medio de pago solicitado.
     */
    @Override
    public ResponseEntity<MedioPago> obtenerMedioPagoId(Integer id) {
        try {
            if(jwtFilter.esAdministrador()) {
                Optional<MedioPago> optional = medioPagoRepository.findById(id);

                if (optional.isPresent()) {
                    MedioPago medioPago = optional.get();
                    return new ResponseEntity<MedioPago>(medioPago, HttpStatus.OK);
                }
                return new ResponseEntity<MedioPago>(new MedioPago(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<MedioPago>(new MedioPago(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new MedioPago(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private MedioPago obtenerMedioPagoDesdeMap(Map<String, String> objetoMap, boolean esAgregado) {
        MedioPago medioPago= new MedioPago();
        boolean disponibidad=true;
        boolean estado=true;

        if(esAgregado){
            Optional<MedioPago> medioOptional=medioPagoRepository.findById(Integer.parseInt(objetoMap.get("id")));

            medioPago.setId(Integer.parseInt(objetoMap.get("id")));
            medioOptional.ifPresent(value -> medioPago.setDisponibilidad(value.isDisponibilidad()));

        }else {
            medioPago.setDisponibilidad(true);
        }

        medioPago.setNombre(objetoMap.get("nombre"));
        medioPago.setDescripcion(objetoMap.get("descripcion"));
        return medioPago;
    }
    //Se valida una caja Existente mediante el nombre
    private boolean medioPagoExistente(Map<String, String> objetoMap) {
        return medioPagoRepository.existsMedioPagoByNombreLikeIgnoreCase(objetoMap.get("nombre"));
    }
    //Se valida que el json contenga las llaves
    private boolean validarMedioPagoMap(Map<String, String> objetoMap, boolean validarId) {
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
