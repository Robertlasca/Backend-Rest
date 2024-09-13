package com.residencia.restaurante.security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtUtil {

    private Logger log= (Logger) LoggerFactory.getLogger(JwtUtil.class);

    // Generar una clave secreta con algoritmo HS512
    private final Key secreto = Keys.secretKeyFor(SignatureAlgorithm.HS512);
/*
* Extrae el nombre de usuario del token JWT.

* Utiliza el método extractClaims para obtener las reclamaciones (claims)
* del token y luego extrae el sujeto (subject) que
* */
    public String extraerUsername(String token){
        return extraerClaims(token,Claims::getSubject);
    }
/*
* Extrae la fecha de expiración del token JWT.
Utiliza el método extractClaims para obtener las reclamaciones del
* token y luego extrae la fecha de expiración.
* */
    public Date extraerExpiracion(String token){
        return extraerClaims(token,Claims::getExpiration);
    }
/*Extrae las reclamaciones del token JWT utilizando un claimsResolver
que es una función que toma las reclamaciones y devuelve un valor de tipo T.
Obtiene todas las reclamaciones llamando al método extractAllClaims.*/
    public <T> T extraerClaims(String token, Function<Claims,T>claimsResolver){
        final Claims cliam= extraerTodosClaims(token);
        return claimsResolver.apply(cliam);
    }
/*Extrae todas las reclamaciones del token JWT.
Utiliza el secreto para firmar y validar el token.
En caso de error al analizar el token, registra un mensaje de error y
devuelve null*/
    public Claims extraerTodosClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secreto).build().parseClaimsJws(token).getBody();

        }catch (Exception e){
            log.error("Error parsing token: {}",e.getMessage(),e);
        }
        return null;
    }
/*Verifica si el token JWT ha expirado.
Compara la fecha de expiración del token con la fecha actual.*/
    public Boolean esTokenExpirado(String token){
        return extraerExpiracion(token).before(new Date());
    }
/*Genera un nuevo token JWT para un usuario específico.
Incluye el nombre de usuario como sujeto (subject) y un rol (role) como una
reclamación adicional.
Llama al método createToken para crear el token con las reclamaciones y el sujeto.*/
    public String generarToken(String username,String role){
        Map<String,Object> claims=new HashMap<>();
        claims.put("role",role);
        return crearToken(claims,username);
    }
/*
* Crea un nuevo token JWT con las reclamaciones y el sujeto especificados.
Establece la fecha de emisión y la fecha de expiración (24 horas a partir del momento actual).
Firma el token con la clave secreta (secreto).
* */
    private String crearToken(Map<String,Object>claims,String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(secreto).compact();
    }
/*Valida el token JWT comprobando si el nombre de usuario extraído del token coincide con el nombre de usuario del UserDetails proporcionado y si el token no ha expirado.*/
    public Boolean validarToken(String token, UserDetails userDetail){
        final String username=extraerUsername(token);
        return (username.equals(userDetail.getUsername()) && !esTokenExpirado(token));
    }




}
