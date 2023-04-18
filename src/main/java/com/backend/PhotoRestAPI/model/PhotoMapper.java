package com.backend.PhotoRestAPI.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface PhotoMapper {

    Photo fromEntity(PhotoEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    PhotoEntity toEntity(Photo photo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "tags", ignore = true)
    void updateImageFromDto(Photo photo, @MappingTarget PhotoEntity photoEntity);

    default List<String> mapTagsToStrings(List<TagEntity> tags) {
        return Objects.isNull(tags) ? new ArrayList<>() :
                tags.stream()
                        .map(TagEntity::getName)
                        .collect(Collectors.toList());
    }

}
