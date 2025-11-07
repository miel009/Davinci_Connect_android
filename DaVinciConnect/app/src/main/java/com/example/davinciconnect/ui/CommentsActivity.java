package com.example.davinciconnect.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private static final String TAG = "CommentsActivity";

    private RecyclerView rvComments;
    private EditText etNewComment;
    private Button btnSendComment;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private CommentAdapter adapter;
    private List<Comment> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);
        setContentView(R.layout.activity_comments);

        try {
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();

            rvComments = findViewById(R.id.rvComments);
            etNewComment = findViewById(R.id.etNewComment);
            btnSendComment = findViewById(R.id.btnSendComment);

            rvComments.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CommentAdapter(commentList);
            rvComments.setAdapter(adapter);

            btnSendComment.setOnClickListener(v -> sendComment());

            loadComments();
        } catch (Throwable t) {
            Log.e(TAG, "Error en onCreate", t);
            Toast.makeText(this, "Error fatal: " + t.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void sendComment() {
        String commentText = etNewComment.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Debes iniciar sesión para comentar", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String userName = (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) ? currentUser.getDisplayName() : currentUser.getEmail();

        Comment comment = new Comment(commentText, userId, userName);

        db.collection("comentarios").add(comment)
                .addOnSuccessListener(documentReference -> {
                    etNewComment.setText("");
                    Toast.makeText(this, "Comentario enviado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al enviar el comentario", Toast.LENGTH_SHORT).show());
    }

    private void loadComments() {
        db.collection("comentarios").orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error al cargar comentarios", error);
                        Toast.makeText(this, "Error al cargar los comentarios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        commentList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            try {
                                Comment comment = doc.toObject(Comment.class);
                                commentList.add(comment);
                            } catch (Exception e) {
                                Log.e(TAG, "Error al parsear el comentario: " + doc.getId(), e);
                                // Se omite el comentario con formato incorrecto
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
