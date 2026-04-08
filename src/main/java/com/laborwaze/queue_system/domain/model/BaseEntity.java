package com.laborwaze.queue_system.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseEntity {

    protected String id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public BaseEntity(String id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (id == null || id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
