package com.yawarSoft.interoperability.Services.Impelementations;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.yawarSoft.interoperability.Entities.AuthExternalSystemMock;
import com.yawarSoft.interoperability.Repositories.InMemoryAuthExternalRepositoryMock;
import com.yawarSoft.interoperability.Services.Interfaces.AuthenticatedExternalClientService;
import com.yawarSoft.interoperability.Utils.AuthExternalClientUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedExternalClientServiceImpl implements AuthenticatedExternalClientService {

    private final InMemoryAuthExternalRepositoryMock authExternalRepositoryMock;

    public AuthenticatedExternalClientServiceImpl(InMemoryAuthExternalRepositoryMock authExternalRepositoryMock) {
        this.authExternalRepositoryMock = authExternalRepositoryMock;
    }

    @Override
    public AuthExternalSystemMock getExternalClient() {
        AuthExternalSystemMock externalSystem = AuthExternalClientUtils.getAuthenticatedUser();
        return authExternalRepositoryMock.findByUuid(externalSystem.getUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente externo no encontrado"));
    }
}
