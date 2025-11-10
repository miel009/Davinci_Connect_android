package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.List;

public class CapacitacionListActivity extends AppCompatActivity implements CapacitacionAdapter.OnItemClickListener {

    public static final String EXTRA_MATERIA_NAME = "EXTRA_MATERIA_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacitacion_list);

        String materiaName = getIntent().getStringExtra(EXTRA_MATERIA_NAME);

        TextView tvMateriaTitle = findViewById(R.id.tvMateriaTitle);
        tvMateriaTitle.setText("Capacitaciones - " + materiaName);

        RecyclerView rvCapacitacionList = findViewById(R.id.rvCapacitacionList);
        rvCapacitacionList.setLayoutManager(new LinearLayoutManager(this));

        List<Capacitacion> capacitacionList = new ArrayList<>();
        capacitacionList.add(new Capacitacion("Capacitación 1", "20/10/2024", "10:00"));
        capacitacionList.add(new Capacitacion("Capacitación 2", "22/10/2024", "15:30"));

        CapacitacionAdapter adapter = new CapacitacionAdapter(capacitacionList, this);
        rvCapacitacionList.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Capacitacion capacitacion) {
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("EVENT_DESCRIPTION", capacitacion.getNombre());
        intent.putExtra("EVENT_DATE", capacitacion.getFecha());
        intent.putExtra("EVENT_TIME", capacitacion.getHora());
        startActivity(intent);
    }
}
