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
        folderList.add(new Folder("General", R.drawable.general));
        folderList.add(new Folder("Codigo", R.drawable.codigo));
        folderList.add(new Folder("Sincronización", R.drawable.sincronizacion));
        folderList.add(new Folder("Varios", R.drawable.varios));
        folderList.add(new Folder("Carpeta", R.drawable.carpeta));
        folderList.add(new Folder("Estadisticas", R.drawable.estadisticas));
        folderList.add(new Folder("Claves", R.drawable.claves));
        folderList.add(new Folder("Usuarios", R.drawable.usuarios));
        folderList.add(new Folder("Backup", R.drawable.backup));
        folderList.add(new Folder("Archivos", R.drawable.archivos));
        folderList.add(new Folder("Correo", R.drawable.correo));
        folderList.add(new Folder("Video", R.drawable.video));
        folderList.add(new Folder("Privado", R.drawable.privado));

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
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
