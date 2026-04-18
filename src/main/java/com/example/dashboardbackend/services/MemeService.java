package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.meme.MemeResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class MemeService {

  private final RestTemplate restTemplate = new RestTemplate();

  public MemeResponse getMemeOfTheDay() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "DashboardBot/1.0");

    ResponseEntity<RedditResponse> response = restTemplate.exchange(
        "https://www.reddit.com/r/memes/top.json?t=day&limit=1",
        HttpMethod.GET,
        new HttpEntity<>(headers),
        RedditResponse.class
    );

    RedditPost post = response.getBody().data().children().get(0).data();
    if (post.is_video()) {
      RedditVideo video = post.media().reddit_video();
      return new MemeResponse(
          post.title(), post.selftext(), video.fallback_url(),
          "https://reddit.com" + post.permalink(),
          post.author(), video.width(), video.height(), true
      );
    } else {
      RedditImageSource source = post.preview().images().get(0).source();
      return new MemeResponse(
          post.title(), post.selftext(), post.url(),
          "https://reddit.com" + post.permalink(),
          post.author(), source.width(), source.height(), false
      );
    }
  }

  record RedditResponse(RedditData data) {}
  record RedditData(List<RedditChild> children) {}
  record RedditChild(RedditPost data) {}
  record RedditPost(String title, String selftext, String url, String permalink, String author, RedditPreview preview,RedditMedia media, boolean is_video) {}
  record RedditPreview(List<RedditImage> images) {}
  record RedditImage(RedditImageSource source) {}
  record RedditImageSource(String url, int width, int height) {}
  record RedditMedia(RedditVideo reddit_video) {}
  record RedditVideo(String fallback_url, int width, int height) {}
}