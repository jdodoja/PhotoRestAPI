package com.backend.PhotoRestAPI.repository;

import com.backend.PhotoRestAPI.model.Tag;
import com.backend.PhotoRestAPI.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> findByName(String name);
}
