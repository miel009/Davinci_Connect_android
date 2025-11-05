package com.example.davinciconnect.storage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        RecyclerView rvFolders = findViewById(R.id.rvFolders);
        rvFolders.setLayoutManager(new GridLayoutManager(this, 3));

        List<Folder> folderList = new ArrayList<>();
        folderList.add(new Folder("General", android.R.drawable.ic_menu_agenda));
        folderList.add(new Folder("Codigo", android.R.drawable.ic_menu_myplaces));
        folderList.add(new Folder("Sincronización", android.R.drawable.ic_menu_share));
        folderList.add(new Folder("Varios", android.R.drawable.ic_menu_gallery));
        folderList.add(new Folder("Carpeta", android.R.drawable.ic_menu_directions));
        folderList.add(new Folder("Estadisticas", android.R.drawable.ic_menu_sort_by_size));
        folderList.add(new Folder("Claves", android.R.drawable.ic_menu_crop));
        folderList.add(new Folder("Usuarios", android.R.drawable.ic_menu_my_calendar));
        folderList.add(new Folder("Backup", android.R.drawable.ic_menu_save));
        folderList.add(new Folder("Archivos", android.R.drawable.ic_menu_save)); 
        folderList.add(new Folder("Correo", android.R.drawable.ic_menu_send));
        folderList.add(new Folder("Video", android.R.drawable.ic_media_play));
        folderList.add(new Folder("Privado", android.R.drawable.ic_lock_lock));

        FolderAdapter adapter = new FolderAdapter(folderList, this);
        rvFolders.setAdapter(adapter);
    }

    @Override
    public void onFolderClick(Folder folder) {
        // Ahora, en lugar de un Toast, abrimos FileListActivity y le pasamos el nombre de la carpeta
        Intent intent = new Intent(this, FileListActivity.class);
        intent.putExtra("FOLDER_NAME", folder.getName());
        startActivity(intent);
    }
}
