package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import com.example.davinciconnect.storage.Folder;
import com.example.davinciconnect.storage.FolderAdapter;
import java.util.ArrayList;
import java.util.List;

public class AsignaturasActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaturas);

        RecyclerView rvAsignaturas = findViewById(R.id.rvAsignaturas);
        rvAsignaturas.setLayoutManager(new LinearLayoutManager(this));

        List<Folder> asignaturasList = new ArrayList<>();
        asignaturasList.add(new Folder("Materia 1ABC", R.drawable.ic_launcher_foreground));
        asignaturasList.add(new Folder("Materia 2ABC", R.drawable.ic_launcher_foreground));
        asignaturasList.add(new Folder("Materia 3ABC", R.drawable.ic_launcher_foreground));
        asignaturasList.add(new Folder("Materia 4ABC", R.drawable.ic_launcher_foreground));
        asignaturasList.add(new Folder("Materia 5ABC", R.drawable.ic_launcher_foreground));
        asignaturasList.add(new Folder("Materia 6ABC", R.drawable.ic_launcher_foreground));
        asignaturasList.add(new Folder("Materia 7ABC", R.drawable.ic_launcher_foreground));

        FolderAdapter adapter = new FolderAdapter(asignaturasList, this);
        rvAsignaturas.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        Intent intent = new Intent(this, StaticPageActivity.class);
        String title = folder.getName();
        String content = "Contenido de " + title;
        intent.putExtra(StaticPageActivity.EXTRA_TITLE, title);
        intent.putExtra(StaticPageActivity.EXTRA_CONTENT, content);
        startActivity(intent);
    }
}
