package com.residencia.restaurante.proyecto.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.dto.IngredienteProductoTerminado;
import com.residencia.restaurante.proyecto.dto.ProductoDto;
import com.residencia.restaurante.proyecto.dto.ProductoTerminadoDto;
import com.residencia.restaurante.proyecto.dto.RecetaDTO;
import com.residencia.restaurante.proyecto.entity.*;
import com.residencia.restaurante.proyecto.repository.*;
import com.residencia.restaurante.proyecto.service.IProductoTerminadoService;
import com.residencia.restaurante.proyecto.wrapper.IngredientesProductoTerminadoWrapper;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoTerminadoServiceImpl implements IProductoTerminadoService {
    @Autowired
    private IProductoTerminadoRepository productoTerminadoRepository;

    @Autowired
    private IMateriaPrimaRepository materiaPrimaRepository;

    @Autowired
    private IInventarioRepository inventarioRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;
    @Autowired
    private  UploadFileService uploadFileService;

    @Autowired
    private IProductoTerminado_MenuRepository productoTerminadoMenuRepository;

    @Autowired
    private IMateriaPrima_ProductoTerminadoRepository materiaPrimaProductoTerminadoRepository;


    @Override
    public ResponseEntity<List<ProductoTerminadoDto>> obtenerActivos() {
       try {

           List<ProductoTerminado> listaMenor= productoTerminadoRepository.findProductoTerminadoByStockActualMenorAlMinimo();

           List<ProductoTerminado> listMayor= productoTerminadoRepository.findProductoTerminadoByStockActualMayorAlMaximo();

           List<ProductoTerminado> listSuficiente= productoTerminadoRepository.findProductoTerminadoByStockActualEntreMinimoYMaximo();

           List<ProductoTerminadoDto> terminadoDtoList= new ArrayList<>();
           double costo=0;

           for (ProductoTerminado productoTerminado: listaMenor) {

               ProductoTerminadoDto productoTerminadoDto= new ProductoTerminadoDto();
               List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados= materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminado);
               productoTerminadoDto.setProductoTerminado(productoTerminado);
               if(productoTerminado.isVisibilidad()){
                   productoTerminadoDto.setDisponibilidad("Visible");
                   productoTerminadoDto.setCostoProduccion(calcularCosto(productoTerminado));

                   productoTerminadoDto.setEstado("Insuficiente");
                   terminadoDtoList.add(productoTerminadoDto);
               }



           }

           for (ProductoTerminado productoTerminado: listMayor) {

               ProductoTerminadoDto productoTerminadoDto= new ProductoTerminadoDto();
               productoTerminadoDto.setProductoTerminado(productoTerminado);
               if(productoTerminado.isVisibilidad()){
                   productoTerminadoDto.setDisponibilidad("Visible");
                   productoTerminadoDto.setCostoProduccion(calcularCosto(productoTerminado));


                   productoTerminadoDto.setEstado("Excedido");
                   terminadoDtoList.add(productoTerminadoDto);
               }



           }

           for (ProductoTerminado productoTerminado: listSuficiente) {

               ProductoTerminadoDto productoTerminadoDto= new ProductoTerminadoDto();
               productoTerminadoDto.setProductoTerminado(productoTerminado);
               if(productoTerminado.isVisibilidad()){
                   productoTerminadoDto.setDisponibilidad("Visible");
                   productoTerminadoDto.setCostoProduccion(calcularCosto(productoTerminado));

                   productoTerminadoDto.setEstado("Suficiente");
                   terminadoDtoList.add(productoTerminadoDto);
               }



           }

           return new ResponseEntity<List<ProductoTerminadoDto>>(terminadoDtoList,HttpStatus.OK);


       }catch (Exception e){
           e.printStackTrace();
       }
       return new ResponseEntity<List<ProductoTerminadoDto>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoTerminadoDto>> obtenerNoActivos() {
        try {
            List<ProductoTerminado> listaMenor= productoTerminadoRepository.getAllByVisibilidadFalse();


            List<ProductoTerminadoDto> terminadoDtoList= new ArrayList<>();
            double costo=0;

            for (ProductoTerminado productoTerminado: listaMenor) {

                ProductoTerminadoDto productoTerminadoDto= new ProductoTerminadoDto();
                List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados= materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminado);
                productoTerminadoDto.setProductoTerminado(productoTerminado);

                    productoTerminadoDto.setDisponibilidad("No visible");
                    productoTerminadoDto.setCostoProduccion(calcularCosto(productoTerminado));

                    productoTerminadoDto.setEstado("Indisponible");
                    terminadoDtoList.add(productoTerminadoDto);

            }




            return new ResponseEntity<List<ProductoTerminadoDto>>(terminadoDtoList,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductoTerminadoDto>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<ProductoTerminadoDto>> obtenerTodos() {
        try {
            List<ProductoTerminado> listaMenor= productoTerminadoRepository.findProductoTerminadoByStockActualMenorAlMinimo();

            List<ProductoTerminado> listMayor= productoTerminadoRepository.findProductoTerminadoByStockActualMayorAlMaximo();

            List<ProductoTerminado> listSuficiente= productoTerminadoRepository.findProductoTerminadoByStockActualEntreMinimoYMaximo();

            List<ProductoTerminadoDto> terminadoDtoList= new ArrayList<>();
            double costo=0;

            for (ProductoTerminado productoTerminado: listaMenor) {

                ProductoTerminadoDto productoTerminadoDto= new ProductoTerminadoDto();
                List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados= materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminado);
                productoTerminadoDto.setProductoTerminado(productoTerminado);
                if(productoTerminado.isVisibilidad()){
                    productoTerminadoDto.setDisponibilidad("Visible");
                }else {
                    productoTerminadoDto.setDisponibilidad("No visible");
                }
                productoTerminadoDto.setCostoProduccion(calcularCosto(productoTerminado));

                productoTerminadoDto.setEstado("Insuficiente");
                terminadoDtoList.add(productoTerminadoDto);


            }

            for (ProductoTerminado productoTerminado: listMayor) {

                ProductoTerminadoDto productoTerminadoDto= new ProductoTerminadoDto();
                productoTerminadoDto.setProductoTerminado(productoTerminado);
                if(productoTerminado.isVisibilidad()){
                    productoTerminadoDto.setDisponibilidad("Visible");
                }else {
                    productoTerminadoDto.setDisponibilidad("No visible");
                }
                productoTerminadoDto.setCostoProduccion(calcularCosto(productoTerminado));


                productoTerminadoDto.setEstado("Excedido");
                terminadoDtoList.add(productoTerminadoDto);


            }

            for (ProductoTerminado productoTerminado: listSuficiente) {

                ProductoTerminadoDto productoTerminadoDto= new ProductoTerminadoDto();
                productoTerminadoDto.setProductoTerminado(productoTerminado);
                if(productoTerminado.isVisibilidad()){
                    productoTerminadoDto.setDisponibilidad("Visible");
                }else {
                    productoTerminadoDto.setDisponibilidad("No visible");
                }
                productoTerminadoDto.setCostoProduccion(calcularCosto(productoTerminado));

                productoTerminadoDto.setEstado("Suficiente");
                terminadoDtoList.add(productoTerminadoDto);


            }

            return new ResponseEntity<List<ProductoTerminadoDto>>(terminadoDtoList,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductoTerminadoDto>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);



    }

    private double calcularCosto(ProductoTerminado productoTerminado) {

        List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados= materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminado);
        double costoTotal=0;
        if(!materiaPrimaProductoTerminados.isEmpty()){
            for (MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado:materiaPrimaProductoTerminados) {
                System.out.println("Materia prima id"+materiaPrimaProductoTerminado.getProductoTerminado());
                System.out.println("Inventario id"+materiaPrimaProductoTerminado.getInventario().getAlmacen());
                MateriaPrima materiaPrima= materiaPrimaProductoTerminado.getInventario().getMateriaPrima();
                double costo= calcularCostoProduccion(materiaPrima.getCostoUnitario());
                costoTotal=costoTotal+rendondearADos((materiaPrimaProductoTerminado.getCantidad()*1000)*costo);

            }
        }
        return rendondearADos(costoTotal);

    }

    @Override
    public ResponseEntity<String> agregar(String nombre, String unidadMedida, String descripcion, double stockMax, double stockMin, MultipartFile file, int idCategoria, String materias) {

        try {
            if(!productoTerminadoRepository.existsByNombreLikeIgnoreCase(nombre) || !nombre.isEmpty() ){


            if(validarCategoriaId(idCategoria)){
                ProductoTerminado productoTerminado= new ProductoTerminado();
                Optional<Categoria> categoriaOptional= categoriaRepository.findById(idCategoria);
                categoriaOptional.ifPresent(productoTerminado::setCategoria);

                productoTerminado.setNombre(nombre);
                productoTerminado.setDescripcion(descripcion);
                productoTerminado.setUnidadMedida(unidadMedida);
                productoTerminado.setVisibilidad(true);
                productoTerminado.setStockMax(stockMax);
                productoTerminado.setStockMin(stockMin);
                productoTerminado.setStockActual(0);

                if(file==null || file.isEmpty()){
                    productoTerminado.setImagen("default.jpg");
                }else {
                    String nombreImagen=uploadFileService.guardarImagen(file);
                    productoTerminado.setImagen(nombreImagen);
                }


                productoTerminadoRepository.save(productoTerminado);

                //Asignar materias primas
                ObjectMapper objectMapper=new ObjectMapper();
                try {
                    List<IngredientesProductoTerminadoWrapper> ingredientesProductoTerminadoWrapperList=objectMapper.readValue(materias, new TypeReference<List<IngredientesProductoTerminadoWrapper>>() {});
                    if(!ingredientesProductoTerminadoWrapperList.isEmpty()){
                        for(IngredientesProductoTerminadoWrapper ingrediente: ingredientesProductoTerminadoWrapperList){
                            MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado= new MateriaPrima_ProductoTerminado();
                            materiaPrimaProductoTerminado.setCantidad(ingrediente.getCantidad());
                            materiaPrimaProductoTerminado.setProductoTerminado(productoTerminado);

                            Optional<MateriaPrima> materiaPrimaOptional= materiaPrimaRepository.findById(ingrediente.getIdMateriaPrima());
                            Optional<Inventario> inventarioOptional=inventarioRepository.findById(ingrediente.getIdMateriaPrima());

                            if(inventarioOptional.isPresent()){
                                Inventario inventario= inventarioOptional.get();
                                materiaPrimaProductoTerminado.setInventario(inventario);
                                //materiaPrimaProductoTerminado.setMateriaPrima(materiaPrima);
                            }

                            materiaPrimaProductoTerminadoRepository.save(materiaPrimaProductoTerminado);

                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                return Utils.getResponseEntity("Producto terminado guardado correctamente.",HttpStatus.OK);


            }
            return Utils.getResponseEntity("Error al asignar la categoría.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity("Ya existe un producto con este nombre.",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validarCategoriaId(int idCategoria) {
        Optional<Categoria> categoriaOptional= categoriaRepository.findById(idCategoria);
        return categoriaOptional.isPresent();

    }

    @Override
    public ResponseEntity<IngredienteProductoTerminado> obtenerMateriaPrimaId(Integer id) {
        try {
            Optional<Inventario> inventarioOptional= inventarioRepository.findById(id);
            if(inventarioOptional.isPresent()){

                MateriaPrima materiaPrima= inventarioOptional.get().getMateriaPrima();
                IngredienteProductoTerminado ingredienteProductoTerminado= new IngredienteProductoTerminado();
                //Se debe calcular el costo de produccion por gramo
                double precio= materiaPrima.getCostoUnitario();

                ingredienteProductoTerminado.setId(inventarioOptional.get().getId());
                ingredienteProductoTerminado.setNombre(materiaPrima.getNombre());
                ingredienteProductoTerminado.setUnidadMedida(materiaPrima.getUnidadMedida());
                ingredienteProductoTerminado.setCostoProduccion(calcularCostoProduccion(precio));
                return new ResponseEntity<IngredienteProductoTerminado>(ingredienteProductoTerminado,HttpStatus.OK);

            }
            return new ResponseEntity<IngredienteProductoTerminado>(new IngredienteProductoTerminado(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<IngredienteProductoTerminado>(new IngredienteProductoTerminado(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductoTerminado> obtenerProductoTerminado(Integer id) {
        try {
            Optional<ProductoTerminado> productoTerminadoOptional= productoTerminadoRepository.findById(id);
            if(productoTerminadoOptional.isPresent()){
                ProductoTerminado productoTerminado= productoTerminadoOptional.get();
                return new ResponseEntity<ProductoTerminado>(productoTerminado,HttpStatus.OK);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<ProductoTerminado>(new ProductoTerminado(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<RecetaDTO>> obtenerReceta(Integer id) {
        try {
            Optional<ProductoTerminado> optionalProductoTerminado= productoTerminadoRepository.findById(id);
            System.out.println("Este es el costo de produccion final."+calcularCostoProduccionTotal(id));
            List<RecetaDTO> recetaDTOS= new ArrayList<>();
            if(optionalProductoTerminado.isPresent()){
                ProductoTerminado productoTerminado=optionalProductoTerminado.get();
                List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados= materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminado);
                if(!materiaPrimaProductoTerminados.isEmpty()){
                    for (MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado:materiaPrimaProductoTerminados) {
                        MateriaPrima materiaPrima= materiaPrimaProductoTerminado.getInventario().getMateriaPrima();
                        double costo= calcularCostoProduccion(materiaPrima.getCostoUnitario());
                        RecetaDTO recetaDTO= new RecetaDTO();
                        recetaDTO.setEsIngrediente(true);
                        recetaDTO.setId(materiaPrimaProductoTerminado.getId());
                        recetaDTO.setUnidadMedida(materiaPrima.getUnidadMedida());
                        recetaDTO.setNombre(materiaPrima.getNombre());
                        recetaDTO.setCantidad(materiaPrimaProductoTerminado.getCantidad());
                        recetaDTO.setIdProductoTerminado(productoTerminado.getId());
                        //Costo de produccion ejemplo que la materia prima ocupe 0.200
                        recetaDTO.setCostoProduccion(rendondearADos((materiaPrimaProductoTerminado.getCantidad()*1000)*costo));

                        recetaDTOS.add(recetaDTO);


                    }
                }
                System.out.println("Este es su costo de produccion:"+materiaPrimaProductoTerminadoRepository.calcularCostoProduccionPorProductoTerminado(productoTerminado.getId()));

                return new ResponseEntity<List<RecetaDTO>>(recetaDTOS,HttpStatus.OK);


            }
            return new ResponseEntity<List<RecetaDTO>>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<RecetaDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> eliminar(Integer id) {
        try {
            Optional<ProductoTerminado> productoTerminadoOptional= productoTerminadoRepository.findById(id);
            if(productoTerminadoOptional.isPresent()){
                List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados=materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminadoOptional.get());
                materiaPrimaProductoTerminadoRepository.deleteAll(materiaPrimaProductoTerminados);
                productoTerminadoRepository.delete(productoTerminadoOptional.get());
                return Utils.getResponseEntity("Producto terminado elimnado correctamente",HttpStatus.OK);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> preparacionDiaria(Map<String, String> objetoMap) {
        try {
            if(validarMap(objetoMap)){
                if(validarStock(objetoMap)){


                Integer id= Integer.parseInt(objetoMap.get("id"));
                int cantidad= Integer.parseInt(objetoMap.get("cantidad"));
                Optional<ProductoTerminado> productoTerminadoOptional= productoTerminadoRepository.findById(id);
                if(productoTerminadoOptional.isPresent()) {
                    ProductoTerminado productoTerminado= productoTerminadoOptional.get();
                    List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados = materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminadoOptional.get());
                    productoTerminado.setStockActual(productoTerminado.getStockActual()+cantidad);

                    for (MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado:materiaPrimaProductoTerminados){
                        Inventario inventarioOptional=materiaPrimaProductoTerminado.getInventario();
                        double stockActual = materiaPrimaProductoTerminado.getInventario().getStockActual();
                        System.out.println("Este es el stock de la materia prima:"+stockActual);
                        double cantidadAPreparar= cantidad*materiaPrimaProductoTerminado.getCantidad();
                        System.out.println("Esta es la cantidad:" +cantidad+" :Esto es lo que requiere"+materiaPrimaProductoTerminado.getCantidad()+" y que se va a preparar:"+cantidadAPreparar);
                        inventarioOptional.setStockActual(stockActual-cantidadAPreparar);
                        inventarioRepository.save(inventarioOptional);

                    }
                    productoTerminadoRepository.save(productoTerminado);
                    return Utils.getResponseEntity("Preparación exitosa.",HttpStatus.OK);
                }
                }
                return Utils.getResponseEntity("No hay ingredientes suficientes.",HttpStatus.BAD_REQUEST);


            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> validarStockActual(Map<String, String> objetoMap) {
        try {
            if(validarStock(objetoMap)){
                return Utils.getResponseEntity("Si hay suficientes ingredientes.",HttpStatus.OK);
            }
            return Utils.getResponseEntity("No hay suficientes ingredientes.",HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizar(Integer id,String nombre, String descripcion, double stockMax, double stockMin, MultipartFile file, int idCategoria) {
        try {
            Optional<ProductoTerminado> optionalProductoTerminado= productoTerminadoRepository.findById(id);
            if(optionalProductoTerminado.isPresent()){
                ProductoTerminado productoTerminado= optionalProductoTerminado.get();
                if(productoTerminado.getNombre().equalsIgnoreCase(nombre)){
                    productoTerminadoRepository.save(actualizarDatos(productoTerminado,nombre,stockMax,stockMin,descripcion,idCategoria,file));

                    return Utils.getResponseEntity("Producto actualizado correctamente.",HttpStatus.OK);
                }else {
                    if(!productoTerminadoRepository.existsByNombreLikeIgnoreCase(nombre)){
                        productoTerminadoRepository.save(actualizarDatos(productoTerminado,nombre,stockMax,stockMin,descripcion,idCategoria,file));
                        return Utils.getResponseEntity("Producto actualizado correctamente.",HttpStatus.OK);

                    }
                    return Utils.getResponseEntity("No se puede asignar el nombre al producto.",HttpStatus.BAD_REQUEST);
                }

            }
            return Utils.getResponseEntity("No existe el producto terminado.",HttpStatus.BAD_REQUEST);


        }catch (Exception e){
            e.printStackTrace();
        }
        return  Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("idProducto") && objetoMap.containsKey("visibilidad") ){
                Optional<ProductoTerminado> menuOptional= productoTerminadoRepository.findById(Integer.parseInt(objetoMap.get("idProducto")));
                if(menuOptional.isPresent()){
                    ProductoTerminado menu= menuOptional.get();
                    if(objetoMap.get("visibilidad").equalsIgnoreCase("false")){
                        if(productoTerminadoMenuRepository.existsByProductoTerminadoIdAndMenuVisibilidadIsTrue(menu.getId())){
                            return Utils.getResponseEntity("No puedes cambiar el estado del producto ya que pertenece a un platillo.",HttpStatus.BAD_REQUEST);
                        }
                        menu.setVisibilidad(false);
                    }else{
                        menu.setVisibilidad(true);
                    }

                    productoTerminadoRepository.save(menu);
                    return Utils.getResponseEntity("El estado del producto ha sido actualizado.",HttpStatus.OK);

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoTerminadoDto>> obtenerTodosPorCategoria(Integer id) {
        try {
            List<ProductoTerminado> listaMenor= productoTerminadoRepository.getAllByCategoriaId(id);


            List<ProductoTerminadoDto> terminadoDtoList= new ArrayList<>();
            double costo=0;

            for (ProductoTerminado productoTerminado: listaMenor) {

                ProductoTerminadoDto productoTerminadoDto= new ProductoTerminadoDto();
                List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados= materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminado);
                productoTerminadoDto.setProductoTerminado(productoTerminado);
if(productoTerminado.isVisibilidad()){
    productoTerminadoDto.setDisponibilidad("Visible");
    productoTerminadoDto.setCostoProduccion(calcularCosto(productoTerminado));

    productoTerminadoDto.setEstado("Disponible");
}else {productoTerminadoDto.setDisponibilidad("No visible");
    productoTerminadoDto.setCostoProduccion(calcularCosto(productoTerminado));

    productoTerminadoDto.setEstado("Indisponible");

}

                terminadoDtoList.add(productoTerminadoDto);




            }




            return new ResponseEntity<List<ProductoTerminadoDto>>(terminadoDtoList,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductoTerminadoDto>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Integer> obtenerTotalProductos() {
        try {
            return new ResponseEntity<Integer>((int) productoTerminadoRepository.count(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Integer>(0,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ProductoTerminado actualizarDatos(ProductoTerminado productoTerminado, String nombre, double stockMax, double stockMin, String descripcion, int idCategoria, MultipartFile file) throws IOException {
        productoTerminado.setNombre(nombre);
        productoTerminado.setStockMax(stockMax);
        productoTerminado.setStockMin(stockMin);
        productoTerminado.setDescripcion(descripcion);
        Optional<Categoria> categoriaOptional= categoriaRepository.findById(idCategoria);
        categoriaOptional.ifPresent(productoTerminado::setCategoria);
        if(file==null || file.isEmpty()){
            productoTerminado.setImagen(productoTerminado.getImagen());
        }else {
            if(!productoTerminado.getImagen().equalsIgnoreCase("default.jpg")){
                uploadFileService.eliminarImagen(productoTerminado.getImagen());
            }
            String nombreImagen=uploadFileService.guardarImagen(file);
            productoTerminado.setImagen(nombreImagen);
        }
        return productoTerminado;

    }

    public boolean validarStock(Map<String, String> objetoMap){
        Integer id= Integer.parseInt(objetoMap.get("id"));
        int cantidad= Integer.parseInt(objetoMap.get("cantidad"));
        boolean suficienteStock=true;

        Optional<ProductoTerminado> productoTerminadoOptional= productoTerminadoRepository.findById(id);
        if(productoTerminadoOptional.isPresent()){
            List<MateriaPrima_ProductoTerminado> materiaPrimaProductoTerminados= materiaPrimaProductoTerminadoRepository.getAllByProductoTerminado(productoTerminadoOptional.get());

            for (MateriaPrima_ProductoTerminado materiaPrimaProductoTerminado:materiaPrimaProductoTerminados){
                double stockActual = materiaPrimaProductoTerminado.getInventario().getStockActual();
                System.out.println("Este es el stock de la materia prima:"+stockActual);
                double cantidadAPreparar= cantidad*materiaPrimaProductoTerminado.getCantidad();
                System.out.println("Esta es la cantidad:" +cantidad+" :Esto es lo que requiere"+materiaPrimaProductoTerminado.getCantidad()+" y que se va a preparar:"+cantidadAPreparar);
                if(cantidadAPreparar>stockActual){
                    suficienteStock=false;
                }

            }
        }

        return suficienteStock;

    }

    private boolean validarMap(Map<String, String> objetoMap) {
        return objetoMap.containsKey("id") && objetoMap.containsKey("cantidad");
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

    private double rendondearADos(double valor){
        // Crear un objeto DecimalFormat para redondear a 4 decimales
        DecimalFormat df = new DecimalFormat("#.####");

        // Aplicar el formato y convertir el resultado a String
        String costoPorGramoFormateado = df.format(valor);

        // Convertir el String resultante de nuevo a double si es necesario
        double costoPorGramoRedondeado = Double.parseDouble(costoPorGramoFormateado);
        return costoPorGramoRedondeado;

    }

    // Método para calcular el costo de producción total de un producto terminado
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
}
