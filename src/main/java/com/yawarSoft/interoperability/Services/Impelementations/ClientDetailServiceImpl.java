package com.yawarSoft.interoperability.Services.Impelementations;


import com.yawarSoft.interoperability.Dtos.AuthLoginRequest;
import com.yawarSoft.interoperability.Dtos.AuthResponse;
import com.yawarSoft.interoperability.Entities.AuthExternalSystemMock;
import com.yawarSoft.interoperability.Model.CustomUserDetails;
import com.yawarSoft.interoperability.Repositories.InMemoryAuthExternalRepositoryMock;
import com.yawarSoft.interoperability.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


//Se crea userDetailService personalizado para obtener usuarios de BD, se requiere implementar UserDetailsService de spring security
@Service
public class ClientDetailServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private InMemoryAuthExternalRepositoryMock authExternalSystemMock;


    @Override
    public UserDetails loadUserByUsername(String client_user) throws UsernameNotFoundException {
        AuthExternalSystemMock entity = authExternalSystemMock.findByClientUser(client_user)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente externo no encontrado"));

        if (!"ACTIVO".equals(entity.getEstado())) {
            throw new BadCredentialsException("Cliente externo inactivo");
        }

        return new CustomUserDetails(
                entity.getUuid(),
                entity.getUsuario(),
                entity.getContrasena(),
                true,
                true,true,true,
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT_EXTERNAL"))
        );
    }
    public AuthResponse loginClientExternal(AuthLoginRequest authLoginRequest) {
        String clientUser = authLoginRequest.username();
        String clientSecret = authLoginRequest.password();

        Authentication authentication = this.authenticate(clientUser, clientSecret);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.createToken(authentication);
        return new AuthResponse(clientUser, "Cliente externo autenticado exitosamente", token, true);
    }

    private Authentication authenticate(String client_user, String client_secret) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) loadUserByUsername(client_user);

            if (!passwordEncoder.matches(client_secret, userDetails.getPassword())) {
                throw new BadCredentialsException("Client user o secret inválidos");
            }

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } catch (Exception e) {
            throw new BadCredentialsException("Client user o secret inválidos");
        }
    }
}

