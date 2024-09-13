package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.entity.Categoria;
import com.residencia.restaurante.proyecto.entity.ProductoNormal;
import com.residencia.restaurante.proyecto.repository.ICategoriaRepository;
import com.residencia.restaurante.proyecto.repository.IProductoNormalRepository;
import com.residencia.restaurante.proyecto.service.IProductoNormalService;
import com.residencia.restaurante.proyecto.dto.ProductoNormalDTO;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoNormalServiceImpl implements IProductoNormalService {
    @Autowired
    private IProductoNormalRepository productoNormalRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private UploadFileService uploadFileService;
    /**
     * Obtiene todos los productos normales activos.
     * @return ResponseEntity<List<ProductoNormal>> Lista de productos normales activos.
     */
    @Override
    public ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesActivos() {
        try {

            // Obtener las tres listas de inventarios
            List<ProductoNormal> inventariosMenorMinimo = productoNormalRepository.findProductoNormalByStockActualMenorAlMinimo();

            List<ProductoNormal> inventariosMayorMaximo = productoNormalRepository.findProductoNormalByStockActualMayorAlMaximo();

            List<ProductoNormal> inventariosEntreMinimoYMaximo = productoNormalRepository.findProductoNormalByStockActualEntreMinimoYMaximo();

            // Crear una lista para almacenar los inventarios con su estado
            List<ProductoNormalDTO> inventariosConEstado = new ArrayList<>();
            // Agregar inventarios con estado "Suficiente" a la lista

            // Agregar inventarios con estado "Insuficiente" a la lista
            for (ProductoNormal productoNormal : inventariosMenorMinimo) {
                if(productoNormal.isVisibilidad()){
                    ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                    productoNormalDTO.setProductoNormal(productoNormal);
                    productoNormalDTO.setEstado("Insuficiente");
                    productoNormalDTO.setDisponibilidad("Visible");
                    inventariosConEstado.add(productoNormalDTO);
                }
            }


            // Agregar inventarios con estado "Excedido" a la lista
            for (ProductoNormal productoNormal  : inventariosMayorMaximo) {
                if(productoNormal.isVisibilidad()){



                    ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                    productoNormalDTO.setProductoNormal(productoNormal);
                    productoNormalDTO.setEstado("Excedido");
                    productoNormalDTO.setDisponibilidad("Visible");
                    inventariosConEstado.add(productoNormalDTO);
                }

            }


            for (ProductoNormal productoNormal  : inventariosEntreMinimoYMaximo) {
                if(productoNormal.isVisibilidad()) {
                    ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                    productoNormalDTO.setProductoNormal(productoNormal);
                    productoNormalDTO.setEstado("Suficiente");
                    productoNormalDTO.setDisponibilidad("Visible");
                    inventariosConEstado.add(productoNormalDTO);
                }
            }
            return new ResponseEntity<List<ProductoNormalDTO>>(inventariosConEstado,HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductoNormalDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene todos los productos normales no activos.
     * @return ResponseEntity<List<ProductoNormal>> Lista de productos normales no activos.
     */
    @Override
    public ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesNoActivos() {
        try {

            // Obtener las tres listas de inventarios
            List<ProductoNormal> inventariosMenorMinimo = productoNormalRepository.findProductoNormalByStockActualMenorAlMinimo();

            List<ProductoNormal> inventariosMayorMaximo = productoNormalRepository.findProductoNormalByStockActualMayorAlMaximo();

            List<ProductoNormal> inventariosEntreMinimoYMaximo = productoNormalRepository.findProductoNormalByStockActualEntreMinimoYMaximo();

            // Crear una lista para almacenar los inventarios con su estado
            List<ProductoNormalDTO> inventariosConEstado = new ArrayList<>();
            // Agregar inventarios con estado "Suficiente" a la lista

            // Agregar inventarios con estado "Insuficiente" a la lista
            for (ProductoNormal productoNormal : inventariosMenorMinimo) {
                if(!productoNormal.isVisibilidad()){


                    ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                    productoNormalDTO.setProductoNormal(productoNormal);
                    productoNormalDTO.setEstado("Insuficiente");
                    productoNormalDTO.setDisponibilidad("No visible");
                    inventariosConEstado.add(productoNormalDTO);
                }
            }


            // Agregar inventarios con estado "Excedido" a la lista
            for (ProductoNormal productoNormal  : inventariosMayorMaximo) {
                if(!productoNormal.isVisibilidad()){



                ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                productoNormalDTO.setProductoNormal(productoNormal);
                productoNormalDTO.setEstado("Excedido");
                productoNormalDTO.setDisponibilidad("No visible");
                inventariosConEstado.add(productoNormalDTO);
                }

            }


            for (ProductoNormal productoNormal  : inventariosEntreMinimoYMaximo) {
                if(!productoNormal.isVisibilidad()) {
                    ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                    productoNormalDTO.setProductoNormal(productoNormal);
                    productoNormalDTO.setEstado("Suficiente");
                    productoNormalDTO.setDisponibilidad("No visible");
                    inventariosConEstado.add(productoNormalDTO);
                }
            }
            return new ResponseEntity<List<ProductoNormalDTO>>(inventariosConEstado,HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductoNormalDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Obtiene todos los productos normales.
     * @return ResponseEntity<List<ProductoNormal>> Lista de todos los productos normales.
     */
    @Override
    public ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormales() {
        try {

            // Obtener las tres listas de inventarios
            List<ProductoNormal> inventariosMenorMinimo = productoNormalRepository.findProductoNormalByStockActualMenorAlMinimo();

            List<ProductoNormal> inventariosMayorMaximo = productoNormalRepository.findProductoNormalByStockActualMayorAlMaximo();

            List<ProductoNormal> inventariosEntreMinimoYMaximo = productoNormalRepository.findProductoNormalByStockActualEntreMinimoYMaximo();

            // Crear una lista para almacenar los inventarios con su estado
            List<ProductoNormalDTO> inventariosConEstado = new ArrayList<>();
            // Agregar inventarios con estado "Suficiente" a la lista

            // Agregar inventarios con estado "Insuficiente" a la lista
            for (ProductoNormal productoNormal : inventariosMenorMinimo) {
                if(productoNormal.isVisibilidad()){


                ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                productoNormalDTO.setProductoNormal(productoNormal);
                productoNormalDTO.setEstado("Insuficiente");
                inventariosConEstado.add(productoNormalDTO);
                }
            }


            // Agregar inventarios con estado "Excedido" a la lista
            for (ProductoNormal productoNormal  : inventariosMayorMaximo) {
                ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                productoNormalDTO.setProductoNormal(productoNormal);
                productoNormalDTO.setEstado("Excedido");
                inventariosConEstado.add(productoNormalDTO);

            }


            for (ProductoNormal productoNormal  : inventariosEntreMinimoYMaximo) {
                ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                productoNormalDTO.setProductoNormal(productoNormal);
                productoNormalDTO.setEstado("Suficiente");
                inventariosConEstado.add(productoNormalDTO);
            }
            return new ResponseEntity<List<ProductoNormalDTO>>(inventariosConEstado,HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductoNormalDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Cambia el estado de visibilidad de un producto normal.
     * @param objetoMap Un mapa de datos con la información del producto y su nuevo estado.
     * @return ResponseEntity<String> Respuesta que indica el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("id") && objetoMap.containsKey("visibilidad")){
                Optional<ProductoNormal> productoNormalOptional= productoNormalRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(!productoNormalOptional.isEmpty()){
                    ProductoNormal productoNormal= productoNormalOptional.get();
                    if(objetoMap.get("visibilidad").equalsIgnoreCase("false")){
                        productoNormal.setVisibilidad(false);
                    }else{
                        productoNormal.setVisibilidad(true);
                    }

                    productoNormalRepository.save(productoNormal);

                    return Utils.getResponseEntity("El estado del producto ha sido cambiado.",HttpStatus.OK);

                }
                return Utils.getResponseEntity("El producto no existe.",HttpStatus.BAD_REQUEST);

            }

            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Agrega un nuevo producto normal.
     * @param nombre Nombre del producto.
     * @param descripcion Descripción del producto.
     * @param idCategoria ID de la categoría del producto.
     * @param stockMax Stock máximo del producto.
     * @param stockMin Stock mínimo del producto.
     * @param stockActual Stock actual del producto.
     * @param costoUnitario Costo unitario del producto.
     * @param margenGanancia Margen de ganancia del producto.
     * @param precioUnitario Precio unitario del producto.
     * @param file Archivo de imagen del producto.
     * @return ResponseEntity<String> Respuesta que indica el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> agregar(String nombre, String descripcion, int idCategoria,
                                          int stockMax, int stockMin, int stockActual, double costoUnitario,
                                          double margenGanancia, double precioUnitario, MultipartFile file) {
        try {

            if(!productoNormalExistente(nombre) || !nombre.isEmpty() ){
                if(validarCategoria(idCategoria)){
                    ProductoNormal productoNormal= new ProductoNormal();

                    productoNormal.setNombre(nombre);
                    productoNormal.setDescripcion(descripcion);
                    Optional<Categoria> optionalCategoria= categoriaRepository.findById(idCategoria);
                    if(optionalCategoria.isPresent()){
                        Categoria categoria=optionalCategoria.get();
                        productoNormal.setCategoria(categoria);
                    }
                    productoNormal.setCostoUnitario(costoUnitario);
                    productoNormal.setStockMax(stockMax);
                    productoNormal.setStockMin(stockMin);
                    productoNormal.setStockActual(stockActual);
                    productoNormal.setMargenGanacia(margenGanancia);
                    productoNormal.setPrecioUnitario(precioUnitario);
                    productoNormal.setVisibilidad(true);

                    if(file==null || file.isEmpty()){
                        productoNormal.setImagen("default.jpg");
                    }else {
                        String nombreImagen=uploadFileService.guardarImagen(file);
                        productoNormal.setImagen(nombreImagen);
                    }


                    productoNormalRepository.save(productoNormal);

                    return Utils.getResponseEntity("Producto guardado exitosamente.",HttpStatus.OK);

                }
                return Utils.getResponseEntity("No existe la categoría.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity("El producto ya existe.",HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Actualiza un producto normal existente.
     * @param id ID del producto a actualizar.
     * @param nombre Nombre del producto actualizado.
     * @param descripcion Descripción del producto actualizado.
     * @param idCategoria ID de la categoría del producto actualizado.
     * @param stockMax Stock máximo del producto actualizado.
     * @param stockMin Stock mínimo del producto actualizado.
     * @param stockActual Stock actual del producto actualizado.
     * @param costoUnitario Costo unitario del producto actualizado.
     * @param margenGanancia Margen de ganancia del producto actualizado.
     * @param precioUnitario Precio unitario del producto actualizado.
     * @param file Archivo de imagen del producto actualizado.
     * @return ResponseEntity<String> Respuesta que indica el resultado de la operación.
     */
    @Override
    public ResponseEntity<String> actualizar(Integer id,String nombre, String descripcion, int idCategoria, int stockMax, int stockMin, int stockActual, double costoUnitario, double margenGanancia, double precioUnitario, MultipartFile file) {
        try {
            Optional<ProductoNormal> productoNormalOptional=productoNormalRepository.findById(id);
            if(productoNormalOptional.isPresent()){
                if(validarCategoria(idCategoria)){
                    ProductoNormal productoNormal= productoNormalOptional.get();
                    if(productoNormalOptional.get().getNombre().equalsIgnoreCase(nombre)){

                        productoNormal.setNombre(nombre);
                        productoNormal.setDescripcion(descripcion);
                        Optional<Categoria> optionalCategoria= categoriaRepository.findById(idCategoria);
                        if(optionalCategoria.isPresent()){
                            Categoria categoria=optionalCategoria.get();
                            productoNormal.setCategoria(categoria);
                        }
                        productoNormal.setCostoUnitario(costoUnitario);
                        productoNormal.setStockMax(stockMax);
                        productoNormal.setStockMin(stockMin);
                        productoNormal.setStockActual(stockActual);
                        productoNormal.setMargenGanacia(margenGanancia);
                        productoNormal.setPrecioUnitario(precioUnitario);

                        if(file.isEmpty() || file==null){
                            productoNormal.setImagen(productoNormal.getImagen());
                        }else{
                            if(!productoNormal.getImagen().equalsIgnoreCase("default.jpg")){
                                uploadFileService.eliminarImagen(productoNormal.getImagen());
                            }
                            String nombreImagen=uploadFileService.guardarImagen(file);
                            productoNormal.setImagen(nombreImagen);
                        }

                        productoNormalRepository.save(productoNormal);
                        return Utils.getResponseEntity("Producto actualizado.",HttpStatus.OK);


                    }else {
                        if(!productoNormalExistente(nombre)){
                            productoNormal.setNombre(nombre);
                            productoNormal.setDescripcion(descripcion);
                            Optional<Categoria> optionalCategoria= categoriaRepository.findById(idCategoria);
                            if(optionalCategoria.isPresent()){
                                Categoria categoria=optionalCategoria.get();
                                productoNormal.setCategoria(categoria);
                            }
                            productoNormal.setCostoUnitario(costoUnitario);
                            productoNormal.setStockMax(stockMax);
                            productoNormal.setStockMin(stockMin);
                            productoNormal.setStockActual(stockActual);
                            productoNormal.setMargenGanacia(margenGanancia);
                            productoNormal.setPrecioUnitario(precioUnitario);

                            if(file.isEmpty()){
                                productoNormal.setImagen(productoNormal.getImagen());
                            }else{
                                if(!productoNormal.getImagen().equalsIgnoreCase("goku.jpg")){
                                    uploadFileService.eliminarImagen(productoNormal.getImagen());
                                }
                                String nombreImagen=uploadFileService.guardarImagen(file);
                                productoNormal.setImagen(nombreImagen);
                            }



                            productoNormalRepository.save(productoNormal);
                            return Utils.getResponseEntity("Producto actualizado.",HttpStatus.OK);

                        }
                        return Utils.getResponseEntity("No puedes asignarle este nombre.",HttpStatus.BAD_REQUEST);
                    }
                }
                return Utils.getResponseEntity("No existe la categoría.",HttpStatus.BAD_REQUEST);


            }
            return Utils.getResponseEntity("El producto no existe.",HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Lista los productos normales según su estado de stock.
     * @return ResponseEntity<List<ProductoNormalDTO>> Lista de productos normales con su estado de stock.
     */
    @Override
    public ResponseEntity<ProductoNormal> obtenerProductoNormalId(Integer id) {
        try {
            Optional<ProductoNormal> optional=productoNormalRepository.findById(id);
            if(optional.isPresent()){
                ProductoNormal productoNormal=optional.get();
                return new ResponseEntity<ProductoNormal>(productoNormal,HttpStatus.OK);
            }
            return new ResponseEntity<ProductoNormal>(new ProductoNormal(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<ProductoNormal>(new ProductoNormal(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Lista los productos normales según su estado de stock.
     * @return ResponseEntity<List<ProductoNormalDTO>> Lista de productos normales con su estado de stock.
     */
    @Override
    public ResponseEntity<List<ProductoNormalDTO>> listarPorStock() {
        try {

            // Obtener las tres listas de inventarios
            List<ProductoNormal> inventariosMenorMinimo = productoNormalRepository.findProductoNormalByStockActualMenorAlMinimo();

            List<ProductoNormal> inventariosMayorMaximo = productoNormalRepository.findProductoNormalByStockActualMayorAlMaximo();

            List<ProductoNormal> inventariosEntreMinimoYMaximo = productoNormalRepository.findProductoNormalByStockActualEntreMinimoYMaximo();

            // Crear una lista para almacenar los inventarios con su estado
            List<ProductoNormalDTO> inventariosConEstado = new ArrayList<>();
            // Agregar inventarios con estado "Suficiente" a la lista

            // Agregar inventarios con estado "Insuficiente" a la lista
            for (ProductoNormal productoNormal : inventariosMenorMinimo) {
                ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                productoNormalDTO.setProductoNormal(productoNormal);
                productoNormalDTO.setEstado("Insuficiente");
                if(productoNormal.isVisibilidad()){
                    productoNormalDTO.setDisponibilidad("Visible");
                }else{
                    productoNormalDTO.setDisponibilidad("No visible");
                }
                inventariosConEstado.add(productoNormalDTO);
            }


            // Agregar inventarios con estado "Excedido" a la lista
            for (ProductoNormal productoNormal  : inventariosMayorMaximo) {
                ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                productoNormalDTO.setProductoNormal(productoNormal);
                productoNormalDTO.setEstado("Excedido");
                if(productoNormal.isVisibilidad()){
                    productoNormalDTO.setDisponibilidad("Visible");
                }else{
                    productoNormalDTO.setDisponibilidad("No visible");
                }
                inventariosConEstado.add(productoNormalDTO);

            }


            for (ProductoNormal productoNormal  : inventariosEntreMinimoYMaximo) {
                ProductoNormalDTO productoNormalDTO = new ProductoNormalDTO();
                productoNormalDTO.setProductoNormal(productoNormal);
                productoNormalDTO.setEstado("Suficiente");
                if(productoNormal.isVisibilidad()){
                    productoNormalDTO.setDisponibilidad("Visible");
                }else{
                    productoNormalDTO.setDisponibilidad("No visible");
                }
                inventariosConEstado.add(productoNormalDTO);
            }
            return new ResponseEntity<List<ProductoNormalDTO>>(inventariosConEstado,HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductoNormalDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Integer> obtenerTotalProductosNormales() {
        try {
            return new ResponseEntity<Integer>((int) productoNormalRepository.count(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Integer>(0,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesPorCategoria(Integer id) {
        try {
            List<ProductoNormal> list= productoNormalRepository.getAllByCategoriaId(id);
            List<ProductoNormalDTO> productoNormalDTOS=new ArrayList<>();
            if(!list.isEmpty()){
                for (ProductoNormal productoNormal:list) {
                    ProductoNormalDTO productoNormalDTO=new ProductoNormalDTO();
                    productoNormalDTO.setProductoNormal(productoNormal);
                    if(productoNormal.isVisibilidad()){
                        productoNormalDTO.setEstado("Visible");
                    }else{
                        productoNormalDTO.setEstado("No visible");
                    }
                    productoNormalDTOS.add(productoNormalDTO);
                }

            }
            return new ResponseEntity<List<ProductoNormalDTO>>(productoNormalDTOS,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductoNormalDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean productoNormalExistente(String nombre) {
        return productoNormalRepository.existsProductoNormalByNombreLikeIgnoreCase(nombre);
    }

    private boolean validarCategoria(Integer id) {
        Optional<Categoria> optionalCategoria=categoriaRepository.findById(id);
        return optionalCategoria.isPresent();
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

}
