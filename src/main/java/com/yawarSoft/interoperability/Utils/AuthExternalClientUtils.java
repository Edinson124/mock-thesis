package com.yawarSoft.interoperability.Utils;

import com.yawarSoft.interoperability.Entities.AuthExternalSystemMock;
import com.yawarSoft.interoperability.Model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthExternalClientUtils {
    public static AuthExternalSystemMock getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            String userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
            return AuthExternalSystemMock.builder().uuid(userId).build();
        }
        return null; // Retornar null si no hay usuario autenticado
    }
}
