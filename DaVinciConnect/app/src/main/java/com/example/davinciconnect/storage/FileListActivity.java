package com.example.davinciconnect.storage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends AppCompatActivity implements FileAdapter.OnFileOptionClickListener {

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = "FileListActivity";

    private StorageReference rootReference;
    private StorageReference currentPathReference;
    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private List<StorageReference> itemList;
    private String initialFolderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        initialFolderName = getIntent().getStringExtra("FOLDER_NAME");
        boolean isStudent = getIntent().getBooleanExtra("IS_STUDENT", false);
        boolean isShared = getIntent().getBooleanExtra("IS_SHARED", false);

        if (initialFolderName == null) {
            initialFolderName = "Archivos";
        }

        if (isShared) {
            rootReference = FirebaseStorage.getInstance("gs://davinciconnect-4817d.firebasestorage.app").getReference("materias");
        } else {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            String userId = currentUser.getUid();
            rootReference = FirebaseStorage.getInstance("gs://davinciconnect-4817d.firebasestorage.app").getReference("users").child(userId);
        }

        currentPathReference = rootReference.child(initialFolderName);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        fileAdapter = new FileAdapter(itemList, this);
        recyclerView.setAdapter(fileAdapter);

        Button uploadButton = findViewById(R.id.uploadButton);
        if (isStudent) {
            uploadButton.setVisibility(View.GONE);
        }
        uploadButton.setOnClickListener(v -> openFileChooser());

        loadItems();
    }

    private void loadItems() {
        currentPathReference.listAll().addOnSuccessListener(listResult -> {
            itemList.clear();
            itemList.addAll(listResult.getPrefixes());
            itemList.addAll(listResult.getItems());
            fileAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
             itemList.clear();
             fileAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onFolderClick(StorageReference folderRef) {
        currentPathReference = folderRef;
        loadItems();
    }

    @Override
    public void onBackPressed() {
        if (currentPathReference == null || currentPathReference.getPath().equals(rootReference.child(initialFolderName).getPath())) {
            super.onBackPressed();
        } else {
            currentPathReference = currentPathReference.getParent();
            loadItems();
        }
    }

    @Override
    public void onMoveClick(StorageReference itemToMove, boolean isFolder) {
        final String[] folders = {"General", "Codigo", "Sincronización", "Varios", "Carpeta", "Estadisticas", "Claves", "Usuarios", "Backup", "Archivos", "Correo", "Video", "Privado"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("Mover a...");
        builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, folders), (dialog, which) -> {
            String destinationFolderName = folders[which];
            StorageReference destinationRef = rootReference.child(destinationFolderName).child(itemToMove.getName());
            
            itemToMove.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                destinationRef.putBytes(bytes).addOnSuccessListener(taskSnapshot -> {
                    itemToMove.delete().addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Movido a " + destinationFolderName, Toast.LENGTH_SHORT).show();
                        loadItems();
                    });
                });
            });
        });
        builder.show();
    }

    @Override
    public void onRenameClick(StorageReference itemRef, boolean isFolder) {
        if (isFolder) {
            Toast.makeText(this, "Renombrar carpetas no está implementado.", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("Renombrar Archivo");
        final EditText input = new EditText(this);
        input.setText(itemRef.getName());
        builder.setView(input);

        builder.setPositiveButton("Renombrar", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (newName.isEmpty() || newName.equals(itemRef.getName())) return;

            StorageReference newFileRef = itemRef.getParent().child(newName);
            itemRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                newFileRef.putBytes(bytes).addOnSuccessListener(taskSnapshot -> {
                    itemRef.delete().addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Archivo renombrado", Toast.LENGTH_SHORT).show();
                        loadItems();
                    });
                });
            });
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onDeleteClick(StorageReference itemRef, boolean isFolder) {
         new AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar '" + itemRef.getName() + "'?")
            .setPositiveButton("Eliminar", (dialog, which) -> {
                itemRef.delete().addOnSuccessListener(aVoid -> {
                    Toast.makeText(FileListActivity.this, "Elemento eliminado", Toast.LENGTH_SHORT).show();
                    loadItems();
                });
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    @Override
    public void onSendClick(StorageReference fileRef) {
        try {
            final File localFile = new File(getCacheDir(), fileRef.getName());
            fileRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri fileUri = FileProvider.getUriForFile(FileListActivity.this, getApplicationContext().getPackageName() + ".provider", localFile);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("*/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Enviar archivo vía..."));
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating temp file for sharing", e);
        }
    }

    @Override
    public void onFileClick(StorageReference fileRef) {
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
    
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uploadFile(data.getData());
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting file name", e);
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void uploadFile(Uri fileUri) {
        String fileName = getFileName(fileUri);
        if (fileName == null) {
            Toast.makeText(this, "No se pudo obtener el nombre del archivo.", Toast.LENGTH_SHORT).show();
            return;
        }
        StorageReference fileReference = currentPathReference.child(fileName);

        fileReference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(FileListActivity.this, "Archivo subido con éxito", Toast.LENGTH_SHORT).show();
            loadItems();
        });
    }
}
