package com.backend.PhotoRestAPI.service;

import com.backend.PhotoRestAPI.model.Photo;
import com.backend.PhotoRestAPI.model.PhotoEntity;
import com.backend.PhotoRestAPI.model.PhotoMapper;
import com.backend.PhotoRestAPI.model.TagEntity;
import com.backend.PhotoRestAPI.repository.PhotoRepository;
import com.backend.PhotoRestAPI.repository.TagRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;
    private final PhotoMapper photoMapper;
    private final EntityManagerFactory factory;


    public Photo create(Photo photo) {
        var photoEntity = photoMapper.toEntity(photo);

        var tagEntities = getTagEntities(photo.getTags());
        photoEntity.setTags(tagEntities);
        return photoMapper.fromEntity(photoRepository.save(photoEntity));
    }

    @Transactional
    public Photo update(Photo photo) {
        var photoId = photo.getId();
        var photoEntity = photoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));

        photoMapper.updateImageFromDto(photo, photoEntity);

        var tagEntities = getTagEntities(photo.getTags());
        photoEntity.setTags(tagEntities);

        return photoMapper.fromEntity(photoRepository.save(photoEntity));
    }

    public Photo getPhotoAtTime(Long id, LocalDateTime timestamp) {
        var entity = getPhotoEntityAudits(id, timestamp).stream().findFirst();
        if (entity.isPresent()) {
            return photoMapper.fromEntity(entity.get());
        } else {
            throw new EntityNotFoundException("Photo for given time not found");
        }
    }


    public List<Photo> findPhotos(String tag, Integer pageNo, Integer pageSize, String order) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, getOrder(order));

        var result = isNull(tag) ?
                photoRepository.findAll(pageable) :
                photoRepository.findByTagsName(tag, pageable);

        return result.getContent()
                .stream()
                .map(photoMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        photoRepository.deleteById(id);
    }

    public void deleteAll() {
        photoRepository.deleteAll();
    }

    private Sort getOrder(String order) {
        if (isNull(order)) {
            return Sort.unsorted();
        }
        var direction = Sort.Direction.fromString(order);
        var o = new Sort.Order(direction, "createdAt");
        return Sort.by(o);

    }

    private List<PhotoEntity> getPhotoEntityAudits(Long id, LocalDateTime updatedAt) {
        AuditReader reader = AuditReaderFactory.get(factory.createEntityManager());

        return reader.createQuery()
                .forRevisionsOfEntity(PhotoEntity.class, PhotoEntity.class.getName(), true, false)
                .add(AuditEntity.id()
                        .eq(id))
                .add(AuditEntity.property("updatedAt")
                        .lt(updatedAt))
                .addOrder(AuditEntity.revisionNumber()
                        .desc())
                .setMaxResults(1)
                .getResultList();
    }


    private List<TagEntity> getTagEntities(List<String> tags) {
        return tags.stream()
                .map(tagName ->
                        tagRepository.findByName(tagName)
                                .orElseGet(
                                        () -> tagRepository.save(TagEntity.builder()
                                                .name(tagName)
                                                .build())
                                ))
                .collect(Collectors.toList());

    }
}