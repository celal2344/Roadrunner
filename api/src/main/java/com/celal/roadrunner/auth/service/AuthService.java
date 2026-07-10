package com.celal.roadrunner.auth.service;

import com.celal.roadrunner.auth.dto.LoginRequestDTO;
import com.celal.roadrunner.auth.dto.LoginResponseDTO;
import com.celal.roadrunner.auth.dto.RefreshRequestDTO;
import com.celal.roadrunner.common.exception.UnauthorizedException;
import com.celal.roadrunner.user.dto.AppUserDTO;
import com.celal.roadrunner.user.entity.AppUserEntity;
import com.celal.roadrunner.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO auth);
    LoginResponseDTO refresh(RefreshRequestDTO req);
    void logout(RefreshRequestDTO req);
}
@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {
    private final AppUserRepository appUserRepo;
    private final AuthenticationManager authenticationManager;
    private final StringRedisTemplate redisTemp;
    private final JwtService jwtSvc;

    public LoginResponseDTO login(LoginRequestDTO req){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.password())
            );
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("auth.invalid_credentials");
        }

        AppUserEntity user = appUserRepo.findByEmail(req.email())
                .orElseThrow(() -> new UnauthorizedException("auth.invalid_credentials"));

        JwtService.AccessToken accessToken = jwtSvc.generateAccessToken(user);
        JwtService.RefreshToken refreshToken = jwtSvc.generateRefreshToken(user);

        return new LoginResponseDTO(
                accessToken.value(),
                refreshToken.value(),
                "Bearer",
                accessToken.expiresAt(),
                refreshToken.expiresAt(),
                AppUserDTO.fromEntity(user)
        );
    }

    public LoginResponseDTO refresh(RefreshRequestDTO req){
        String refreshTokenKey = jwtSvc.refreshTokenKey(req.refreshToken());
        String userId = redisTemp.opsForValue().get(refreshTokenKey);
        if(userId == null){
            throw new UnauthorizedException("auth.refreshToken.invalid");
        }
        AppUserEntity user = appUserRepo.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UnauthorizedException("auth.refreshToken.invalid"));

        JwtService.AccessToken accessToken = jwtSvc.generateAccessToken(user);

        redisTemp.delete(refreshTokenKey);
        JwtService.RefreshToken refreshToken = jwtSvc.generateRefreshToken(user);

        return new LoginResponseDTO(
                accessToken.value(),
                refreshToken.value(),
                "Bearer",
                accessToken.expiresAt(),
                refreshToken.expiresAt(),
                AppUserDTO.fromEntity(user)
        );
    }

    public void logout(RefreshRequestDTO req) {
        redisTemp.delete(jwtSvc.refreshTokenKey(req.refreshToken()));
    }
}
