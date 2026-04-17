package com.example.dashboardbackend.controllers;
import com.example.dashboardbackend.dtos.meme.MemeResponse;
import com.example.dashboardbackend.services.MemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meme")
public class MemeController {

  private final MemeService memeService;

  public MemeController(MemeService memeService) {
    this.memeService = memeService;
  }

  @GetMapping("/day")
  public ResponseEntity<MemeResponse> getMemeOfTheDay() {
    return ResponseEntity.ok(memeService.getMemeOfTheDay());
  }
}
