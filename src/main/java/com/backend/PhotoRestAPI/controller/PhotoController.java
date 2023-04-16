package com.backend.PhotoRestAPI.controller;

import com.backend.PhotoRestAPI.model.Photo;
import com.backend.PhotoRestAPI.service.PhotoService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/photo")
@AllArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping
    ResponseEntity<Photo> create(@RequestBody Photo photo) {
        photoService.create(photo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(photo); //TODO change
    }

    @PutMapping("/{id}")
    Photo update(@PathVariable("id") Long id,
                 @RequestBody @NonNull Photo photo) {
        photo.setId(id);
        return photoService.update(photo);

    }

    @GetMapping
    List<Photo> findPhotos(@RequestParam(required = false) String tag,
                           @RequestParam(defaultValue = "0") Integer pageNo,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "desc") String direction
    ) {
        return photoService.findPhotos(tag, pageNo, pageSize, direction);

    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id) {
        photoService.delete(id);
    }

    @DeleteMapping()
    void deleteAll() {
        photoService.deleteAll();
    }
}