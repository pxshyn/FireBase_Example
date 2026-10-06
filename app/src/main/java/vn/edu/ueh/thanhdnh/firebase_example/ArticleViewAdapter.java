package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleViewAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
  private static final String TAG = "ArticleViewAdapter";
  private final Context context;
  private final LayoutInflater mInflater;
  private List<Article> articles;

  public ArticleViewAdapter(Context context, List<Article> articles) {
    this.context = context;
    this.mInflater = LayoutInflater.from(context);
    this.articles = articles;
  }

  public void update(List<Article> articles) {
    this.articles = articles;
  }

  @NonNull
  @Override
  public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View customView = mInflater.inflate(R.layout.article_item, parent, false);
    return new ArticleViewHolder(customView);
  }

  @Override
  public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
    Article currentArticle = articles.get(position);

    holder.getTvTitle().setText(currentArticle.getTitle());
    holder.getTvContent().setText(currentArticle.getContent());
    holder.getTvViews().setText(currentArticle.getViews() + " lượt xem");

    String imageUrl = currentArticle.getImageUrl();
    if (imageUrl != null && !imageUrl.trim().isEmpty()) {
      Picasso.get()
          .load(imageUrl)
          .placeholder(R.mipmap.ic_launcher)
          .error(R.mipmap.ic_launcher)
          .transform(new CircleTransform())
          .into(holder.getIvArticleImage());
    } else {
      holder.getIvArticleImage().setImageResource(R.mipmap.ic_launcher);
    }

    holder.itemView.setOnClickListener(v -> {
      String docId = currentArticle.getId();
      long currentViews = currentArticle.getViews();
      long newViews = currentViews + 1;

      // Tăng số lượng view trên Firestore
      if (docId != null && !docId.isEmpty()) {
        FirebaseFirestore.getInstance()
            .collection("articles")
            .document(docId)
            .update("views", FieldValue.increment(1))
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Cập nhật view thành công cho doc: " + docId))
            .addOnFailureListener(e -> Log.e(TAG, "Lỗi cập nhật view cho doc: " + docId, e));
      }

      // Mở màn hình chi tiết bài viết
      Intent intent = new Intent(context, ArticleDetailActivity.class);
      intent.putExtra("id", docId);
      intent.putExtra("title", currentArticle.getTitle());
      intent.putExtra("content", currentArticle.getContent());
      intent.putExtra("imageUrl", currentArticle.getImageUrl());
      intent.putExtra("views", newViews);
      context.startActivity(intent);
    });
  }

  @Override
  public int getItemCount() {
    return articles.size();
  }
}
