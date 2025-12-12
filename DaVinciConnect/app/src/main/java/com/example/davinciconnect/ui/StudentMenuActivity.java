package com.example.davinciconnect.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.davinciconnect.R;
import com.example.davinciconnect.storage.FileAdapter;
import com.example.davinciconnect.storage.FileListActivity;
import com.example.davinciconnect.storage.StorageActivity;
import com.example.davinciconnect.ui.menu.MenuAdapter;
import com.example.davinciconnect.ui.menu.MenuItemModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentMenuActivity extends AppCompatActivity implements FileAdapter.OnFileOptionClickListener {

    private ShapeableImageView ivAvatar;
    private RecyclerView rvMenu;
    private FileAdapter searchAdapter;
    private MenuAdapter menuAdapter;
    private List<MenuItemModel> mainMenuItems;
    private List<StorageReference> searchResults;
    private StorageReference userRootRef;
    private StorageReference sharedRootRef;
    private EditText etSearch;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    uploadAndSaveProfilePicture(imageUri);
                }
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);
        setContentView(R.layout.activity_student_menu);
        setupMainMenu();

        startService(new Intent(this, AppLifecycleService.class));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userRootRef = FirebaseStorage.getInstance().getReference("users").child(currentUser.getUid());
        }
        sharedRootRef = FirebaseStorage.getInstance("gs://davinciconnect-4817d.firebasestorage.app").getReference("materias");

        etSearch = findViewById(R.id.etSearch);
        setupSearch();

        findViewById(R.id.btnMenu).setOnClickListener(this::showCustomMenu);
    }

    private void setupSearch() {
        searchResults = new ArrayList<>();
        searchAdapter = new FileAdapter(searchResults, this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    restoreMainMenu();
                } else {
                    performSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void restoreMainMenu() {
        rvMenu.setLayoutManager(new GridLayoutManager(this, 2));
        rvMenu.setAdapter(menuAdapter);
    }

    private void performSearch(String query) {
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setAdapter(searchAdapter);
        searchResults.clear();

        if (userRootRef != null) {
            searchInFolder(userRootRef, query);
        }
        
        if (sharedRootRef != null) {
            searchInFolder(sharedRootRef, query);
        }
    }

    private void searchInFolder(StorageReference folderRef, String query) {
        folderRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference itemRef : listResult.getItems()) {
                if (itemRef.getName().toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(itemRef);
                }
            }
            for (StorageReference subFolderRef : listResult.getPrefixes()) {
                if (!subFolderRef.getName().equals("Privado")) {
                    searchInFolder(subFolderRef, query);
                }
            }
            searchAdapter.notifyDataSetChanged();
        });
    }

    private void showCustomMenu(View anchor) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu_with_avatar, null);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, ViewGroup.LayoutParams.MATCH_PARENT, true);

        ivAvatar = popupView.findViewById(R.id.ivAvatar);
        TextView tvUserName = popupView.findViewById(R.id.tvUserName);

        loadAvatarAndName(tvUserName);

        popupView.findViewById(R.id.btnCloseMenu).setOnClickListener(v -> popupWindow.dismiss());

        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        popupView.findViewById(R.id.menu_profile).setOnClickListener(v -> {
            showPasswordDialog();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.menu_contact).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dvconnectapp/"));
            startActivity(intent);
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.menu_comments).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://davinci-connect-web.vercel.app/#contacto"));
            startActivity(intent);
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.menu_dark_mode).setOnClickListener(v -> {
            ThemeManager.toggleTheme(this);
            recreate();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.menu_logout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            popupWindow.dismiss();
        });

        popupWindow.showAtLocation(anchor, Gravity.END, 0, 0);
    }

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_password, null);
        builder.setView(dialogView);

        final EditText etPassword = dialogView.findViewById(R.id.etPasswordDialog);

        builder.setTitle("Verificación de seguridad")
            .setPositiveButton("Aceptar", (dialog, which) -> {
                String password = etPassword.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.getEmail() != null) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(this, ProfileActivity.class));
                            } else {
                                CustomToast.show(this, "Contraseña incorrecta", Toast.LENGTH_SHORT);
                            }
                        });
                }
            })
            .setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.create().show();
    }

    private void loadAvatarAndName(TextView tvUserName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            tvUserName.setText(user.getDisplayName());
        } else {
            tvUserName.setText(user.getEmail());
        }

        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).into(ivAvatar);
        } else {
            String avatarURL = "https://api.dicebear.com/9.x/identicon/svg?seed=" + user.getEmail();
            Glide.with(this).load(avatarURL).into(ivAvatar);
        }
    }

    private void uploadAndSaveProfilePicture(Uri imageUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference avatarRef = storageRef.child("profile_images/" + user.getUid() + "/avatar.jpg");

        avatarRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            avatarRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUrl)
                    .build();

                user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        CustomToast.show(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT);
                        Glide.with(this).load(downloadUrl).into(ivAvatar);
                    } else {
                        CustomToast.show(this, "Error al actualizar el perfil", Toast.LENGTH_SHORT);
                    }
                });
            });
        });
    }

    private void setupMainMenu() {
        rvMenu = findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new GridLayoutManager(this, 2));
        rvMenu.setHasFixedSize(true);
        mainMenuItems = Arrays.asList(
            new MenuItemModel(R.drawable.documentos, "Documentos"),
            new MenuItemModel(R.drawable.campus, "Campus"),
            new MenuItemModel(R.drawable.asignaturas, "Asignaturas"),
            new MenuItemModel(R.drawable.calificaciones, "Calificaciones"),
            new MenuItemModel(R.drawable.calendario, "Calendario"),
            new MenuItemModel(R.drawable.chat, "Chat Leo")
        );

        menuAdapter = new MenuAdapter((item, pos) -> {
            switch (item.label) {
                case "Documentos": startActivity(new Intent(this, StorageActivity.class)); break;
                case "Calendario": startActivity(new Intent(this, CalendarActivity.class)); break;
                case "Chat Leo": startActivity(new Intent(this, ChatIntroActivity.class)); break;
                case "Asignaturas": startActivity(new Intent(this, AsignaturasActivity.class)); break;
                case "Calificaciones": startActivity(new Intent(this, CalificacionesActivity.class)); break;
                case "Campus": startActivity(new Intent(this, CampusActivity.class)); break;
                default: CustomToast.show(this, "Alumno → " + item.label, Toast.LENGTH_SHORT);
            }
        });
        menuAdapter.submit(mainMenuItems);
        rvMenu.setAdapter(menuAdapter);
    }

    @Override
    public void onFileClick(StorageReference fileRef) {
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    @Override
    public void onFolderClick(StorageReference folderRef) {
        Intent intent = new Intent(this, FileListActivity.class);
        intent.putExtra("FOLDER_NAME", folderRef.getName());
        startActivity(intent);
    }

    @Override
    public void onMoveClick(StorageReference itemToMove, boolean isFolder) {}

    @Override
    public void onRenameClick(StorageReference itemRef, boolean isFolder) {}

    @Override
    public void onDeleteClick(StorageReference itemRef, boolean isFolder) {}

    @Override
    public void onSendClick(StorageReference fileRef) {}
}
