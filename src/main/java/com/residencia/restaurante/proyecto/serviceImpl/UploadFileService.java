package com.residencia.restaurante.proyecto.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Servicio para la carga y eliminación de archivos.
 */
@Service
public class UploadFileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String guardarImagen(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Path directoryPath = Paths.get(uploadDir);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());
            return fileName;
        }
        return "default.jpg";
    }

    public void eliminarImagen(String nombre) {
        File file = new File(uploadDir + nombre);
        if (file.exists()) {
            file.delete();
        }
    }
}