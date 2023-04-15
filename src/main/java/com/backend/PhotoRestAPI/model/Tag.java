package com.backend.PhotoRestAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    private String name;

    @ManyToMany (mappedBy = "tags")
    private List<Photo> photos;
}
