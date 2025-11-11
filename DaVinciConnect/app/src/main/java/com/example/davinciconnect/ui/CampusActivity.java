package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import com.example.davinciconnect.storage.Folder;
import com.example.davinciconnect.storage.FolderAdapter;
import java.util.ArrayList;
import java.util.List;

public class CampusActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);

        RecyclerView rvCampus = findViewById(R.id.rvCampus);
        rvCampus.setLayoutManager(new GridLayoutManager(this, 2));

        List<Folder> campusList = new ArrayList<>();
        campusList.add(new Folder("Campus Virtual", R.drawable.campus_virtual));
        campusList.add(new Folder("Control Escolar", R.drawable.control_escolar));
        campusList.add(new Folder("Credenciales", R.drawable.credenciales));
        campusList.add(new Folder("Correo Institucional", R.drawable.correo_institucional));
        campusList.add(new Folder("Orden de Pago", R.drawable.orden_de_pago));
        campusList.add(new Folder("Videotutoriales", R.drawable.videotutoriales));

        FolderAdapter adapter = new FolderAdapter(campusList, this);
        rvCampus.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        Intent intent = null;
        switch (folder.getName()) {
            case "Campus Virtual":
                intent = new Intent(this, AsignaturasActivity.class);
                break;
            case "Control Escolar":
                intent = new Intent(this, ControlEscolarMateriasActivity.class);
                break;
            case "Credenciales":
                intent = new Intent(this, StaticPageActivity.class);
                intent.putExtra(StaticPageActivity.EXTRA_TITLE, "Credenciales");
                intent.putExtra(StaticPageActivity.EXTRA_CONTENT, "Carrera: Desarrollo de Software");
                break;
            case "Correo Institucional":
                intent = new Intent(this, CorreoActivity.class);
                break;
            case "Orden de Pago":
                intent = new Intent(this, OrdenPagoActivity.class);
                break;
            case "Videotutoriales":
                 intent = new Intent(this, ClasesActivity.class); // Re-using ClasesActivity for video tutorials
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
