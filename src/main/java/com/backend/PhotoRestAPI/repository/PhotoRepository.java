package com.backend.PhotoRestAPI.repository;

import com.backend.PhotoRestAPI.model.PhotoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

    Page<PhotoEntity> findByTagsName(String tagName, Pageable pageable);

}
