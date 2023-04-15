package com.backend.PhotoRestAPI.controller;

import com.backend.PhotoRestAPI.model.Photo;
import com.backend.PhotoRestAPI.model.PhotoEntity;
import com.backend.PhotoRestAPI.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/photo")
@AllArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping
    ResponseEntity<Photo> create(@RequestBody Photo photo) {
        photoService.create(photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(photo); //TODO change
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