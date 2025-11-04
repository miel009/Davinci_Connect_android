package com.example.davinciconnect.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.davinciconnect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private String selectedDate;
    private DatabaseReference databaseReference;
    private TextView eventsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("calendar_events");
            eventsTextView = findViewById(R.id.eventsTextView);

            CalendarView calendarView = findViewById(R.id.calendarView);
            if (calendarView == null) {
                Toast.makeText(this, "Error: CalendarView no encontrado", Toast.LENGTH_LONG).show();
                return;
            }
            // Listener para cuando el usuario cambia la fecha
            calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                loadEventsForDate(selectedDate);
            });

            FloatingActionButton fab = findViewById(R.id.fab_add_event);
            if (fab == null) {
                Toast.makeText(this, "Error: FAB no encontrado", Toast.LENGTH_LONG).show();
                return;
            }
            fab.setOnClickListener(view -> {
                if (selectedDate == null) {
                    Toast.makeText(CalendarActivity.this, "Por favor, selecciona una fecha primero", Toast.LENGTH_SHORT).show();
                    return;
                }
                showAddEventDialog();
            });

            // Cargar eventos para el día actual al iniciar
            initializeWithToday();

        } catch (Throwable t) {
            Log.e("CalendarActivity", "Error FATAL en onCreate", t);
            Toast.makeText(this, "Error FATAL: " + t.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initializeWithToday() {
        Calendar cal = Calendar.getInstance();
        selectedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
        loadEventsForDate(selectedDate);
    }

    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir evento");

        final EditText input = new EditText(this);
        input.setHint("Descripción del evento");
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String eventDescription = input.getText().toString();
            if (!eventDescription.isEmpty()) {
                saveEventToFirebase(eventDescription);
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void saveEventToFirebase(String eventDescription) {
        if (selectedDate != null) {
            databaseReference.child(selectedDate).push().setValue(eventDescription)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CalendarActivity.this, "Evento guardado", Toast.LENGTH_SHORT).show();
                    loadEventsForDate(selectedDate); // <-- ¡SOLUCIÓN! Refrescar la lista de eventos
                })
                .addOnFailureListener(e -> Toast.makeText(CalendarActivity.this, "Error al guardar el evento", Toast.LENGTH_SHORT).show());
        }
    }

    private void loadEventsForDate(String date) {
        if (eventsTextView == null) return;
        databaseReference.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder events = new StringBuilder();
                if (snapshot.exists()) {
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String event = eventSnapshot.getValue(String.class);
                        events.append("• ").append(event).append("\n\n");
                    }
                } else {
                    events.append("No hay eventos para esta fecha.");
                }
                eventsTextView.setText(events.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CalendarActivity.this, "Error al cargar eventos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
