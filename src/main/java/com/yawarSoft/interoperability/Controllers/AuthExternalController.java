package com.yawarSoft.interoperability.Controllers;

import com.yawarSoft.interoperability.Dtos.AuthLoginRequest;
import com.yawarSoft.interoperability.Dtos.AuthResponse;
import com.yawarSoft.interoperability.Services.Impelementations.ClientDetailServiceImpl;
import com.yawarSoft.interoperability.Utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/client/auth")
@PreAuthorize("permitAll()")
public class AuthExternalController {

    private final ClientDetailServiceImpl clientDetailService;

    public AuthExternalController(ClientDetailServiceImpl clientDetailService) {
        this.clientDetailService = clientDetailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthLoginRequest userRequest){
        System.out.println("login Controller API");
        AuthResponse authResponse = clientDetailService.loginClientExternal(userRequest);
        String token = authResponse.jwt();
        long expirationTimestamp = Constants.getTimeToken();
        return ResponseEntity.ok()
                .body(Map.of(
                        "client_user", authResponse.username(),
                        "token", token,
                        "expires_in", expirationTimestamp
                ));
    }
}
