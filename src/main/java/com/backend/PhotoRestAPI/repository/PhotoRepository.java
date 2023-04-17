package com.backend.PhotoRestAPI.repository;

import com.backend.PhotoRestAPI.model.PhotoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

    Page<PhotoEntity> findByTagsName(String tagName, Pageable pageable);

    @Query("SELECT e FROM PhotoEntity e JOIN FETCH e.tags t WHERE e.id = :id AND e.createdAt <= :updatedAt ORDER BY e.createdAt DESC LIMIT 1")
    PhotoEntity findByIdAndClosestUpdatedAt(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt);


}
