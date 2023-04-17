package com.backend.PhotoRestAPI.controller;

import com.backend.PhotoRestAPI.model.Photo;
import com.backend.PhotoRestAPI.service.PhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/photo")
@AllArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping
    ResponseEntity<Photo> create(@RequestBody Photo photo) {
        var createdPhoto = photoService.create(photo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdPhoto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Photo> update(@PathVariable("id") Long id,
                                        @RequestBody @NonNull Photo photo) {
        photo.setId(id);
        var updatedPhoto = photoService.update(photo);
        return ResponseEntity.ok(updatedPhoto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Photo> getPhotoAtTime(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        try {
            var photo = photoService.getPhotoAtTime(id, timestamp);
            return ResponseEntity.ok(photo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        }

    }

    @GetMapping
    public ResponseEntity<List<Photo>> findPhotos(@RequestParam(required = false) String tag,
                                                  @RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "desc") String direction
    ) {
        var photos = photoService.findPhotos(tag, pageNo, pageSize, direction);
        return ResponseEntity.ok(photos);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        photoService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAll() {
        photoService.deleteAll();
        return ResponseEntity.noContent()
                .build();
    }
}