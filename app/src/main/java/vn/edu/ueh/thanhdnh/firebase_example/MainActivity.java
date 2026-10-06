package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String TAG = "MainActivity";
  private FirebaseFirestore db;
  private Button btAdd, btShow;
  private EditText etTitle, etContent, etImageUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    FirebaseApp.initializeApp(this);
    db = FirebaseFirestore.getInstance();

    btAdd = findViewById(R.id.btAdd);
    btShow = findViewById(R.id.btShow);
    etTitle = findViewById(R.id.etTitle);
    etContent = findViewById(R.id.etContent);
    etImageUrl = findViewById(R.id.etImageUrl);

    btAdd.setOnClickListener(this);
    btShow.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.btAdd) {
      String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
      String content = etContent.getText() != null ? etContent.getText().toString().trim() : "";
      String imageUrl = etImageUrl.getText() != null ? etImageUrl.getText().toString().trim() : "";

      if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
        Toast.makeText(this, "Vui lòng nhập đầy đủ Tiêu đề và Nội dung", Toast.LENGTH_SHORT).show();
        return;
      }

      Map<String, Object> articleMap = new HashMap<>();
      articleMap.put("title", title);
      articleMap.put("content", content);
      articleMap.put("imageUrl", imageUrl);
      articleMap.put("views", 0);
      articleMap.put("timestamp", System.currentTimeMillis()); // Lưu thời điểm tạo bài viết

      db.collection("articles").add(articleMap)
          .addOnSuccessListener(documentReference -> {
            Toast.makeText(MainActivity.this, "Thêm bài viết thành công!", Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            etContent.setText("");
            etImageUrl.setText("");
          })
          .addOnFailureListener(e -> {
            Log.e(TAG, "Lỗi thêm bài viết: ", e);
            Toast.makeText(MainActivity.this, "Lỗi thêm bài viết: " + e.getMessage(), Toast.LENGTH_LONG).show();
          });

    } else if (view.getId() == R.id.btShow) {
      Intent intent = new Intent(MainActivity.this, ShowDataActivity.class);
      startActivity(intent);
    }
  }
}
