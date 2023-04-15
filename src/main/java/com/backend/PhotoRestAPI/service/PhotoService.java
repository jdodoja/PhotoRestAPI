package com.backend.PhotoRestAPI.service;

import com.backend.PhotoRestAPI.model.Photo;
import com.backend.PhotoRestAPI.model.PhotoMapper;
import com.backend.PhotoRestAPI.model.TagEntity;
import com.backend.PhotoRestAPI.model.TagMapper;
import com.backend.PhotoRestAPI.repository.PhotoRepository;
import com.backend.PhotoRestAPI.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;
    private final PhotoMapper photoMapper;

    public Photo create(Photo photo) {
        var photoEntity = photoMapper.toEntity(photo);

        var tagEntities = getTagEntities(photo.getTags());
        photoEntity.setTags(tagEntities);
        return photoMapper.fromEntity(photoRepository.save(photoEntity));
    }

    public void delete(Long id) {
        photoRepository.deleteById(id);
    }

    public void deleteAll() {
        photoRepository.deleteAll();
    }

    private List<TagEntity> getTagEntities(List<String> tags) {
        return tags.stream().map(tagName ->
                tagRepository.findByName(tagName).orElseGet(
                        () -> tagRepository.save(TagEntity.builder().name(tagName).build())
                )).collect(Collectors.toList());

    }
}
