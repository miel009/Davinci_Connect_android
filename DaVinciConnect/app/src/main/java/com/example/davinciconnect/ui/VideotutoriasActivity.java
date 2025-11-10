package com.example.davinciconnect.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.List;

public class VideotutoriasActivity extends AppCompatActivity implements ClasesAdapter.OnItemClickListener {

    public static final String EXTRA_MATERIA_NAME = "EXTRA_MATERIA_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videotutorias);

        String materiaName = getIntent().getStringExtra(EXTRA_MATERIA_NAME);

        TextView tvMateriaTitle = findViewById(R.id.tvMateriaTitle);
        tvMateriaTitle.setText("Videotutoriales - " + materiaName);

        RecyclerView rvVideotutorias = findViewById(R.id.rvVideotutorias);
        rvVideotutorias.setLayoutManager(new LinearLayoutManager(this));

        List<String> videotutoriasList = new ArrayList<>();
        videotutoriasList.add("Videotutoria 1 (Ejemplo)");
        videotutoriasList.add("Videotutoria 2 (Ejemplo)");

        ClasesAdapter adapter = new ClasesAdapter(videotutoriasList, this);
        rvVideotutorias.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String clase) {
        String videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        intent.setDataAndType(Uri.parse(videoUrl), "video/*");
        startActivity(intent);
    }
}
