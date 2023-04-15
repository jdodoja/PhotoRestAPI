package com.backend.PhotoRestAPI.model;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    private Long id;

    private String name;
}
