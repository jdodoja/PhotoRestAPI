package com.backend.PhotoRestAPI.repository;

import com.backend.PhotoRestAPI.model.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
}
