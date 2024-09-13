package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.AreaServicioDTO;
import com.residencia.restaurante.proyecto.entity.AreaServicio;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.Impresora;
import com.residencia.restaurante.proyecto.entity.Mesa;
import com.residencia.restaurante.proyecto.repository.IAreaServicioRepository;
import com.residencia.restaurante.proyecto.repository.IImpresoraRepository;
import com.residencia.restaurante.proyecto.repository.IMesaRepository;
import com.residencia.restaurante.proyecto.repository.IOrdenRepository;
import com.residencia.restaurante.proyecto.service.IAreaServicioService;
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
public class
AreaServicioServiceImpl implements IAreaServicioService {
    @Autowired
    private IAreaServicioRepository areaServicioRepository;
     @Autowired
     private IImpresoraRepository impresoraRepository;

     @Autowired
     private IMesaRepository mesaRepository;

     @Autowired
     private IOrdenRepository ordenRepository;

    @Override
    public ResponseEntity<List<AreaServicioDTO>> obtenerAreasActivas() {
        try {
            List<AreaServicioDTO> areaServicioDTOS= new ArrayList<>();
            for (AreaServicio areaServicio: areaServicioRepository.getAllByDisponibilidadTrue()) {
                AreaServicioDTO areaServicioDTO= new AreaServicioDTO();
                areaServicioDTO.setAreaServicio(areaServicio);
                areaServicioDTO.setEstado("Visible");
                areaServicioDTO.setCantidadMesas(mesaRepository.countByAreaServicio_Id(areaServicio.getId()));
                areaServicioDTOS.add(areaServicioDTO);
            }
            return new ResponseEntity<List<AreaServicioDTO>>(areaServicioDTOS,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<AreaServicioDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<AreaServicioDTO>> obtenerAreasNoActivas() {
        try {
            List<AreaServicioDTO> areaServicioDTOS= new ArrayList<>();
            for (AreaServicio areaServicio: areaServicioRepository.getAllByDisponibilidadFalse()) {
                AreaServicioDTO areaServicioDTO= new AreaServicioDTO();
                areaServicioDTO.setAreaServicio(areaServicio);
                areaServicioDTO.setEstado("No visible");
                areaServicioDTO.setCantidadMesas(mesaRepository.countByAreaServicio_Id(areaServicio.getId()));
                areaServicioDTOS.add(areaServicioDTO);
            }
            return new ResponseEntity<List<AreaServicioDTO>>(areaServicioDTOS,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<AreaServicioDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<AreaServicioDTO>> obtenerAreas() {
        try {
            List<AreaServicioDTO> areaServicioDTOS= new ArrayList<>();
            for (AreaServicio areaServicio: areaServicioRepository.findAll()) {
                AreaServicioDTO areaServicioDTO= new AreaServicioDTO();
                areaServicioDTO.setAreaServicio(areaServicio);
                areaServicioDTO.setCantidadMesas(mesaRepository.countByAreaServicio_Id(areaServicio.getId()));
                if(areaServicio.isDisponibilidad()){
                    areaServicioDTO.setEstado("Visible");
                }else {
                    areaServicioDTO.setEstado("No visible");
                }

                areaServicioDTOS.add(areaServicioDTO);
            }
            return new ResponseEntity<List<AreaServicioDTO>>(areaServicioDTOS,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<AreaServicioDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("id") && objetoMap.containsKey("estado")){
                Optional<AreaServicio> areaServicioOptional= areaServicioRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(!areaServicioOptional.isEmpty()){
                    AreaServicio areaServicio= areaServicioOptional.get();
                    if(objetoMap.get("estado").equalsIgnoreCase("false")){
                        if(ordenRepository.existsByAreaServicioIdAndEstadoNotIn(Integer.parseInt(objetoMap.get("id")))){
                            return Utils.getResponseEntity("No se puede cambiar el estado del área de servicio por que tiene comandas activas.",HttpStatus.BAD_REQUEST);
                        }
                        areaServicio.setDisponibilidad(false);

                    }else{
                        areaServicio.setDisponibilidad(true);
                    }

                    areaServicioRepository.save(areaServicio);

                    return Utils.getResponseEntity("El estado del área de servicio ha sido cambiado.",HttpStatus.OK);

                }
                return Utils.getResponseEntity("La área de servicio no existe.",HttpStatus.BAD_REQUEST);

            }

            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            if(validarAreaMap(objetoMap,false)){
                if(!areaExistente(objetoMap)){
                    AreaServicio areaServicio= obtenerAreaDesdeMap(objetoMap, false);
                        areaServicioRepository.save(areaServicio);

                    int numeroDeMesas= mesaRepository.countByAreaServicio_IdAndVisibilidadTrue(areaServicio.getId());
                    String nombreMesa= String.valueOf(numeroDeMesas+1);


                    Mesa mesa= new Mesa();
                    mesa.setNombre(nombreMesa);
                    //mesa.setTipo(Boolean.parseBoolean(objetoMap.get("tipo")));
                    mesa.setTipoMesa("miCuadrado");
                    mesa.setEstado("Disponible");
                    mesa.setCoordX(5);
                    mesa.setCoordY(5);
                    mesa.setVisibilidad(true);
                    mesa.setAreaServicio(areaServicio);




                    mesaRepository.save(mesa);
                    return Utils.getResponseEntity("Área de servicio guardada.", HttpStatus.OK);
                }
                return Utils.getResponseEntity("Esta área de servicio ya existe.",HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity("El área de servicio ya existe.",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            if(validarAreaMap(objetoMap,true)){
                Optional<AreaServicio> optional= areaServicioRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(!optional.isEmpty()){

                        if(optional.get().getNombre().equalsIgnoreCase(objetoMap.get("nombre"))){
                            areaServicioRepository.save(obtenerAreaDesdeMap(objetoMap,true));
                            return Utils.getResponseEntity("Área de servicio actualizada.",HttpStatus.OK);
                        }else {
                            if(!areaExistente(objetoMap)){
                                areaServicioRepository.save(obtenerAreaDesdeMap(objetoMap,true));
                                return Utils.getResponseEntity("Área de servicio actualizada.",HttpStatus.OK);
                            }
                            return Utils.getResponseEntity("No puedes asignarle este nombre.",HttpStatus.BAD_REQUEST);
                        }



                }
                return Utils.getResponseEntity("El área de servicio no existe.",HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<AreaServicio> obtenerAreaId(Integer id) {
        try {
            Optional<AreaServicio> areaServicioOptional=areaServicioRepository.findById(id);

            if(areaServicioOptional.isPresent()){
                AreaServicio areaServicio= areaServicioOptional.get();
                return new ResponseEntity<>(areaServicio,HttpStatus.OK);
            }
            return new ResponseEntity<>(new AreaServicio(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new AreaServicio(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> eliminar() {
        try {
            List<AreaServicio> areaServicios= areaServicioRepository.findAll();
            for (AreaServicio areaServicio:areaServicios) {
                List<Mesa> optional= mesaRepository.findAllByAreaServicio_Id(areaServicio.getId());

                if (optional.isEmpty()){
                    areaServicioRepository.delete(areaServicio);
                }
            }
            return Utils.getResponseEntity("Mesas eliminadas correctamente",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private AreaServicio obtenerAreaDesdeMap(Map<String,String> objetoMap,boolean esAgregado){
        AreaServicio areaServicio= new AreaServicio();

        if(esAgregado){
            Optional<AreaServicio> areaServicioOptional= areaServicioRepository.findById(Integer.parseInt(objetoMap.get("id")));
            areaServicio.setId(Integer.parseInt(objetoMap.get("id")));
            areaServicioOptional.ifPresent(value -> areaServicio.setDisponibilidad(value.isDisponibilidad()));
        }else {
            areaServicio.setDisponibilidad(true);
        }

        areaServicio.setNombre(objetoMap.get("nombre"));


        return areaServicio;

    }

    private boolean areaExistente(Map<String,String> objetoMap){
        return areaServicioRepository.existsAreaServiciosByNombreLikeIgnoreCase(objetoMap.get("nombre"));
    }

    private  boolean validarAreaMap(Map<String,String> objetoMap,boolean validarId){
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
