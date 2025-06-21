package com.yawarSoft.interoperability.Services.Impelementations;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.yawarSoft.interoperability.Entities.AuthExternalSystemEntity;
import com.yawarSoft.interoperability.Repositories.AuthExternalSystemRepository;
import com.yawarSoft.interoperability.Services.Interfaces.AuthenticatedExternalClientService;
import com.yawarSoft.interoperability.Utils.AuthExternalClientUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedExternalClientServiceImpl implements AuthenticatedExternalClientService {

    private  final AuthExternalSystemRepository authExternalSystemRepository;

    public AuthenticatedExternalClientServiceImpl(AuthExternalSystemRepository authExternalSystemRepository) {
        this.authExternalSystemRepository = authExternalSystemRepository;
    }

    @Override
    public AuthExternalSystemEntity getExternalClient() {
        AuthExternalSystemEntity externalSystem = AuthExternalClientUtils.getAuthenticatedUser();
        return authExternalSystemRepository.findById(externalSystem.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente externo no encontrado"));
    }
}
