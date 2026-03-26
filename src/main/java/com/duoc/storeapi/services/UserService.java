package com.duoc.storeapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.storeapi.dto.UserUpdateRequest;
import com.duoc.storeapi.exceptions.BadRequestException;
import com.duoc.storeapi.exceptions.ResourceNotFoundException;
import com.duoc.storeapi.models.User;
import com.duoc.storeapi.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        log.info("Obteniendo todos los usuarios");
        return userRepository.findAll();
    }

    public User findById(Long id) {
        log.info("Obteniendo usuario con ID={}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));
    }

    public User create(User user) {
        log.info("Creando usuario con correo={}", user.getEmail());

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn("Creación de usuario fallida, correo ya registrado: {}", user.getEmail());
            throw new BadRequestException("Correo ya registrado");
        }

        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }

        User savedUser = userRepository.save(user);
        log.info("Usuario creado con éxito con ID={}", savedUser.getId());
        return savedUser;
    }

    public User update(Long id, UserUpdateRequest user) {
        log.info("Actualizando usuario con ID={}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));

        if (!existingUser.getEmail().equals(user.getEmail()) && userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn("Actualización de usuario fallida, correo ya registrado: {}", user.getEmail());
            throw new BadRequestException("Correo ya registrado");
        }

        existingUser.setName(user.getName() != null ? user.getName() : existingUser.getName());
        existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
        existingUser.setPassword(user.getPassword() != null ? user.getPassword() : existingUser.getPassword());
        existingUser.setRole(user.getRole() != null ? user.getRole() : existingUser.getRole());
        existingUser.setEnabled(user.getEnabled() != null ? user.getEnabled() : existingUser.getEnabled());

        User updatedUser = userRepository.save(existingUser);
        log.info("Usuario actualizado con éxito con ID={}", updatedUser.getId());
        return updatedUser;
    }

    public void delete(Long id) {
        log.info("Eliminando usuario con ID={}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));

        userRepository.delete(existingUser);
        log.info("Usuario eliminado con éxito con ID={}", id);
    }

    public User login(String email, String password) {
        log.info("Intentando inicio de sesión con correo={}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Correo o contraseña inválidos"));

        if (!user.getPassword().equals(password)) {
            log.warn("Inicio de sesión fallido, contraseña inválida para correo={}", email);
            throw new BadRequestException("Correo o contraseña inválidos");
        }

        if (Boolean.FALSE.equals(user.getEnabled())) {
            log.warn("Inicio de sesión fallido, usuario inactiv. Correo={}", email);
            throw new BadRequestException("Usuario inactivo");
        }

        log.info("Inicio de sesión exitoso para correo={}", email);
        return user;
    }
}