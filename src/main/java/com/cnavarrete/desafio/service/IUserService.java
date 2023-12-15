package com.cnavarrete.desafio.service;

import com.cnavarrete.desafio.dto.UserResponseDTO;
import com.cnavarrete.desafio.models.UserEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserResponseDTO createUser(UserEntity user);
    public List<UserEntity> getAllUsers();
    public Optional<UserEntity> getUserById(Long userId);
    public UserEntity updateUser(Long userId, UserEntity newUser);
    public HashMap<String, String> deleteUser(Long userId);
}
