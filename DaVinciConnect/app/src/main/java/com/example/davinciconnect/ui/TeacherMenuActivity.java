package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davinciconnect.R;
import com.example.davinciconnect.storage.StorageActivity;
import com.example.davinciconnect.ui.ChatIntroActivity;
import com.example.davinciconnect.ui.menu.MenuAdapter;
import com.example.davinciconnect.ui.menu.MenuItemModel;

import java.util.Arrays;
import java.util.List;

public class TeacherMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu);

        RecyclerView rv = findViewById(R.id.rvMenu);
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        MenuAdapter adapter = new MenuAdapter((item, pos) -> {
            switch (item.label) {
                case "Documentos":
                    startActivity(new Intent(this, StorageActivity.class));
                    break;
                case "Chat IA":
                    startActivity(new Intent(this, ChatIntroActivity.class));
                    break;
                case "Calendario":
                    startActivity(new Intent(this, CalendarActivity.class));
                    break;
                default:
                    Toast.makeText(this, "Profesor → " + item.label, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        rv.setAdapter(adapter);

        List<MenuItemModel> items = Arrays.asList(
                new MenuItemModel(android.R.drawable.ic_menu_agenda, "Documentos"),
                new MenuItemModel(android.R.drawable.ic_dialog_map, "Campus Profesor"),
                new MenuItemModel(android.R.drawable.ic_menu_edit, "Temarios"),
                new MenuItemModel(android.R.drawable.ic_menu_week, "Clases Profesor"),
                new MenuItemModel(android.R.drawable.ic_menu_sort_by_size, "Calificaciones"),
                new MenuItemModel(android.R.drawable.ic_menu_my_calendar, "Calendario"),
                new MenuItemModel(android.R.drawable.ic_btn_speak_now, "Chat IA"),
                new MenuItemModel(android.R.drawable.ic_menu_manage, "Pagos")
        );
        adapter.submit(items);
    }
}
