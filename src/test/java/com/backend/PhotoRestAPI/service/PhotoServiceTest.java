package com.backend.PhotoRestAPI.service;

import com.backend.PhotoRestAPI.model.Photo;
import com.backend.PhotoRestAPI.model.PhotoEntity;
import com.backend.PhotoRestAPI.model.PhotoMapper;
import com.backend.PhotoRestAPI.model.TagEntity;
import com.backend.PhotoRestAPI.repository.PhotoRepository;
import com.backend.PhotoRestAPI.repository.TagRepository;
import org.assertj.core.api.BDDAssertions;
import org.hibernate.envers.AuditReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {
    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PhotoMapper photoMapper;

    @Mock
    private AuditReader auditReader;

    @InjectMocks
    private PhotoService photoService;

    @Test
    void shouldCreatePhoto() {
        // given
        var photo = givenPhoto("Given name");
        var photoEntity = givenPhotoEntity("Given name");
        var expectedPhoto = givenExpectedPhoto("Given name", photoEntity.getCreatedAt());

        given(photoMapper.toEntity(photo)).willReturn(photoEntity);
        given(photoRepository.save(photoEntity))
                .willReturn(photoEntity);
        given(tagRepository.findByName("tag1")).willReturn(Optional.ofNullable(givenTagEntity("tag1", 1L)));
        given(tagRepository.findByName("tag2")).willReturn(Optional.ofNullable(givenTagEntity("tag2", 2L)));
        given(photoMapper.fromEntity(photoEntity)).willReturn(expectedPhoto);

        // when
        var actual = photoService.create(photo);

        // then
        BDDAssertions.then(actual)
                .extracting(Photo::getId, Photo::getCreatedAt)
                .containsExactly(photoEntity.getId(), photoEntity.getCreatedAt());
    }

    @Test
    public void shouldUpdatePhoto() {
        // given
        long id = 1L;
        var photo = givenPhoto("Updated name");
        photo.setId(id);
        var photoEntity = givenPhotoEntity("Given name");
        var updatedEntity = givenPhotoEntity("Updated name");

        given(photoRepository.findById(id)).willReturn(Optional.of(photoEntity));
        given(photoRepository.save(photoEntity)).willReturn(updatedEntity);
        given(photoMapper.fromEntity(updatedEntity)).willReturn(photo);

        // when
        var actual = photoService.update(photo);

        // then
        then(photoMapper).should()
                .updateImageFromDto(photo, photoEntity);
        BDDAssertions.then(actual)
                .extracting(Photo::getId, Photo::getName)
                .containsExactly(photo.getId(), photo.getName());
    }

    @Test
    void testFindPhotos() {
        // given
        var pageNo = 0;
        var pageSize = 10;
        var order = "desc";
        var photoEntities = List.of(givenPhotoEntity("Given name"));
        var photos = List.of(givenPhoto("Given name"));
        var pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        given(photoRepository.findByTagsName("tag1", pageable)).willReturn(new PageImpl<>(photoEntities));
        given(photoMapper.fromEntity(photoEntities.get(0))).willReturn(photos.get(0));

        // when
        var actual = photoService.findPhotos("tag1", pageNo, pageSize, order);

        // then
        BDDAssertions.then(actual)
                .isEqualTo(photos);
    }

    @Test
    void testFindPhotosWithoutTag() {
        // given
        var pageNo = 0;
        var pageSize = 10;
        var order = "desc";
        var photoEntities = List.of(givenPhotoEntity("Given name"));
        var photos = List.of(givenPhoto("Given name"));
        var pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        given(photoRepository.findAll(pageable)).willReturn(new PageImpl<>(photoEntities));
        given(photoMapper.fromEntity(photoEntities.get(0))).willReturn(photos.get(0));

        // when
        var actual = photoService.findPhotos(null, pageNo, pageSize, order);

        // then
        BDDAssertions.then(actual)
                .isEqualTo(photos);
    }


    @Test
    public void shouldDeletePhoto() {
        // given
        var id = 1L;

        // when
        photoService.delete(id);

        // then
        then(photoRepository).should()
                .deleteById(id);
    }

    @Test
    public void shouldDeleteAll() {
        // when
        photoService.deleteAll();

        // then
        then(photoRepository).should()
                .deleteAll();
    }

    private Photo givenPhoto(String name) {
        var givenPhoto = new Photo();
        givenPhoto.setName(name);
        givenPhoto.setImageUrl("http://some.url.com");
        givenPhoto.setAuthor("Joe Doe");
        givenPhoto.setHeight(12.3);
        givenPhoto.setWidth(11.0);
        givenPhoto.setTags(List.of("tag1", "tag2"));
        return givenPhoto;
    }

    private Photo givenExpectedPhoto(String name, LocalDateTime createdAt) {
        var givenPhoto = new Photo();
        givenPhoto.setId(1L);
        givenPhoto.setName(name);
        givenPhoto.setImageUrl("http://some.url.com");
        givenPhoto.setAuthor("Joe Doe");
        givenPhoto.setHeight(12.3);
        givenPhoto.setWidth(11.0);
        givenPhoto.setTags(List.of("tag1", "tag2"));
        givenPhoto.setCreatedAt(createdAt);
        return givenPhoto;
    }


    private PhotoEntity givenPhotoEntity(String name) {
        var tags = List.of(givenTagEntity("tag1", 1L),
                givenTagEntity("tag2", 2L));
        var givenPhotoEntity = new PhotoEntity();
        givenPhotoEntity.setId(1L);
        givenPhotoEntity.setName(name);
        givenPhotoEntity.setImageUrl("http://some.url.com");
        givenPhotoEntity.setAuthor("Joe Doe");
        givenPhotoEntity.setHeight(12.3);
        givenPhotoEntity.setWidth(11.0);
        givenPhotoEntity.setTags(tags);
        givenPhotoEntity.setCreatedAt(LocalDateTime.now());
        givenPhotoEntity.setUpdatedAt(LocalDateTime.now());
        return givenPhotoEntity;
    }

    private TagEntity givenTagEntity(String name, Long id) {
        return TagEntity.builder()
                .id(id)
                .name(name)
                .build();
    }
}
