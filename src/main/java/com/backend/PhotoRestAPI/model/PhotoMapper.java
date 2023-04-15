package com.backend.PhotoRestAPI.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface PhotoMapper {

    Photo fromEntity(PhotoEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    PhotoEntity toEntity(Photo photo);

    default List<String> mapTagsToStrings(List<TagEntity> tags) {
        return tags.stream().map(TagEntity::getName).collect(Collectors.toList());
    }

}
