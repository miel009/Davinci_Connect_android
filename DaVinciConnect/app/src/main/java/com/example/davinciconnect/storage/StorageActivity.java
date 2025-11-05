package com.example.davinciconnect.storage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    private static final String PREFS_NAME = "PinPrefs";
    private static final String PIN_KEY = "user_pin";

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
        if ("Privado".equals(folder.getName())) {
            handlePrivateFolderClick();
        } else {
            Intent intent = new Intent(this, FileListActivity.class);
            intent.putExtra("FOLDER_NAME", folder.getName());
            startActivity(intent);
        }
    }

    private void handlePrivateFolderClick() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedPin = prefs.getString(PIN_KEY, null);

        if (savedPin == null) {
            showCreatePinDialog();
        } else {
            showEnterPinDialog(savedPin);
        }
    }

    private void showCreatePinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crear PIN para Carpeta Privada");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setHint("Crea un PIN de 4 dígitos");
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String pin = input.getText().toString();
            if (pin.length() == 4) {
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString(PIN_KEY, pin);
                editor.apply();
                Toast.makeText(this, "PIN guardado con éxito", Toast.LENGTH_SHORT).show();
                openPrivateFolder();
            } else {
                Toast.makeText(this, "El PIN debe tener 4 dígitos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void showEnterPinDialog(String correctPin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduce tu PIN");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setHint("PIN de 4 dígitos");
        builder.setView(input);

        builder.setPositiveButton("Entrar", (dialog, which) -> {
            String pin = input.getText().toString();
            if (pin.equals(correctPin)) {
                openPrivateFolder();
            } else {
                Toast.makeText(this, "PIN incorrecto", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void openPrivateFolder() {
        Intent intent = new Intent(this, FileListActivity.class);
        intent.putExtra("FOLDER_NAME", "Privado");
        startActivity(intent);
    }
}
