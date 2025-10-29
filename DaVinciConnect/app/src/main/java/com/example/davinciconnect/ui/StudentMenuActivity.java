package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davinciconnect.R;
import com.example.davinciconnect.ui.chat.ChatLeoActivity;
import com.example.davinciconnect.ui.menu.MenuAdapter;
import com.example.davinciconnect.ui.menu.MenuItemModel;

import java.util.Arrays;
import java.util.List;

public class StudentMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        RecyclerView rv = findViewById(R.id.rvMenu);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setHasFixedSize(true);

        MenuAdapter adapter = new MenuAdapter((item, pos) -> {
            // Abrir pantallas según item.label (puedes expandir este switch más adelante)
            switch (item.label) {
                case "Chat IA":
                    // Abre el chat con Gemini
                    startActivity(new Intent(this, ChatIntroActivity.class));
                    break;

                case "Documentos":
                case "Campus":
                case "Asignaturas":
                case "Calificaciones":
                case "Calendario":
                case "Clases":
                case "Institucional":
                    Toast.makeText(this, "Alumno → " + item.label, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(this, "Alumno → " + item.label, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        rv.setAdapter(adapter);

        // Íconos del sistema para compilar sin drawables propios (luego reemplaza por los tuyos)
        List<MenuItemModel> items = Arrays.asList(
                new MenuItemModel(android.R.drawable.ic_menu_agenda, "Documentos"),
                new MenuItemModel(android.R.drawable.ic_dialog_map, "Campus"),
                new MenuItemModel(android.R.drawable.ic_menu_info_details, "Asignaturas"),
                new MenuItemModel(android.R.drawable.ic_menu_sort_by_size, "Calificaciones"),
                new MenuItemModel(android.R.drawable.ic_menu_my_calendar, "Calendario"),
                new MenuItemModel(android.R.drawable.ic_menu_week, "Clases"),
                new MenuItemModel(android.R.drawable.ic_btn_speak_now, "Chat IA"),
                new MenuItemModel(android.R.drawable.ic_menu_help, "Institucional")
        );
        adapter.submit(items);
    }
}
