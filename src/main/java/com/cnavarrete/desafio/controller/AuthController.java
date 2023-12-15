package com.cnavarrete.desafio.controller;


import com.cnavarrete.desafio.dto.ErrorResponse;
import com.cnavarrete.desafio.dto.UserCredentials;
import com.cnavarrete.desafio.models.UserEntity;
import com.cnavarrete.desafio.repository.UserRepository;
import com.cnavarrete.desafio.security.CryptoConfig;
import com.cnavarrete.desafio.security.JWTAuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RestController
public class AuthController {

    @Value("${default.admin.mail}")
    private String defaultAdminMail;

    @Value("${default.admin.password}")
    private String defaultAdminPassword;

    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private CryptoConfig cryptoConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserCredentials getDefaultAdminCredentials() {
        String token = jwtAuthenticationConfig.getJWTToken(defaultAdminMail);
        return new UserCredentials(defaultAdminMail, defaultAdminPassword, token);
    }

    private UserCredentials getUserCredentials(UserEntity userEntity) {
        String token = jwtAuthenticationConfig.getJWTToken(userEntity.getName());
        return new UserCredentials(userEntity.getEmail(), userEntity.getPassword(), token);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserCredentials userCredentials) {
        try {
            String mail = userCredentials.getMail();
            String pass = userCredentials.getPass();

            if (Objects.equals(defaultAdminMail, mail) && Objects.equals(defaultAdminPassword, pass)) {
                // Devolver las credenciales del administrador sin crear un nuevo usuario
                return ResponseEntity.ok(getDefaultAdminCredentials());
            }

            // Buscar el usuario por correo
            Optional<UserEntity> optionalUser = userRepository.findByEmail(mail);

            if (optionalUser.isPresent()) {
                UserEntity existingUser = optionalUser.get();

                // Verificar la contraseña
                if (passwordEncoder.matches(pass, existingUser.getPassword())) {
                    // Actualizar la última fecha de inicio de sesión
                    existingUser.setLastLogin(LocalDateTime.now());
                    userRepository.save(existingUser);

                    // Devolver las credenciales del usuario existente
                    return ResponseEntity.ok(getUserCredentials(existingUser));
                } else {
                    throw new RuntimeException("Usuario o contraseña inválidos");
                }
            } else {
                throw new RuntimeException("Usuario o contraseña inválidos");
            }
        } catch (RuntimeException ex) {
            // Maneja la excepción y devuelve una respuesta personalizada en formato JSON
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }



}


