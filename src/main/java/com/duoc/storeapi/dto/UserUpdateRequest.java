package com.duoc.storeapi.dto;

import com.duoc.storeapi.models.enums.RoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @Size(min = 3, max = 50, message = "Nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @Email(message = "El correo electrónico no es válido")
    private String email;

    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    private String password;

    private Boolean enabled;

    private RoleEnum role;
}