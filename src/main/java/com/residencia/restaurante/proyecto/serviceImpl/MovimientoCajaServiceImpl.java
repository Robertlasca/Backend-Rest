package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.entity.Arqueo;
import com.residencia.restaurante.proyecto.entity.MovimientosCaja;
import com.residencia.restaurante.proyecto.repository.IArqueoRepository;
import com.residencia.restaurante.proyecto.repository.ICajaRepository;
import com.residencia.restaurante.proyecto.repository.IMovimientoCajaRepository;
import com.residencia.restaurante.proyecto.service.IMovimientoCajaService;
import com.residencia.restaurante.security.model.Usuario;
import com.residencia.restaurante.security.repository.IUsuarioRepository;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MovimientoCajaServiceImpl implements IMovimientoCajaService {
    @Autowired
    private IMovimientoCajaRepository movimientoCajaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ICajaRepository cajaRepository;

    @Autowired
    private IArqueoRepository arqueoRepository;
    @Override
    public ResponseEntity<List<MovimientosCaja>> filtarXtipoMovimiento(String tipo) {
        try {
            return new ResponseEntity<List<MovimientosCaja>>(movimientoCajaRepository.findAllByTipoMovimientoEqualsIgnoreCase(tipo),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MovimientosCaja>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MovimientosCaja>> obtenerMovimientos() {
        try {
            return new ResponseEntity<List<MovimientosCaja>>(movimientoCajaRepository.findAll(),HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MovimientosCaja>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("cantidad")&& objetoMap.containsKey("motivo") && objetoMap.containsKey("tipo") && objetoMap.containsKey("arqueo_id") && objetoMap.containsKey("usuario_id")){
               if(validarArqueo(Integer.parseInt(objetoMap.get("arqueo_id")))){
                   if(validarEmpleado(Integer.parseInt(objetoMap.get("usuario_id")))){
                       Usuario usuario= obtenerUsuario(Integer.parseInt(objetoMap.get("usuario_id")));
                       Arqueo arqueo=obtenerArqueo(Integer.parseInt(objetoMap.get("arqueo_id")));
                       double cantidad= Double.parseDouble(objetoMap.get("cantidad"));

                       if(cantidad<arqueo.getSaldoFinal()){
                            MovimientosCaja movimientosCaja=new MovimientosCaja();

                            movimientosCaja.setCantidad(cantidad);
                            movimientosCaja.setFechaHoraMovimiento(LocalDateTime.now());
                            movimientosCaja.setTipoMovimiento(objetoMap.get("tipo"));
                            movimientosCaja.setArqueo(arqueo);
                            movimientosCaja.setUsuario(usuario);
                            movimientosCaja.setMotivo(objetoMap.get("motivo"));

                            arqueo.setSaldoFinal(arqueo.getSaldoFinal()-cantidad);

                            movimientoCajaRepository.save(movimientosCaja);
                            arqueoRepository.save(arqueo);
                            return Utils.getResponseEntity("Movimiento agregado correctamente.",HttpStatus.OK);
                       }
                       return Utils.getResponseEntity("No se puede hacer este movimiento ya que no se tiene el suficiente efectivo.",HttpStatus.BAD_REQUEST);

                   }
                   return Utils.getResponseEntity("Existe un problema con el usuario.",HttpStatus.BAD_REQUEST);
               }
               return Utils.getResponseEntity("El arqueo ya no esta abierto.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("id") && objetoMap.containsKey("cantidad")&& objetoMap.containsKey("motivo") && objetoMap.containsKey("tipo") ){
                Optional<MovimientosCaja> movimientosCajaOptional=movimientoCajaRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(movimientosCajaOptional.isPresent()){
                    MovimientosCaja movimientosCaja=movimientosCajaOptional.get();
                    double cantidadActual= movimientosCaja.getCantidad();

                    if(movimientosCaja.getArqueo().isEstadoArqueo()){
                        Arqueo arqueo= movimientosCaja.getArqueo();

                        double cantidadActualizada= arqueo.getSaldoFinal()+cantidadActual;
                        if(cantidadActualizada>Double.parseDouble(objetoMap.get("cantidad"))){
                            movimientosCaja.setMotivo(objetoMap.get("motivo"));
                            movimientosCaja.setTipoMovimiento(objetoMap.get("tipo"));
                            movimientosCaja.setCantidad(Double.parseDouble(objetoMap.get("cantidad")));
                            arqueo.setSaldoFinal(cantidadActualizada-Double.parseDouble(objetoMap.get("cantidad")));

                            arqueoRepository.save(arqueo);
                            movimientoCajaRepository.save(movimientosCaja);

                            return Utils.getResponseEntity("Movimiento actualizado.",HttpStatus.OK);
                        }
                        return Utils.getResponseEntity("La nueva cantidad excede el dinero en caja.",HttpStatus.BAD_REQUEST);


                    }
                    return Utils.getResponseEntity("Lo sentimos el arqueo ya no esta abierto.",HttpStatus.BAD_REQUEST);
                }
                return Utils.getResponseEntity("No hay datos de este registro.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> eliminar(Integer id) {
        try {
            Optional<MovimientosCaja> optionalMovimientosCaja= movimientoCajaRepository.findById(id);
            if(optionalMovimientosCaja.isPresent()){
                MovimientosCaja movimientosCaja=optionalMovimientosCaja.get();
                if(movimientosCaja.getArqueo().isEstadoArqueo()){
                    Arqueo arqueo=movimientosCaja.getArqueo();

                    arqueo.setSaldoFinal(arqueo.getSaldoFinal()+movimientosCaja.getCantidad());
                    movimientoCajaRepository.delete(movimientosCaja);
                    arqueoRepository.save(arqueo);
                    return Utils.getResponseEntity("Eliminado correctamente.",HttpStatus.OK);
                }
                return Utils.getResponseEntity("No puedes realizar esta operación por que el arquep ya no esta abierto",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity("No se encontro información del movimiento",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MovimientosCaja>> filtarXArqueo(Integer id) {
        try {
            return new ResponseEntity<List<MovimientosCaja>>(movimientoCajaRepository.findAllByArqueo_Id(id),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MovimientosCaja>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<MovimientosCaja> obtenerXid(Integer id) {
        try {
            Optional<MovimientosCaja> optionalMovimientosCaja=movimientoCajaRepository.findById(id);
            if(optionalMovimientosCaja.isPresent()){
                MovimientosCaja movimientosCaja=optionalMovimientosCaja.get();
                return new ResponseEntity<MovimientosCaja>(movimientosCaja,HttpStatus.OK);
            }
            return new ResponseEntity<MovimientosCaja>(new MovimientosCaja(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<MovimientosCaja>(new MovimientosCaja(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Arqueo obtenerArqueo(int arqueoId) {
        Optional<Arqueo> arqueoOptional=arqueoRepository.findById(arqueoId);

        return arqueoOptional.get();
    }

    private Usuario obtenerUsuario(int usuarioId) {
        Optional<Usuario> usuarioOptional=usuarioRepository.findById(usuarioId);
        return usuarioOptional.get();
    }

    private boolean validarEmpleado(int usuarioId) {
        Optional<Usuario> optional=usuarioRepository.findById(usuarioId);
        return optional.isPresent();
    }

    private boolean validarArqueo(int arqueoId) {
        Optional<Arqueo> optional= arqueoRepository.findById(arqueoId);
        if (optional.isPresent()){
            if (optional.get().isEstadoArqueo()){
                return true;
            }
            return false;
        }
        return false;
    }
}
