package com.example.davinciconnect.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davinciconnect.R;
import com.example.davinciconnect.adapter.MenuAdapter;
import com.example.davinciconnect.model.MenuItem;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerMenu = findViewById(R.id.recyclerMenu);

        List<MenuItem> menuAlumno = Arrays.asList(
                new MenuItem("Documentos", R.drawable.ic_launcher_foreground),
                new MenuItem("Calendario", R.drawable.ic_launcher_foreground),
                new MenuItem("Clases", R.drawable.ic_launcher_foreground),
                new MenuItem("Chat IA", R.drawable.ic_launcher_foreground)
        );

        MenuAdapter adapter = new MenuAdapter(menuAlumno, new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                Toast.makeText(HomeActivity.this, "Clic en: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerMenu.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerMenu.setAdapter(adapter);
    }
}
