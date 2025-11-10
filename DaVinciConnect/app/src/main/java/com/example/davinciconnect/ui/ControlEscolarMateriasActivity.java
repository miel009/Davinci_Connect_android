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

public class ControlEscolarMateriasActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_escolar_materias);

        RecyclerView rvMaterias = findViewById(R.id.rvMaterias);
        rvMaterias.setLayoutManager(new LinearLayoutManager(this));

        List<Folder> materiasList = new ArrayList<>();
        materiasList.add(new Folder("Materia 1ABC", R.drawable.ic_launcher_foreground));
        materiasList.add(new Folder("Materia 2ABC", R.drawable.ic_launcher_foreground));
        materiasList.add(new Folder("Materia 3ABC", R.drawable.ic_launcher_foreground));
        materiasList.add(new Folder("Materia 4ABC", R.drawable.ic_launcher_foreground));
        materiasList.add(new Folder("Materia 5ABC", R.drawable.ic_launcher_foreground));
        materiasList.add(new Folder("Materia 6ABC", R.drawable.ic_launcher_foreground));
        materiasList.add(new Folder("Materia 7ABC", R.drawable.ic_launcher_foreground));

        FolderAdapter adapter = new FolderAdapter(materiasList, this);
        rvMaterias.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        Intent intent = new Intent(this, StudentAttendanceActivity.class);
        intent.putExtra(StudentAttendanceActivity.EXTRA_MATERIA_NAME, folder.getName());
        startActivity(intent);
    }
}
