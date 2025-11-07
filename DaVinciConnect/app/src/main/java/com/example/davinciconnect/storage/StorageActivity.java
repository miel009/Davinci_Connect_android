package com.example.davinciconnect.storage;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity implements FolderAdapter.OnFolderClickListener {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("pin")) {
                        String savedPin = documentSnapshot.getString("pin");
                        if (savedPin != null && !savedPin.isEmpty()) {
                            showEnterPinDialog(savedPin);
                        } else {
                            showCreatePinDialog();
                        }
                    } else {
                        showCreatePinDialog();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(StorageActivity.this, "Error al verificar el PIN.", Toast.LENGTH_SHORT).show());
    }

    private void showCreatePinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crear PIN para Carpeta Privada");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setHint("Crea un PIN");
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String pin = input.getText().toString();
            if (!pin.isEmpty()) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    db.collection("users").document(currentUser.getUid())
                            .update("pin", pin)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "PIN guardado con éxito", Toast.LENGTH_SHORT).show();
                                openPrivateFolder();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar el PIN", Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(this, "El PIN no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showEnterPinDialog(String correctPin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduce tu PIN");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setHint("PIN");
        builder.setView(input);

        builder.setPositiveButton("Entrar", (dialog, which) -> {
            String pin = input.getText().toString();
            if (pin.equals(correctPin)) {
                openPrivateFolder();
            } else {
                Toast.makeText(this, "PIN incorrecto", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openPrivateFolder() {
        Intent intent = new Intent(this, FileListActivity.class);
        intent.putExtra("FOLDER_NAME", "Privado");
        startActivity(intent);
    }
}
