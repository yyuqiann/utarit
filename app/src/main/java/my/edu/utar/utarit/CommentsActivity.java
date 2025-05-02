package my.edu.utar.utarit;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private TextInputEditText commentEditText;
    private DatabaseReference commentsRef;
    private FirebaseUser currentUser;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        postId = getIntent().getStringExtra("postId");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        commentsRef = FirebaseDatabase.getInstance().getReference("comments").child(postId);

        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        commentsRecyclerView.setAdapter(commentAdapter);

        commentEditText = findViewById(R.id.commentEditText);
        findViewById(R.id.postCommentButton).setOnClickListener(v -> postComment());

        loadComments();
    }

    private void loadComments() {
        commentsRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> comments = new ArrayList<>();
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    comments.add(comment);
                }
                commentAdapter.setComments(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommentsActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postComment() {
        String content = commentEditText.getText().toString().trim();
        if (content.isEmpty()) {
            commentEditText.setError("Comment cannot be empty");
            return;
        }

        String commentId = commentsRef.push().getKey();
        Map<String, Object> commentValues = new HashMap<>();
        commentValues.put("author", currentUser.getUid());
        commentValues.put("content", content);
        commentValues.put("timestamp", System.currentTimeMillis());

        commentsRef.child(commentId).setValue(commentValues)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        commentEditText.setText("");
                    } else {
                        Toast.makeText(this, "Failed to post comment", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}