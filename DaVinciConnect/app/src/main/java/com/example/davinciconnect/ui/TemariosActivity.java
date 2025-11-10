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

public class TemariosActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temarios);

        RecyclerView rvTemarios = findViewById(R.id.rvTemarios);
        rvTemarios.setLayoutManager(new LinearLayoutManager(this));

        List<Folder> temariosList = new ArrayList<>();
        temariosList.add(new Folder("Materia 1ABC", R.drawable.ic_launcher_foreground));
        temariosList.add(new Folder("Materia 2ABC", R.drawable.ic_launcher_foreground));
        temariosList.add(new Folder("Materia 3ABC", R.drawable.ic_launcher_foreground));
        temariosList.add(new Folder("Materia 4ABC", R.drawable.ic_launcher_foreground));
        temariosList.add(new Folder("Materia 5ABC", R.drawable.ic_launcher_foreground));
        temariosList.add(new Folder("Materia 6ABC", R.drawable.ic_launcher_foreground));
        temariosList.add(new Folder("Materia 7ABC", R.drawable.ic_launcher_foreground));

        FolderAdapter adapter = new FolderAdapter(temariosList, this);
        rvTemarios.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        Intent intent = new Intent(this, FileListActivity.class);
        intent.putExtra("FOLDER_NAME", folder.getName());
        startActivity(intent);
    }
}
