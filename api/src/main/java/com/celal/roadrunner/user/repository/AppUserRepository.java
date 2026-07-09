package com.celal.roadrunner.user.repository;

import com.celal.roadrunner.user.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUserEntity, Long>, JpaSpecificationExecutor<AppUserEntity> {
    Optional<AppUserEntity> findByEmail(String email);
}
