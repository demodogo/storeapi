package com.duoc.storeapi.models;

import com.duoc.storeapi.models.enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre requerido")
    @Size(min= 3, max=50, message="Nombre debe tener entre 3 y 50 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message="El correo electrónico no es válido")
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min= 6, max=20, message="La contraseña debe tener entre 6 y 20 caracteres")
    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RoleEnum role = RoleEnum.USER;
}

