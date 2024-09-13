package com.residencia.restaurante.proyecto.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.ComandaDTO;
import com.residencia.restaurante.proyecto.dto.DetalleOrdenProductoDTO;
import com.residencia.restaurante.proyecto.dto.OrdenDTO;
import com.residencia.restaurante.proyecto.dto.ProductoDto;
import com.residencia.restaurante.proyecto.entity.*;
import com.residencia.restaurante.proyecto.repository.*;
import com.residencia.restaurante.proyecto.service.IComanderoService;
import com.residencia.restaurante.proyecto.wrapper.DetalleOrdenWrapper;
import com.residencia.restaurante.proyecto.wrapper.Venta_MedioPagoWrapper;
import com.residencia.restaurante.security.model.Usuario;
import com.residencia.restaurante.security.repository.IUsuarioRepository;
import com.residencia.restaurante.security.utils.TicketComanda;
import com.residencia.restaurante.security.utils.TicketOrden;
import com.residencia.restaurante.security.utils.Utils;
import com.residencia.restaurante.security.utils.ValidarStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ComanderoServiceImpl implements IComanderoService {
    @Autowired
    private IArqueoRepository arqueoRepository;
    @Autowired
    private IOrdenRepository ordenRepository;
    @Autowired
    private IDetalleOrden_MenuRepository ordenMenuRepository;
    @Autowired
    private IDetalleOrden_ProductoNormalRepository detalleOrdenProductoNormalRepository;
    @Autowired
    private FolioService folioService;
    @Autowired
    private ICajaRepository cajaRepository;
    @Autowired
    private IProductoTerminado_MenuRepository productoTerminadoMenuRepository;
    @Autowired
    private IProductoTerminadoRepository productoTerminadoRepository;
    @Autowired
    private IDetalleOrden_MenuRepository detalleOrdenMenuRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private IMesaRepository mesaRepository;
    @Autowired
    private IInventarioRepository inventarioRepository;
    @Autowired
    private IMateriaPrima_MenuRepository materiaPrimaMenuRepository;
    @Autowired
    private IProductoNormalRepository productoNormalRepository;
    @Autowired
    private IMenuRepository menuRepository;
    @Autowired
    private  IImpresoraRepository impresoraRepository;
    @Autowired
    private IVenta_MedioPagoRepository ventaMedioPagoRepository;
    @Autowired
    private IMedioPagoRepository medioPagoRepository;
    @Autowired
    private IVentaRepository ventaRepository;
    @Autowired
    private ICocinaRepository cocinaRepository;
    @Override
    public ResponseEntity<Orden> abrirOrden(Map<String, String> objetoMap) {
       try {
           if(objetoMap.containsKey("idMesa") && objetoMap.containsKey("idUsuario") && objetoMap.containsKey("numComensales") && objetoMap.containsKey("idCaja")){
               Optional<Mesa> optionalMesa= mesaRepository.findById(Integer.parseInt(objetoMap.get("idMesa")));
               Optional<Usuario> optionalUsuario=usuarioRepository.findById(Integer.parseInt(objetoMap.get("idUsuario")));
               Optional<Caja> optionalCaja=cajaRepository.findById(Integer.parseInt(objetoMap.get("idCaja")));
               Orden orden= new Orden();
               if (!optionalMesa.get().getEstado().equalsIgnoreCase("Disponible")){
                   return new ResponseEntity<Orden>(new Orden(),HttpStatus.BAD_REQUEST);
               }
               optionalMesa.ifPresent(orden::setMesa);
               Mesa mesa=optionalMesa.get();
               mesa.setEstado("Ocupada");
               mesaRepository.save(mesa);
               optionalUsuario.ifPresent(orden::setUsuario);
               optionalCaja.ifPresent(orden::setCaja);
               if(objetoMap.containsKey("nombreCliente")){
                   orden.setNombreCliente(objetoMap.get("nombreCliente"));
               }
               orden.setEstado("En curso");
               Integer lastFolio = ordenRepository.findMaxFolio();
               int newFolio = (lastFolio != null) ? lastFolio + 1 : 1;
               orden.setFolio(newFolio);
               orden.setCantidadComensal(Integer.parseInt(objetoMap.get("numComensales")));
               ordenRepository.save(orden);
               return new ResponseEntity<Orden>(orden,HttpStatus.OK);
           }
           return new ResponseEntity<Orden>(new Orden(),HttpStatus.BAD_REQUEST);
       }catch (Exception e){
           e.printStackTrace();
       }
       return new ResponseEntity<Orden>(new Orden(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
//Imprimir y enviar a cocina
    @Override
    public ResponseEntity<String> asignarPlatillos(Map<String, String> objetoMap) {
        try{
            if(objetoMap.containsKey("idOrden") && objetoMap.containsKey("detalleOrden")){
              if(validarStock(objetoMap.get("detalleOrden")).equalsIgnoreCase("suficiente")){
                Optional<Orden> optional=ordenRepository.findById(Integer.parseInt(objetoMap.get("idOrden")));
                if (optional.isPresent()){
                    Orden orden= optional.get();
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<DetalleOrdenWrapper> detalleOrdenWrappers = objectMapper.readValue(objetoMap.get("detalleOrden"), new TypeReference<List<DetalleOrdenWrapper>>() {});

                    Set<Integer> cocinas = new HashSet<>();
                    Map<Integer, List<String>> productosPorCocina = new HashMap<>();

                    if(!detalleOrdenWrappers.isEmpty()) {
                        if (validarStock(objetoMap.get("detalleOrden")).equalsIgnoreCase("Si hay suficiente stock")){
                            for (DetalleOrdenWrapper detalleOrdenWrapper : detalleOrdenWrappers) {
                                if (detalleOrdenWrapper.getIsMenu().equalsIgnoreCase("esDetalleOrdenMenu")) {
                                    Optional<DetalleOrdenMenu> detalleOrdenMenuOptional = detalleOrdenMenuRepository.findById(detalleOrdenWrapper.getIdProducto());
                                    if (detalleOrdenMenuOptional.isPresent()) {
                                        //Solo dos operaciones sumar o restar
                                        DetalleOrdenMenu detalleOrdenMenu = detalleOrdenMenuOptional.get();
                                        Integer cocinaId = 90000;
                                        //Para el caso en el que se agrega a la nueva comanda
                                        if (detalleOrdenMenu.getCantidad() < detalleOrdenWrapper.getCantidad()) {
                                            cocinaId = detalleOrdenMenu.getMenu().getCocina().getId();
                                            int cantidad = detalleOrdenWrapper.getCantidad()-detalleOrdenMenu.getCantidad() ;
                                            // Añadir el producto a la lista correspondiente a la cocina

                                            List<String> productos = productosPorCocina.get(cocinaId);
                                            String productoDetalle =cantidad+"  "+ detalleOrdenMenu.getMenu().getNombre()  +"  "+ detalleOrdenWrapper.getComentario();
                                            productos.add(productoDetalle); // O cualquier otra propiedad del menú que represente al

                                        }
                                        descontarOAgregarStockMenu(detalleOrdenMenu.getMenu(), detalleOrdenMenu, detalleOrdenWrapper.getCantidad());
                                    }
                                }
                                if (detalleOrdenWrapper.getIsMenu().equalsIgnoreCase("esMenu")) {
                                    Optional<Menu> menuOptional = menuRepository.findById(detalleOrdenWrapper.getIdProducto());
                                    Menu menu = new Menu();
                                    Integer cocinaId = 9000;
                                    if (menuOptional.isPresent()) {
                                        menu = menuOptional.get();
                                        cocinaId = menuOptional.get().getCocina().getId();
                                    }

                                    if (!cocinas.contains(menu.getCocina().getId())) {
                                        cocinas.add(cocinaId);
                                        productosPorCocina.put(cocinaId, new ArrayList<>());
                                    }

                                    // Añadir el producto a la lista correspondiente a la cocina
                                    List<String> productos = productosPorCocina.get(cocinaId);
                                    String productoDetalle = detalleOrdenWrapper.getCantidad()+"   "+menu.getNombre() +  "   "+detalleOrdenWrapper.getComentario();
                                    productos.add(productoDetalle); // O cualquier otra propiedad del menú que represente al producto

                                    //Verificamos la existencia de la relación de una orden y un menú mediante su id
                                    //Optional<DetalleOrdenMenu> detalleOrdenMenuOptional = detalleOrdenMenuRepository.findDetalleOrdenMenuByOrden_IdAndMenu_Id(orden.getId(), detalleOrdenWrapper.getIdProducto());
                                    //if (detalleOrdenMenuOptional.isPresent()) {
                                       // DetalleOrdenMenu detalleOrdenMenu = detalleOrdenMenuOptional.get();
                                        //detalleOrdenMenu.setCantidad(detalleOrdenMenu.getCantidad() + detalleOrdenWrapper.getCantidad());
                                        //detalleOrdenMenuRepository.save(detalleOrdenMenu);
                                        //descontarStockMenu(menu, detalleOrdenMenu);

                                    //} else {
                                        //Verificar si es un menu con producto terminados o
                                        DetalleOrdenMenu detalleOrdenMenu = new DetalleOrdenMenu();

                                        detalleOrdenMenu.setMenu(menu);
                                        detalleOrdenMenu.setOrden(orden);
                                        detalleOrdenMenu.setCantidad(detalleOrdenWrapper.getCantidad());
                                        detalleOrdenMenu.setTotal(menu.getPrecioVenta() * detalleOrdenWrapper.getCantidad());
                                        detalleOrdenMenu.setComentario(detalleOrdenWrapper.getComentario());
                                        detalleOrdenMenu.setEstado("En espera");
                                        detalleOrdenMenu.setNombreMenu(menu.getNombre());
                                        detalleOrdenMenu.setPrecioMenu(menu.getPrecioVenta());
                                        detalleOrdenMenuRepository.save(detalleOrdenMenu);
                                        descontarStockMenu(menu, detalleOrdenMenu);
                                    //}

                                }



                            }


                        if (imprimirComandas(productosPorCocina, orden.getNombreCliente(), orden.getMesa().getAreaServicio().getNombre()+orden.getMesa().getNombre(), orden.getUsuario().getNombre()) == 200) {
                            return Utils.getResponseEntity("Impreso correctamente", HttpStatus.OK);
                        } else{
                          //  return Utils.getResponseEntity("Sucedio un problema al imprimir el ticket.", HttpStatus.BAD_REQUEST);
                        }

                    }
                        return Utils.getResponseEntity(validarStock(objetoMap.get("detalleOrden")),HttpStatus.BAD_REQUEST);


                    }
                    return Utils.getResponseEntity("Error al obtener los detalles de la orden.",HttpStatus.BAD_REQUEST);

            }
                return Utils.getResponseEntity("La orden no existe.",HttpStatus.BAD_REQUEST);


                }

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void descontarOAgregarStockProductoNormal(ProductoNormal productoNormal, DetalleOrden_ProductoNormal detalleOrdenProductoNormal1, int cantidad) {
        int cantidadRegresada=detalleOrdenProductoNormal1.getCantidad();
        int cantidadQuitar= cantidad;

        ProductoNormal productoNormal1=productoNormal;
        productoNormal1.setStockActual((productoNormal1.getStockActual()+cantidadRegresada)-cantidadQuitar);
        productoNormalRepository.save(productoNormal1);
    }

    private  int imprimirComandas(Map<Integer, List<String>> productosPorCocina,String cliente,String areaServicio,String usuario) throws IOException, InterruptedException {
        HttpResponse<String> response=null;
        for (Map.Entry<Integer, List<String>> entry : productosPorCocina.entrySet()) {
            //El número de veces que quieres que e imprima la comanda
                Integer cocinaId = entry.getKey();
                List<String> productos = entry.getValue();
                System.out.println("Cocina ID: " + cocinaId);
                System.out.println("Productos: " + productos);
                TicketComanda ticket = new TicketComanda(cliente, areaServicio, usuario, productos);
                String ticketContent = ticket.getContentTicket();
                // ticket.print(selectedService);
                Optional<Cocina> cocinaOptional= cocinaRepository.findById(cocinaId);
                Optional<Impresora> impresoraOptional1= impresoraRepository.getImpresoraByPorDefectoTrue();
                Map<String, String> printRequest = new HashMap<>();
                printRequest.put("ticketContent", ticketContent);
                if(cocinaOptional.isPresent()){
                    printRequest.put("nombreIm",cocinaOptional.get().getImpresora().getNombre());
                    //if(cocinaOptional.get().getImpresora().getDireccionIp()!=null||cocinaOptional.get().getImpresora().getDireccionIp().length()!=0 ){
                      //  printRequest.put("printerIp",cocinaOptional.get().getImpresora().getDireccionIp());
                    //}

                }

            String printRequestJson = new ObjectMapper().writeValueAsString(printRequest);

            // Configuración de la solicitud HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8082/print")) // Usa http
                    .POST(HttpRequest.BodyPublishers.ofString(printRequestJson, StandardCharsets.UTF_8))
                    .header("Content-Type", "application/json")
                    .build();

            // Envío de la solicitud y obtención de la respuesta
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        if(response!=null){
            return response.statusCode();
        }
        return 0;
    }

    private void descontarOAgregarStockMenu(Menu menu, DetalleOrdenMenu detalleOrdenMenu, int cantidad){
        List<ProductoTerminado_Menu> productoTerminadoMenus= productoTerminadoMenuRepository.getAllByMenu(menu);
        List<MateriaPrima_Menu> materiaPrimaMenus=materiaPrimaMenuRepository.getAllByMenu(menu);
            if(!productoTerminadoMenus.isEmpty()){
                for (ProductoTerminado_Menu productoTerminadoMenu:productoTerminadoMenus){
                    double cantidadRegresada= detalleOrdenMenu.getCantidad()*productoTerminadoMenu.getCantidad();
                    double cantidadQuitar= cantidad*productoTerminadoMenu.getCantidad();
                    ProductoTerminado productoTerminado= productoTerminadoMenu.getProductoTerminado();
                    productoTerminado.setStockActual((productoTerminado.getStockActual()+cantidadRegresada)-cantidadQuitar);
                    productoTerminadoRepository.save(productoTerminado);
                }
            }
            if(!materiaPrimaMenus.isEmpty()){
                for (MateriaPrima_Menu materiaPrimaMenu:materiaPrimaMenus) {
                    double cantidadRegresada= detalleOrdenMenu.getCantidad()*materiaPrimaMenu.getCantidad();
                    Inventario inventario= materiaPrimaMenu.getInventario();
                    inventario.setStockActual(inventario.getStockActual()-cantidadRegresada);
                    inventarioRepository.save(inventario);
                }
            }

            detalleOrdenMenu.setEstado("En espera");
            detalleOrdenMenuRepository.save(detalleOrdenMenu);
    }

    private void descontarStockMenu(Menu menu,DetalleOrdenMenu detalleOrdenMenu){
        List<ProductoTerminado_Menu> productoTerminadoMenus= productoTerminadoMenuRepository.getAllByMenu(menu);
        List<MateriaPrima_Menu> materiaPrimaMenus=materiaPrimaMenuRepository.getAllByMenu(menu);
        if(!productoTerminadoMenus.isEmpty()){
            for (ProductoTerminado_Menu productoTerminadoMenu:productoTerminadoMenus){
                double cantidadRegresada= detalleOrdenMenu.getCantidad()*productoTerminadoMenu.getCantidad();
                ProductoTerminado productoTerminado= productoTerminadoMenu.getProductoTerminado();
                productoTerminado.setStockActual(productoTerminado.getStockActual()-cantidadRegresada);
                productoTerminadoRepository.save(productoTerminado);
            }
        }
        if(!materiaPrimaMenus.isEmpty()){
            for (MateriaPrima_Menu materiaPrimaMenu:materiaPrimaMenus) {
                double cantidadRegresada= detalleOrdenMenu.getCantidad()*materiaPrimaMenu.getCantidad();
                Inventario inventario= materiaPrimaMenu.getInventario();
                inventario.setStockActual(inventario.getStockActual()-cantidadRegresada);
                inventarioRepository.save(inventario);
            }
        }
    }

    public String validarStock(String detalleOrdenJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        ValidarStock validarStock= new ValidarStock();
        ValidarStock validarStockTerminado= new ValidarStock();
        ValidarStock validarStockProductoNormal= new ValidarStock();
        try {
            List<DetalleOrdenWrapper> detalleOrdenWrappers = objectMapper.readValue(detalleOrdenJson, new TypeReference<List<DetalleOrdenWrapper>>() {});
            for (DetalleOrdenWrapper detalle : detalleOrdenWrappers) {

                if (detalle.getIsMenu().equalsIgnoreCase("esMenu")) {

                    List<MateriaPrima_Menu> ingredientesMenu = materiaPrimaMenuRepository.findByMenuId(detalle.getIdProducto());
                    List<ProductoTerminado_Menu> productoTerminadoMenus = productoTerminadoMenuRepository.findAllByMenuId(detalle.getIdProducto());

                    if (!ingredientesMenu.isEmpty()) {


                    for (MateriaPrima_Menu ingrediente : ingredientesMenu) {
                        int inventarioId = ingrediente.getInventario().getId();
                        double descontandoStock = ingrediente.getInventario().getStockActual() - (ingrediente.getCantidad() * detalle.getCantidad());
                        if (descontandoStock <= 0) {
                            return "No hay suficiente stock para preparar los platillos" + ingrediente.getMenu().getNombre() + ". Ya que no hay suficiente stock en el inventario en el almacen" + ingrediente.getInventario().getAlmacen().getNombre() + ".Hace falta ingresar mas " + ingrediente.getInventario().getMateriaPrima().getNombre();
                        } else {
                            if (validarStock.agregarSiNoExiste(ingrediente.getInventario().getId(), descontandoStock)) {

                            } else {
                                if (validarStock.obtenerCantidadPorId(ingrediente.getInventario().getId()) - (ingrediente.getCantidad() * detalle.getCantidad()) > 0) {
                                    validarStock.actualizar(ingrediente.getInventario().getId(), validarStock.obtenerCantidadPorId(ingrediente.getInventario().getId()) - (ingrediente.getCantidad() * detalle.getCantidad()));
                                } else {
                                    return "No hay suficiente stock para preparar los platillos" + ingrediente.getMenu().getNombre() + ". Ya que no hay suficiente stock en el inventario en el almacen" + ingrediente.getInventario().getAlmacen().getNombre() + ".Hace falta ingresar mas " + ingrediente.getInventario().getMateriaPrima().getNombre();
                                }

                            }
                        }

                    }
                }
                    //Validar stock para los productos terminados
                    if(!productoTerminadoMenus.isEmpty()){
                        for (ProductoTerminado_Menu ingrediente : productoTerminadoMenus) {
                            double descontandoStock = ingrediente.getProductoTerminado().getStockActual()-(ingrediente.getCantidad() * detalle.getCantidad());
                            System.out.println("Cantidad descontando"+descontandoStock);
                            if(descontandoStock<=1){
                                System.out.println("Entre 1");
                                return "No hay suficiente stock para preparar los platillos"+ingrediente.getMenu().getNombre()+". Ya que no hay suficiente stock en el producto terminado"+ingrediente.getProductoTerminado().getNombre();
                            }else {
                                System.out.println("Entre 2");
                                if(validarStockTerminado.agregarSiNoExiste(ingrediente.getProductoTerminado().getId(),descontandoStock)){
                                    System.out.println("Entre 4");

                                }else {
                                    System.out.println("Entre 5");
                                    System.out.println("Esta es la cantidad obtenida"+(validarStockTerminado.obtenerCantidadPorId(ingrediente.getProductoTerminado().getId())-(ingrediente.getCantidad() * detalle.getCantidad())));
                                    if (validarStockTerminado.obtenerCantidadPorId(ingrediente.getProductoTerminado().getId())-(ingrediente.getCantidad() * detalle.getCantidad()) >1){
                                        validarStockTerminado.actualizar(ingrediente.getProductoTerminado().getId(), validarStockTerminado.obtenerCantidadPorId(ingrediente.getProductoTerminado().getId())-(ingrediente.getCantidad() * detalle.getCantidad()));
                                    }else{
                                        return "No hay suficiente stock para preparar los platillos"+ingrediente.getMenu().getNombre()+". Ya que no hay suficiente stock en el producto terminado"+ingrediente.getProductoTerminado().getNombre();
                                    }

                                }
                            }

                        }

                    }
                }

                if(detalle.getIsMenu().equalsIgnoreCase("esDetalleMenu")){
                    Optional<DetalleOrdenMenu> detalleOrdenMenuOptional= detalleOrdenMenuRepository.findById(detalle.getIdProducto());
                    if(detalleOrdenMenuOptional.isPresent()){
                        DetalleOrdenMenu detalleOrdenMenu = detalleOrdenMenuOptional.get();
                        int cantidad= detalle.getCantidad()-detalleOrdenMenu.getCantidad();
                        //Si ya esta agregado ya no se verifica
                        if(cantidad>0) {
                            List<MateriaPrima_Menu> ingredientesMenu = materiaPrimaMenuRepository.findByMenuId(detalleOrdenMenu.getMenu().getId());
                            List<ProductoTerminado_Menu> productoTerminadoMenus= productoTerminadoMenuRepository.findAllByMenuId(detalleOrdenMenu.getMenu().getId());
                            if(!ingredientesMenu.isEmpty()){

                            for (MateriaPrima_Menu ingrediente : ingredientesMenu) {
                                double descontandoStock = ingrediente.getInventario().getStockActual()-(ingrediente.getCantidad() * cantidad);
                                if(descontandoStock<=0){
                                    return "No hay suficiente stock para preparar los platillos"+ingrediente.getMenu().getNombre()+". Ya que no hay suficiente stock en el inventario en el almacen"+ingrediente.getInventario().getAlmacen().getNombre()+".Hace falta ingresar mas "+ingrediente.getInventario().getMateriaPrima().getNombre();
                                }else {
                                    if(validarStock.agregarSiNoExiste(ingrediente.getInventario().getId(),descontandoStock)){

                                    }else {
                                        if (validarStock.obtenerCantidadPorId(ingrediente.getInventario().getId())-(ingrediente.getCantidad() * cantidad) >0){
                                            validarStock.actualizar(ingrediente.getInventario().getId(), validarStock.obtenerCantidadPorId(ingrediente.getInventario().getId())-(ingrediente.getCantidad() * cantidad));
                                        }else{
                                            return "No hay suficiente stock para preparar los platillos"+ingrediente.getMenu().getNombre()+". Ya que no hay suficiente stock en el inventario en el almacen"+ingrediente.getInventario().getAlmacen().getNombre()+".Hace falta ingresar mas "+ingrediente.getInventario().getMateriaPrima().getNombre();
                                        }

                                    }
                                }

                            }

                            }
                            //producto terminado
                            if(!productoTerminadoMenus.isEmpty()){
                                for (ProductoTerminado_Menu ingrediente : productoTerminadoMenus) {
                                    int inventarioId = ingrediente.getProductoTerminado().getId();
                                    double descontandoStock = ingrediente.getProductoTerminado().getStockActual()-(ingrediente.getCantidad() * cantidad);
                                    if(descontandoStock<=0){
                                        return "No hay suficiente stock para preparar los platillos"+ingrediente.getMenu().getNombre()+". Ya que no hay suficiente stock en el producto terminado"+ingrediente.getProductoTerminado().getNombre();
                                    }else {
                                        if(validarStockTerminado.agregarSiNoExiste(ingrediente.getProductoTerminado().getId(),descontandoStock)){
                                        }else {
                                            if (validarStockTerminado.obtenerCantidadPorId(ingrediente.getProductoTerminado().getId())-(ingrediente.getCantidad() * cantidad) >0){
                                                validarStockTerminado.actualizar(ingrediente.getProductoTerminado().getId(), validarStockTerminado.obtenerCantidadPorId(ingrediente.getProductoTerminado().getId())-(ingrediente.getCantidad() * cantidad));
                                            }else{
                                                return "No hay suficiente stock para preparar los platillos"+ingrediente.getMenu().getNombre()+". Ya que no hay suficiente stock en el producto terminado"+ingrediente.getProductoTerminado().getNombre();
                                            }

                                        }
                                    }

                                }
                            }

                        }

                    }
                }

                if (detalle.getIsMenu().equalsIgnoreCase("esProductoNormal")){
                    Optional<ProductoNormal> productoNormalOptional= productoNormalRepository.findById(detalle.getIdProducto());
                    if(productoNormalOptional.isPresent()){
                        ProductoNormal productoNormal= productoNormalOptional.get();

                        double descontandoStock = productoNormal.getStockActual()-detalle.getCantidad();
                        if(descontandoStock<=0){
                            return "No hay suficiente stock para el producto"+productoNormal.getNombre();
                        }else {
                            if(validarStockProductoNormal.agregarSiNoExiste(productoNormal.getId(),descontandoStock)){

                            }else {
                                if ((validarStockProductoNormal.obtenerCantidadPorId(productoNormal.getId())-detalle.getCantidad()) >0){
                                    validarStockProductoNormal.actualizar(productoNormal.getId(), validarStock.obtenerCantidadPorId(productoNormal.getId())-detalle.getCantidad());
                                }else{
                                    return "No hay suficiente stock para el producto"+productoNormal.getNombre();
                                }

                            }
                        }

                    }
                }

                if(detalle.getIsMenu().equalsIgnoreCase("esDetalleOrdenProducto")){
                    Optional<DetalleOrden_ProductoNormal> detalleOrdenProductoOptional= detalleOrdenProductoNormalRepository.findById(detalle.getIdProducto());
                    if(detalleOrdenProductoOptional.isPresent()){
                        DetalleOrden_ProductoNormal detalleOrdenProductoNormal= detalleOrdenProductoOptional.get();
                        ProductoNormal productoNormal= detalleOrdenProductoNormal.getProductoNormal();
                        int cantidad = detalle.getCantidad()-detalleOrdenProductoNormal.getCantidad();
                        //Si ya esta agregado ya no se verifica
                        if(cantidad>0) {

                            double descontandoStock = productoNormal.getStockActual()-cantidad;
                            if(descontandoStock<=0){
                                return "No hay suficiente stock para el producto"+productoNormal.getNombre();
                            }else {
                                if(validarStockProductoNormal.agregarSiNoExiste(productoNormal.getId(),descontandoStock)){

                                }else {
                                    if ((validarStockProductoNormal.obtenerCantidadPorId(productoNormal.getId())-cantidad) >0){
                                        validarStockProductoNormal.actualizar(productoNormal.getId(), validarStock.obtenerCantidadPorId(productoNormal.getId())-cantidad);
                                    }else{
                                        return "No hay suficiente stock para el producto"+productoNormal.getNombre();
                                    }

                                }
                            }


                        }
                    }
                }
            }
            return "Si hay suficiente stock";


        } catch (Exception e) {
            e.printStackTrace();
            return "Error al procesar el pedido.";
        }

    }


    @Override
    public ResponseEntity<List<ProductoDto>> obtenerProductos() {
        try {
            List<ProductoDto> productoDtos=new ArrayList<>();
            List<ProductoNormal> productoNormalList= productoNormalRepository.getAllByVisibilidadTrue();
            List<Menu> menuList=menuRepository.getAllByVisibilidadTrue();
            if(!productoNormalList.isEmpty()){
                for (ProductoNormal productoNormal:productoNormalList) {
                    ProductoDto productoDto= new ProductoDto();
                    productoDto.setId(productoNormal.getId());
                    productoDto.setNombre(productoNormal.getNombre());
                    productoDto.setImagen(productoNormal.getImagen());
                    productoDto.setPrecio(productoNormal.getPrecioUnitario());
                    productoDto.setMenu(false);
                    productoDtos.add(productoDto);
                }
            }
            if(!menuList.isEmpty()){
                for (Menu menu:menuList) {
                    ProductoDto productoDto= new ProductoDto();
                    productoDto.setId(menu.getId());
                    productoDto.setNombre(menu.getNombre());
                    productoDto.setImagen(menu.getImagen());
                    productoDto.setPrecio(menu.getPrecioVenta());
                    productoDto.setMenu(true);
                    productoDtos.add(productoDto);
                }
            }
            return new ResponseEntity<List<ProductoDto>>(productoDtos,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductoDto>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<List<Orden>> obtenerOrdenes() {
        try {
            return new ResponseEntity<List<Orden>>(ordenRepository.findAll(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Orden>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<ProductoDto> obtenerProducto(Map<String, String> objetoMap) {
        try{
            if(objetoMap.containsKey("id") && objetoMap.containsKey("esMenu")){
                Integer id= Integer.parseInt(objetoMap.get("id"));
                ProductoDto productoDto= new ProductoDto();
                if(objetoMap.get("esMenu").equalsIgnoreCase("true")){
                    Optional<Menu> menuOptional= menuRepository.findById(id);
                    if(menuOptional.isPresent()){
                        Menu menu= menuOptional.get();
                        productoDto.setMenu(true);
                        productoDto.setId(menu.getId());
                        productoDto.setNombre(menu.getNombre());
                        productoDto.setImagen(menu.getImagen());
                        productoDto.setPrecio(menu.getPrecioVenta());
                    }
                }else {
                    Optional<ProductoNormal> productoNormalOptional= productoNormalRepository.findById(id);
                    if(productoNormalOptional.isPresent()){
                        ProductoNormal productoNormal= productoNormalOptional.get();
                        productoDto.setMenu(false);
                        productoDto.setId(productoNormal.getId());
                        productoDto.setNombre(productoNormal.getNombre());
                        productoDto.setImagen(productoNormal.getImagen());
                        productoDto.setPrecio(productoDto.getPrecio());
                    }
                }
                return new ResponseEntity<ProductoDto>(productoDto,HttpStatus.OK);
            }
            return new ResponseEntity<ProductoDto>(new ProductoDto(),HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<ProductoDto>(new ProductoDto(),HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @Override
    public ResponseEntity<ComandaDTO> obtenerComandaPorIdOrden(Integer id) {
        try{
            Optional<Orden> optionalOrden=ordenRepository.findById(id);
            if(optionalOrden.isPresent()){
                double total= 0;
                Orden orden= optionalOrden.get();
                List<DetalleOrdenMenu> detalleOrdenMenus= detalleOrdenMenuRepository.getAllByOrden(orden);
                List<DetalleOrden_ProductoNormal>detalleOrdenProductoNormals= detalleOrdenProductoNormalRepository.getAllByOrden(orden);
                List<DetalleOrdenProductoDTO> detalleOrdenProductoDTOS= new ArrayList<>();
                ComandaDTO comandaDTO= new ComandaDTO();
                comandaDTO.setOrden(orden);
                if(!detalleOrdenMenus.isEmpty()){
                    for (DetalleOrdenMenu detalleOrdenMenu: detalleOrdenMenus) {
                        DetalleOrdenProductoDTO detalleOrdenProductoDTO= new DetalleOrdenProductoDTO();
                        detalleOrdenProductoDTO.setIdDetalleOrden(detalleOrdenMenu.getId());
                        detalleOrdenProductoDTO.setIdProducto(detalleOrdenMenu.getMenu().getId());
                        detalleOrdenProductoDTO.setEsDetalleMenu("esDetalleMenu");
                        detalleOrdenProductoDTO.setNombreProducto(detalleOrdenMenu.getMenu().getNombre());
                        detalleOrdenProductoDTO.setComentario(detalleOrdenMenu.getComentario());
                        detalleOrdenProductoDTO.setCantidad(detalleOrdenMenu.getCantidad());
                        detalleOrdenProductoDTO.setEstado(detalleOrdenMenu.getEstado());
                        detalleOrdenProductoDTO.setTotal(detalleOrdenMenu.getTotal());
                        total=total+detalleOrdenMenu.getTotal();
                        detalleOrdenProductoDTOS.add(detalleOrdenProductoDTO);
                    }
                }
                if(!detalleOrdenProductoNormals.isEmpty()){
                    for (DetalleOrden_ProductoNormal detalleOrdenProductoNormal:detalleOrdenProductoNormals) {
                        DetalleOrdenProductoDTO detalleOrdenProductoDTO= new DetalleOrdenProductoDTO();
                        detalleOrdenProductoDTO.setIdDetalleOrden(detalleOrdenProductoNormal.getId());
                        detalleOrdenProductoDTO.setIdProducto(detalleOrdenProductoNormal.getProductoNormal().getId());
                        detalleOrdenProductoDTO.setEsDetalleMenu("esDetalleNormal");
                        detalleOrdenProductoDTO.setNombreProducto(detalleOrdenProductoNormal.getProductoNormal().getNombre());
                        detalleOrdenProductoDTO.setComentario(detalleOrdenProductoNormal.getComentario());
                        detalleOrdenProductoDTO.setCantidad(detalleOrdenProductoNormal.getCantidad());
                        detalleOrdenProductoDTO.setEstado(detalleOrdenProductoNormal.getEstado());
                        detalleOrdenProductoDTO.setTotal(detalleOrdenProductoNormal.getTotal());
                        total=total+detalleOrdenProductoNormal.getTotal();
                        detalleOrdenProductoDTOS.add(detalleOrdenProductoDTO);
                    }
                }
                comandaDTO.setDetalleOrdenProductoDTOS(detalleOrdenProductoDTOS);
                comandaDTO.setTotal(total);
                return new ResponseEntity<ComandaDTO>(comandaDTO,HttpStatus.OK);

            }
            return new ResponseEntity<ComandaDTO>(new ComandaDTO(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<ComandaDTO>(new ComandaDTO(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<ComandaDTO> obtenerComandaPorIdOrdenMesa(Integer id) {
        try{
            Optional<Orden> optionalOrden=ordenRepository.findOrdensByMesaId(id);
            if(optionalOrden.isPresent()){
                double total= 0;
                Orden orden= optionalOrden.get();
                List<DetalleOrdenMenu> detalleOrdenMenus= detalleOrdenMenuRepository.getAllByOrden(orden);
                List<DetalleOrden_ProductoNormal>detalleOrdenProductoNormals= detalleOrdenProductoNormalRepository.getAllByOrden(orden);
                List<DetalleOrdenProductoDTO> detalleOrdenProductoDTOS= new ArrayList<>();
                ComandaDTO comandaDTO= new ComandaDTO();
                comandaDTO.setOrden(orden);
                if(!detalleOrdenMenus.isEmpty()){
                    for (DetalleOrdenMenu detalleOrdenMenu: detalleOrdenMenus) {
                        if(!detalleOrdenMenu.getEstado().equalsIgnoreCase("cancelado")){
                        DetalleOrdenProductoDTO detalleOrdenProductoDTO= new DetalleOrdenProductoDTO();
                        detalleOrdenProductoDTO.setIdDetalleOrden(detalleOrdenMenu.getId());
                        detalleOrdenProductoDTO.setIdProducto(detalleOrdenMenu.getMenu().getId());
                        detalleOrdenProductoDTO.setEsDetalleMenu("esDetalleMenu");
                        detalleOrdenProductoDTO.setNombreProducto(detalleOrdenMenu.getMenu().getNombre());
                        detalleOrdenProductoDTO.setComentario(detalleOrdenMenu.getComentario());
                        detalleOrdenProductoDTO.setCantidad(detalleOrdenMenu.getCantidad());
                        detalleOrdenProductoDTO.setEstado(detalleOrdenMenu.getEstado());
                        detalleOrdenProductoDTO.setTotal(detalleOrdenMenu.getTotal());
                        total=total+detalleOrdenMenu.getTotal();
                        detalleOrdenProductoDTOS.add(detalleOrdenProductoDTO);
                        }
                    }
                }
                if(!detalleOrdenProductoNormals.isEmpty()){
                    for (DetalleOrden_ProductoNormal detalleOrdenProductoNormal:detalleOrdenProductoNormals) {
                        DetalleOrdenProductoDTO detalleOrdenProductoDTO= new DetalleOrdenProductoDTO();
                        detalleOrdenProductoDTO.setIdDetalleOrden(detalleOrdenProductoNormal.getId());
                        detalleOrdenProductoDTO.setIdProducto(detalleOrdenProductoNormal.getProductoNormal().getId());
                        detalleOrdenProductoDTO.setEsDetalleMenu("esDetalleNormal");
                        detalleOrdenProductoDTO.setNombreProducto(detalleOrdenProductoNormal.getProductoNormal().getNombre());
                        detalleOrdenProductoDTO.setComentario(detalleOrdenProductoNormal.getComentario());
                        detalleOrdenProductoDTO.setCantidad(detalleOrdenProductoNormal.getCantidad());
                        detalleOrdenProductoDTO.setEstado(detalleOrdenProductoNormal.getEstado());
                        detalleOrdenProductoDTO.setTotal(detalleOrdenProductoNormal.getTotal());
                        total=total+detalleOrdenProductoNormal.getTotal();
                        detalleOrdenProductoDTOS.add(detalleOrdenProductoDTO);
                    }
                }
                comandaDTO.setDetalleOrdenProductoDTOS(detalleOrdenProductoDTOS);

                comandaDTO.setTotal(total);

                return new ResponseEntity<ComandaDTO>(comandaDTO,HttpStatus.OK);

            }
            return new ResponseEntity<ComandaDTO>(new ComandaDTO(),HttpStatus.BAD_REQUEST);


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<ComandaDTO>(new ComandaDTO(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<String> cerrarCuenta(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("idOrden") && objetoMap.containsKey("subTotal") && objetoMap.containsKey("descuento") && objetoMap.containsKey("totalPagar") && objetoMap.containsKey("idUsuario")){
                Integer idOrden= Integer.parseInt(objetoMap.get("idOrden"));
                Optional<Orden> ordenOptional= ordenRepository.findById(idOrden);
                Optional<Usuario> optionalUsuario= usuarioRepository.findById(Integer.parseInt(objetoMap.get("idUsuario")));
                if(ordenOptional.isPresent() && optionalUsuario.isPresent()){
                    double total= 0;
                    Orden orden= ordenOptional.get();
                    Optional<Arqueo> arqueoOptional= arqueoRepository.findArqueoByEstadoArqueoTrueAndCaja_Id(orden.getCaja().getId());
                    if(arqueoOptional.isPresent()){
                        Usuario usuario= optionalUsuario.get();
                        Mesa mesa= orden.getMesa();
                        Arqueo arqueo= arqueoOptional.get();
                        mesa.setEstado("Disponible");
                        orden.setEstado("Terminado");
                        ordenRepository.save(orden);
                        mesaRepository.save(mesa);
                        Venta venta= new Venta();
                        venta.setOrden(orden);
                        if(objetoMap.containsKey("comentario")){
                            venta.setComentario(objetoMap.get("comentario"));
                        }
                        venta.setUsuario(usuario);
                        venta.setEstado("Cerrado");
                        venta.setArqueo(arqueo);
                        venta.setDescuento(Double.parseDouble(objetoMap.get("descuento")));
                        venta.setSubTotal(Double.parseDouble(objetoMap.get("subTotal")));
                        venta.setTotalPagar(Double.parseDouble(objetoMap.get("totalPagar")));
                        venta.setFechaHoraConsolidacion(LocalDateTime.now());
                        ventaRepository.save(venta);
                        List<String[]> paymentMethods = new ArrayList<>();

                        ObjectMapper objectMapper= new ObjectMapper();
                        try {
                            List<Venta_MedioPagoWrapper> mediosPago=objectMapper.readValue(objetoMap.get("mediosPago"), new TypeReference<List<Venta_MedioPagoWrapper>>() {});

                            if(!mediosPago.isEmpty()){
                                for(Venta_MedioPagoWrapper ventaMedioPagoWrapper: mediosPago){
                                    Venta_MedioPago ventaMedioPago= new Venta_MedioPago();
                                    ventaMedioPago.setVenta(venta);
                                    Optional<MedioPago> medioPagoOptional= medioPagoRepository.findById(ventaMedioPagoWrapper.getId());
                                    if(medioPagoOptional.isPresent()){
                                        MedioPago medioPago= medioPagoOptional.get();
                                        ventaMedioPago.setMedioPago(medioPago);
                                        paymentMethods.add(new String[]{medioPago.getNombre(), String.valueOf(ventaMedioPagoWrapper.getPagoRecibido())});
                                    }
                                    ventaMedioPago.setPagoRecibido(ventaMedioPagoWrapper.getPagoRecibido());
                                    ventaMedioPagoRepository.save(ventaMedioPago);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        //Imprimir información
                        List<DetalleOrdenMenu> detalleOrdenMenus= detalleOrdenMenuRepository.getAllByOrden(orden);
                        List<DetalleOrden_ProductoNormal>detalleOrdenProductoNormals= detalleOrdenProductoNormalRepository.getAllByOrden(orden);
                        List<DetalleOrdenProductoDTO> detalleOrdenProductoDTOS= new ArrayList<>();
                        List<String[]> products = new ArrayList<>();

                        int contador=0;
                        ComandaDTO comandaDTO= new ComandaDTO();
                        comandaDTO.setOrden(orden);

                        if(!detalleOrdenMenus.isEmpty()){
                            for (DetalleOrdenMenu detalleOrdenMenu: detalleOrdenMenus) {
                                if(detalleOrdenMenu.getEstado().equalsIgnoreCase("terminado")){
                                    DetalleOrdenProductoDTO detalleOrdenProductoDTO= new DetalleOrdenProductoDTO();
                                    detalleOrdenProductoDTO.setIdDetalleOrden(detalleOrdenMenu.getId());
                                    detalleOrdenProductoDTO.setIdProducto(detalleOrdenMenu.getMenu().getId());
                                    detalleOrdenProductoDTO.setEsDetalleMenu("true");
                                    detalleOrdenProductoDTO.setNombreProducto(detalleOrdenMenu.getMenu().getNombre());
                                    detalleOrdenProductoDTO.setComentario(detalleOrdenMenu.getComentario());
                                    detalleOrdenProductoDTO.setCantidad(detalleOrdenMenu.getCantidad());
                                    detalleOrdenProductoDTO.setEstado(detalleOrdenMenu.getEstado());
                                    detalleOrdenProductoDTO.setTotal(detalleOrdenMenu.getTotal());
                                    total=total+detalleOrdenMenu.getTotal();
                                    detalleOrdenProductoDTOS.add(detalleOrdenProductoDTO);
                                    String[] info={String.valueOf(detalleOrdenMenu.getCantidad()),detalleOrdenMenu.getMenu().getNombre(),String.valueOf(detalleOrdenMenu.getTotal())};
                                    products.add(info);
                                    contador++;
                                }
                            }
                        }



                        if(!detalleOrdenProductoNormals.isEmpty()){
                            for (DetalleOrden_ProductoNormal detalleOrdenProductoNormal:detalleOrdenProductoNormals) {
                                DetalleOrdenProductoDTO detalleOrdenProductoDTO= new DetalleOrdenProductoDTO();
                                detalleOrdenProductoDTO.setIdDetalleOrden(detalleOrdenProductoNormal.getId());
                                detalleOrdenProductoDTO.setIdProducto(detalleOrdenProductoNormal.getProductoNormal().getId());
                                detalleOrdenProductoDTO.setEsDetalleMenu("true");
                                detalleOrdenProductoDTO.setNombreProducto(detalleOrdenProductoNormal.getProductoNormal().getNombre());
                                detalleOrdenProductoDTO.setComentario(detalleOrdenProductoNormal.getComentario());
                                detalleOrdenProductoDTO.setCantidad(detalleOrdenProductoNormal.getCantidad());
                                detalleOrdenProductoDTO.setEstado(detalleOrdenProductoNormal.getEstado());
                                detalleOrdenProductoDTO.setTotal(detalleOrdenProductoNormal.getTotal());
                                total=total+detalleOrdenProductoNormal.getTotal();
                                detalleOrdenProductoDTOS.add(detalleOrdenProductoDTO);
                                String[] info={String.valueOf(detalleOrdenProductoNormal.getCantidad()),detalleOrdenProductoNormal.getProductoNormal().getNombre(),String.valueOf(detalleOrdenProductoNormal.getTotal())};
                                products.add(info);
                                contador++;
                            }
                        }


                        Impresora impresora1= orden.getCaja().getImpresora();

                        TicketOrden ticket = new TicketOrden(String.valueOf(orden.getFolio()), String.valueOf(orden.getCaja().getNombre()), usuario.getNombre(), orden.getNombreCliente(), products,
                                "$"+objetoMap.get("subTotal"), objetoMap.get("descuento"), String.valueOf(contador), objetoMap.get("totalPagar"), objetoMap.get("recibido"), objetoMap.get("cambio"),
                                paymentMethods
                        );
                        String ticketContent = ticket.getContentTicket().toString();
                        Map<String, String> printRequest = new HashMap<>();
                        printRequest.put("ticketContent", ticketContent);
                        printRequest.put("printerIp", impresora1.getDireccionIp());

                        String printRequestJson = new ObjectMapper().writeValueAsString(printRequest);

                        // Enviar solicitud al servidor de impresión local
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("https://d77b-189-129-48-107.ngrok-free.app/print"))
                                .POST(HttpRequest.BodyPublishers.ofString(printRequestJson, StandardCharsets.UTF_8))
                                .header("Content-Type", "application/json")
                                .build();

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        if (response.statusCode()==200) {
                            return Utils.getResponseEntity("Impreso correctamente", HttpStatus.OK);
                        } else {
                            Optional<Impresora> impresoraOptional = impresoraRepository.getImpresoraByPorDefectoTrue();
                            if (impresoraOptional.isPresent()) {
                                Impresora impresora= impresoraOptional.get();
                                Map<String, String> printRequest1 = new HashMap<>();
                                printRequest.put("ticketContent", ticketContent);
                                printRequest.put("printerIp", impresora.getDireccionIp());

                                String printRequestJson1 = new ObjectMapper().writeValueAsString(printRequest1);

                                // Enviar solicitud al servidor de impresión local
                                HttpClient client1 = HttpClient.newHttpClient();
                                HttpRequest request1 = HttpRequest.newBuilder()
                                        .uri(URI.create("https://67b5-2806-10ae-10-4b65-889c-99b4-a753-8fbc.ngrok-free.app/print"))
                                        .POST(HttpRequest.BodyPublishers.ofString(printRequestJson1, StandardCharsets.UTF_8))
                                        .header("Content-Type", "application/json")
                                        .build();

                                HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

                            }
                            return Utils.getResponseEntity("Error al imprimir: ",HttpStatus.INTERNAL_SERVER_ERROR);
                        }





                    }
                    return Utils.getResponseEntity("El arqueo ya no esta abierto.",HttpStatus.BAD_REQUEST);

                }
                return Utils.getResponseEntity("La orden o usuario no existe.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<String> validarStocks(String productos) {
        try {
            return Utils.getResponseEntity(validarStock(productos),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<List<OrdenDTO>> obtenerOrdenesActuales() {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
            List<Orden> ordenList= ordenRepository.findOrdenesDelDia(startOfDay,endOfDay);
            List<OrdenDTO> ordenDTOS= new ArrayList<>();

            if(!ordenList.isEmpty()){
                for (Orden orden:ordenList) {
                    OrdenDTO ordenDTO= new OrdenDTO();
                    ordenDTO.setIdOrden(orden.getId());
                    ordenDTO.setNombreCliente(orden.getNombreCliente());
                    ordenDTO.setFolio(orden.getFolio());
                    ordenDTO.setNombreArea(orden.getMesa().getAreaServicio().getNombre()+orden.getMesa().getNombre());
                    ordenDTO.setEstado(orden.getEstado());
                    double total=0;
                    List<DetalleOrdenMenu> detalleOrdenMenuList= detalleOrdenMenuRepository.getAllByOrden(orden);
                    List<DetalleOrden_ProductoNormal> detalleOrdenProductoNormals= detalleOrdenProductoNormalRepository.getAllByOrden(orden);

                    if(!detalleOrdenMenuList.isEmpty()){
                        for (DetalleOrdenMenu detalleOrdenMenu:detalleOrdenMenuList) {
                            total=total+(detalleOrdenMenu.getTotal());
                        }
                    }

                    if(!detalleOrdenProductoNormals.isEmpty()){
                        for(DetalleOrden_ProductoNormal detalleOrdenProductoNormal:detalleOrdenProductoNormals){
                            total=total+(detalleOrdenProductoNormal.getTotal());
                        }
                    }
                    ordenDTO.setTotal(total);
                    ordenDTOS.add(ordenDTO);
                }
            }
            return new ResponseEntity<List<OrdenDTO>>(ordenDTOS,HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<OrdenDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
//Solo asignar platillo
    @Override
    public ResponseEntity<String> asignarPlatillo(Map<String, String> objetoMap) {
        try{
            if(objetoMap.containsKey("idProducto")&& objetoMap.containsKey("idOrden")&& objetoMap.containsKey("cantidad") && objetoMap.containsKey("esMenu")){
                Optional<Orden> ordenOptional= ordenRepository.findById(Integer.parseInt(objetoMap.get("idOrden")));

                if(ordenOptional.isPresent()){
                    Orden orden= ordenOptional.get();
                    Integer idProducto=Integer.parseInt(objetoMap.get("idProducto"));

                    if(objetoMap.get("esMenu").equalsIgnoreCase("esMenu")){
                        Optional<Menu> menuOptional= menuRepository.findById(idProducto);
                        if (menuOptional.isPresent()){
                            Menu menu= menuOptional.get();

                            List<MateriaPrima_Menu> materiaPrimaMenus= materiaPrimaMenuRepository.getAllByMenu(menu);
                            List<ProductoTerminado_Menu> productoTerminadoMenus=productoTerminadoMenuRepository.getAllByMenu(menu);

                            //Validar el stock
                            if(!materiaPrimaMenus.isEmpty()){
                                for (MateriaPrima_Menu materiaPrimaMenu:materiaPrimaMenus) {
                                    double descontandoStock= materiaPrimaMenu.getInventario().getStockActual()-(materiaPrimaMenu.getCantidad()*Integer.parseInt(objetoMap.get("cantidad")));
                                    if(descontandoStock<=0){
                                        return Utils.getResponseEntity("No hay suficiente stock para preparar los platillos" + materiaPrimaMenu.getMenu().getNombre() + ". Ya que no hay suficiente stock en el inventario en el almacen" + materiaPrimaMenu.getInventario().getAlmacen().getNombre() + ".Hace falta ingresar mas " + materiaPrimaMenu.getInventario().getMateriaPrima().getNombre(),HttpStatus.BAD_REQUEST);
                                    }
                                }
                            }

                            if(!productoTerminadoMenus.isEmpty()){
                                for (ProductoTerminado_Menu productoTerminadoMenu:productoTerminadoMenus) {
                                    double descontandoStock = productoTerminadoMenu.getProductoTerminado().getStockActual()-(productoTerminadoMenu.getCantidad() * Integer.parseInt(objetoMap.get("cantidad")));
                                    if(descontandoStock<=0){
                                        return Utils.getResponseEntity("No hay suficiente stock para preparar los platillos"+productoTerminadoMenu.getMenu().getNombre()+". Ya que no hay suficiente stock en el producto terminado"+productoTerminadoMenu.getProductoTerminado().getNombre(),HttpStatus.BAD_REQUEST);
                                    }
                                }
                            }

                            //Descontando stock

                            if(!materiaPrimaMenus.isEmpty()){
                                for (MateriaPrima_Menu materiaPrimaMenu:materiaPrimaMenus) {
                                    Inventario inventario= materiaPrimaMenu.getInventario();
                                    inventario.setStockActual(materiaPrimaMenu.getInventario().getStockActual()-(materiaPrimaMenu.getCantidad()*Integer.parseInt(objetoMap.get("cantidad"))));
                                    inventarioRepository.save(inventario);
                                }
                            }

                            if(!productoTerminadoMenus.isEmpty()){
                                for (ProductoTerminado_Menu productoTerminadoMenu:productoTerminadoMenus) {
                                    ProductoTerminado productoTerminado= productoTerminadoMenu.getProductoTerminado();
                                    productoTerminado.setStockActual(productoTerminadoMenu.getProductoTerminado().getStockActual()-(productoTerminadoMenu.getCantidad() * Integer.parseInt(objetoMap.get("cantidad"))));
                                    productoTerminadoRepository.save(productoTerminado);
                                }
                            }

                            DetalleOrdenMenu detalleOrdenMenu= new DetalleOrdenMenu();
                            detalleOrdenMenu.setMenu(menu);
                            detalleOrdenMenu.setOrden(orden);
                            detalleOrdenMenu.setCantidad(Integer.parseInt(objetoMap.get("cantidad")));
                            detalleOrdenMenu.setTotal(menu.getPrecioVenta()*Integer.parseInt(objetoMap.get("cantidad")));
                            if(objetoMap.containsKey("comentario")){
                                detalleOrdenMenu.setComentario(objetoMap.get("comentario"));
                            }
                            detalleOrdenMenu.setEstado("Por enviar");
                            detalleOrdenMenu.setNombreMenu(menu.getNombre());
                            detalleOrdenMenu.setPrecioMenu(menu.getPrecioVenta());
                            detalleOrdenMenuRepository.save(detalleOrdenMenu);
                            return Utils.getResponseEntity("Platillo asignado correctamente.",HttpStatus.OK);

                        }
                        return Utils.getResponseEntity("El producto no existe.",HttpStatus.BAD_REQUEST);

                    }else {
                        Optional<ProductoNormal> productoNormalOptional= productoNormalRepository.findById(idProducto);
                        if(productoNormalOptional.isPresent()){
                            ProductoNormal productoNormal= productoNormalOptional.get();

                            if((productoNormal.getStockActual() - Integer.parseInt(objetoMap.get("cantidad")))<=0){
                                return Utils.getResponseEntity("No hay suficiente stock para el producto "+productoNormal.getNombre(),HttpStatus.BAD_REQUEST);
                            }
                            productoNormal.setStockActual(productoNormal.getStockActual() - Integer.parseInt(objetoMap.get("cantidad")));
                            DetalleOrden_ProductoNormal detalleOrdenProductoNormal= new DetalleOrden_ProductoNormal();
                            detalleOrdenProductoNormal.setProductoNormal(productoNormal);
                            detalleOrdenProductoNormal.setOrden(orden);
                            detalleOrdenProductoNormal.setEstado("Por enviar");
                            detalleOrdenProductoNormal.setPrecioProductoNormal(productoNormal.getPrecioUnitario());
                            detalleOrdenProductoNormal.setNombreProductoNormal(productoNormal.getNombre());
                            detalleOrdenProductoNormal.setTotal(productoNormal.getPrecioUnitario()*Integer.parseInt(objetoMap.get("cantidad")));
                            if(objetoMap.containsKey("comentario")){
                                detalleOrdenProductoNormal.setComentario(objetoMap.get("comentario"));
                            }
                            detalleOrdenProductoNormalRepository.save(detalleOrdenProductoNormal);
                            return Utils.getResponseEntity("Producto asignado correctamente.",HttpStatus.OK);

                        }

                    }

                }
                return Utils.getResponseEntity("La orden no existe.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
//Solo imprimir
    @Override
    public ResponseEntity<String> imprimirComanda(Integer id) {
        try {
            Optional<Orden> ordenOptional= ordenRepository.findById(id);

            Set<Integer> cocinas = new HashSet<>();
            Map<Integer, List<String>> productosPorCocina = new HashMap<>();

            if(ordenOptional.isPresent()){
                Orden orden= ordenOptional.get();
                List<DetalleOrdenMenu> detalleOrdenMenus= detalleOrdenMenuRepository.getAllByOrden(orden);

                if(!detalleOrdenMenus.isEmpty()){
                    for (DetalleOrdenMenu detalleOrdenMenu: detalleOrdenMenus){
                        Integer cocinaId= detalleOrdenMenu.getMenu().getCocina().getId();


                        if (!cocinas.contains(detalleOrdenMenu.getMenu().getId())) {
                            cocinas.add(cocinaId);
                            productosPorCocina.put(cocinaId, new ArrayList<>());
                        }

                        List<String> productos= productosPorCocina.get(cocinaId);
                        String productoDetalle =detalleOrdenMenu.getCantidad()+"  "+ detalleOrdenMenu.getMenu().getNombre()  +"  "+ detalleOrdenMenu.getComentario();
                        productos.add(productoDetalle);
                    }
                }

                if (imprimirComandas(productosPorCocina, orden.getNombreCliente(), orden.getMesa().getAreaServicio().getNombre()+orden.getMesa().getNombre(), orden.getUsuario().getNombre()) == 200) {
                    return Utils.getResponseEntity("Impreso correctamente", HttpStatus.OK);
                } else{
                    //  return Utils.getResponseEntity("Sucedio un problema al imprimir el ticket.", HttpStatus.BAD_REQUEST);
                }


            }
            return Utils.getResponseEntity("No existe la orden.",HttpStatus.BAD_REQUEST);


        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
//Eliminar platillo de la comanda
    @Override
    public ResponseEntity<String> eliminarPlatilloDeComanda(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("idProducto") && objetoMap.containsKey("esDetalleMenu")){
                Integer idProducto=Integer.parseInt(objetoMap.get("idProducto"));
                if(objetoMap.get("esDetalleMenu").equalsIgnoreCase("esDetalleMenu")){
                    Optional<DetalleOrdenMenu>detalleOrdenMenuOptional= detalleOrdenMenuRepository.findById(idProducto);
                    if(detalleOrdenMenuOptional.isPresent()){
                        DetalleOrdenMenu detalleOrdenMenu= detalleOrdenMenuOptional.get();
                        List<MateriaPrima_Menu> materiaPrimaMenus= materiaPrimaMenuRepository.getAllByMenu(detalleOrdenMenu.getMenu());
                        List<ProductoTerminado_Menu> productoTerminadoMenus= productoTerminadoMenuRepository.getAllByMenu(detalleOrdenMenu.getMenu());

                        if(!materiaPrimaMenus.isEmpty()){
                            for (MateriaPrima_Menu materiaPrimaMenu:materiaPrimaMenus) {
                                Inventario inventario= materiaPrimaMenu.getInventario();
                                inventario.setStockActual(inventario.getStockActual()+(detalleOrdenMenu.getCantidad()*materiaPrimaMenu.getCantidad()));
                                inventarioRepository.save(inventario);
                            }
                        }

                        if(!productoTerminadoMenus.isEmpty()){
                            for (ProductoTerminado_Menu productoTerminadoMenu:productoTerminadoMenus) {
                                ProductoTerminado productoTerminado= productoTerminadoMenu.getProductoTerminado();
                                productoTerminado.setStockActual(productoTerminado.getStockActual()+(detalleOrdenMenu.getCantidad()*productoTerminadoMenu.getCantidad()));
                                productoTerminadoRepository.save(productoTerminado);
                            }
                        }
                        //Regresar el stock
                        detalleOrdenMenuRepository.delete(detalleOrdenMenu);
                        return Utils.getResponseEntity("Platillo eliminado de la orden correctamente.",HttpStatus.OK);
                    }
                    return Utils.getResponseEntity("Ya no existe el registro.",HttpStatus.BAD_REQUEST);
                }

                if(objetoMap.get("esDetalleMenu").equalsIgnoreCase("esDetalleNormal")){
                    Optional<DetalleOrden_ProductoNormal>detalleOrdenProductoNormalOptional= detalleOrdenProductoNormalRepository.findById(idProducto);
                    if(detalleOrdenProductoNormalOptional.isPresent()){
                        DetalleOrden_ProductoNormal detalleOrdenProductoNormal= detalleOrdenProductoNormalOptional.get();
                        //Regresar el stock
                        ProductoNormal productoNormal= detalleOrdenProductoNormal.getProductoNormal();
                        productoNormal.setStockActual(productoNormal.getStockActual()+detalleOrdenProductoNormal.getCantidad());
                        productoNormalRepository.save(productoNormal);
                        detalleOrdenProductoNormalRepository.delete(detalleOrdenProductoNormal);
                        return Utils.getResponseEntity("Platillo eliminado de la orden correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("Ya no existe el registro.",HttpStatus.BAD_REQUEST);
                }
            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> enviarACocina(Map<String, String> objetoMap) {
        try{
            if(objetoMap.containsKey("idOrden") && objetoMap.containsKey("detalleOrden")){
                if(validarStock(objetoMap.get("detalleOrden")).equalsIgnoreCase("suficiente")){
                    Optional<Orden> optional=ordenRepository.findById(Integer.parseInt(objetoMap.get("idOrden")));
                    if (optional.isPresent()){
                        Orden orden= optional.get();
                        ObjectMapper objectMapper = new ObjectMapper();
                        List<DetalleOrdenWrapper> detalleOrdenWrappers = objectMapper.readValue(objetoMap.get("detalleOrden"), new TypeReference<List<DetalleOrdenWrapper>>() {});



                        if(!detalleOrdenWrappers.isEmpty()) {
                            if (validarStock(objetoMap.get("detalleOrden")).equalsIgnoreCase("Si hay suficiente stock")){
                                for (DetalleOrdenWrapper detalleOrdenWrapper : detalleOrdenWrappers) {
                                    if (detalleOrdenWrapper.getIsMenu().equalsIgnoreCase("esDetalleOrdenMenu")) {
                                        Optional<DetalleOrdenMenu> detalleOrdenMenuOptional = detalleOrdenMenuRepository.findById(detalleOrdenWrapper.getIdProducto());
                                        if (detalleOrdenMenuOptional.isPresent()) {
                                            //Solo dos operaciones sumar o restar
                                            DetalleOrdenMenu detalleOrdenMenu = detalleOrdenMenuOptional.get();
                                            Integer cocinaId = 90000;
                                            //Para el caso en el que se agrega a la nueva comanda
                                            if (detalleOrdenMenu.getCantidad() < detalleOrdenWrapper.getCantidad()) {
                                                cocinaId = detalleOrdenMenu.getMenu().getCocina().getId();
                                                int cantidad = detalleOrdenWrapper.getCantidad()-detalleOrdenMenu.getCantidad() ;
                                                // Añadir el producto a la lista correspondiente a la cocina

                                            }
                                            descontarOAgregarStockMenu(detalleOrdenMenu.getMenu(), detalleOrdenMenu, detalleOrdenWrapper.getCantidad());
                                        }
                                    }
                                    if (detalleOrdenWrapper.getIsMenu().equalsIgnoreCase("esMenu")) {
                                        Optional<Menu> menuOptional = menuRepository.findById(detalleOrdenWrapper.getIdProducto());
                                        Menu menu = new Menu();
                                        Integer cocinaId = 9000;
                                        if (menuOptional.isPresent()) {
                                            menu = menuOptional.get();
                                            cocinaId = menuOptional.get().getCocina().getId();
                                        }



                                        //Verificamos la existencia de la relación de una orden y un menú mediante su id
                                        //Optional<DetalleOrdenMenu> detalleOrdenMenuOptional = detalleOrdenMenuRepository.findDetalleOrdenMenuByOrden_IdAndMenu_Id(orden.getId(), detalleOrdenWrapper.getIdProducto());
                                        //if (detalleOrdenMenuOptional.isPresent()) {
                                        // DetalleOrdenMenu detalleOrdenMenu = detalleOrdenMenuOptional.get();
                                        //detalleOrdenMenu.setCantidad(detalleOrdenMenu.getCantidad() + detalleOrdenWrapper.getCantidad());
                                        //detalleOrdenMenuRepository.save(detalleOrdenMenu);
                                        //descontarStockMenu(menu, detalleOrdenMenu);

                                        //} else {
                                        //Verificar si es un menu con producto terminados o
                                        DetalleOrdenMenu detalleOrdenMenu = new DetalleOrdenMenu();

                                        detalleOrdenMenu.setMenu(menu);
                                        detalleOrdenMenu.setOrden(orden);
                                        detalleOrdenMenu.setCantidad(detalleOrdenWrapper.getCantidad());
                                        detalleOrdenMenu.setTotal(menu.getPrecioVenta() * detalleOrdenWrapper.getCantidad());
                                        detalleOrdenMenu.setComentario(detalleOrdenWrapper.getComentario());
                                        detalleOrdenMenu.setEstado("En espera");
                                        detalleOrdenMenu.setNombreMenu(menu.getNombre());
                                        detalleOrdenMenu.setPrecioMenu(menu.getPrecioVenta());
                                        detalleOrdenMenuRepository.save(detalleOrdenMenu);
                                        descontarStockMenu(menu, detalleOrdenMenu);
                                        //}

                                    }



                                }

                                return Utils.getResponseEntity("Platillos enviados a cocina correctamente.",HttpStatus.OK);


                            }
                            return Utils.getResponseEntity(validarStock(objetoMap.get("detalleOrden")),HttpStatus.BAD_REQUEST);


                        }
                        return Utils.getResponseEntity("Error al obtener los detalles de la orden.",HttpStatus.BAD_REQUEST);

                    }
                    return Utils.getResponseEntity("La orden no existe.",HttpStatus.BAD_REQUEST);


                }

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Solo enviar a cocina

}
