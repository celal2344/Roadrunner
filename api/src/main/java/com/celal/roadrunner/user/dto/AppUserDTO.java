package com.celal.roadrunner.user.dto;

import com.celal.roadrunner.user.entity.AppUserEntity;
import com.celal.roadrunner.user.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserDTO {
    private Long id;
    private String fullName;
    private String email;
    private Role role;

    public static AppUserDTO fromEntity(AppUserEntity user) {
        return AppUserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}