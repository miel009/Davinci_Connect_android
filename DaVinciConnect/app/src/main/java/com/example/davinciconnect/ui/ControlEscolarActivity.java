package com.example.davinciconnect.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.List;

public class ControlEscolarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_escolar);

        RecyclerView rvAsistencias = findViewById(R.id.rvAsistencias);
        rvAsistencias.setLayoutManager(new LinearLayoutManager(this));

        List<Asistencia> asistenciasList = new ArrayList<>();
        asistenciasList.add(new Asistencia("Materia 1ABC", "Presente"));
        asistenciasList.add(new Asistencia("Materia 2ABC", "Ausente"));
        asistenciasList.add(new Asistencia("Materia 3ABC", "Presente"));
        asistenciasList.add(new Asistencia("Materia 4ABC", "Presente"));
        asistenciasList.add(new Asistencia("Materia 5ABC", "Ausente"));
        asistenciasList.add(new Asistencia("Materia 6ABC", "Presente"));
        asistenciasList.add(new Asistencia("Materia 7ABC", "Presente"));

        AsistenciaAdapter adapter = new AsistenciaAdapter(asistenciasList);
        rvAsistencias.setAdapter(adapter);
    }
}
