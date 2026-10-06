package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowDataActivity extends AppCompatActivity {
    private static final String TAG = "ShowDataActivity";
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private ArticleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);

        recyclerView = findViewById(R.id.reclyclerview);
        adapter = new ArticleViewAdapter(ShowDataActivity.this, articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowDataActivity.this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        db.collection("articles").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "Lỗi lắng nghe dữ liệu từ Firestore", error);
                    Toast.makeText(ShowDataActivity.this, "Lỗi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                if (snapshots != null) {
                    articles.clear();
                    for (QueryDocumentSnapshot q : snapshots) {
                        Map<String, Object> data = q.getData();
                        String id = q.getId();
                        String title = data.get("title") != null ? data.get("title").toString() : "";
                        String content = data.get("content") != null ? data.get("content").toString() : "";
                        String imageUrl = data.get("imageUrl") != null ? data.get("imageUrl").toString() : "";
                        long views = 0;
                        if (data.get("views") instanceof Long) {
                            views = (Long) data.get("views");
                        } else if (data.get("views") instanceof Number) {
                            views = ((Number) data.get("views")).longValue();
                        }

                        Article article = new Article(id, title, content, imageUrl, views);
                        articles.add(article);
                    }
                    adapter.update(articles);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
