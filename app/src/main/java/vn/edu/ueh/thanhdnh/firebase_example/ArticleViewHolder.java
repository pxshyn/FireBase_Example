package vn.edu.ueh.thanhdnh.firebase_example;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
  private ImageView ivArticleImage;
  private TextView tvTitle, tvContent, tvViews;

  public ArticleViewHolder(@NonNull View itemView) {
    super(itemView);
    ivArticleImage = itemView.findViewById(R.id.ivArticleImage);
    tvTitle = itemView.findViewById(R.id.tvTitle);
    tvContent = itemView.findViewById(R.id.tvContent);
    tvViews = itemView.findViewById(R.id.tvViews);
  }

  public ImageView getIvArticleImage() {
    return ivArticleImage;
  }

  public TextView getTvTitle() {
    return tvTitle;
  }

  public TextView getTvContent() {
    return tvContent;
  }

  public TextView getTvViews() {
    return tvViews;
  }
}
