package com.backend.PhotoRestAPI.model;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag fromEntity(TagEntity entity);

    TagEntity toEntity(Tag tag);
}
