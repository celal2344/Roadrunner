package com.celal.roadrunner.auth.service;

import com.celal.roadrunner.auth.dto.LoginRequestDTO;
import com.celal.roadrunner.auth.dto.LoginResponseDTO;
import com.celal.roadrunner.common.exception.UnauthorizedException;
import com.celal.roadrunner.user.entity.AppUserEntity;
import com.celal.roadrunner.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO auth);
}
@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {
    private final AppUserRepository appUserRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtSvc;

    public LoginResponseDTO login(LoginRequestDTO auth){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(auth.email(), auth.password())
        );

        AppUserEntity user = appUserRepo.findByEmail(auth.email())
                .orElseThrow(() -> new UnauthorizedException("auth.unauthorized", auth.email()));

        return jwtSvc.generateToken(user);
    }
}

