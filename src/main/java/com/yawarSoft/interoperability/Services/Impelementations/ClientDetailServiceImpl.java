package com.yawarSoft.interoperability.Services.Impelementations;


import com.yawarSoft.interoperability.Dtos.AuthLoginRequest;
import com.yawarSoft.interoperability.Dtos.AuthResponse;
import com.yawarSoft.interoperability.Entities.AuthExternalSystemEntity;
import com.yawarSoft.interoperability.Entities.AuthoritiesExternalSystemEntity;
import com.yawarSoft.interoperability.Model.CustomUserDetails;
import com.yawarSoft.interoperability.Repositories.AuthExternalSystemRepository;
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
    private AuthExternalSystemRepository externalSystemRepository;


    @Override
    public UserDetails loadUserByUsername(String client_user) throws UsernameNotFoundException {
        //Obteniendo userEntity de Base de datos
        AuthExternalSystemEntity entity = externalSystemRepository.findByClientUser(client_user)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente externo no encontrado"));

        if (!entity.getIsActive()) {
            throw new BadCredentialsException("Cliente externo inactivo");
        }

        //Mapeando a User de Spring Security (clase que implementa interfaz UserDetails)

        List<SimpleGrantedAuthority> authorities = entity.getAuthorities().stream()
                .filter(AuthoritiesExternalSystemEntity::getIsActive)
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .toList();

        //Se retorna y arma el objeto User de spring security
        return new CustomUserDetails(
                entity.getId(),
                entity.getClientUser(),
                entity.getClientSecret(),
                entity.getIsActive(),
                true, // accountNoExpired
                true, // credentialNoExpired
                true, // accountNoLocked
                authorities
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

