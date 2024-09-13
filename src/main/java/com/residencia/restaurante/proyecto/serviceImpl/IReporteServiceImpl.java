package com.residencia.restaurante.proyecto.serviceImpl;
import com.residencia.restaurante.proyecto.entity.*;
import com.residencia.restaurante.proyecto.repository.*;
import org.springframework.http.HttpHeaders;
import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.residencia.restaurante.proyecto.service.IReporteService;
import com.residencia.restaurante.security.model.Usuario;
import com.residencia.restaurante.security.repository.IUsuarioRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IReporteServiceImpl implements IReporteService {
    @Autowired
    private IProductoNormalRepository productoNormalRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private IAlmacenRepository almacenRepository;
    @Autowired
    private IInventarioRepository inventarioRepository;
    @Autowired
    private IVentaRepository ventaRepository;
    @Autowired
    private IDetalleOrden_MenuRepository detalleOrdenMenuRepository;

    @Override
    public ResponseEntity<InputStreamResource> downloadPDF(Map<String,String> objetoMap) {
        // Generar el PDF
        ByteArrayInputStream bis = generarReporteProductosAgotandose(objetoMap);

        // Configurar los headers para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @Override
    public ResponseEntity<InputStreamResource> descargarReporteInventarioAgotandose(Map<String, String> objetoMap) {
        // Generar el PDF
        ByteArrayInputStream bis = generarReporteInventarioAgotandose(objetoMap);

        // Configurar los headers para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=inventarioAgotado.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @Override
    public ResponseEntity<InputStreamResource> descargarReporteInventarioAgotandoseXAlmacen(Map<String, String> objetoMap) {
        // Generar el PDF
        ByteArrayInputStream bis = generarReporteInventarioAgotandoseXAlmacen(objetoMap);

        // Configurar los headers para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=inventarioAgotado.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @Override
    public ResponseEntity<InputStreamResource> descargarReporteDiarioVentas(Map<String, String> objetoMap) {
        // Generar el PDF
        ByteArrayInputStream bis = generarReporteDiarioVentas(objetoMap);

        // Configurar los headers para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ventasDiarias.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @Override
    public ResponseEntity<InputStreamResource> descargarReporteDiarioSemanal(Map<String, String> objetoMap) {
        // Generar el PDF
        ByteArrayInputStream bis = generarReporteSemanalVentas(objetoMap);

        // Configurar los headers para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ventasDiarias.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    private ByteArrayInputStream generarReporteSemanalVentas(Map<String, String> objetoMap) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(Integer.parseInt(objetoMap.get("idUsuario")));

        Usuario usuario = new Usuario();
        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Agregar logo y encabezado
            Image logo = Image.getInstance("uploads/images/logo.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.add(new Paragraph("Plazita Gourmet", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK)));
            document.add(new Paragraph(" "));
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            document.add(new Paragraph("Reporte Semanal de Ventas", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph("Semana: " + objetoMap.get("semana"), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Responsable: " + usuario.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Información de contacto del restaurante
            agregarInformacionContacto(document);

            // Datos de ventas
            LocalDate startDate = LocalDate.parse(objetoMap.get("diaInicio"));
            LocalDate endDate = LocalDate.parse(objetoMap.get("diaFinal"));
            List<Venta> ventas = ventaRepository.findByFechaBetween(startDate, endDate);
            double totalVentas = ventas.stream().mapToDouble(Venta::getTotalPagar).sum();
            document.add(new Paragraph("Total Ventas de la Semana: $" + totalVentas, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));

            // Tabla de ventas por día
            float[] columnWidths = {1, 3, 2, 2};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            PdfPCell cell;
            cell = new PdfPCell(new Phrase("Fecha", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Subtotal", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Descuento", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                LocalDate finalDate = date;
                List<Venta> ventasDiarias = ventas.stream().filter(v -> v.getFechaHoraConsolidacion().toLocalDate().equals(finalDate)).collect(Collectors.toList());
                double subTotalDiario = ventasDiarias.stream().mapToDouble(Venta::getSubTotal).sum();
                double descuentoDiario = ventasDiarias.stream().mapToDouble(Venta::getDescuento).sum();
                double totalDiario = ventasDiarias.stream().mapToDouble(Venta::getTotalPagar).sum();

                table.addCell(date.toString());
                table.addCell(String.valueOf(subTotalDiario));
                table.addCell(String.valueOf(descuentoDiario));
                table.addCell(String.valueOf(totalDiario));
            }

            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ResponseEntity<InputStreamResource> descargarReporteDiarioMensual(Map<String, String> objetoMap) {
        // Generar el PDF
        ByteArrayInputStream bis = generarReporteMensualVentas(objetoMap);

        // Configurar los headers para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ventasDiarias.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    private ByteArrayInputStream generarReporteMensualVentas(Map<String, String> objetoMap) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(Integer.parseInt(objetoMap.get("idUsuario")));

        Usuario usuario = new Usuario();
        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Agregar logo y encabezado
            Image logo = Image.getInstance("uploads/images/logo.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.add(new Paragraph("Plazita Gourmet", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK)));
            document.add(new Paragraph(" "));
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
            Date date = new Date();
            document.add(new Paragraph("Reporte Mensual de Ventas", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph("Mes: " + objetoMap.get("mes"), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Responsable: " + usuario.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Información de contacto del restaurante
            agregarInformacionContacto(document);

            // Datos de ventas
            YearMonth yearMonth = YearMonth.parse(objetoMap.get("mes"));
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();
            List<Venta> ventas = ventaRepository.findByFechaBetween(startDate, endDate);
            double totalVentas = ventas.stream().mapToDouble(Venta::getTotalPagar).sum();
            document.add(new Paragraph("Total Ventas del Mes: $" + totalVentas, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));

            // Tabla de ventas por semana
            float[] columnWidths = {1, 3, 2, 2};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            PdfPCell cell;
            cell = new PdfPCell(new Phrase("Semana", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Subtotal", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Descuento", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            for (int i = 0; i < 5; i++) {
                LocalDate weekStart = startDate.plusWeeks(i);
                LocalDate weekEnd = weekStart.plusDays(6).isBefore(endDate) ? weekStart.plusDays(6) : endDate;
                if (weekStart.isAfter(endDate)) break;

                List<Venta> ventasSemanales = ventas.stream().filter(v -> !v.getFechaHoraConsolidacion().toLocalDate().isBefore(weekStart) && !v.getFechaHoraConsolidacion().toLocalDate().isAfter(weekEnd)).collect(Collectors.toList());
                double subTotalSemanal = ventasSemanales.stream().mapToDouble(Venta::getSubTotal).sum();
                double descuentoSemanal = ventasSemanales.stream().mapToDouble(Venta::getDescuento).sum();
                double totalSemanal = ventasSemanales.stream().mapToDouble(Venta::getTotalPagar).sum();

                table.addCell("Semana " + (i + 1));
                table.addCell(String.valueOf(subTotalSemanal));
                table.addCell(String.valueOf(descuentoSemanal));
                table.addCell(String.valueOf(totalSemanal));
            }

            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ResponseEntity<InputStreamResource> descargarReporteAnualVentas(Map<String, String> objetoMap) {
        // Generar el PDF
        ByteArrayInputStream bis = generarReporteAnualVentas(objetoMap);

        // Configurar los headers para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ventasDiarias.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    private ByteArrayInputStream generarReporteAnualVentas(Map<String, String> objetoMap) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(Integer.parseInt(objetoMap.get("idUsuario")));

        Usuario usuario = new Usuario();
        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Agregar logo y encabezado
            Image logo = Image.getInstance("uploads/images/logo.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.add(new Paragraph("Plazita Gourmet", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK)));
            document.add(new Paragraph(" "));
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            Date date = new Date();
            document.add(new Paragraph("Reporte Anual de Ventas", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph("Año: " + objetoMap.get("anio"), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Responsable: " + usuario.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Información de contacto del restaurante
            agregarInformacionContacto(document);

            // Datos de ventas
            int anio = Integer.parseInt(objetoMap.get("anio"));
            LocalDate startDate = LocalDate.of(anio, 1, 1);
            LocalDate endDate = LocalDate.of(anio, 12, 31);
            List<Venta> ventas = ventaRepository.findByFechaBetween(startDate, endDate);
            double totalVentas = ventas.stream().mapToDouble(Venta::getTotalPagar).sum();
            document.add(new Paragraph("Total Ventas del Año: $" + totalVentas, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));

            // Tabla de ventas por mes
            float[] columnWidths = {1, 3, 2, 2};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            PdfPCell cell;
            cell = new PdfPCell(new Phrase("Mes", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Subtotal", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Descuento", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            for (int i = 1; i <= 12; i++) {
                YearMonth yearMonth = YearMonth.of(anio, i);
                LocalDate monthStart = yearMonth.atDay(1);
                LocalDate monthEnd = yearMonth.atEndOfMonth();

                List<Venta> ventasMensuales = ventas.stream().filter(v -> !v.getFechaHoraConsolidacion().toLocalDate().isBefore(monthStart) && !v.getFechaHoraConsolidacion().toLocalDate().isAfter(monthEnd)).collect(Collectors.toList());
                double subTotalMensual = ventasMensuales.stream().mapToDouble(Venta::getSubTotal).sum();
                double descuentoMensual = ventasMensuales.stream().mapToDouble(Venta::getDescuento).sum();
                double totalMensual = ventasMensuales.stream().mapToDouble(Venta::getTotalPagar).sum();

                table.addCell(yearMonth.getMonth().name());
                table.addCell(String.valueOf(subTotalMensual));
                table.addCell(String.valueOf(descuentoMensual));
                table.addCell(String.valueOf(totalMensual));
            }

            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private ByteArrayInputStream generarReporteDiarioVentas(Map<String, String> objetoMap) {
        Optional<Usuario> usuarioOptional= usuarioRepository.findById(Integer.parseInt(objetoMap.get("idUsuario")));

        Usuario usuario= new Usuario();
        if(usuarioOptional.isPresent()){
            usuario= usuarioOptional.get();
        }

        Document document = new Document();
        ByteArrayOutputStream out= new ByteArrayOutputStream();
        try {
            PdfWriter writer= PdfWriter.getInstance(document,out);
            document.open();

            //Agregar logo y encabezado
            Image logo= Image.getInstance("uploads/images/logo.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.add(new Paragraph("Plazita Gourmet", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK)));
            document.add(new Paragraph(" "));
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            document.add(new Paragraph("Reporte Diario de Ventas", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph("Fecha: " + dateFormat.format(date), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Responsable: " + usuario.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Información de contacto del restaurante
            agregarInformacionContacto(document);

            List<Venta> ventaList=ventaRepository.findVentasPorMes(Integer.parseInt(objetoMap.get("anio")),Integer.parseInt(objetoMap.get("mes")));
            String platilloMasVendido= obtenerPlatilloMasVendido(ventaList);
            double totalVentas= ventaList.stream().mapToDouble(Venta::getTotalPagar).sum();
            document.add(new Paragraph("Total Ventas del Día: $" + totalVentas, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));

            // Tabla de ventas
            float[] columnWidths = {1, 3, 2, 2};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            PdfPCell cell;
            cell = new PdfPCell(new Phrase("ID Venta", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Subtotal", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Descuento", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            for (Venta venta : ventaList) {
                table.addCell(String.valueOf(venta.getId()));
                table.addCell(String.valueOf(venta.getSubTotal()));
                table.addCell(String.valueOf(venta.getDescuento()));
                table.addCell(String.valueOf(venta.getTotalPagar()));
            }



            document.add(table);

            document.add(new Paragraph("Platillo mas vendido durante el día: " + platilloMasVendido, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));



            document.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return  new ByteArrayInputStream(out.toByteArray());


    }

    private String obtenerPlatilloMasVendido(List<Venta> ventaList) {
        if(!ventaList.isEmpty()){
            Map<String,Integer> conteoPlatillos= new HashMap<>();
            for (Venta venta:ventaList) {
                Orden orden= venta.getOrden();
                List<DetalleOrdenMenu> list= detalleOrdenMenuRepository.getAllByOrden(orden);
                for (DetalleOrdenMenu detalleOrdenMenu:list) {
                    String nombrePlatillo= detalleOrdenMenu.getNombreMenu();
                    if(conteoPlatillos.containsKey(nombrePlatillo)){
                        conteoPlatillos.put(nombrePlatillo,conteoPlatillos.get(nombrePlatillo)+detalleOrdenMenu.getCantidad());

                    }else {
                        conteoPlatillos.put(nombrePlatillo,detalleOrdenMenu.getCantidad());
                    }

                }



            }

            String platilloMasVendio=null;
            int cantidadMasVendida=0;

            for (Map.Entry<String,Integer> entry: conteoPlatillos.entrySet()) {
                if(entry.getValue()>cantidadMasVendida){
                    platilloMasVendio=entry.getKey();
                    cantidadMasVendida=entry.getValue();
                }
            }

            if(platilloMasVendio!=null){
                return platilloMasVendio;
            }

        }
        return "Ninguno";
    }

    private void agregarInformacionContacto(Document document) throws DocumentException {
        document.add(new Paragraph("Dirección: Luis G. Urbina 108, FERROCARRIL, Col del Periodista, 68060 Oaxaca de Juárez, Oax.",
                FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
        document.add(new Paragraph("Teléfono: +52 9514526384", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
        document.add(new Paragraph("Correo: contacto@plazitagourmet.com", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
        document.add(new Paragraph(" "));
    }

    private ByteArrayInputStream generarReporteInventarioAgotandoseXAlmacen(Map<String, String> objetoMap) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(Integer.parseInt(objetoMap.get("idUsuario")));


        Usuario usuario = new Usuario();
        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Agregar logo
            Image logo = Image.getInstance("uploads/images/logo.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            // Nombre del restaurante
            document.add(new Paragraph("Plazita Gourmet",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK)));
            document.add(new Paragraph(" ")); // Espacio

            // Encabezado del reporte
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String responsable = usuario.getNombre();  // Cambiar según sea necesario

            document.add(new Paragraph("Reporte de Inventario a Punto de Agotarse",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph("Fecha y Hora: " + dateFormat.format(date),
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Nombre del Responsable: " + responsable,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Información de contacto del restaurante
            document.add(new Paragraph("Dirección: Luis G. Urbina 108, FERROCARRIL, Col del Periodista, 68060 Oaxaca de Juárez, Oax.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Teléfono: +52 9514526384",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Correo: contacto@plazitagourmet.com",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Añadir espacio
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Resumen Ejecutivo",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            document.add(new Paragraph("Descripción General: Este reporte muestra las materias primas que están cerca de alcanzar su stock mínimo, separadas por almacén, necesitando atención inmediata para evitar la interrupción de producción.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Puntos Críticos: Consulte las materias primas listadas en la tabla y tome las medidas necesarias para reabastecerlas.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Añadir espacio
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));



            Optional<Almacen> almacenOptional= almacenRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if (almacenOptional.isPresent()) {

                    System.out.println("Entre");
                    List<Inventario> inventarios=inventarioRepository.inventariosInsuficientePorAlmacen(almacenOptional.get().getId());
                    if(!inventarios.isEmpty()){
                        System.out.println("Entre 1");
                        document.add(new Paragraph("Almacén "+almacenOptional.get().getNombre(),
                                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));

                        document.add(new Paragraph(" "));

                        // Crear la tabla
                        float[] columnWidths = {1, 3, 3, 2, 2, 2};
                        PdfPTable table = new PdfPTable(columnWidths);
                        table.setWidthPercentage(100);
                        //Agregando cabeceras de la tabla
                        PdfPCell cell;

                        cell = new PdfPCell(new Phrase("ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Materia prima", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Unidad Medida", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Stock Actual", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Stock Mínimo", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Stock Máximo", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        for (Inventario inventario:inventarios) {
                            table.addCell(String.valueOf(inventario.getId()));
                            table.addCell(inventario.getMateriaPrima().getNombre());
                            table.addCell(inventario.getMateriaPrima().getUnidadMedida());
                            table.addCell(String.valueOf(inventario.getStockActual()));
                            table.addCell(String.valueOf(inventario.getStockMin()));
                            table.addCell(String.valueOf(inventario.getStockMax()));

                        }

                        document.add(table);

                        // Añadir espacio
                        document.add(new Paragraph(" "));
                    }


                }


            // Comentarios y Recomendaciones
            String comentarios = objetoMap.containsKey("comentarios") ? objetoMap.get("comentarios") : "";
            String recomendaciones = objetoMap.containsKey("recomendaciones") ? objetoMap.get("recomendaciones") : "";

            document.add(new Paragraph("Comentarios y Recomendaciones",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            document.add(new Paragraph("Comentarios: " + comentarios,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Recomendaciones: " + recomendaciones,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Añadir espacio
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Sección de Validación
            document.add(new Paragraph("Sección de Validación",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            document.add(new Paragraph("Aprobaciones: ________________________________________________________________",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            String comentariosAdicionales = objetoMap.containsKey("comentariosAdicionales") ? objetoMap.get("comentariosAdicionales") : "";
            document.add(new Paragraph("Comentarios Adicionales: " + comentariosAdicionales,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Espacio extra para mejor separación visual.
            document.add(new Paragraph(" "));

            // Cierre del documento
            document.add(new Paragraph("Gracias por su atención.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Firma del Responsable: __________________________________________",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private ByteArrayInputStream generarReporteInventarioAgotandose(Map<String, String> objetoMap) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(Integer.parseInt(objetoMap.get("idUsuario")));
        List<Almacen> almacens= almacenRepository.findAll();

        Usuario usuario = new Usuario();
        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Agregar logo
            Image logo = Image.getInstance("uploads/images/logo.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            // Nombre del restaurante
            document.add(new Paragraph("Plazita Gourmet",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK)));
            document.add(new Paragraph(" ")); // Espacio

            // Encabezado del reporte
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String responsable = usuario.getNombre();  // Cambiar según sea necesario

            document.add(new Paragraph("Reporte de Inventario a Punto de Agotarse",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph("Fecha y Hora: " + dateFormat.format(date),
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Nombre del Responsable: " + responsable,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Información de contacto del restaurante
            document.add(new Paragraph("Dirección: Luis G. Urbina 108, FERROCARRIL, Col del Periodista, 68060 Oaxaca de Juárez, Oax.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Teléfono: +52 9514526384",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Correo: contacto@plazitagourmet.com",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Añadir espacio
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Resumen Ejecutivo",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            document.add(new Paragraph("Descripción General: Este reporte muestra las materias primas que están cerca de alcanzar su stock mínimo, separadas por almacén, necesitando atención inmediata para evitar la interrupción de producción.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Puntos Críticos: Consulte las materias primas listadas en la tabla y tome las medidas necesarias para reabastecerlas.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Añadir espacio
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));



            if(!almacens.isEmpty()){
                for (Almacen almacen:almacens) {
                    System.out.println("Entre");
                    List<Inventario> inventarios=inventarioRepository.inventariosInsuficientePorAlmacen(almacen.getId());
                    if(!inventarios.isEmpty()){
                        System.out.println("Entre 1");
                        document.add(new Paragraph("Almacén "+almacen.getNombre(),
                                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));

                        document.add(new Paragraph(" "));

                        // Crear la tabla
                        float[] columnWidths = {1, 3, 3, 2, 2, 2};
                        PdfPTable table = new PdfPTable(columnWidths);
                        table.setWidthPercentage(100);
                        //Agregando cabeceras de la tabla
                        PdfPCell cell;

                        cell = new PdfPCell(new Phrase("ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Materia prima", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Unidad Medida", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Stock Actual", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Stock Mínimo", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Stock Máximo", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                        cell.setBackgroundColor(BaseColor.GRAY);
                        table.addCell(cell);

                        for (Inventario inventario:inventarios) {
                            table.addCell(String.valueOf(inventario.getId()));
                            table.addCell(inventario.getMateriaPrima().getNombre());
                            table.addCell(inventario.getMateriaPrima().getUnidadMedida());
                            table.addCell(String.valueOf(inventario.getStockActual()));
                            table.addCell(String.valueOf(inventario.getStockMin()));
                            table.addCell(String.valueOf(inventario.getStockMax()));

                        }

                        document.add(table);

                        // Añadir espacio
                        document.add(new Paragraph(" "));
                    }


                }
            }

            // Comentarios y Recomendaciones
            String comentarios = objetoMap.containsKey("comentarios") ? objetoMap.get("comentarios") : "";
            String recomendaciones = objetoMap.containsKey("recomendaciones") ? objetoMap.get("recomendaciones") : "";

            document.add(new Paragraph("Comentarios y Recomendaciones",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            document.add(new Paragraph("Comentarios: " + comentarios,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Recomendaciones: " + recomendaciones,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Añadir espacio
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Sección de Validación
            document.add(new Paragraph("Sección de Validación",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            document.add(new Paragraph("Aprobaciones: ________________________________________________________________",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            String comentariosAdicionales = objetoMap.containsKey("comentariosAdicionales") ? objetoMap.get("comentariosAdicionales") : "";
            document.add(new Paragraph("Comentarios Adicionales: " + comentariosAdicionales,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Espacio extra para mejor separación visual.
            document.add(new Paragraph(" "));

            // Cierre del documento
            document.add(new Paragraph("Gracias por su atención.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Firma del Responsable: __________________________________________",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());



    }

    @Override
    public ByteArrayInputStream generarReporteProductosAgotandose(Map<String,String> objetoMap) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(Integer.parseInt(objetoMap.get("idUsuario")));
        List<ProductoNormal> productos = productoNormalRepository.findProductoNormalByStockActualMenorAlMinimo();

        Usuario usuario = new Usuario();
        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Agregar logo
            Image logo = Image.getInstance("uploads/images/logo.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            // Nombre del restaurante
            document.add(new Paragraph("Plazita Gourmet",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK)));
            document.add(new Paragraph(" ")); // Espacio

            // Encabezado del reporte
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String responsable = usuario.getNombre();  // Cambiar según sea necesario

            document.add(new Paragraph("Reporte de Productos a Punto de Agotarse",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
            document.add(new Paragraph("Fecha y Hora: " + dateFormat.format(date),
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Nombre del Responsable: " + responsable,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Información de contacto del restaurante
            document.add(new Paragraph("Dirección: Luis G. Urbina 108, FERROCARRIL, Col del Periodista, 68060 Oaxaca de Juárez, Oax.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Teléfono: +52 9514526384",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Correo: contacto@plazitagourmet.com",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Añadir espacio
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Resumen ejecutivo
            document.add(new Paragraph("Resumen Ejecutivo",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            document.add(new Paragraph("Descripción General: Este reporte muestra los productos que están cerca de alcanzar su stock mínimo, necesitando atención inmediata para evitar la ruptura de stock.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Puntos Críticos: Consultar los productos listados en la tabla y realizar las acciones necesarias para reabastecerlos.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Añadir espacio
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Crear la tabla
            float[] columnWidths = {1, 3, 3, 2, 2, 2};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            // Agregando cabeceras de la tabla
            PdfPCell cell;

            cell = new PdfPCell(new Phrase("ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Nombre", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Descripción", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Stock Actual", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Stock Mínimo", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Stock Máximo", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            // Llenando la tabla con datos de los productos
            for (ProductoNormal producto : productos) {
                table.addCell(String.valueOf(producto.getId()));
                table.addCell(producto.getNombre());
                table.addCell(producto.getDescripcion());
                table.addCell(String.valueOf(producto.getStockActual()));
                table.addCell(String.valueOf(producto.getStockMin()));
                table.addCell(String.valueOf(producto.getStockMax()));
            }

            document.add(table);

            // Comentarios y Recomendaciones
            String comentarios = objetoMap.containsKey("comentarios") ? objetoMap.get("comentarios") : "";
            String recomendaciones = objetoMap.containsKey("recomendaciones") ? objetoMap.get("recomendaciones") : "";

            document.add(new Paragraph("Comentarios y Recomendaciones",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            document.add(new Paragraph("Comentarios: " + comentarios,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Recomendaciones: " + recomendaciones,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Añadir espacio
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Sección de Validación
            document.add(new Paragraph("Sección de Validación",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            document.add(new Paragraph("Aprobaciones: ________________________________________________________________",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            String comentariosAdicionales = objetoMap.containsKey("comentariosAdicionales") ? objetoMap.get("comentariosAdicionales") : "";
            document.add(new Paragraph("Comentarios Adicionales: " + comentariosAdicionales,
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            // Espacio extra para mejor separación visual.
            document.add(new Paragraph(" "));

            // Cierre del documento
            document.add(new Paragraph("Gracias por su atención.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
            document.add(new Paragraph("Firma del Responsable: __________________________________________",
                    FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());

    }
}
