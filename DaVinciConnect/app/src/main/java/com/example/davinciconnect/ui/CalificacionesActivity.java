package com.example.davinciconnect.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalificacionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificaciones);

        RecyclerView rvCalificaciones = findViewById(R.id.rvCalificaciones);
        rvCalificaciones.setLayoutManager(new LinearLayoutManager(this));

        List<Calificacion> calificacionesList = new ArrayList<>();
        calificacionesList.add(new Calificacion("Materia 1ABC", Arrays.asList(100, 80, 90, 100)));
        calificacionesList.add(new Calificacion("Materia 2ABC", Arrays.asList(90, 90, 100, 80)));
        calificacionesList.add(new Calificacion("Materia 3ABC", Arrays.asList(70, 80, 70, 90)));
        calificacionesList.add(new Calificacion("Materia 4ABC", Arrays.asList(100, 100, 100, 100)));
        calificacionesList.add(new Calificacion("Materia 5ABC", Arrays.asList(80, 70, 90, 80)));
        calificacionesList.add(new Calificacion("Materia 6ABC", Arrays.asList(60, 70, 80, 70)));
        calificacionesList.add(new Calificacion("Materia 7ABC", Arrays.asList(90, 100, 90, 100)));

        CalificacionesAdapter adapter = new CalificacionesAdapter(calificacionesList);
        rvCalificaciones.setAdapter(adapter);
    }
}
