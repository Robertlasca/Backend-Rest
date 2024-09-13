package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.AlmacenDTO;
import com.residencia.restaurante.proyecto.dto.MedioPagoDTO;
import com.residencia.restaurante.proyecto.entity.Almacen;
import com.residencia.restaurante.proyecto.entity.Cocina;
import com.residencia.restaurante.proyecto.entity.MedioPago;
import com.residencia.restaurante.proyecto.repository.IAlmacenRepository;
import com.residencia.restaurante.proyecto.repository.ICocinaRepository;
import com.residencia.restaurante.proyecto.repository.IInventarioRepository;
import com.residencia.restaurante.proyecto.service.IAlmacenService;
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
/**
 * Servicio de implementación para gestionar las operaciones relacionadas con los almacenes.
 * Provee funcionalidades para obtener, agregar, actualizar y cambiar el estado de visibilidad de los almacenes.
 */
@Service
public class AlmacenServiceImpl implements IAlmacenService {
    //Inyección de dependencias
    @Autowired
    IAlmacenRepository almacenRepository;

    @Autowired
    ICocinaRepository cocinaRepository;

    @Autowired
    IInventarioRepository inventarioRepository;

    @Autowired
    JwtFilter jfilter;
    /**
     * Obtiene una lista de todos los almacenes activos.
     *
     * @return ResponseEntity con la lista de almacenes activos.
     */
    @Override
    public ResponseEntity<List<AlmacenDTO>> obtenerAlmacenActivos() {
        try {
            if(jfilter.esAdministrador()|| jfilter.esEncargadoAlmacen() || jfilter.esCocinero() || jfilter.esCajero() || jfilter.esEncargadoUtensilio() || jfilter.esComandero()){
            List<AlmacenDTO> almacenConEstado = new ArrayList<>();
            for (Almacen almacen : almacenRepository.getAllByVisibilidadTrue()) {
                AlmacenDTO almacenDTO= new AlmacenDTO();
                almacenDTO.setAlmacen(almacen);
                almacenDTO.setEstado("Visible");

                almacenConEstado.add(almacenDTO);
            }
            return new ResponseEntity<List<AlmacenDTO>>(almacenConEstado, HttpStatus.OK);

            }else {
                return new ResponseEntity<List<AlmacenDTO>>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<AlmacenDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene una lista de todos los almacenes no activos.
     *
     * @return ResponseEntity con la lista de almacenes no activos.
     */
    @Override
    public ResponseEntity<List<AlmacenDTO>> obtenerAlmacenNoActivos() {
        try {
            if(jfilter.esAdministrador()|| jfilter.esEncargadoAlmacen() || jfilter.esCocinero()) {
                List<AlmacenDTO> almacenConEstado = new ArrayList<>();

                for (Almacen almacen : almacenRepository.getAllByVisibilidadFalse()) {
                    AlmacenDTO almacenDTO = new AlmacenDTO();
                    almacenDTO.setAlmacen(almacen);
                    almacenDTO.setEstado("No visible");

                    almacenConEstado.add(almacenDTO);
                }
                return new ResponseEntity<List<AlmacenDTO>>(almacenConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<AlmacenDTO>>(new ArrayList<>(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<AlmacenDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene una lista de todos los almacenes.
     *
     * @return ResponseEntity con la lista de todos los almacenes.
     */
    @Override
    public ResponseEntity<List<AlmacenDTO>> obtenerAlmacen() {
        try {
            if(jfilter.esAdministrador()|| jfilter.esEncargadoAlmacen() || jfilter.esCocinero()) {
            List<AlmacenDTO> almacenConEstado = new ArrayList<>();
            for (Almacen almacen : almacenRepository.findAll()) {
                AlmacenDTO almacenDTO= new AlmacenDTO();
                almacenDTO.setAlmacen(almacen);
                if(almacen.isVisibilidad()){
                    almacenDTO.setEstado("Visible");
                }else{
                    almacenDTO.setEstado("No visible");
                }

                almacenConEstado.add(almacenDTO);
            }
            return new ResponseEntity<List<AlmacenDTO>>(almacenConEstado, HttpStatus.OK);
            }
            return new ResponseEntity<List<AlmacenDTO>>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<AlmacenDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Cambia el estado de visibilidad de un almacén basado en su ID.
     *
     * @param objetoMap Mapa conteniendo el ID del almacén y el nuevo estado de visibilidad.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()|| jfilter.esEncargadoAlmacen()) {
                if (objetoMap.containsKey("id") && objetoMap.containsKey("visibilidad")) {
                    Optional<Almacen> almacenOptional = almacenRepository.findById(Integer.parseInt(objetoMap.get("id")));
                    if (!almacenOptional.isEmpty()) {
                        Almacen almacen = almacenOptional.get();
                        if (objetoMap.get("visibilidad").equalsIgnoreCase("false")) {
                            if (inventarioRepository.existsByAlmacen_Id(Integer.parseInt(objetoMap.get("id")))) {
                                return Utils.getResponseEntity("No puedes desactivar este almacén ya que contiene materias primas.", HttpStatus.BAD_REQUEST);
                            }
                            almacen.setVisibilidad(false);
                        } else {
                            almacen.setVisibilidad(true);
                        }

                        almacenRepository.save(almacen);

                        return Utils.getResponseEntity("El estado del almacén ha sido cambiado.", HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("El almacén no existe.", HttpStatus.BAD_REQUEST);

                }

                return Utils.getResponseEntity(Constantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Agrega un nuevo almacén basado en la información proporcionada.
     *
     * @param objetoMap Mapa conteniendo los datos del nuevo almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            if(jfilter.esAdministrador()|| jfilter.esEncargadoAlmacen()) {

            if(validarAlmaceMap(objetoMap,false)){
                Optional<Almacen> almacenOptional= almacenRepository.findAlmacenByCocina_Id(Integer.parseInt(objetoMap.get("cocinaId")));
                if(almacenOptional.isEmpty()){
                    if(!almacenExistente(objetoMap)){
                        almacenRepository.save(obtenerAlmacenDesdeMap(objetoMap,false));
                        return Utils.getResponseEntity("Almacén guardada exitosamente.",HttpStatus.OK);
                    }
                    return Utils.getResponseEntity("Este almacén ya existe.",HttpStatus.BAD_REQUEST);
                }
                return Utils.getResponseEntity("No se le puede asignar la cocina por que ya pertenece a un almacén.",HttpStatus.BAD_REQUEST);


            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Actualiza la información de un almacén existente.
     *
     * @param objetoMap Mapa conteniendo los datos actualizados del almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {

            if(jfilter.esAdministrador()|| jfilter.esEncargadoAlmacen()) {
            if(validarAlmaceMap(objetoMap,true)){
                Optional<Almacen> optional=almacenRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(!optional.isEmpty()){
                    if(optional.get().getNombre().equalsIgnoreCase(objetoMap.get("nombre"))){
                        almacenRepository.save(obtenerAlmacenDesdeMap(objetoMap,true));
                        return Utils.getResponseEntity("Almacén actualizada",HttpStatus.OK);
                    }else{
                        if(!almacenExistente(objetoMap)){
                            almacenRepository.save(obtenerAlmacenDesdeMap(objetoMap,true));
                            return Utils.getResponseEntity("Almacén actualizada",HttpStatus.OK);
                        }
                        return Utils.getResponseEntity("No puedes asignarle este nombre.",HttpStatus.BAD_REQUEST);
                    }

                }
                return Utils.getResponseEntity("No existe el almacén.",HttpStatus.BAD_REQUEST);
            }

            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene los detalles de un almacén basado en su ID.
     *
     * @param id El ID del almacén a buscar.
     * @return ResponseEntity con los detalles del almacén encontrado o un mensaje de error si no se encuentra.
     */
    @Override
    public ResponseEntity<Almacen> obtenerAlmacenId(Integer id) {
        try {
            if(jfilter.esAdministrador()|| jfilter.esEncargadoAlmacen()) {
            Optional<Almacen> almacenOptional=almacenRepository.findById(id);

            if(almacenOptional.isPresent()){
                Almacen almacen= almacenOptional.get();
                return new ResponseEntity<>(almacen,HttpStatus.OK);
            }
            return new ResponseEntity<>(new Almacen(),HttpStatus.BAD_REQUEST);

            }
            return new ResponseEntity<>(new Almacen(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new Almacen(),HttpStatus.INTERNAL_SERVER_ERROR);
    }



    /**
     * Crea un objeto Almacen a partir de un mapa de propiedades.
     *
     * @param objetoMap Mapa conteniendo los datos del almacén.
     * @param esAgregado Indica si es una operación de agregado o actualización.
     * @return Un objeto Almacen poblado con los datos proporcionados.
     */
    private Almacen obtenerAlmacenDesdeMap(Map<String, String> objetoMap, boolean esAgregado) {
        Almacen almacen= new Almacen();
        boolean disponibidad=true;
        boolean estado=true;

        if(esAgregado){
            Optional<Almacen> almacenOptional= almacenRepository.findById(Integer.parseInt(objetoMap.get("id")));
            Almacen clon=almacenOptional.get();
            almacen.setId(Integer.parseInt(objetoMap.get("id")));
            almacenOptional.ifPresent(value -> almacen.setVisibilidad(value.isVisibilidad()));
            almacen.setCocina(clon.getCocina());

        }else {
            almacen.setVisibilidad(true);

        }
if(objetoMap.containsKey("cocinaId")){
    Optional<Cocina> cocinaOptional= cocinaRepository.findById(Integer.parseInt(objetoMap.get("cocinaId")));
    System.out.println("Entre");
    if(!cocinaOptional.isEmpty()){
        System.out.println("Entre 2");
        Cocina cocina= cocinaOptional.get();
        almacen.setCocina(cocina);
    }

}

        almacen.setNombre(objetoMap.get("nombre"));
        almacen.setDescripcion(objetoMap.get("descripcion"));


        return almacen;
    }
    /**
     * Verifica si existe un almacén con el mismo nombre.
     *
     * @param objetoMap Mapa conteniendo los datos del almacén.
     * @return true si existe un almacén con el mismo nombre, false en caso contrario.
     */
    private boolean almacenExistente(Map<String, String> objetoMap) {
        return almacenRepository.existsAlmacenByNombreLikeIgnoreCase(objetoMap.get("nombre"));
    }
    /**
     * Valida si el mapa de propiedades contiene las claves necesarias para crear o actualizar un almacén.
     *
     * @param objetoMap Mapa conteniendo los datos del almacén.
     * @param validarId Indica si se debe validar la presencia del ID del almacén.
     * @return true si el mapa contiene las claves necesarias, false en caso contrario.
     */
    private boolean validarAlmaceMap(Map<String, String> objetoMap, boolean validarId) {
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
