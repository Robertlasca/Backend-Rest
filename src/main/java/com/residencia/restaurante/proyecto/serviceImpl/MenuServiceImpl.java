package com.residencia.restaurante.proyecto.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.IngredienteProductoTerminado;
import com.residencia.restaurante.proyecto.dto.MenuDTO;
import com.residencia.restaurante.proyecto.dto.RecetaDTO;
import com.residencia.restaurante.proyecto.entity.*;
import com.residencia.restaurante.proyecto.repository.*;
import com.residencia.restaurante.proyecto.service.IMenuService;
import com.residencia.restaurante.proyecto.wrapper.RecetaWrapper;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class MenuServiceImpl implements IMenuService {
    @Autowired
    private IMenuRepository menuRepository;

    @Autowired
    private IMateriaPrima_MenuRepository materiaPrimaMenuRepository;

    @Autowired
    private IProductoTerminado_MenuRepository productoTerminadoMenuRepository;

    @Autowired
    private IProductoTerminadoRepository productoTerminadoRepository;

    @Autowired
    private IAlmacenRepository almacenRepository;

    @Autowired
    private IInventarioRepository inventarioRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private  UploadFileService uploadFileService;

    @Autowired
    private IMateriaPrima_ProductoTerminadoRepository materiaPrimaProductoTerminadoRepository;

    @Autowired
    private ICocinaRepository cocinaRepository;

    @Autowired
    private IDetalleOrden_MenuRepository detalleOrdenMenuRepository;


    @Override
    public ResponseEntity<List<MenuDTO>> obtenerActivos() {
        try {
            List<Menu> menuList=menuRepository.getAllByVisibilidadTrue();
            List<MenuDTO> menuDTOS=new ArrayList<>();
            for (Menu menu: menuList) {
                MenuDTO menuDTO= new MenuDTO();
                menuDTO.setMenu(menu);
                menuDTO.setGanancia(menu.getPrecioVenta()-menu.getCostoProduccionDirecto());
                if(menu.isVisibilidad()){
                    menuDTO.setDisponibilidad("Visible");
                    menuDTOS.add(menuDTO);

                }

            }

            return new ResponseEntity<List<MenuDTO>>(menuDTOS,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MenuDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MenuDTO>> obtenerNoActivos() {
        try {
            List<Menu> menuList=menuRepository.getAllByVisibilidadFalse();
            List<MenuDTO> menuDTOS=new ArrayList<>();
            for (Menu menu: menuList) {
                MenuDTO menuDTO= new MenuDTO();
                menuDTO.setMenu(menu);
                menuDTO.setGanancia(menu.getPrecioVenta()-menu.getCostoProduccionDirecto());
                if(menu.isVisibilidad()){
                    menuDTO.setDisponibilidad("Visible");

                }else {
                    menuDTO.setDisponibilidad("No visible");
                    menuDTOS.add(menuDTO);
                }



            }

            return new ResponseEntity<List<MenuDTO>>(menuDTOS,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MenuDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MenuDTO>> obtenerTodos() {
        try {
            List<Menu> menuList=menuRepository.findAll();
            List<MenuDTO> menuDTOS=new ArrayList<>();
            for (Menu menu: menuList) {
                MenuDTO menuDTO= new MenuDTO();
                menuDTO.setMenu(menu);
                menuDTO.setGanancia(menu.getPrecioVenta()-menu.getCostoProduccionDirecto());
                if(menu.isVisibilidad()){
                    menuDTO.setDisponibilidad("Visible");

                }else {
                    menuDTO.setDisponibilidad("No visible");
                }

                menuDTOS.add(menuDTO);

            }

            return new ResponseEntity<List<MenuDTO>>(menuDTOS,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MenuDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregar(String nombre, String descripcion, double costoProduccion, double margenGanancia, double precioVenta, MultipartFile file, int idCategoria, String receta) {
        try {
            if(!menuRepository.existsByNombreLikeIgnoreCase(nombre) || !nombre.isEmpty()){
                if(validarCategoriaId(idCategoria)){
                    Menu menu=new Menu();
                    Optional<Categoria> categoriaOptional= categoriaRepository.findById(idCategoria);
                    categoriaOptional.ifPresent(menu::setCategoria);
                    menu.setNombre(nombre);
                    menu.setDescripcion(descripcion);
                    menu.setVisibilidad(true);
                    menu.setDependent(false);
                    menu.setCostoProduccionDirecto(costoProduccion);
                    menu.setMargenGanancia(margenGanancia);
                    menu.setPrecioVenta(precioVenta);

                    if(file==null || file.isEmpty()){
                        menu.setImagen("default.jpg");
                    }else {
                        String nombreImagen= uploadFileService.guardarImagen(file);
                        menu.setImagen(nombreImagen);
                    }



                    menuRepository.save(menu);

                    ObjectMapper objectMapper= new ObjectMapper();
                    try {
                        List<RecetaWrapper> recetaWrappers=objectMapper.readValue(receta, new TypeReference<List<RecetaWrapper>>() {
                        });
                        if(!recetaWrappers.isEmpty()){
                            for(RecetaWrapper recetaWrapper: recetaWrappers){
                                if(recetaWrapper.getEsIngrediente().equalsIgnoreCase("true")){
                                    Optional<Inventario> optionalInventario= inventarioRepository.findById(recetaWrapper.getId());
                                    if(optionalInventario.isPresent()){
                                        MateriaPrima_Menu materiaPrimaMenu= new MateriaPrima_Menu();
                                        Inventario inventario= optionalInventario.get();
                                        materiaPrimaMenu.setMenu(menu);
                                        materiaPrimaMenu.setInventario(inventario);
                                        materiaPrimaMenu.setCantidad(recetaWrapper.getCantidad());
                                        materiaPrimaMenuRepository.save(materiaPrimaMenu);


                                    }

                                }else {
                                    Optional<ProductoTerminado> productoTerminadoOptional=productoTerminadoRepository.findById(recetaWrapper.getId());
                                    if(productoTerminadoOptional.isPresent()){
                                        ProductoTerminado productoTerminado=productoTerminadoOptional.get();
                                        ProductoTerminado_Menu productoTerminadoMenu=new ProductoTerminado_Menu();

                                        productoTerminadoMenu.setMenu(menu);
                                        productoTerminadoMenu.setProductoTerminado(productoTerminado);
                                        productoTerminadoMenu.setCantidad(recetaWrapper.getCantidad());
                                        productoTerminadoMenuRepository.save(productoTerminadoMenu);
                                    }

                                }
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return Utils.getResponseEntity("Menú  guardado correctamente.",HttpStatus.OK);


                }
                return Utils.getResponseEntity("Error al asignar la categoría.",HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity("El nombre del menú ya existe.",HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<IngredienteProductoTerminado> obtenerMateriaPrimaId(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<ProductoTerminado> obtenerProductoTerminado(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<RecetaDTO>> obtenerRecetaId(Integer id) {
        try {
            Optional<Menu> menuOptional= menuRepository.findById(id);
            List<RecetaDTO> recetaDTOS= new ArrayList<>();
            if(menuOptional.isPresent()){
                Menu menu= menuOptional.get();

                List<MateriaPrima_Menu> materiaPrimaMenus= materiaPrimaMenuRepository.getAllByMenu(menu);
                List<ProductoTerminado_Menu> productoTerminadoMenus= productoTerminadoMenuRepository.getAllByMenu(menu);

                if(!materiaPrimaMenus.isEmpty()){
                    for (MateriaPrima_Menu materiaPrimaMenu:materiaPrimaMenus ) {
                        MateriaPrima materiaPrima= materiaPrimaMenu.getInventario().getMateriaPrima();
                        double costo= calcularCostoProduccion(materiaPrima.getCostoUnitario());
                        RecetaDTO recetaDTO= new RecetaDTO();
                        System.out.println("Id del inventario"+materiaPrimaMenu.getInventario().getId());
                        recetaDTO.setEsIngrediente(true);
                        recetaDTO.setId(materiaPrimaMenu.getId());
                        recetaDTO.setUnidadMedida(materiaPrima.getUnidadMedida());
                        recetaDTO.setNombre(materiaPrima.getNombre());
                        recetaDTO.setIdProductoTerminado(materiaPrimaMenu.getMenu().getId());
                        recetaDTO.setCantidad(materiaPrimaMenu.getCantidad());
                        //Costo de produccion ejemplo que la materia prima ocupe 0.200
                        recetaDTO.setCostoProduccion(rendondearADos((materiaPrimaMenu.getCantidad()*1000)*costo));
                        recetaDTOS.add(recetaDTO);


                    }
                }

                if(!productoTerminadoMenus.isEmpty()){
                    for(ProductoTerminado_Menu productoTerminadoMenu:productoTerminadoMenus){

                        ProductoTerminado productoTerminado= productoTerminadoMenu.getProductoTerminado();
                        RecetaDTO recetaDTO= new RecetaDTO();
                        double precio=rendondearADos(calcularCostoProduccionTotal(productoTerminado.getId()));
                        double costoProduccionXUnidad= rendondearADos(precio/1000);
                        recetaDTO.setId(productoTerminadoMenu.getId());
                        recetaDTO.setNombre(productoTerminado.getNombre());
                        recetaDTO.setCostoProduccion(rendondearADos((productoTerminadoMenu.getCantidad()*1000)*costoProduccionXUnidad));
                        recetaDTO.setEsIngrediente(false);
                        recetaDTO.setIdProductoTerminado(productoTerminadoMenu.getMenu().getId());
                        recetaDTO.setUnidadMedida(productoTerminado.getUnidadMedida());
                        recetaDTO.setCantidad(productoTerminadoMenu.getCantidad());
                        recetaDTOS.add(recetaDTO);


                    }
                }
                System.out.println("Este es el costo de producción total:"+calcularCostoTotalMenu(menu.getId()));

                return new ResponseEntity<List<RecetaDTO>>(recetaDTOS,HttpStatus.OK);

            }
            return new ResponseEntity<List<RecetaDTO>>(new ArrayList<>(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<RecetaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<RecetaDTO>> obtenerIngredientesOProductosTerminadosIdCocina(Integer id) {
        try {
            List<RecetaDTO> recetaDTOList = new ArrayList<>();
            List<Inventario> inventarios = obtenerMateriasIdCocina(id);
            List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados = obtenerProductosTerminadosIdCocina(id);

            // Usamos un mapa para mantener un único elemento por nombre de receta
            Map<String, RecetaDTO> recetasUnicas = new HashMap<>();

            // Agregamos las recetas de los inventarios
            for (Inventario inventario : inventarios) {
                RecetaDTO recetaDTO = new RecetaDTO();
                recetaDTO.setId(inventario.getId());
                recetaDTO.setEsIngrediente(true);
                recetaDTO.setNombre(inventario.getMateriaPrima().getNombre());
                //Precio por gramo
                recetaDTO.setCostoProduccion(calcularCostoProduccion(inventario.getMateriaPrima().getCostoUnitario()));
                recetaDTO.setUnidadMedida(inventario.getMateriaPrima().getUnidadMedida());
                recetasUnicas.put(recetaDTO.getNombre(), recetaDTO);
            }

            // Agregamos las recetas de los productos terminados
            for (MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado : materiaPrimaProductoTerminados) {
                RecetaDTO recetaDTO = new RecetaDTO();
                recetaDTO.setId(materiaPrimaProductoTerminado.getProductoTerminado().getId());
                recetaDTO.setEsIngrediente(false);
                recetaDTO.setNombre(materiaPrimaProductoTerminado.getProductoTerminado().getNombre());
                //Precio por gramo
                double precio=rendondearADos(calcularCostoProduccionTotal(materiaPrimaProductoTerminado.getProductoTerminado().getId()));
                double costoProduccionXUnidad= rendondearADos(precio/1000);
                recetaDTO.setCostoProduccion(calcularCostoProduccion(costoProduccionXUnidad));
                recetaDTO.setUnidadMedida(materiaPrimaProductoTerminado.getProductoTerminado().getUnidadMedida());
                recetasUnicas.put(recetaDTO.getNombre(), recetaDTO);
            }

            // Convertimos el mapa de recetas únicas en una lista
            recetaDTOList.addAll(recetasUnicas.values());

            return new ResponseEntity<List<RecetaDTO>>(recetaDTOList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<List<RecetaDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<RecetaDTO> obtenerIngredientesOProductosTerminadosXId(Map<String, String> objetoMap) {
        try {
            if(validarExistenciaMateriaOProducto(Integer.parseInt(objetoMap.get("id")),Boolean.parseBoolean(objetoMap.get("esIngrediente"))) ){

                if(objetoMap.get("esIngrediente").equalsIgnoreCase("true")){
                    Optional<Inventario> optionalInventario= inventarioRepository.findById(Integer.parseInt(objetoMap.get("id")));

                    if(optionalInventario.isPresent()){

                        MateriaPrima materiaPrima= optionalInventario.get().getMateriaPrima();

                        double precio= materiaPrima.getCostoUnitario();
                        RecetaDTO recetaDTO= new RecetaDTO();
                        recetaDTO.setNombre(materiaPrima.getNombre());
                        //Precio por gramo
                        recetaDTO.setCostoProduccion(calcularCostoProduccion(precio));
                        recetaDTO.setUnidadMedida(materiaPrima.getUnidadMedida());
                        recetaDTO.setEsIngrediente(true);
                        recetaDTO.setId(materiaPrima.getId());
                        return new ResponseEntity<RecetaDTO>(recetaDTO,HttpStatus.OK);


                    }


                }else {
                    Optional<ProductoTerminado> optionalProductoTerminado= productoTerminadoRepository.findById(Integer.parseInt(objetoMap.get("id")));
                    if(optionalProductoTerminado.isPresent()){
                        ProductoTerminado productoTerminado= optionalProductoTerminado.get();
                        RecetaDTO recetaDTO= new RecetaDTO();
                        double precio=rendondearADos(calcularCostoProduccionTotal(productoTerminado.getId()));
                        double costoProduccionXUnidad= rendondearADos(precio/1000);
                        recetaDTO.setId(productoTerminado.getId());
                        recetaDTO.setNombre(productoTerminado.getNombre());
                        recetaDTO.setCostoProduccion(costoProduccionXUnidad);
                        recetaDTO.setEsIngrediente(false);
                        recetaDTO.setUnidadMedida(productoTerminado.getUnidadMedida());
                        return new ResponseEntity<RecetaDTO>(recetaDTO,HttpStatus.OK);



                    }
                }

            }
            return new ResponseEntity<RecetaDTO>(new RecetaDTO(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<RecetaDTO>(new RecetaDTO(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizar(Integer id, String nombre, String descripcion, double margenGanancia, double precioVenta, MultipartFile file, int idCategoria,int idCocina) {
        try {
            Optional<Menu> optional= menuRepository.findById(id);
            if(optional.isPresent()){
                Menu menu= optional.get();
                if(menu.getNombre().equalsIgnoreCase(nombre)){
                    menuRepository.save(actualizarDatos(menu,nombre,descripcion,margenGanancia,precioVenta,file,idCategoria,idCocina));
                    return Utils.getResponseEntity("Menú actualizado correctamente.",HttpStatus.OK);

                }else {
                    if(!menuRepository.existsByNombreLikeIgnoreCase(nombre)){
                        menuRepository.save(actualizarDatos(menu,nombre,descripcion,margenGanancia,precioVenta,file,idCategoria,idCocina));
                        return Utils.getResponseEntity("Menú actualizado correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("No se puede asignar el nombre al menú.",HttpStatus.BAD_REQUEST);
                }

            }
            return Utils.getResponseEntity("No existe el menú.",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Menu> agregarMenu(String nombre, String descripcion, double precioVenta, MultipartFile file, int idCategoria,int idCocina) {
        try {
            if(!menuRepository.existsByNombreLikeIgnoreCase(nombre) || nombre.isEmpty() || nombre==null){
                if(validarCategoriaId(idCategoria)){
                    Menu menu= new Menu();
                    Optional<Categoria> categoriaOptional= categoriaRepository.findById(idCategoria);
                    categoriaOptional.ifPresent(menu::setCategoria);
                    Optional<Cocina> cocinaOptional=cocinaRepository.findById(idCocina);
                    cocinaOptional.ifPresent(menu::setCocina);
                    menu.setNombre(nombre);
                    menu.setDescripcion(descripcion);
                    menu.setVisibilidad(true);
                    menu.setDependent(false);
                    menu.setCostoProduccionDirecto(0);
                    menu.setPrecioVenta(precioVenta);

if(file==null||file.isEmpty()){
    menu.setImagen("default.jpg");
}else{
    String nombreImagen= uploadFileService.guardarImagen(file);
    menu.setImagen(nombreImagen);

}


                    menuRepository.save(menu);
                    return new ResponseEntity<Menu>(menu,HttpStatus.OK);


                }
                return new ResponseEntity<Menu>(new Menu(),HttpStatus.BAD_REQUEST);

            }
            return new ResponseEntity<Menu>(new Menu(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Menu>(new Menu(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> crearReceta(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("idMenu") && objetoMap.containsKey("receta") && objetoMap.containsKey("margenGanancia")){
                Optional<Menu> menuOptional= menuRepository.findById(Integer.parseInt(objetoMap.get("idMenu")));
                if(menuOptional.isPresent()){
                    Menu menu= menuOptional.get();
                    ObjectMapper objectMapper= new ObjectMapper();
                    try {
                        List<RecetaWrapper> recetaWrappers=objectMapper.readValue(objetoMap.get("receta"), new TypeReference<List<RecetaWrapper>>() {
                        });
                        if(!recetaWrappers.isEmpty()){
                            for(RecetaWrapper recetaWrapper: recetaWrappers){
                                if(recetaWrapper.getEsIngrediente().equalsIgnoreCase("true")){
                                    Optional<Inventario> optionalInventario= inventarioRepository.findById(recetaWrapper.getId());
                                    if(optionalInventario.isPresent()){
                                        MateriaPrima_Menu materiaPrimaMenu= new MateriaPrima_Menu();
                                        Inventario inventario= optionalInventario.get();
                                        materiaPrimaMenu.setMenu(menu);
                                        materiaPrimaMenu.setInventario(inventario);
                                        materiaPrimaMenu.setCantidad(recetaWrapper.getCantidad());
                                        materiaPrimaMenuRepository.save(materiaPrimaMenu);


                                    }

                                }else {
                                    Optional<ProductoTerminado> productoTerminadoOptional=productoTerminadoRepository.findById(recetaWrapper.getId());
                                    if(productoTerminadoOptional.isPresent()){
                                        ProductoTerminado productoTerminado=productoTerminadoOptional.get();
                                        ProductoTerminado_Menu productoTerminadoMenu=new ProductoTerminado_Menu();
                                        productoTerminadoMenu.setMenu(menu);
                                        productoTerminadoMenu.setProductoTerminado(productoTerminado);
                                        productoTerminadoMenu.setCantidad(recetaWrapper.getCantidad());
                                        productoTerminadoMenuRepository.save(productoTerminadoMenu);
                                    }

                                }
                            }
                            menu.setCostoProduccionDirecto(calcularCostoTotalMenu(menu.getId()));
                            menu.setMargenGanancia(Double.parseDouble(objetoMap.get("margenGanacia")));
                            menu.setDependent(true);
                            menuRepository.save(menu);
                            return Utils.getResponseEntity("Menú  guardado correctamente.",HttpStatus.OK);
                        }
                        return Utils.getResponseEntity("Menú guardado correctamente.",HttpStatus.OK);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return Utils.getResponseEntity("Menú guardado correctamente.",HttpStatus.OK);
                }
                return Utils.getResponseEntity("No existe el menú.",HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("idMenu") && objetoMap.containsKey("visibilidad") ){
                Optional<Menu> menuOptional= menuRepository.findById(Integer.parseInt(objetoMap.get("idMenu")));
                if(menuOptional.isPresent()){
                    Menu menu= menuOptional.get();
                    if(objetoMap.get("visibilidad").equalsIgnoreCase("false")){
                        if(detalleOrdenMenuRepository.existsByMenuIdAndEstadoNotIn(menu.getId())){
                            return Utils.getResponseEntity("No puedes cambiar el estado del menú ya que tiene una comanda en proceso.",HttpStatus.BAD_REQUEST);
                        }
                        menu.setVisibilidad(false);
                    }else{
                        menu.setVisibilidad(true);
                    }

                    menuRepository.save(menu);
                    return Utils.getResponseEntity("El estado del menú ha sido actualizado.",HttpStatus.OK);

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<RecetaDTO>> obtenerRecetasUnicasPorCocinaYMenu(Integer idMenu) {
        try {
            Set<String> recetasMenuSet = new HashSet<>();  // Para almacenar nombres únicos de la receta del menú
            Integer idCocina= 0;

            // Obtenemos las recetas del menú
            Optional<Menu> menuOptional = menuRepository.findById(idMenu);
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
                List<MateriaPrima_Menu> materiaPrimaMenus = materiaPrimaMenuRepository.getAllByMenu(menu);
                MateriaPrima_Menu materiaPrimaMenu= materiaPrimaMenus.get(0);
               idCocina= menu.getCocina().getId();

                List<ProductoTerminado_Menu> productoTerminadoMenus = productoTerminadoMenuRepository.getAllByMenu(menu);

                // Agregamos nombres a set para materia prima y productos terminados en el menú
                materiaPrimaMenus.forEach(mpm -> recetasMenuSet.add(mpm.getInventario().getMateriaPrima().getNombre()));
                productoTerminadoMenus.forEach(ptm -> recetasMenuSet.add(ptm.getProductoTerminado().getNombre()));
            }

            List<RecetaDTO> recetaDTOList = new ArrayList<>();
            List<Inventario> inventarios = obtenerMateriasIdCocina(idCocina);
            List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados = obtenerProductosTerminadosIdCocina(idCocina);

            // Agregamos las recetas de los inventarios si no están en el menú
            for (Inventario inventario : inventarios) {
                if (!recetasMenuSet.contains(inventario.getMateriaPrima().getNombre())) {
                    RecetaDTO recetaDTO = new RecetaDTO();
                    recetaDTO.setId(inventario.getId());
                    recetaDTO.setEsIngrediente(true);
                    recetaDTO.setNombre(inventario.getMateriaPrima().getNombre());
                    recetaDTOList.add(recetaDTO);
                }
            }

            // Agregamos las recetas de los productos terminados si no están en el menú
            for (MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado : materiaPrimaProductoTerminados) {
                if (!recetasMenuSet.contains(materiaPrimaProductoTerminado.getProductoTerminado().getNombre())) {
                    RecetaDTO recetaDTO = new RecetaDTO();
                    recetaDTO.setId(materiaPrimaProductoTerminado.getProductoTerminado().getId());
                    recetaDTO.setEsIngrediente(false);
                    recetaDTO.setNombre(materiaPrimaProductoTerminado.getProductoTerminado().getNombre());
                    recetaDTOList.add(recetaDTO);
                }
            }

            Set<Integer> idVistos = new HashSet<>();
            List<RecetaDTO> listaLimpia = new ArrayList<>();

            for (RecetaDTO receta : recetaDTOList) {
                if (!idVistos.contains(receta.getId())) {
                    listaLimpia.add(receta);
                    idVistos.add(receta.getId());
                }
            }

            return new ResponseEntity<>(listaLimpia, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<MenuDTO>> obtenerTodosPorCategoria(Integer id) {
        try {
            List<Menu> menuList=menuRepository.getAllByCategoriaId(id);
            List<MenuDTO> menuDTOS=new ArrayList<>();
            for (Menu menu: menuList) {
                MenuDTO menuDTO= new MenuDTO();
                menuDTO.setMenu(menu);
                menuDTO.setGanancia(menu.getPrecioVenta()-menu.getCostoProduccionDirecto());
                if(menu.isVisibilidad()){
                    menuDTO.setDisponibilidad("Visible");

                }else {
                    menuDTO.setDisponibilidad("No visible");
                }

                menuDTOS.add(menuDTO);

            }

            return new ResponseEntity<List<MenuDTO>>(menuDTOS,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<MenuDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Integer> obtenerTotalMenu() {
        try {
            return new ResponseEntity<Integer>((int) menuRepository.count(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Integer>(0,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Menu actualizarDatos(Menu menu, String nombre, String descripcion, double margenGanancia, double precioVenta, MultipartFile file, int idCategoria,int idCocina) throws IOException {
        menu.setNombre(nombre);
        menu.setDescripcion(descripcion);
        menu.setPrecioVenta(precioVenta);
        menu.setMargenGanancia(margenGanancia);
        Optional<Categoria> categoriaOptional= categoriaRepository.findById(idCategoria);
        Optional<Cocina> cocinaOptional=cocinaRepository.findById(idCocina);
        cocinaOptional.ifPresent(menu::setCocina);
        categoriaOptional.ifPresent(menu::setCategoria);

        if(file.isEmpty() || file==null){
            menu.setImagen(menu.getImagen());
        }else {
            if(!menu.getImagen().equalsIgnoreCase("default.jpg")){
                uploadFileService.eliminarImagen(menu.getImagen());
            }
            String nombreImagen= uploadFileService.guardarImagen(file);
            menu.setImagen(nombreImagen);
        }
        return menu;
    }

    private boolean validarExistenciaMateriaOProducto(int id, boolean esIngrediente) {
        System.out.println("Si llegue a la validacion");
        if(esIngrediente){
            Optional<Inventario> inventarioOptional= inventarioRepository.findById(id);
            System.out.println(inventarioOptional.isPresent());
            if(inventarioOptional.isPresent()){
                return true;
            }
            return false;
        }else {
            Optional<ProductoTerminado> productoTerminadoOptional=productoTerminadoRepository.findById(id);
            if(productoTerminadoOptional.isPresent()){
                return true;
            }
            return false;
        }
    }

    public List<Inventario> obtenerMateriasIdCocina(Integer id) {
        List<Inventario> inventarios = new ArrayList<>();
        Optional<Almacen> optional = almacenRepository.findAlmacenByCocina_Id(id);
        if (optional.isPresent()) {
            List<Inventario> inventarioList = inventarioRepository.getAllByAlmacen_Id(optional.get().getId());
            for (Inventario inventario : inventarioList) {
                if (inventario.getMateriaPrima().isVisibilidad()) {
                    inventarios.add(inventario);
                }

            }
            return inventarios;

        }
        return new ArrayList<>();
    }

    public List<MateriaPrima_ProductoTerminado> obtenerProductosTerminadosIdCocina(Integer id){
        List<MateriaPrima_ProductoTerminado> primaProductoTerminados=new ArrayList<>();
        List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados= materiaPrimaProductoTerminadoRepository.findAll();
        Optional<Almacen> almacenOptional= almacenRepository.findAlmacenByCocina_Id(id);
        if(almacenOptional.isPresent()){
            Almacen almacen= almacenOptional.get();
            for (MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado: materiaPrimaProductoTerminados) {
                Optional<Inventario> inventarioOptional= inventarioRepository.findById(materiaPrimaProductoTerminado.getInventario().getId());
                if(inventarioOptional.isPresent()){
                    Inventario inventario=inventarioOptional.get();
                    if(inventario.getMateriaPrima().isVisibilidad() && materiaPrimaProductoTerminado.getProductoTerminado().isVisibilidad()){
                        primaProductoTerminados.add(materiaPrimaProductoTerminado);
                    }
                }


            }

            return primaProductoTerminados;

        }
        return new ArrayList<>();



    }

    private double calcularCostoProduccion(double precio){
        double costoPorGramo= precio/1000;

        // Crear un objeto DecimalFormat para redondear a 4 decimales
        DecimalFormat df = new DecimalFormat("#.####");

        // Aplicar el formato y convertir el resultado a String
        String costoPorGramoFormateado = df.format(costoPorGramo);

        // Convertir el String resultante de nuevo a double si es necesario
        double costoPorGramoRedondeado = Double.parseDouble(costoPorGramoFormateado);
        return costoPorGramoRedondeado;
    }

    public Double calcularCostoProduccionTotal(Integer idProductoTerminado) {
        List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminadoList =
                materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(
                        productoTerminadoRepository.findById(idProductoTerminado).get()
                );

        double costoProduccionTotal = 0.0;

        for (MateriaPrima_ProductoTerminado mpt : materiaPrimaProductoTerminadoList) {
            MateriaPrima materiaPrima = mpt.getInventario().getMateriaPrima();
            double costoUnitario = materiaPrima.getCostoUnitario()/1000;
            double cantidad = mpt.getCantidad();

            // Convertir la cantidad a gramos si es menor que 1 (se asume que está en kilogramos)
            if (cantidad < 1) {
                cantidad *= 1000; // Convertir a gramos
            }

            costoProduccionTotal += cantidad * costoUnitario;
        }

        return costoProduccionTotal;
    }

    private double rendondearADos(double valor){
        // Crear un objeto DecimalFormat para redondear a 4 decimales
        DecimalFormat df = new DecimalFormat("#.####");

        // Aplicar el formato y convertir el resultado a String
        String costoPorGramoFormateado = df.format(valor);

        // Convertir el String resultante de nuevo a double si es necesario
        double costoPorGramoRedondeado = Double.parseDouble(costoPorGramoFormateado);
        return costoPorGramoRedondeado;

    }

    private boolean validarCategoriaId(int idCategoria) {
        Optional<Categoria> categoriaOptional= categoriaRepository.findById(idCategoria);
        return categoriaOptional.isPresent();
    }


    public Double calcularCostoTotalProductoTerminadoPorMenu(Integer menuId) {
        // Obtener todos los productos terminados en un menú
        List<ProductoTerminado_Menu> productosMenu = productoTerminadoMenuRepository.findAllByMenuId(menuId);
        double costoTotal = 0.0;

        // Calcular el costo de producción de cada producto terminado
        for (ProductoTerminado_Menu productoMenu : productosMenu) {
            ProductoTerminado producto = productoMenu.getProductoTerminado();
            double costoProducto = calcularCostoProduccionProducto(producto);
            costoTotal += costoProducto * productoMenu.getCantidad();
        }

        return costoTotal;
    }

    private double calcularCostoProduccionProducto(ProductoTerminado producto) {
        List<MateriaPrima_ProductoTerminado> materiales = materiaPrimaProductoTerminadoRepository.findAllByProductoTerminado(producto);
        double costo = 0.0;
        for (MateriaPrima_ProductoTerminado material : materiales) {
            double costoMateria = material.getInventario().getMateriaPrima().getCostoUnitario();
            costo += costoMateria * material.getCantidad();
        }
        return costo;
    }

    public Double calcularCostoTotalMenu(Integer menuId) {
        Double costoIngredientes = materiaPrimaMenuRepository.calcularCostoIngredientesPorMenu(menuId);
        Double costoProductosTerminados = calcularCostoTotalProductoTerminadoPorMenu(menuId);

        // Considerando la posibilidad de valores nulos si no hay ingredientes o productos terminados
        costoIngredientes = costoIngredientes != null ? costoIngredientes : 0.0;
        costoProductosTerminados = costoProductosTerminados != null ? costoProductosTerminados : 0.0;

        return costoIngredientes + costoProductosTerminados;
    }

}


/*
Editar la receta del menú solo con el id del ingrediente relacionado
Eliminar ingrediente de la receta del menú solo con el id del ingrediente relacionado
Editar la receta del producto terminado solo con el id del ingrediente relacionado
Agregar la receta con un enpoint separado tanto de menú como de producto terminado
*/