package com.yawarSoft.interoperability.Utils;

import com.yawarSoft.interoperability.Entities.AuthExternalSystemEntity;
import com.yawarSoft.interoperability.Model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthExternalClientUtils {
    public static AuthExternalSystemEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            Integer userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
            return AuthExternalSystemEntity.builder().id(userId).build();
        }
        return null; // Retornar null si no hay usuario autenticado
    }
}
