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

public class CapacitacionesActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacitaciones);

        RecyclerView rvCapacitaciones = findViewById(R.id.rvCapacitaciones);
        rvCapacitaciones.setLayoutManager(new GridLayoutManager(this, 2));

        List<Folder> capacitacionesList = new ArrayList<>();
        capacitacionesList.add(new Folder("Materia 1ABC", R.drawable.materia_1abc));
        capacitacionesList.add(new Folder("Materia 2ABC", R.drawable.materia_2abc));
        capacitacionesList.add(new Folder("Materia 3ABC", R.drawable.materia_3abc));
        capacitacionesList.add(new Folder("Materia 4ABC", R.drawable.materia_4abc));
        capacitacionesList.add(new Folder("Materia 5ABC", R.drawable.materia_5abc));
        capacitacionesList.add(new Folder("Materia 6ABC", R.drawable.materia_6abc));
        capacitacionesList.add(new Folder("Materia 7ABC", R.drawable.materia_7abc));

        FolderAdapter adapter = new FolderAdapter(capacitacionesList, this);
        rvCapacitaciones.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        Intent intent = new Intent(this, CapacitacionListActivity.class);
        intent.putExtra(CapacitacionListActivity.EXTRA_MATERIA_NAME, folder.getName());
        startActivity(intent);
    }
}
