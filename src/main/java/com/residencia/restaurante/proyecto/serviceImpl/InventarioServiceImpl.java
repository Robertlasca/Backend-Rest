package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.MovimientoInventarioDTO;
import com.residencia.restaurante.proyecto.entity.*;
import com.residencia.restaurante.proyecto.repository.IAlmacenRepository;
import com.residencia.restaurante.proyecto.repository.IInventarioRepository;
import com.residencia.restaurante.proyecto.repository.IMateriaPrimaRepository;
import com.residencia.restaurante.proyecto.repository.IMovimiento_InventarioRepository;
import com.residencia.restaurante.proyecto.service.IInventarioService;
import com.residencia.restaurante.proyecto.dto.InventarioDTO;
import com.residencia.restaurante.security.model.Usuario;
import com.residencia.restaurante.security.repository.IUsuarioRepository;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventarioServiceImpl implements IInventarioService {
    @Autowired
    IInventarioRepository inventarioRepository;

    @Autowired
    private IAlmacenRepository almacenRepository;

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Autowired
    IMovimiento_InventarioRepository movimientoInventarioRepository;

    @Autowired
    IMateriaPrimaRepository materiaPrimaRepository;

    /**
     * Lista los inventarios según su estado de stock.
     * @return ResponseEntity<List<InventarioDTO>> Una lista de inventarios con su estado.
     */
    @Override
    public ResponseEntity<List<InventarioDTO>> listarPorStock() {
        try {
            System.out.println("entre 1");
            // Obtener las tres listas de inventarios
            List<Inventario> inventariosMenorMinimo = inventarioRepository.findInventarioByStockActualMenorAlMinimo();

            List<Inventario> inventariosMayorMaximo = inventarioRepository.findInventarioByStockActualMayorAlMaximo();

            List<Inventario> inventariosEntreMinimoYMaximo = inventarioRepository.findInventarioByStockActualEntreMinimoYMaximo();

            // Crear una lista para almacenar los inventarios con su estado
            List<InventarioDTO> inventariosConEstado = new ArrayList<>();
            // Agregar inventarios con estado "Suficiente" a la lista

            // Agregar inventarios con estado "Insuficiente" a la lista
            for (Inventario inventario : inventariosMenorMinimo) {
                InventarioDTO inventarioDTO = new InventarioDTO();
                inventarioDTO.setNombreMateria(inventario.getMateriaPrima().getNombre());
                inventarioDTO.setId(inventario.getId());
                inventarioDTO.setStockActual(inventario.getStockActual());
                inventarioDTO.setCostoUnitario(inventario.getMateriaPrima().getCostoUnitario());
                inventarioDTO.setNombreAlmacen(inventario.getAlmacen().getNombre());
                inventarioDTO.setStockMaximo(inventario.getStockMax());
                inventarioDTO.setStockMinimo(inventario.getStockMin());
                inventarioDTO.setIdAlmacen(inventario.getAlmacen().getId());
                inventarioDTO.setUnidadMedida(inventario.getMateriaPrima().getUnidadMedida());
                inventarioDTO.setCostoTotal(inventario.getStockActual()*inventario.getMateriaPrima().getCostoUnitario());
                if(inventario.getFechaUltimoMovimiento()==null){
                    inventarioDTO.setFechaUtimoMovimiento("Sin fecha");
                }else{
                    inventarioDTO.setFechaUtimoMovimiento(String.valueOf(inventario.getFechaUltimoMovimiento()));
                }
                inventarioDTO.setEstado("Insuficiente");
                inventariosConEstado.add(inventarioDTO);
            }


            // Agregar inventarios con estado "Excedido" a la lista
            for (Inventario inventario : inventariosMayorMaximo) {
                InventarioDTO inventarioDTO = new InventarioDTO();
                inventarioDTO.setNombreMateria(inventario.getMateriaPrima().getNombre());
                inventarioDTO.setId(inventario.getId());
                inventarioDTO.setStockActual(inventario.getStockActual());
                inventarioDTO.setCostoUnitario(inventario.getMateriaPrima().getCostoUnitario());
                inventarioDTO.setNombreAlmacen(inventario.getAlmacen().getNombre());
                inventarioDTO.setStockMaximo(inventario.getStockMax());
                inventarioDTO.setStockMinimo(inventario.getStockMin());
                inventarioDTO.setIdAlmacen(inventario.getAlmacen().getId());
                inventarioDTO.setUnidadMedida(inventario.getMateriaPrima().getUnidadMedida());
                inventarioDTO.setCostoTotal(inventario.getStockActual()*inventario.getMateriaPrima().getCostoUnitario());
                if(inventario.getFechaUltimoMovimiento()==null){
                    inventarioDTO.setFechaUtimoMovimiento("Sin fecha");
                }else{
                    inventarioDTO.setFechaUtimoMovimiento(String.valueOf(inventario.getFechaUltimoMovimiento()));
                }
                inventarioDTO.setEstado("Excedido");
                inventariosConEstado.add(inventarioDTO);
            }


            for (Inventario inventario : inventariosEntreMinimoYMaximo) {
                InventarioDTO inventarioDTO = new InventarioDTO();
                inventarioDTO.setNombreMateria(inventario.getMateriaPrima().getNombre());
                inventarioDTO.setId(inventario.getId());
                inventarioDTO.setStockActual(inventario.getStockActual());
                inventarioDTO.setCostoUnitario(inventario.getMateriaPrima().getCostoUnitario());
                inventarioDTO.setNombreAlmacen(inventario.getAlmacen().getNombre());
                inventarioDTO.setStockMaximo(inventario.getStockMax());
                inventarioDTO.setStockMinimo(inventario.getStockMin());
                inventarioDTO.setIdAlmacen(inventario.getAlmacen().getId());
                inventarioDTO.setUnidadMedida(inventario.getMateriaPrima().getUnidadMedida());
                inventarioDTO.setCostoTotal(inventario.getStockActual()*inventario.getMateriaPrima().getCostoUnitario());
                if(inventario.getFechaUltimoMovimiento()==null){
                    inventarioDTO.setFechaUtimoMovimiento("Sin fecha");
                }else{
                    inventarioDTO.setFechaUtimoMovimiento(String.valueOf(inventario.getFechaUltimoMovimiento()));
                }
                inventarioDTO.setEstado("Suficiente");
                inventariosConEstado.add(inventarioDTO);
            }
            return new ResponseEntity<List<InventarioDTO>>(inventariosConEstado,HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<InventarioDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Agrega stock de una materia prima al inventario.
     * @param objetoMap Un mapa de datos con la información del inventario a agregar.
     * @return ResponseEntity<String> Respuesta que indica el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("almacenId") && objetoMap.containsKey("materiaId") && objetoMap.containsKey("cantidad")){
                Optional<Inventario> optionalOrigen=inventarioRepository.findInventarioByAlmacen_IdAndMateriaPrima_Id(Integer.parseInt(objetoMap.get("almacenId")),Integer.parseInt(objetoMap.get("materiaId")));
                Optional<MateriaPrima> optionalMateriaPrima=materiaPrimaRepository.findById(Integer.parseInt(objetoMap.get("materiaId")));
                if(!optionalOrigen.isEmpty() && !optionalMateriaPrima.isEmpty()){
                    Inventario inventario=optionalOrigen.get();
                    MateriaPrima materiaPrima=optionalMateriaPrima.get();
                    Double cantidad= Double.parseDouble(objetoMap.get("cantidad"));

                    Double stockAnterior=inventario.getStockActual();
                    Double stockActual=inventario.getStockActual()+cantidad;

                    Almacen almacen=inventario.getAlmacen();

                    inventario.setStockActual(stockActual);


                    Movimientos_Inventario movimientosInventario=new Movimientos_Inventario();
                    movimientosInventario.setAlmacen(almacen);
                    movimientosInventario.setStockActual(stockActual);
                    movimientosInventario.setStockAnterior(stockAnterior);
                    movimientosInventario.setTipoMovimiento(objetoMap.get("tipoMovimiento"));
                    if(objetoMap.containsKey("comentario")){
                        movimientosInventario.setComentario(objetoMap.get("comentario"));
                    }

                    movimientosInventario.setNombreMateria(inventario.getMateriaPrima().getNombre());
                    inventario.setFechaUltimoMovimiento(LocalDate.now());
                    Optional<Usuario> usuarioOptional=usuarioRepository.findById(Integer.parseInt(objetoMap.get("usuarioId")));
                    if(!usuarioOptional.isEmpty()){
                        Usuario usuario=usuarioOptional.get();
                        movimientosInventario.setUsuario(usuario);
                    }



                    movimientoInventarioRepository.save(movimientosInventario);

                    inventarioRepository.save(inventario);
                    materiaPrimaRepository.save(materiaPrima);

                    return Utils.getResponseEntity("Agregación exitosa.",HttpStatus.OK);

                }
                return Utils.getResponseEntity("No existen registros.",HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Descontar inventario de un almacén.
     * @param objetoMap Un mapa de datos con la información del inventario a descontar.
     * @return ResponseEntity<String> Respuesta que indica el resultado de la operación.
     */

    @Override
    public ResponseEntity<String> descontar(Map<String, String> objetoMap) {
        try {

           if(objetoMap.containsKey("almacenId")&& objetoMap.containsKey("materiaId") && objetoMap.containsKey("cantidad")){
               Optional<Inventario> optionalOrigen=inventarioRepository.findInventarioByAlmacen_IdAndMateriaPrima_Id(Integer.parseInt(objetoMap.get("almacenId")),Integer.parseInt(objetoMap.get("materiaId")));

               if(!optionalOrigen.isEmpty()){
                   Inventario inventario=optionalOrigen.get();
                   Double cantidad= Double.parseDouble(objetoMap.get("cantidad"));

                   if(cantidad<=inventario.getStockActual() && inventario.getStockActual()!=0){
                       Double stockAnterior=inventario.getStockActual();
                       Double stockActual=inventario.getStockActual()-cantidad;

                       Almacen almacen=inventario.getAlmacen();

                       inventario.setStockActual(stockActual);


                       Movimientos_Inventario movimientosInventario=new Movimientos_Inventario();
                       movimientosInventario.setAlmacen(almacen);
                       movimientosInventario.setStockActual(stockActual);
                       movimientosInventario.setStockAnterior(stockAnterior);
                       movimientosInventario.setNombreMateria(inventario.getMateriaPrima().getNombre());
                       movimientosInventario.setTipoMovimiento(objetoMap.get("tipoMovimiento"));
                       if(objetoMap.containsKey("comentario")){
                           movimientosInventario.setComentario(objetoMap.get("comentario"));
                       }

                       inventario.setFechaUltimoMovimiento(LocalDate.now());

                       Optional<Usuario> usuarioOptional=usuarioRepository.findById(Integer.parseInt(objetoMap.get("usuarioId")));
                       if(!usuarioOptional.isEmpty()){
                           Usuario usuario=usuarioOptional.get();
                           movimientosInventario.setUsuario(usuario);
                       }

                       movimientoInventarioRepository.save(movimientosInventario);

                       inventarioRepository.save(inventario);

                       return Utils.getResponseEntity("Descuento exitoso.",HttpStatus.OK);

                   }
                   return Utils.getResponseEntity("La cantidad a descontar es mayor al stock actual .",HttpStatus.BAD_REQUEST);

               }
               return Utils.getResponseEntity("No existen registros.",HttpStatus.BAD_REQUEST);
           }
           return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Transfiere inventario entre dos almacenes.
     * @param objetoMap Un mapa de datos con la información de la transferencia de inventario.
     * @return ResponseEntity<String> Respuesta que indica el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> transferir(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("idOrigen") && objetoMap.containsKey("idDestino") && objetoMap.containsKey("idMateria")&& objetoMap.containsKey("cantidad") && objetoMap.containsKey("usuarioId")){
                Optional<Inventario> optionalOrigen=inventarioRepository.findInventarioByAlmacen_IdAndMateriaPrima_Id(Integer.parseInt(objetoMap.get("idOrigen")),Integer.parseInt(objetoMap.get("idMateria")));
                Optional<Inventario> optionalDestino=inventarioRepository.findInventarioByAlmacen_IdAndMateriaPrima_Id(Integer.parseInt(objetoMap.get("idDestino")),Integer.parseInt(objetoMap.get("idMateria")));
                if(!optionalOrigen.isEmpty()&&!optionalDestino.isEmpty()){
                    Inventario origen=optionalOrigen.get();
                    origen.setFechaUltimoMovimiento(LocalDate.now());
                    Inventario destino=optionalDestino.get();
                    destino.setFechaUltimoMovimiento(LocalDate.now());
                    Double cantidad= Double.parseDouble(objetoMap.get("cantidad"));

                    if(cantidad<origen.getStockActual()){
                        Double stockAnterior1=origen.getStockActual();
                        Double stockAnterior2=destino.getStockActual();
                        Double stockActual1=origen.getStockActual()-cantidad;
                        Double stockActual2=destino.getStockActual()+cantidad;
                        Almacen almacen1=origen.getAlmacen();
                        Almacen almacen2=destino.getAlmacen();


                        origen.setStockActual(stockActual1);
                        destino.setStockActual(stockActual2);

                        Movimientos_Inventario movimientosInventario1=new Movimientos_Inventario();
                        Movimientos_Inventario movimientosInventario2=new Movimientos_Inventario();
                        movimientosInventario1.setAlmacen(almacen1);
                        movimientosInventario1.setStockActual(stockActual1);
                        movimientosInventario1.setStockAnterior(stockAnterior1);
                        movimientosInventario1.setTipoMovimiento("Salida");
                        movimientosInventario1.setNombreMateria(origen.getMateriaPrima().getNombre());
                        movimientosInventario1.setComentario("Salida por transferencia.");
                        Optional<Usuario> usuarioOptional=usuarioRepository.findById(Integer.parseInt(objetoMap.get("usuarioId")));
                        if(!usuarioOptional.isEmpty()){
                            Usuario usuario=usuarioOptional.get();
                            movimientosInventario1.setUsuario(usuario);
                            movimientosInventario2.setUsuario(usuario);
                        }
                        movimientosInventario2.setAlmacen(almacen2);
                        movimientosInventario2.setStockActual(stockActual2);
                        movimientosInventario2.setStockAnterior(stockAnterior2);
                        movimientosInventario2.setTipoMovimiento("Entrada");
                        movimientosInventario2.setNombreMateria(origen.getMateriaPrima().getNombre());
                        movimientosInventario2.setComentario("Entrada por transferencia");

                        movimientoInventarioRepository.save(movimientosInventario1);
                        movimientoInventarioRepository.save(movimientosInventario2);

                        inventarioRepository.save(origen);
                        inventarioRepository.save(destino);

                        return Utils.getResponseEntity("Transferencia exitosa.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("La cantidad a tranferir es mayor a la stock actual .",HttpStatus.BAD_REQUEST);
                }
                return Utils.getResponseEntity("No existen registros.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Lista las materias primas disponibles en dos almacenes.
     * @param objetoMap Un mapa de datos con la información de los almacenes.
     * @return ResponseEntity<List<MateriaPrima>> Lista de materias primas disponibles en ambos almacenes.
     */
    @Override
    public ResponseEntity<List<MateriaPrima>> listarPorAlmacenes(Map<String, String> objetoMap) {
        try {
            System.out.println("entre 1");
            if(validarMap(objetoMap)){

                // Obtener listas de inventario para cada almacén
                List<Inventario> inventarioAlmacen1 = inventarioRepository.getAllByAlmacen_Id(Integer.parseInt(objetoMap.get("idOrigen")));
                List<Inventario> inventarioAlmacen2 = inventarioRepository.getAllByAlmacen_Id(Integer.parseInt(objetoMap.get("idDestino")));

                // Extraer materias primas de los objetos de inventario
                List<MateriaPrima> materiasPrimasAlmacen1 = inventarioAlmacen1.stream()
                        .map(Inventario::getMateriaPrima)
                        .distinct()
                        .collect(Collectors.toList());
                List<MateriaPrima> materiasPrimasAlmacen2 = inventarioAlmacen2.stream()
                        .map(Inventario::getMateriaPrima)
                        .distinct()
                        .collect(Collectors.toList());

                // Encuentra la intersección (materias primas en ambos almacenes)
                List<MateriaPrima> materiasPrimasEnAmbos = materiasPrimasAlmacen1.stream()
                        .filter(materiasPrimasAlmacen2::contains)
                        .collect(Collectors.toList());


                return new ResponseEntity<List<MateriaPrima>>(materiasPrimasEnAmbos,HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MateriaPrima>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Lista las materias primas disponibles en un almacén.
     * @param id El ID del almacén.
     * @return ResponseEntity<List<MateriaPrima>> Lista de materias primas disponibles en el almacén.
     */
    @Override
    public ResponseEntity<List<MateriaPrima>> listarPorAlmacen(Integer id) {
        try {
                // Obtener listas de inventario para cada almacén
                List<Inventario> inventarioAlmacen = inventarioRepository.getAllByAlmacen_Id(id);
                if(!inventarioAlmacen.isEmpty()){
                    // Extraer materias primas de los objetos de inventario
                    List<MateriaPrima> materiasPrimasAlmacen = inventarioAlmacen.stream()
                            .map(Inventario::getMateriaPrima)
                            .distinct()
                            .collect(Collectors.toList());
                    return new ResponseEntity<List<MateriaPrima>>(materiasPrimasAlmacen,HttpStatus.OK);

                }



                return new ResponseEntity<List<MateriaPrima>>(new ArrayList<>(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MateriaPrima>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene los datos necesarios para realizar una transferencia de inventario.
     * @param objetoMap Un mapa de datos con la información necesaria.
     * @return ResponseEntity<Map<String, String>> Mapa con los datos requeridos para la transferencia.
     */
    @Override
    public ResponseEntity<Map<String, String>> obtenerDatosTransferencia(Map<String, String> objetoMap) {
        Map<String, String> datos = new HashMap<>();
        try {
            if(validarMapTransferir(objetoMap)){
                Optional<Inventario> optionalOrigen=inventarioRepository.findInventarioByAlmacen_IdAndMateriaPrima_Id(Integer.parseInt(objetoMap.get("idOrigen")),Integer.parseInt(objetoMap.get("idMateria")));
                Optional<Inventario> optionalDestino=inventarioRepository.findInventarioByAlmacen_IdAndMateriaPrima_Id(Integer.parseInt(objetoMap.get("idDestino")),Integer.parseInt(objetoMap.get("idMateria")));
                Optional<MateriaPrima> optionalMateriaPrima=materiaPrimaRepository.findById(Integer.parseInt(objetoMap.get("idMateria")));
                if(!optionalOrigen.isEmpty() && !optionalDestino.isEmpty() && !optionalMateriaPrima.isEmpty()){
                    MateriaPrima materiaPrima=optionalMateriaPrima.get();
                    Inventario origen=optionalOrigen.get();
                    Inventario destino=optionalDestino.get();

                    Map<String, String> datosTransferencia = new HashMap<>();
                    datosTransferencia.put("nombreMateria", materiaPrima.getNombre());
                    datosTransferencia.put("costoUnitario", String.valueOf(materiaPrima.getCostoUnitario()));
                    datosTransferencia.put("unidadMedida", materiaPrima.getUnidadMedida());
                    datosTransferencia.put("stockOrigen", String.valueOf(origen.getStockActual()));
                    datosTransferencia.put("stockDestino", String.valueOf(destino.getStockActual()));

                    return new ResponseEntity<Map<String,String>>(datosTransferencia,HttpStatus.OK);


                }
            }
            return new ResponseEntity<Map<String, String>>(datos,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Map<String, String>>(datos,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene los datos de un inventario específico.
     * @param objetoMap Un mapa de datos con la información necesaria.
     * @return ResponseEntity<Map<String, String>> Mapa con los datos del inventario.
     */
    @Override
    public ResponseEntity<Map<String, String>> obtenerDatos(Map<String, String> objetoMap) {
        Map<String, String> datos = new HashMap<>();
        System.out.println("entre2");
        try {
            if(objetoMap.containsKey("almacenId") && objetoMap.containsKey("materiaId")){
                System.out.println("entre3");
                Optional<Inventario> optionalOrigen=inventarioRepository.findInventarioByAlmacen_IdAndMateriaPrima_Id(Integer.parseInt(objetoMap.get("almacenId")),Integer.parseInt(objetoMap.get("materiaId")));
                Optional<MateriaPrima> optionalMateriaPrima=materiaPrimaRepository.findById(Integer.parseInt(objetoMap.get("materiaId")));
                if(!optionalOrigen.isEmpty()  && !optionalMateriaPrima.isEmpty()){
                    System.out.println("entre1");
                    MateriaPrima materiaPrima=optionalMateriaPrima.get();
                    Inventario origen=optionalOrigen.get();

                    Map<String, String> datosDescontar = new HashMap<>();
                    datosDescontar.put("idMateria", String.valueOf(materiaPrima.getId()));
                    datosDescontar.put("nombreMateria", materiaPrima.getNombre());
                    datosDescontar.put("costoUnitario", String.valueOf(materiaPrima.getCostoUnitario()));
                    datosDescontar.put("unidadMedida", materiaPrima.getUnidadMedida());
                    datosDescontar.put("stockOrigen", String.valueOf(origen.getStockActual()));

                    return new ResponseEntity<Map<String,String>>(datosDescontar,HttpStatus.OK);


                }
            }
            return new ResponseEntity<Map<String, String>>(datos,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Map<String, String>>(datos,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Inventario>> obtenerMateriasXCocinaID(Integer id) {
        try {

            Optional<Almacen> optional= almacenRepository.findAlmacenByCocina_Id(id);
            if(optional.isPresent()){
                List<Inventario> inventarioList= inventarioRepository.getAllByAlmacen_Id(optional.get().getId());
                List<Inventario> inventarios= new ArrayList<>();

                for (Inventario inventario:inventarioList) {
                    if(inventario.getMateriaPrima().isVisibilidad()){
                        inventarios.add(inventario);
                    }

                }
                return new ResponseEntity<List<Inventario>>(inventarios,HttpStatus.OK);
            }
            return new ResponseEntity<List<Inventario>>(new ArrayList<>(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Inventario>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MovimientoInventarioDTO>> listarMovimientos() {
        try {
            List<Movimientos_Inventario> movimientosInventarios= movimientoInventarioRepository.findAll();
            List<MovimientoInventarioDTO> movimientoInventarioDTOS= new ArrayList<>();
            System.out.println("Cantidad de movimientos:"+movimientosInventarios.size());
            for (Movimientos_Inventario movimientosInventario: movimientosInventarios) {
                MovimientoInventarioDTO movimientoInventarioDTOO=new MovimientoInventarioDTO();
                movimientoInventarioDTOO.setFecha(movimientosInventario.getFechaMovimiento());
                movimientoInventarioDTOO.setStockAnterior(movimientosInventario.getStockAnterior());
                movimientoInventarioDTOO.setStockActual(movimientosInventario.getStockActual());
                movimientoInventarioDTOO.setDiferencia(movimientosInventario.getStockActual()-movimientosInventario.getStockAnterior());
                movimientoInventarioDTOO.setNombreAlmacen(movimientosInventario.getAlmacen().getNombre());
                movimientoInventarioDTOO.setNombreMateria(movimientosInventario.getNombreMateria());
                movimientoInventarioDTOS.add(movimientoInventarioDTOO);


            }

            return new ResponseEntity<List<MovimientoInventarioDTO>>(movimientoInventarioDTOS,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MovimientoInventarioDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validarMap(Map<String,String> objetoMap){
        return objetoMap.containsKey("idOrigen") && objetoMap.containsKey("idDestino");
    }

    private boolean validarMapTransferir(Map<String,String> objetoMap){
        return objetoMap.containsKey("idOrigen") && objetoMap.containsKey("idDestino") && objetoMap.containsKey("idMateria");
    }
}
