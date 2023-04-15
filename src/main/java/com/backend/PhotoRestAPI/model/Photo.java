package com.backend.PhotoRestAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Photo {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    private String name;

    private String description;

    private String author;

    @Column(updatable = false)
    private String imageUrl;

    private Double width;

    private Double height;

    @ManyToMany
    @JoinTable(
            name="photo_tag",
            joinColumns = @JoinColumn(name="photo_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id")
    )
    private List<Tag> tags;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate(){
        if(Objects.isNull(createdAt)){
            createdAt=Instant.now();
        }
        if(Objects.isNull(updatedAt)){ //check
            updatedAt=Instant.now();
        }
    }
    @PreUpdate
    protected void onUpdate(){
        setUpdatedAt(Instant.now());
    }
}
