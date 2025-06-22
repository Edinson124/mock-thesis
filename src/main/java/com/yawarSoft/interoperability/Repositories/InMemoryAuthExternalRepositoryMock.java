package com.yawarSoft.interoperability.Repositories;

import com.yawarSoft.interoperability.Entities.AuthExternalSystemMock;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InMemoryAuthExternalRepositoryMock {

    private final AuthExternalSystemMock usuario;

    public InMemoryAuthExternalRepositoryMock() {
        this.usuario = AuthExternalSystemMock.builder()
                .uuid("011a82c8-99fd-4aaf-bf4e-55a692f34a7f")
                .usuario("user_demo")
                .contrasena("$2a$10$5AAOHVXohp92y7wH8Pe0/OmuyDhMmx7k92hCUlj/ie7OLsa.B8YBu")
                .estado("ACTIVO")
                .build();
    }

    public Optional<AuthExternalSystemMock> findByUsuario(String usuario) {
        if (this.usuario.getUsuario().equals(usuario)) {
            return Optional.of(this.usuario);
        }
        return Optional.empty();
    }

    public Optional<AuthExternalSystemMock> findByUuid(String id) {
        if (this.usuario.getUuid().equals(id)) {
            return Optional.of(this.usuario);
        }
        return Optional.empty();
    }

    public Optional<AuthExternalSystemMock> findByClientUser(String clientUser) {
        if (this.usuario.getUsuario().equals(clientUser)) {
            return Optional.of(this.usuario);
        }
        return Optional.empty();
    }
}