package com.example.davinciconnect.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.List;

public class CorreoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correo);

        RecyclerView rvCorreos = findViewById(R.id.rvCorreos);
        rvCorreos.setLayoutManager(new LinearLayoutManager(this));

        List<Correo> correoList = new ArrayList<>();
        correoList.add(new Correo("Información Importante", "bedelia@example.com"));
        correoList.add(new Correo("Confirmación de Inscripción", "secretaria.alumnos@example.com"));
        correoList.add(new Correo("Horarios de Tutorías", "tutorias@example.com"));
        correoList.add(new Correo("Recordatorio de Pago", "tesoreria@example.com"));

        CorreoAdapter adapter = new CorreoAdapter(correoList);
        rvCorreos.setAdapter(adapter);
    }
}
