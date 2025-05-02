package my.edu.utar.utarit;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private TextInputEditText titleEditText, contentEditText;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.postButton).setOnClickListener(v -> postContent());
    }

    private void postContent() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty()) {
            titleEditText.setError("Title cannot be empty");
            return;
        }

        if (content.isEmpty()) {
            contentEditText.setError("Content cannot be empty");
            return;
        }

        if (currentUser != null) {
            databaseReference.child("users").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String username = dataSnapshot.child("username").getValue(String.class);
                            createPost(title, content, username);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(AddPostActivity.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void createPost(String title, String content, String username) {
        String postId = databaseReference.child("posts").push().getKey();
        Map<String, Object> postValues = new HashMap<>();
        postValues.put("title", title);
        postValues.put("content", content);
        postValues.put("author", currentUser.getUid());
        postValues.put("username", username);
        postValues.put("timestamp", System.currentTimeMillis());
        postValues.put("likeCount", 0);

        databaseReference.child("posts").child(postId).setValue(postValues)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddPostActivity.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddPostActivity.this, "Failed to create post", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}