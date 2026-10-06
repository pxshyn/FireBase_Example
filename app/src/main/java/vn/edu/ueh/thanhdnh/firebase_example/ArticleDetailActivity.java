package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ArticleDetailActivity extends AppCompatActivity {
    private ImageView ivDetailImage;
    private TextView tvDetailTitle, tvDetailViews, tvDetailContent;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailViews = findViewById(R.id.tvDetailViews);
        tvDetailContent = findViewById(R.id.tvDetailContent);
        Button btBack = findViewById(R.id.btBack);

        btBack.setOnClickListener(v -> finish());

        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        long views = getIntent().getLongExtra("views", 0);

        tvDetailTitle.setText(title != null ? title : "");
        tvDetailContent.setText(content != null ? content : "");
        tvDetailViews.setText("Lượt xem: " + views);

        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .transform(new CircleTransform())
                    .into(ivDetailImage);
        } else {
            ivDetailImage.setImageResource(R.mipmap.ic_launcher);
        }

        if (id != null && !id.isEmpty()) {
            db.collection("articles").document(id).addSnapshotListener((snapshot, error) -> {
                if (snapshot != null && snapshot.exists()) {
                    Long updatedViews = snapshot.getLong("views");
                    if (updatedViews != null) {
                        tvDetailViews.setText("Lượt xem: " + updatedViews);
                    }
                }
            });
        }
    }
}
