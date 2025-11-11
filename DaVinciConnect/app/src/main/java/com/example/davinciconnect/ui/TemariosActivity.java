package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
        rvTemarios.setLayoutManager(new GridLayoutManager(this, 2));

        List<Folder> temariosList = new ArrayList<>();
        temariosList.add(new Folder("Materia 1ABC", R.drawable.materia_1abc));
        temariosList.add(new Folder("Materia 2ABC", R.drawable.materia_2abc));
        temariosList.add(new Folder("Materia 3ABC", R.drawable.materia_3abc));
        temariosList.add(new Folder("Materia 4ABC", R.drawable.materia_4abc));
        temariosList.add(new Folder("Materia 5ABC", R.drawable.materia_5abc));
        temariosList.add(new Folder("Materia 6ABC", R.drawable.materia_6abc));
        temariosList.add(new Folder("Materia 7ABC", R.drawable.materia_7abc));

        FolderAdapter adapter = new FolderAdapter(temariosList, this);
        rvTemarios.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        Intent intent = new Intent(this, FileListActivity.class);
        intent.putExtra("FOLDER_NAME", folder.getName());
        intent.putExtra("IS_SHARED", true);
        startActivity(intent);
    }
}
