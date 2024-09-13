package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.MesaDTO;
import com.residencia.restaurante.proyecto.entity.AreaServicio;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.Mesa;
import com.residencia.restaurante.proyecto.repository.IAreaServicioRepository;
import com.residencia.restaurante.proyecto.repository.IMesaRepository;
import com.residencia.restaurante.proyecto.service.IMesaService;
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
public class MesaServiceImpl implements IMesaService{
    @Autowired
    private IMesaRepository mesaRepository;

    @Autowired
    private IAreaServicioRepository areaServicioRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<List<MesaDTO>> obtenerMesasActivasPorArea(Integer id) {
        try {
            if(jwtFilter.esAdministrador()) {
                List<MesaDTO> mesaDTOS = new ArrayList<>();
                for (Mesa mesa : mesaRepository.findAllByAreaServicio_IdAndVisibilidadTrue(id)) {
                    Optional<AreaServicio> optional = areaServicioRepository.findById(id);
                    String nombreArea = "A";
                    if (optional.isPresent()) {
                        AreaServicio areaServicio = optional.get();
                        String nombreAreas = areaServicio.getNombre(); // Obtiene el nombre completo del área
                        if (nombreAreas != null && !nombreAreas.isEmpty()) {
                            nombreArea = String.valueOf(nombreAreas.charAt(0)); // Obtiene la primera letra del nombre del área
                        } else {
                            System.out.println("El nombre del área está vacío o es nulo.");
                        }

                    }
                    MesaDTO mesaDTO = new MesaDTO();
                    mesaDTO.setName(nombreArea + mesa.getNombre());
                    mesaDTO.setRow(mesa.getCoordY());
                    mesaDTO.setColumn(mesa.getCoordX());
                    mesaDTO.setId(mesa.getId());
                    mesaDTO.setEstado(mesa.getEstado());
                    String tipoMesa = mesa.getTipoMesa();
                    if (tipoMesa == null) {
                        mesaDTO.setType("miCuadrado");
                    } else {
                        mesaDTO.setType(mesa.getTipoMesa());
                    }


                    mesaDTOS.add(mesaDTO);

                }
                return new ResponseEntity<List<MesaDTO>>(mesaDTOS, HttpStatus.OK);
            }
            return new ResponseEntity<List<MesaDTO>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MesaDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Mesa>> obtenerMesasNoActivasPorArea(Integer id) {
        try {
            return new ResponseEntity<List<Mesa>>(mesaRepository.findAllByAreaServicio_IdAndVisibilidadFalse(id),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Mesa>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Mesa>> obtenerMesasPorArea(Integer id) {
        try {
            if(jwtFilter.esAdministrador()) {
                return new ResponseEntity<List<Mesa>>(mesaRepository.findAllByAreaServicio_Id(id), HttpStatus.OK);
            }
            return new ResponseEntity<List<Mesa>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Mesa>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
//Metodo para eliminar una mesa
@Override
public ResponseEntity<String> cambiarEstado(Integer id) {
    try {
        if(jwtFilter.esAdministrador()) {
            if (validarMesa(id)) {
                Optional<Mesa> mesaOptional = mesaRepository.findById(id);
                if (mesaOptional.isPresent()) {
                    Mesa mesa = mesaOptional.get();
                    if (mesa.getEstado().equalsIgnoreCase("Disponible")) {
                        mesa.setEstado("Eliminada");
                        mesa.setCoordY(0);
                        mesa.setCoordX(0);
                        mesa.setVisibilidad(false);
                        mesaRepository.save(mesa);

                        // Llamar al método para reordenar las mesas en el área de servicio
                        reordenarMesas(mesa.getAreaServicio().getId());

                        return Utils.getResponseEntity("Mesa eliminada y mesas reordenadas correctamente.", HttpStatus.OK);
                    }
                    return Utils.getResponseEntity("No se puede eliminar la mesa ya que está ocupada.", HttpStatus.BAD_REQUEST);
                }
                return Utils.getResponseEntity("No existe la mesa.", HttpStatus.NOT_FOUND);
            }
            return Utils.getResponseEntity("Datos inválidos.", HttpStatus.BAD_REQUEST);
        }
        return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
        e.printStackTrace();
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    private void reordenarMesas(Integer idArea) {
        List<Mesa> mesas = mesaRepository.findByAreaServicio_IdAndVisibilidadTrueOrderByNombreAsc(idArea);
        int contador = 1;
        for (Mesa mesa : mesas) {
            mesa.setNombre(String.valueOf(contador++));
            mesaRepository.save(mesa);
        }
    }

    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            if(jwtFilter.esAdministrador()) {
            //Se verifica que no exista la mesa en el área de servicio
            if(validarMap(objetoMap)) {
                int numeroDeMesas= mesaRepository.countByAreaServicio_IdAndVisibilidadTrue(Integer.parseInt(objetoMap.get("idArea")));
                String nombreMesa= String.valueOf(numeroDeMesas+1);


                    Mesa mesa= new Mesa();
                    mesa.setNombre(nombreMesa);
                    //mesa.setTipo(Boolean.parseBoolean(objetoMap.get("tipo")));
                    mesa.setTipoMesa(objetoMap.get("tipo"));
                    mesa.setEstado("Disponible");
                    mesa.setCoordX(Double.parseDouble(objetoMap.get("coordX")));
                    mesa.setCoordY(Double.parseDouble(objetoMap.get("coordY")));
                    mesa.setVisibilidad(true);



                    Optional<AreaServicio> areaServicioOptional= areaServicioRepository.findById(Integer.parseInt(objetoMap.get("idArea")));
                    if(areaServicioOptional.isPresent()){
                        AreaServicio areaServicio= areaServicioOptional.get();
                        mesa.setAreaServicio(areaServicio);
                    }
                    mesaRepository.save(mesa);
                    return Utils.getResponseEntity("Mesa guardada correctamente.",HttpStatus.OK);


            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validarMap(Map<String, String> objetoMap) {
        return objetoMap.containsKey("idArea") && objetoMap.containsKey("tipo")  && objetoMap.containsKey("coordX") && objetoMap.containsKey("coordY");
    }

    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {

            if(jwtFilter.esAdministrador()) {
            if(objetoMap.containsKey("idMesa")){
                Optional<Mesa> mesaOptional= mesaRepository.findById(Integer.parseInt(objetoMap.get("idMesa")));
                if(mesaOptional.isPresent()){
                    Mesa mesa= mesaOptional.get();
                    mesa.setTipoMesa(objetoMap.get("tipo"));

                    mesaRepository.save(mesa);
                    return Utils.getResponseEntity("Mesa actualizada correctamente.",HttpStatus.OK);
                }
                return Utils.getResponseEntity("La mesa no existe.",HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> mover(Map<String, String> objetoMap) {
        try {
            if(jwtFilter.esAdministrador()) {
            if(objetoMap.containsKey("idMesa") && objetoMap.containsKey("coordX") && objetoMap.containsKey("coordY")){
                Optional<Mesa> mesaOptional= mesaRepository.findById(Integer.parseInt(objetoMap.get("idMesa")));
                if(mesaOptional.isPresent()){
                    if (mesaOptional.get().getEstado().equalsIgnoreCase("Disponible")) {
                        Mesa mesa = mesaOptional.get();
                        mesa.setCoordX(Double.parseDouble(objetoMap.get("coordX")));
                        mesa.setCoordY(Double.parseDouble(objetoMap.get("coordY")));

                        mesaRepository.save(mesa);
                        return Utils.getResponseEntity("La mesa ha sido movido correctamente.", HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("No se puede mover la mesa ya que esta siendo ocupada.", HttpStatus.BAD_REQUEST);
                }
                return Utils.getResponseEntity("La mesa no existe.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public boolean validarArea(Integer id){
        Optional<AreaServicio> areaServicioOptional= areaServicioRepository.findById(id);
        return areaServicioOptional.isPresent();
    }

    public boolean validarMesa(Integer id){
        Optional<Mesa> mesaOptional= mesaRepository.findById(id);
        return mesaOptional.isPresent();
    }

    @Override
    public ResponseEntity<Mesa> obtenerMesaId(Integer id) {
        try {
            if (jwtFilter.esAdministrador()){
                Optional<Mesa> mesaO = mesaRepository.findById(id);
            if (mesaO.isPresent()) {
                Mesa mesa = mesaO.get();
                return new ResponseEntity<Mesa>(mesa, HttpStatus.OK);
            }
            return new ResponseEntity<Mesa>(new Mesa(), HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<Mesa>(new Mesa(),HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Mesa>(new Mesa(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
