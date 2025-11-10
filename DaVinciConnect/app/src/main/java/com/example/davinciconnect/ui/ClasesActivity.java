package com.example.davinciconnect.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class ClasesActivity extends AppCompatActivity implements ClasesAdapter.OnItemClickListener {

    private RecyclerView rvClases;
    private ClasesAdapter clasesAdapter;
    private List<String> clasesList;
    private StorageReference clasesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clases);

        rvClases = findViewById(R.id.rvClases);
        rvClases.setLayoutManager(new LinearLayoutManager(this));
        clasesList = new ArrayList<>();
        clasesAdapter = new ClasesAdapter(clasesList, this);
        rvClases.setAdapter(clasesAdapter);

        clasesRef = FirebaseStorage.getInstance().getReference().child("clases");

        loadClases();
    }

    private void loadClases() {
        clasesRef.listAll().addOnSuccessListener(listResult -> {
            if (listResult.getItems().isEmpty()) {
                // Add mock data if no real classes are found
                clasesList.add("Grabacion 001-01-01-25 (Ejemplo)");
                clasesList.add("Grabacion 002-01-01-25 (Ejemplo)");
            } else {
                for (StorageReference item : listResult.getItems()) {
                    clasesList.add(item.getName());
                }
            }
            clasesAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            // In case of failure, also add mock data
            clasesList.add("Grabacion 001-01-01-25 (Ejemplo)");
            clasesList.add("Grabacion 002-01-01-25 (Ejemplo)");
            clasesAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClick(String claseName) {
        if (claseName.contains("(Ejemplo)")) {
            // It's a mock item, play a sample video
            String videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            intent.setDataAndType(Uri.parse(videoUrl), "video/*");
            startActivity(intent);
        } else {
            // It's a real item from Firebase
            clasesRef.child(claseName).getDownloadUrl().addOnSuccessListener(uri -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setDataAndType(uri, "video/*");
                startActivity(intent);
            });
        }
    }
}
