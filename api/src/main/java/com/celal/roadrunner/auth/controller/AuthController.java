package com.celal.roadrunner.auth.controller;

import com.celal.roadrunner.auth.dto.LoginRequestDTO;
import com.celal.roadrunner.auth.dto.LoginResponseDTO;
import com.celal.roadrunner.auth.dto.RefreshRequestDTO;
import com.celal.roadrunner.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authSvc;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO req
    ) {
        return ResponseEntity.ok(authSvc.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(
            @Valid @RequestBody RefreshRequestDTO req
    ) {
        return ResponseEntity.ok(authSvc.refresh(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshRequestDTO req
    ) {
        authSvc.logout(req);
        return ResponseEntity.noContent().build();
    }
}
