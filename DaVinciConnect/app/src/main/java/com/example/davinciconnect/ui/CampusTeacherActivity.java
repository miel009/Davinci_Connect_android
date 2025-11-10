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

public class CampusTeacherActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_teacher);

        RecyclerView rvCampusTeacher = findViewById(R.id.rvCampusTeacher);
        rvCampusTeacher.setLayoutManager(new LinearLayoutManager(this));

        List<Folder> campusList = new ArrayList<>();
        campusList.add(new Folder("Campus Virtual", R.drawable.campus_virtual));
        campusList.add(new Folder("Control Escolar", R.drawable.control_escolar));
        campusList.add(new Folder("Credenciales", R.drawable.credenciales));
        campusList.add(new Folder("Correo Institucional", R.drawable.correo_institucional));
        campusList.add(new Folder("Tutorias", R.drawable.tutorias));
        campusList.add(new Folder("Capacitaciones", R.drawable.capacitaciones));

        FolderAdapter adapter = new FolderAdapter(campusList, this);
        rvCampusTeacher.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        Intent intent = null;
        switch (folder.getName()) {
            case "Campus Virtual":
                intent = new Intent(this, TemariosActivity.class);
                break;
            case "Control Escolar":
                intent = new Intent(this, ControlEscolarMateriasActivity.class);
                break;
            case "Credenciales":
                intent = new Intent(this, StaticPageActivity.class);
                intent.putExtra(StaticPageActivity.EXTRA_TITLE, "Credenciales");
                intent.putExtra(StaticPageActivity.EXTRA_CONTENT, "Rol: Profesor");
                break;
            case "Correo Institucional":
                intent = new Intent(this, CorreoActivity.class);
                break;
            case "Tutorias":
                intent = new Intent(this, TutoriasActivity.class);
                break;
            case "Capacitaciones":
                intent = new Intent(this, CapacitacionesActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
