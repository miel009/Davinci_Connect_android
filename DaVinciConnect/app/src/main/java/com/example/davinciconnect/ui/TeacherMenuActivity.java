package com.example.davinciconnect.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.davinciconnect.R;
import com.example.davinciconnect.storage.StorageActivity;
import com.example.davinciconnect.ui.menu.MenuAdapter;
import com.example.davinciconnect.ui.menu.MenuItemModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Arrays;
import java.util.List;

public class TeacherMenuActivity extends AppCompatActivity {

    private ShapeableImageView ivAvatar;

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
        setContentView(R.layout.activity_teacher_menu);
        setupMainMenu();

        ImageButton btnSearch = findViewById(R.id.btnSearch);
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnSearch.setOnClickListener(v -> Toast.makeText(this, "Búsqueda no implementada", Toast.LENGTH_SHORT).show());
        btnMenu.setOnClickListener(this::showCustomMenu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void showCustomMenu(View anchor) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu_with_avatar, null);

        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        ivAvatar = popupView.findViewById(R.id.ivAvatar);
        TextView tvUserName = popupView.findViewById(R.id.tvUserName);

        loadAvatarAndName(tvUserName);

        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        popupView.findViewById(R.id.menu_profile).setOnClickListener(v -> {
            showPasswordDialog();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.menu_contact).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/davinciconnect/"));
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

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchor);
    }

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();
                        Glide.with(this).load(downloadUrl).into(ivAvatar);
                    } else {
                        Toast.makeText(this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    private void setupMainMenu() {
        RecyclerView rv = findViewById(R.id.rvMenu);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setHasFixedSize(true);
        MenuAdapter adapter = new MenuAdapter((item, pos) -> {
            switch (item.label) {
                case "Documentos": startActivity(new Intent(this, StorageActivity.class)); break;
                case "Calendario": startActivity(new Intent(this, CalendarActivity.class)); break;
                default: Toast.makeText(this, "Profesor → " + item.label, Toast.LENGTH_SHORT).show(); break;
            }
        });
        rv.setAdapter(adapter);
        List<MenuItemModel> items = Arrays.asList(
                new MenuItemModel(android.R.drawable.ic_menu_agenda, "Documentos"),
                new MenuItemModel(android.R.drawable.ic_dialog_map, "Campus"),
                new MenuItemModel(android.R.drawable.ic_menu_edit, "Temarios"),
                new MenuItemModel(android.R.drawable.ic_menu_help, "Institucional"),
                new MenuItemModel(android.R.drawable.ic_menu_my_calendar, "Calendario"),
                new MenuItemModel(android.R.drawable.ic_menu_week, "Clases")
        );
        adapter.submit(items);
    }
}
