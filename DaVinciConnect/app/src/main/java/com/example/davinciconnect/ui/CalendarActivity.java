package com.example.davinciconnect.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity implements EventAdapter.OnEventListener {

    private static final int NOTIFICATION_PERMISSION_CODE = 101;
    private String selectedDate;
    private DatabaseReference userEventsReference; // <-- Referencia específica del usuario
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);


        setContentView(R.layout.activity_calendar);

        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                // Si no hay usuario, no se puede continuar. Redirigir al login.
                Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            String userId = currentUser.getUid();
            userEventsReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("calendar_events");

            requestNotificationPermission();

            RecyclerView rvEvents = findViewById(R.id.rvEvents);
            rvEvents.setLayoutManager(new LinearLayoutManager(this));
            eventAdapter = new EventAdapter(eventList, this);
            rvEvents.setAdapter(eventAdapter);

            CalendarView calendarView = findViewById(R.id.calendarView);
            calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                loadEventsForDate(selectedDate);
            });

            FloatingActionButton fab = findViewById(R.id.fab_add_event);
            fab.setOnClickListener(view -> {
                if (selectedDate == null) {
                    Toast.makeText(CalendarActivity.this, "Por favor, selecciona una fecha primero", Toast.LENGTH_SHORT).show();
                    return;
                }
                showAddEventDialog();
            });

            initializeWithToday();

        } catch (Throwable t) {
            Log.e("CalendarActivity", "Error FATAL en onCreate", t);
            Toast.makeText(this, "Error FATAL: " + t.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    // ... (el resto de los métodos permanecen casi iguales, pero usarán userEventsReference)

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    private void initializeWithToday() {
        Calendar cal = Calendar.getInstance();
        selectedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
        loadEventsForDate(selectedDate);
    }

    private void showAddEventDialog() {
        selectedTime = "";
        showEditEventDialog(null);
    }

    private void showEditEventDialog(final Event eventToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(eventToEdit == null ? "Añadir evento" : "Editar evento");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_event, null);
        final EditText descriptionInput = view.findViewById(R.id.etEventDescription);
        final TextView timeTextView = view.findViewById(R.id.tvSelectedTime);
        final Button selectTimeButton = view.findViewById(R.id.btnSelectTime);
        builder.setView(view);

        if (eventToEdit != null) {
            descriptionInput.setText(eventToEdit.getDescription());
            timeTextView.setText(eventToEdit.getTime());
            selectedTime = eventToEdit.getTime();
        }

        selectTimeButton.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, h, m) -> {
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                timeTextView.setText(selectedTime);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String description = descriptionInput.getText().toString();
            if (!description.isEmpty() && !selectedTime.isEmpty()) {
                if (eventToEdit == null) {
                    DatabaseReference newEventRef = userEventsReference.child(selectedDate).push();
                    String eventId = newEventRef.getKey();
                    Event newEvent = new Event(eventId, description, selectedTime);
                    saveEventToFirebase(newEventRef, newEvent, true);
                } else {
                    cancelNotification(eventToEdit);
                    eventToEdit.setDescription(description);
                    eventToEdit.setTime(selectedTime);
                    DatabaseReference eventRef = userEventsReference.child(selectedDate).child(eventToEdit.getId());
                    saveEventToFirebase(eventRef, eventToEdit, true);
                }
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void saveEventToFirebase(DatabaseReference eventRef, Event event, boolean schedule) {
        eventRef.setValue(event).addOnSuccessListener(aVoid -> {
            Toast.makeText(CalendarActivity.this, "Evento guardado", Toast.LENGTH_SHORT).show();
            if (schedule) {
                scheduleNotification(event);
            }
            loadEventsForDate(selectedDate);
        }).addOnFailureListener(e -> Toast.makeText(CalendarActivity.this, "Error al guardar el evento", Toast.LENGTH_SHORT).show());
    }

    private void deleteEvent(Event event) {
        new AlertDialog.Builder(this).setTitle("Confirmar eliminación").setMessage("¿Estás seguro de que quieres eliminar este evento?").setPositiveButton("Eliminar", (dialog, which) -> {
            cancelNotification(event);
            DatabaseReference eventRef = userEventsReference.child(selectedDate).child(event.getId());
            eventRef.removeValue().addOnSuccessListener(aVoid -> {
                Toast.makeText(CalendarActivity.this, "Evento eliminado", Toast.LENGTH_SHORT).show();
                loadEventsForDate(selectedDate);
            }).addOnFailureListener(e -> Toast.makeText(CalendarActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show());
        }).setNegativeButton("Cancelar", null).show();
    }

    private void scheduleNotification(Event event) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
             new AlertDialog.Builder(this).setTitle("Permiso Requerido").setMessage("Para asegurar que las notificaciones lleguen a tiempo, por favor concede el permiso para programar alarmas exactas.").setPositiveButton("Ir a Ajustes", (d, w) -> {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }).setNegativeButton("Cancelar", null).show();
            return;
        }
        try {
            Intent intent = new Intent(this, NotificationReceiver.class);
            intent.putExtra("event_description", event.getDescription());
            int pendingIntentId = event.getId().hashCode();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pendingIntentId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d HH:mm", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(selectedDate + " " + event.getTime()));
            if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (ParseException e) {
            Log.e("CalendarActivity", "Error parsing date/time for notification", e);
        }
    }

    private void cancelNotification(Event event) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        int pendingIntentId = event.getId().hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pendingIntentId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void loadEventsForDate(String date) {
        if (userEventsReference == null) return;
        userEventsReference.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        if (event != null && event.getId() == null) {
                            event.setId(eventSnapshot.getKey());
                        }
                        if (event != null) {
                            eventList.add(event);
                        }
                    }
                }
                eventAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CalendarActivity.this, "Error al cargar eventos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(Event event) {
        showEditEventDialog(event);
    }

    @Override
    public void onDeleteClick(Event event) {
        deleteEvent(event);
    }
}
