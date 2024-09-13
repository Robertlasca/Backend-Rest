package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.dto.DetalleOrdenProductoDTO;
import com.residencia.restaurante.proyecto.dto.DetalleVentaDTO;
import com.residencia.restaurante.proyecto.dto.VentasDTO;
import com.residencia.restaurante.proyecto.entity.DetalleOrdenMenu;
import com.residencia.restaurante.proyecto.entity.DetalleOrden_ProductoNormal;
import com.residencia.restaurante.proyecto.entity.Venta;
import com.residencia.restaurante.proyecto.entity.Venta_MedioPago;
import com.residencia.restaurante.proyecto.repository.IDetalleOrden_MenuRepository;
import com.residencia.restaurante.proyecto.repository.IDetalleOrden_ProductoNormalRepository;
import com.residencia.restaurante.proyecto.repository.IVentaRepository;
import com.residencia.restaurante.proyecto.repository.IVenta_MedioPagoRepository;
import com.residencia.restaurante.proyecto.service.IVentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VentasServiceImpl implements IVentasService {
    @Autowired
    private IVentaRepository ventaRepository;

    @Autowired
    private IVenta_MedioPagoRepository ventaMedioPagoRepository;

    @Autowired
    private IDetalleOrden_MenuRepository detalleOrdenMenuRepository;

    @Autowired
    private IDetalleOrden_ProductoNormalRepository detalleOrdenProductoNormalRepository;
    //Metodo para obtener todas las ventas
    @Override
    public ResponseEntity<List<VentasDTO>> obtenerTodas() {
        try {
            List<Venta> ventaList= ventaRepository.findAll();
            List<VentasDTO> ventasDTOS=new ArrayList<>();
            if(!ventaList.isEmpty()){
                for (Venta venta:ventaList){
                    VentasDTO ventasDTO= new VentasDTO();
                    ventasDTO.setId(venta.getId());
                    ventasDTO.setCliente(venta.getOrden().getNombreCliente());
                    ventasDTO.setComentario(venta.getComentario());
                    ventasDTO.setMesa(venta.getOrden().getMesa().getNombre());
                    ventasDTO.setEstado(venta.getEstado());
                    ventasDTO.setAreaServicio(venta.getOrden().getMesa().getAreaServicio().getNombre());
                    ventasDTO.setUsuario(venta.getUsuario().getNombre());
                    ventasDTO.setDescuento(venta.getDescuento());
                    ventasDTO.setFechaHora(venta.getFechaHoraConsolidacion());
                    ventasDTO.setFechaHoraApertura(venta.getOrden().getFechaHoraApertura());
                    ventasDTO.setTotalPagar(venta.getTotalPagar());
                    ventasDTO.setSubTotal(venta.getSubTotal());
                    ventasDTO.setOrdenId(venta.getOrden().getId());
                    ventasDTOS.add(ventasDTO);
                }
                return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.OK);

            }
            return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<VentasDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<DetalleVentaDTO> obtenerDetalleVenta(Integer id) {
        try {
            Optional<Venta> ventaOptional= ventaRepository.findById(id);
            if(ventaOptional.isPresent()){

                DetalleVentaDTO detalleVentaDTO= new DetalleVentaDTO();
                Venta venta= ventaOptional.get();
                VentasDTO ventasDTO= new VentasDTO();
                ventasDTO.setId(venta.getId());
                ventasDTO.setCliente(venta.getOrden().getNombreCliente());
                ventasDTO.setComentario(venta.getComentario());
                ventasDTO.setMesa(venta.getOrden().getMesa().getNombre());
                ventasDTO.setEstado(venta.getEstado());
                ventasDTO.setAreaServicio(venta.getOrden().getMesa().getAreaServicio().getNombre());
                ventasDTO.setUsuario(venta.getUsuario().getNombre());
                ventasDTO.setDescuento(venta.getDescuento());
                ventasDTO.setFechaHora(venta.getFechaHoraConsolidacion());
                ventasDTO.setFechaHoraApertura(venta.getOrden().getFechaHoraApertura());
                ventasDTO.setTotalPagar(venta.getTotalPagar());
                ventasDTO.setSubTotal(venta.getSubTotal());
                ventasDTO.setOrdenId(venta.getOrden().getId());
                detalleVentaDTO.setVentasDTO(ventasDTO);

                List<Venta_MedioPago> ventaMedioPagos=ventaMedioPagoRepository.getAllByVenta_Id(id);
                detalleVentaDTO.setVentaMedioPagos(ventaMedioPagos);

                List<DetalleOrdenMenu> detalleOrdenMenus= detalleOrdenMenuRepository.getAllByOrden(venta.getOrden());
                List<DetalleOrden_ProductoNormal> detalleOrdenProductoNormals=detalleOrdenProductoNormalRepository.getAllByOrden(venta.getOrden());
                List<DetalleOrdenProductoDTO> detalleOrdenProductoDTOS=new ArrayList<>();

                if(!detalleOrdenMenus.isEmpty()){
                    for (DetalleOrdenMenu detalleOrdenMenu:detalleOrdenMenus) {
                        DetalleOrdenProductoDTO detalleOrdenProductoDTO= new DetalleOrdenProductoDTO();
                        detalleOrdenProductoDTO.setEsDetalleMenu("true");
                        detalleOrdenProductoDTO.setNombreProducto(detalleOrdenMenu.getNombreMenu());
                        detalleOrdenProductoDTO.setCantidad(detalleOrdenMenu.getCantidad());
                        detalleOrdenProductoDTO.setTotal(detalleOrdenMenu.getTotal());
                        detalleOrdenProductoDTO.setEstado(detalleOrdenMenu.getEstado());
                        detalleOrdenProductoDTO.setIdDetalleOrden(detalleOrdenMenu.getId());
                        detalleOrdenProductoDTO.setPrecioUnitario(detalleOrdenMenu.getPrecioMenu());
                        detalleOrdenProductoDTOS.add(detalleOrdenProductoDTO);
                    }
                }

                if(!detalleOrdenProductoNormals.isEmpty()){
                    for (DetalleOrden_ProductoNormal detalleOrdenProductoNormal:detalleOrdenProductoNormals) {
                        DetalleOrdenProductoDTO detalleOrdenProductoDTO= new DetalleOrdenProductoDTO();
                        detalleOrdenProductoDTO.setEsDetalleMenu("false");
                        detalleOrdenProductoDTO.setNombreProducto(detalleOrdenProductoNormal.getNombreProductoNormal());
                        detalleOrdenProductoDTO.setCantidad(detalleOrdenProductoNormal.getCantidad());
                        detalleOrdenProductoDTO.setTotal(detalleOrdenProductoNormal.getTotal());
                        detalleOrdenProductoDTO.setEstado(detalleOrdenProductoNormal.getEstado());
                        detalleOrdenProductoDTO.setIdDetalleOrden(detalleOrdenProductoNormal.getId());
                        detalleOrdenProductoDTO.setPrecioUnitario(detalleOrdenProductoNormal.getPrecioProductoNormal());
                        detalleOrdenProductoDTOS.add(detalleOrdenProductoDTO);
                    }
                }

                detalleVentaDTO.setDetalleOrdenProductoDTOS(detalleOrdenProductoDTOS);
                detalleVentaDTO.setCaja(venta.getArqueo().getCaja().getNombre());
                detalleVentaDTO.setComensales(venta.getOrden().getCantidadComensal());

                return new ResponseEntity<DetalleVentaDTO>(detalleVentaDTO,HttpStatus.OK);



            }

            return new ResponseEntity<DetalleVentaDTO>(new DetalleVentaDTO(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<DetalleVentaDTO>(new DetalleVentaDTO(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<VentasDTO>> obtenerPorCaja(String caja) {
        try {
            List<Venta> ventaList= ventaRepository.findAll();
            List<VentasDTO> ventasDTOS=new ArrayList<>();
            if(!ventaList.isEmpty()){
                for (Venta venta:ventaList){
                    if(venta.getArqueo().getCaja().getNombre().equalsIgnoreCase(caja) && venta.getArqueo().getCaja().getVisibilidad()){
                    VentasDTO ventasDTO= new VentasDTO();
                    ventasDTO.setId(venta.getId());
                    ventasDTO.setCliente(venta.getOrden().getNombreCliente());
                    ventasDTO.setComentario(venta.getComentario());
                    ventasDTO.setMesa(venta.getOrden().getMesa().getNombre());
                    ventasDTO.setEstado(venta.getEstado());
                    ventasDTO.setAreaServicio(venta.getOrden().getMesa().getAreaServicio().getNombre());
                    ventasDTO.setUsuario(venta.getUsuario().getNombre());
                    ventasDTO.setDescuento(venta.getDescuento());
                    ventasDTO.setFechaHora(venta.getFechaHoraConsolidacion());
                    ventasDTO.setFechaHoraApertura(venta.getOrden().getFechaHoraApertura());
                    ventasDTO.setTotalPagar(venta.getTotalPagar());
                    ventasDTO.setSubTotal(venta.getSubTotal());
                    ventasDTO.setOrdenId(venta.getOrden().getId());
                    ventasDTOS.add(ventasDTO);
                    }
                }
                return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.OK);

            }
            return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<VentasDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<VentasDTO>> obtenerPorArea(String areaServicio) {
        try {
            List<Venta> ventaList= ventaRepository.findAll();
            List<VentasDTO> ventasDTOS=new ArrayList<>();
            if(!ventaList.isEmpty()){
                for (Venta venta:ventaList){
                    if(venta.getOrden().getMesa().getAreaServicio().getNombre().equalsIgnoreCase(areaServicio) && venta.getOrden().getMesa().getAreaServicio().isDisponibilidad()){
                        VentasDTO ventasDTO= new VentasDTO();
                        ventasDTO.setId(venta.getId());
                        ventasDTO.setCliente(venta.getOrden().getNombreCliente());
                        ventasDTO.setComentario(venta.getComentario());
                        ventasDTO.setMesa(venta.getOrden().getMesa().getNombre());
                        ventasDTO.setEstado(venta.getEstado());
                        ventasDTO.setAreaServicio(venta.getOrden().getMesa().getAreaServicio().getNombre());
                        ventasDTO.setUsuario(venta.getUsuario().getNombre());
                        ventasDTO.setDescuento(venta.getDescuento());
                        ventasDTO.setFechaHora(venta.getFechaHoraConsolidacion());
                        ventasDTO.setFechaHoraApertura(venta.getOrden().getFechaHoraApertura());
                        ventasDTO.setTotalPagar(venta.getTotalPagar());
                        ventasDTO.setSubTotal(venta.getSubTotal());
                        ventasDTO.setOrdenId(venta.getOrden().getId());
                        ventasDTOS.add(ventasDTO);
                    }
                }
                return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.OK);

            }
            return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<VentasDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<VentasDTO>> obtenerPorCliente(String cliente) {
        try {
            List<Venta> ventaList= ventaRepository.findAll();

            List<VentasDTO> ventasDTOS=new ArrayList<>();
            if(!ventaList.isEmpty()){
                for (Venta venta:ventaList){
                    if(venta.getOrden().getNombreCliente().equalsIgnoreCase(cliente)){
                        VentasDTO ventasDTO= new VentasDTO();
                        ventasDTO.setId(venta.getId());
                        ventasDTO.setCliente(venta.getOrden().getNombreCliente());
                        ventasDTO.setComentario(venta.getComentario());
                        ventasDTO.setMesa(venta.getOrden().getMesa().getNombre());
                        ventasDTO.setEstado(venta.getEstado());
                        ventasDTO.setAreaServicio(venta.getOrden().getMesa().getAreaServicio().getNombre());
                        ventasDTO.setUsuario(venta.getUsuario().getNombre());
                        ventasDTO.setDescuento(venta.getDescuento());
                        ventasDTO.setFechaHora(venta.getFechaHoraConsolidacion());
                        ventasDTO.setFechaHoraApertura(venta.getOrden().getFechaHoraApertura());
                        ventasDTO.setTotalPagar(venta.getTotalPagar());
                        ventasDTO.setSubTotal(venta.getSubTotal());
                        ventasDTO.setOrdenId(venta.getOrden().getId());
                        ventasDTOS.add(ventasDTO);
                    }
                }
                return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.OK);

            }
            return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<VentasDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<VentasDTO>> obtenerPorFecha(String fecha) {
        try {
            LocalDate fechaActual= LocalDate.parse(fecha);
            List<Venta> ventaList= ventaRepository.findAll();
            List<VentasDTO> ventasDTOS=new ArrayList<>();
            if(!ventaList.isEmpty()){
                for (Venta venta:ventaList){
                    if(venta.getFechaHoraConsolidacion().toLocalDate().equals(fechaActual)){
                        VentasDTO ventasDTO= new VentasDTO();
                        ventasDTO.setId(venta.getId());
                        ventasDTO.setCliente(venta.getOrden().getNombreCliente());
                        ventasDTO.setComentario(venta.getComentario());
                        ventasDTO.setMesa(venta.getOrden().getMesa().getNombre());
                        ventasDTO.setEstado(venta.getEstado());
                        ventasDTO.setAreaServicio(venta.getOrden().getMesa().getAreaServicio().getNombre());
                        ventasDTO.setUsuario(venta.getUsuario().getNombre());
                        ventasDTO.setDescuento(venta.getDescuento());
                        ventasDTO.setFechaHora(venta.getFechaHoraConsolidacion());
                        ventasDTO.setFechaHoraApertura(venta.getOrden().getFechaHoraApertura());
                        ventasDTO.setTotalPagar(venta.getTotalPagar());
                        ventasDTO.setSubTotal(venta.getSubTotal());
                        ventasDTO.setOrdenId(venta.getOrden().getId());
                        ventasDTOS.add(ventasDTO);
                    }
                }
                return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.OK);

            }
            return new ResponseEntity<List<VentasDTO>>(ventasDTOS,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<VentasDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
