package com.duoc.storeapi.utils;

import com.duoc.storeapi.exceptions.ForbiddenException;

public final class AuthUtil {
    private AuthUtil() {}

    public static void requireAuthedUser(Long userId, String role) {
        if (userId == null || role == null || role.isBlank()) {
            throw new ForbiddenException("Usuario no autenticado");
        }
    }

    public static void requireAdminRole(String role) {
        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            throw new ForbiddenException("No es un usuario administrador");
        }
    }

    public static void requireAdminOrOwner(Long headerUserId, String role, Long targetUserId) {
       if (role != null && role.equalsIgnoreCase("ADMIN")) {
        return;
       }

       if (headerUserId == null || !headerUserId.equals(targetUserId)) {
        throw new ForbiddenException("No es el dueño del recurso");
       }
    }
}