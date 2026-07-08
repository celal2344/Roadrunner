package com.celal.roadrunner.common.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    private Long id;

    @CreationTimestamp
    private Instant createdAt;
}
