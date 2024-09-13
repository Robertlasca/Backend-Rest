package com.residencia.restaurante.proyecto.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.mail.MailProperties;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarVerificacion(String mensaje,String token){
        String verificationUrl = "http://localhost:8082/verificar?token=" + token;
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(mensaje);
        message.setSubject("Verificación de correo");
        message.setText("Para verificar to crreo, por favor haz cliente en el siguiente enlace: "+verificationUrl);
        mailSender.send(message);

    }

}
