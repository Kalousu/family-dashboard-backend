package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.picture.PictureResponse;
import com.example.dashboardbackend.models.widgets.picture.PictureItem;
import com.example.dashboardbackend.repositories.PictureRepository;
import com.example.dashboardbackend.repositories.WidgetItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class PictureService {

  private final PictureRepository pictureRepository;
  private final WidgetItemRepository widgetItemRepository;
  private final S3Client s3Client;

  @Value("${cloudflare.r2.bucket}")
  private String bucket;

  @Value("${cloudflare.r2.public-url}")
  private String publicUrl;

  public PictureResponse getPicture(Long widgetId) {
    PictureItem item = pictureRepository.findByWidgetItem_Id(widgetId)
                                        .orElseThrow(() -> new RuntimeException("Kein Bild für Widget " + widgetId));
    return new PictureResponse(item.getId(), widgetId, item.getImageUrl());
  }

  public PictureResponse uploadPicture(Long widgetId, Long familyId, MultipartFile file) {
    String key = "families/" + familyId + "/widgets/" + widgetId + "/" + file.getOriginalFilename();

    pictureRepository.findByWidgetItem_Id(widgetId).ifPresent(existing -> {
      s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(extractKey(existing.getImageUrl())).build());
      pictureRepository.delete(existing);
    });

    try {
      s3Client.putObject(
          PutObjectRequest.builder()
                          .bucket(bucket)
                          .key(key)
                          .contentType(file.getContentType())
                          .build(),
          RequestBody.fromBytes(file.getBytes())
      );
    } catch (Exception e) {
      throw new RuntimeException("Upload fehlgeschlagen", e);
    }

    PictureItem newItem = new PictureItem();
    newItem.setWidgetItem(widgetItemRepository.findById(widgetId)
                                              .orElseThrow(() -> new RuntimeException("Widget " + widgetId + " nicht gefunden")));
    newItem.setImageUrl(publicUrl + "/" + key);

    PictureItem saved = pictureRepository.save(newItem);
    return new PictureResponse(saved.getId(), widgetId, saved.getImageUrl());
  }

  public void deletePicture(Long widgetId) {
    PictureItem item = pictureRepository.findByWidgetItem_Id(widgetId)
                                        .orElseThrow(() -> new RuntimeException("Kein Bild für Widget " + widgetId));

    s3Client.deleteObject(DeleteObjectRequest.builder()
                                             .bucket(bucket)
                                             .key(extractKey(item.getImageUrl()))
                                             .build());

    pictureRepository.delete(item);
  }

  private String extractKey(String imageUrl) {
    return imageUrl.replace(publicUrl + "/", "");
  }
}