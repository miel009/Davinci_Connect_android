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

public class InstitutionalActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institutional);

        RecyclerView rvFolders = findViewById(R.id.rvInstitutionalFolders);
        rvFolders.setLayoutManager(new GridLayoutManager(this, 2));

        List<Folder> folderList = new ArrayList<>();
        folderList.add(new Folder("Institución", R.drawable.institucion));
        folderList.add(new Folder("Autoridades", R.drawable.autoridades));
        folderList.add(new Folder("Consejo Directivo", R.drawable.consejo_directivo));
        folderList.add(new Folder("Departamentos", R.drawable.departamentos));
        folderList.add(new Folder("Programas", R.drawable.programas));

        FolderAdapter adapter = new FolderAdapter(folderList, this);
        rvFolders.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        Intent intent = new Intent(this, StaticPageActivity.class);
        String title = folder.getName();
        String content = "";

        switch (title) {
            case "Institución":
                content = "Institicion DV N°1\n\nNovedades";
                break;
            case "Autoridades":
                content = "Director\n\nVice - Director\n\nSecretario Academico\n\nCoordinador";
                break;
            case "Consejo Directivo":
            case "Departamentos":
            case "Programas":
                content = "Contenido de " + title;
                break;
        }

        intent.putExtra(StaticPageActivity.EXTRA_TITLE, title);
        intent.putExtra(StaticPageActivity.EXTRA_CONTENT, content);
        startActivity(intent);
    }
}
