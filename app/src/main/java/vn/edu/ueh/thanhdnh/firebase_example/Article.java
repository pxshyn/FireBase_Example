package vn.edu.ueh.thanhdnh.firebase_example;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Article {
  @DocumentId
  private String id;
  private String title;
  private String content;
  private String imageUrl;
  private long views;
  private long timestamp;

  public Article() {
    // Constructor rỗng bắt buộc cho Firebase
  }

  public Article(String id, String title, String content, String imageUrl, long views, long timestamp) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.imageUrl = imageUrl;
    this.views = views;
    this.timestamp = timestamp;
  }

  public Article(String title, String content, String imageUrl, long views, long timestamp) {
    this.title = title;
    this.content = content;
    this.imageUrl = imageUrl;
    this.views = views;
    this.timestamp = timestamp;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public long getViews() {
    return views;
  }

  public void setViews(long views) {
    this.views = views;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Exclude
  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("title", title);
    result.put("content", content);
    result.put("imageUrl", imageUrl);
    result.put("views", views);
    result.put("timestamp", timestamp);
    return result;
  }
}
