package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.picture.PictureResponse;
import com.example.dashboardbackend.services.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/widgets/picture")
@RequiredArgsConstructor
public class PictureController {

  private final PictureService pictureService;

  @GetMapping("/{widgetId}")
  public ResponseEntity<PictureResponse> getPicture(@PathVariable Long widgetId) {
    return new ResponseEntity<>(pictureService.getPicture(widgetId), HttpStatus.OK);
  }

  @PostMapping("/{widgetId}")
  public ResponseEntity<PictureResponse> uploadPicture(
      @PathVariable Long widgetId,
      @RequestParam Long familyId,
      @RequestParam MultipartFile file
  ) {
    return new ResponseEntity<>(pictureService.uploadPicture(widgetId, familyId, file), HttpStatus.CREATED);
  }

  @DeleteMapping("/{widgetId}")
  public ResponseEntity<Void> deletePicture(@PathVariable Long widgetId) {
    pictureService.deletePicture(widgetId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}