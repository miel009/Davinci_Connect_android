package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import com.example.davinciconnect.storage.Folder;
import com.example.davinciconnect.storage.FolderAdapter;
import com.example.davinciconnect.storage.FileListActivity;
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
        asignaturasList.add(new Folder("Materia 1ABC", R.drawable.materia_1abc));
        asignaturasList.add(new Folder("Materia 2ABC", R.drawable.materia_2abc));
        asignaturasList.add(new Folder("Materia 3ABC", R.drawable.materia_3abc));
        asignaturasList.add(new Folder("Materia 4ABC", R.drawable.materia_4abc));
        asignaturasList.add(new Folder("Materia 5ABC", R.drawable.materia_5abc));
        asignaturasList.add(new Folder("Materia 6ABC", R.drawable.materia_6abc));
        asignaturasList.add(new Folder("Materia 7ABC", R.drawable.materia_7abc));

        FolderAdapter adapter = new FolderAdapter(asignaturasList, this);
        rvAsignaturas.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        Intent intent = new Intent(this, FileListActivity.class);
        intent.putExtra("FOLDER_NAME", folder.getName());
        intent.putExtra("IS_STUDENT", true);
        intent.putExtra("IS_SHARED", true);
        startActivity(intent);
    }
}
