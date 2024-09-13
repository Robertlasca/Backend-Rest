package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.entity.Cocina_Utensilio;
import com.residencia.restaurante.proyecto.entity.Utensilio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de utensilios en el restaurante.
 */
public interface IUtensilioService {


    /**
     * Obtiene un utensilio por su ID.
     * @param id El ID del utensilio.
     * @return ResponseEntity con el utensilio correspondiente al ID.
     */
    ResponseEntity<Utensilio> obtenerUtensilioId(Integer id);

    /**
     * Obtiene todos los utensilios.
     * @return ResponseEntity con la lista de todos los utensilios.
     */
    ResponseEntity<List<Utensilio>> obtenerUtensilios();

    /**
     * Elimina un utensilio por su ID.
     * @param id El ID del utensilio a eliminar.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> eliminar(Integer id);

    /**
     * Obtiene todos los utensilios de una cocina específica.
     * @param id El ID de la cocina.
     * @return ResponseEntity con la lista de utensilios de la cocina especificada.
     */
    ResponseEntity<List<Cocina_Utensilio>> obtenerUtensiliosXCocina(Integer id);

    ResponseEntity<String> agregarUtensilio(String nombre,
                                            String descripcion,
                                            String inventario,
                                            MultipartFile file);

    ResponseEntity<String> actualizarUtensilio(
            Integer id,
            String nombre,
            String descripcion,
            int idCocina,
            int cantidad,
            MultipartFile file);
}
