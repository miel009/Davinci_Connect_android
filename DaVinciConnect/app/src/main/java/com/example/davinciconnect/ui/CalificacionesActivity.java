package com.example.davinciconnect.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.List;

public class CalificacionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificaciones);

        RecyclerView rvCalificaciones = findViewById(R.id.rvCalificaciones);
        rvCalificaciones.setLayoutManager(new LinearLayoutManager(this));

        List<Calificacion> calificacionesList = new ArrayList<>();
        calificacionesList.add(new Calificacion("Materia 1ABC (1)", "100"));
        calificacionesList.add(new Calificacion("Materia 2ABC (1)", "95"));
        calificacionesList.add(new Calificacion("Materia 3ABC (1)", "-"));
        calificacionesList.add(new Calificacion("Materia 4ABC (1)", "100"));
        calificacionesList.add(new Calificacion("Materia 5ABC (1)", "-"));
        calificacionesList.add(new Calificacion("Materia 6ABC (2)", "-"));
        calificacionesList.add(new Calificacion("Materia 7ABC (1)", "95"));

        CalificacionesAdapter adapter = new CalificacionesAdapter(calificacionesList);
        rvCalificaciones.setAdapter(adapter);
    }
}
