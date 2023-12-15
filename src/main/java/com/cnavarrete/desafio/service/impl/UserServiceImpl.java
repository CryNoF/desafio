package com.cnavarrete.desafio.service.impl;

import com.cnavarrete.desafio.constants.RegexPatterns;
import com.cnavarrete.desafio.dto.UserResponseDTO;
import com.cnavarrete.desafio.models.UserEntity;
import com.cnavarrete.desafio.repository.UserRepository;
import com.cnavarrete.desafio.security.CryptoConfig;
import com.cnavarrete.desafio.security.JWTAuthenticationConfig;
import com.cnavarrete.desafio.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserServiceImpl implements IUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTAuthenticationConfig jWTAuthenticationConfig;

    @Autowired
    private CryptoConfig cryptoConfig;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyecta el PasswordEncoder

    @Override
    public UserResponseDTO createUser(UserEntity user) {
        try {

            // Validación de correo electrónico
            String emailRegex = RegexPatterns.EMAIL;
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(user.getEmail());

            if (!matcher.matches()) {
                throw new RuntimeException("Correo electrónico no válido");
            }

            // Verificación de correo existente
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("El correo electrónico ya está registrado");
            }

            String passwordEncrypt = passwordEncoder.encode(user.getPassword());
            String token = jWTAuthenticationConfig.getJWTToken(user.getName());
            String newToken = token.startsWith("Bearer ") ? token.split(" ")[1] : token;

            user.setPassword(passwordEncrypt);
            user.setCreated(LocalDateTime.now());
            user.setModified(LocalDateTime.now());
            user.setLastLogin(LocalDateTime.now());
            user.setToken(newToken);
            user.setActive(true);

            UserEntity createdUser = userRepository.save(user);
            // Mapea la entidad a tu DTO
            UserResponseDTO responseDTO = new UserResponseDTO();
            responseDTO.setId(createdUser.getId());
            responseDTO.setName(createdUser.getName());
            responseDTO.setCreated(createdUser.getCreated());
            responseDTO.setModified(createdUser.getModified());
            responseDTO.setLastLogin(createdUser.getLastLogin());
            responseDTO.setToken(createdUser.getToken());
            responseDTO.setActive(createdUser.isActive());


            return responseDTO;
        } catch (Exception e) {
            LOGGER.error("Error while creating user: {}", e.getMessage());
            throw new RuntimeException("Error creating user");
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            LOGGER.error("Error, no se encontraron usuarios: {}", e.getMessage());
            throw new RuntimeException("Error encontrando usuarios");
        }
    }

    @Override
    public Optional<UserEntity> getUserById(Long userId) {
        try {
            return userRepository.findById(userId);
        } catch (Exception e) {
            LOGGER.error("Error al buscar ID: {}", e.getMessage());
            throw new RuntimeException("Error al buscar por ID");
        }
    }

    @Override
    public UserEntity updateUser(Long userId, UserEntity newUser) {
        try {
            UserEntity existingUser = userRepository.findById(userId).orElse(null);
            if (existingUser != null) {
                existingUser.setName(newUser.getName());
                existingUser.setEmail(newUser.getEmail());
                existingUser.setPassword(newUser.getPassword());

                return userRepository.save(existingUser);
            }
            throw new RuntimeException("Usuario no encontrado");
        } catch (Exception e) {
            LOGGER.error("Error al actualizar usuario: {}", e.getMessage());
            throw new RuntimeException("Error actualizando usuario");
        }
    }

    @Override
    public HashMap<String, String> deleteUser(Long userId) {
        try {
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Usuario eliminado exitosamente!");
            userRepository.deleteById(userId);
            return response;
        } catch (Exception e) {
            LOGGER.error("Error al borrar usuario: {}", e.getMessage());
            throw new RuntimeException("Error borrando usuario");
        }
    }
}
