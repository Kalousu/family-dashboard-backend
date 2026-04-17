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
    RedditImageSource source = post.preview().images().get(0).source();

    return new MemeResponse(
        post.title(),
        post.selftext(),
        post.url(),
        "https://reddit.com" + post.permalink(),
        post.author(),
        source.width(),
        source.height()
    );
  }

  record RedditResponse(RedditData data) {}
  record RedditData(List<RedditChild> children) {}
  record RedditChild(RedditPost data) {}
  record RedditPost(String title, String selftext, String url, String permalink, String author, RedditPreview preview) {}
  record RedditPreview(List<RedditImage> images) {}
  record RedditImage(RedditImageSource source) {}
  record RedditImageSource(String url, int width, int height) {}
}