package com.backend.PhotoRestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    private Long id;

    private String name;

    private String description;

    private String author;

    private String imageUrl;

    private Double width;

    private Double height;

    private List<String> tags;

    private Instant createdAt;

    private Instant updatedAt;
}
