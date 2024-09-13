package com.residencia.restaurante.proyecto.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.entity.Categoria;
import com.residencia.restaurante.proyecto.entity.Cocina;
import com.residencia.restaurante.proyecto.entity.Cocina_Utensilio;
import com.residencia.restaurante.proyecto.entity.Utensilio;
import com.residencia.restaurante.proyecto.repository.ICocinaRepository;
import com.residencia.restaurante.proyecto.repository.ICocina_UtensilioRepository;
import com.residencia.restaurante.proyecto.repository.IUtensilioRepository;
import com.residencia.restaurante.proyecto.service.IUtensilioService;
import com.residencia.restaurante.proyecto.wrapper.Cocina_UtensilioWrapper;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UtensilioServiceImpl implements IUtensilioService {

    @Autowired
    IUtensilioRepository utensilioRepository;

    @Autowired
    ICocinaRepository cocinaRepository;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private ICocina_UtensilioRepository cocinaUtensilioRepository;




    @Override
    public ResponseEntity<Utensilio> obtenerUtensilioId(Integer id) {
        try {

            Optional<Utensilio> utensilioOptional=utensilioRepository.findById(id);

            if(utensilioOptional.isPresent()){
                Utensilio utensilio= utensilioOptional.get();
                return new ResponseEntity<>(utensilio,HttpStatus.OK);
            }
            return new ResponseEntity<>(new Utensilio(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new Utensilio(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Utensilio>> obtenerUtensilios() {
        try {
            return new ResponseEntity<List<Utensilio>>(utensilioRepository.findAll(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Utensilio>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> eliminar(Integer id) {
        try {
            Optional<Utensilio> utensilioOptional= utensilioRepository.findById(id);
            if(!utensilioOptional.isEmpty()){
                Utensilio utensilio= utensilioOptional.get();
                uploadFileService.eliminarImagen(utensilio.getImagen());                utensilioRepository.delete(utensilio);
                return Utils.getResponseEntity("Utensilio eliminado correctamente.",HttpStatus.OK);
            }
            return Utils.getResponseEntity("Utensilio no existe.",HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Cocina_Utensilio>> obtenerUtensiliosXCocina(Integer id) {
        try {

                return new ResponseEntity<List<Cocina_Utensilio>>(cocinaUtensilioRepository.findAllByCocina_Id(id),HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Cocina_Utensilio>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregarUtensilio(String nombre, String descripcion,String inventario, MultipartFile file) {
        try {
            //Se guarda nombre descripcion e imagen
            Utensilio utensilio= new Utensilio();
            utensilio.setNombre(nombre);
            utensilio.setDescripcion(descripcion);
            String nombreImagen= uploadFileService.guardarImagen(file);
            utensilio.setImagen(nombreImagen);
            utensilioRepository.save(utensilio);

            ObjectMapper objectMapper=new ObjectMapper();

            try {
                List<Cocina_UtensilioWrapper> cocinaUtensilioWrappers= objectMapper.readValue(inventario, new TypeReference<List<Cocina_UtensilioWrapper>>() {
                });
                if(!cocinaUtensilioWrappers.isEmpty()){
                    for(Cocina_UtensilioWrapper cocinaUtensilioWrapper:cocinaUtensilioWrappers){
                        Cocina_Utensilio cocinaUtensilio= new Cocina_Utensilio();
                        cocinaUtensilio.setCantidad(cocinaUtensilioWrapper.getCantidad());
                        cocinaUtensilio.setUtensilio(utensilio);

                        Optional<Cocina> cocinaOptional= cocinaRepository.findById(cocinaUtensilioWrapper.getIdCocina());
                        if(cocinaOptional.isPresent()){
                            Cocina cocina= cocinaOptional.get();
                            cocinaUtensilio.setCocina(cocina);
                        }
                        cocinaUtensilioRepository.save(cocinaUtensilio);
                        System.out.println("si llegue hasta aqui");
                    }
                }

            }catch (Exception e){
                e.printStackTrace();

            }
            return Utils.getResponseEntity("Utensilio guardado correctamente.",HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizarUtensilio(Integer id, String nombre, String descripcion, int idCocina, int cantidad, MultipartFile file) {
        try {

            Optional<Utensilio> utensilioOptional=utensilioRepository.findById(id);
            if(utensilioOptional.isPresent()){


                    Utensilio utensilio=utensilioOptional.get();
                    utensilio.setNombre(nombre);
                    utensilio.setDescripcion(descripcion);
                    Optional<Cocina> cocinaOptional= cocinaRepository.findById(idCocina);


                    if(file.isEmpty()){
                        utensilio.setImagen(utensilio.getImagen());
                    }else {
                        if(!utensilio.getImagen().equalsIgnoreCase("default.jpg")){
                            uploadFileService.eliminarImagen(utensilio.getImagen());
                        }
                        String nombreImagen=uploadFileService.guardarImagen(file);
                        utensilio.setImagen(nombreImagen);
                    }
                    utensilioRepository.save(utensilio);
                    return Utils.getResponseEntity("Utensilio actualizado.",HttpStatus.OK);

            }

            return Utils.getResponseEntity("No existe el utensilio.",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validarCocina(int idCocina) {
        Optional<Cocina> optional=cocinaRepository.findById(idCocina);
        return optional.isPresent();
    }

    private Utensilio obtenerUtensilioDesdeMap(Map<String, String> objetoMap, boolean esAgregado) {
        Utensilio utensilio= new Utensilio();


        if(esAgregado){
            utensilio.setId(Integer.parseInt(objetoMap.get("id")));
            Optional<Utensilio> utensilioOptional=utensilioRepository.findById(Integer.parseInt(objetoMap.get("id")));
            if(!utensilioOptional.isEmpty()){
                //utensilio.setCocina(utensilioOptional.get().getCocina());
            }


        }else{
            Optional<Cocina> cocinaOptional=cocinaRepository.findById(Integer.parseInt(objetoMap.get("idCocina")));
            if(!cocinaOptional.isEmpty()){
                Cocina cocina=cocinaOptional.get();
                //utensilio.setCocina(cocina);

            }

        }

        utensilio.setNombre(objetoMap.get("nombre"));
        //utensilio.setCantidad(Integer.parseInt(objetoMap.get("cantidad")));
        utensilio.setDescripcion(objetoMap.get("descripcion"));

        return utensilio;
    }

    //Se valida que el json contenga las llaves
    private boolean validarUtensilioMap(Map<String, String> objetoMap, boolean validarId) {
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
