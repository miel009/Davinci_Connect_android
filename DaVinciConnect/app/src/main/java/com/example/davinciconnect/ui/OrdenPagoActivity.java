package com.example.davinciconnect.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.List;

public class OrdenPagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden_pago);

        RecyclerView rvOrdenesPago = findViewById(R.id.rvOrdenesPago);
        rvOrdenesPago.setLayoutManager(new LinearLayoutManager(this));

        List<OrdenPago> ordenPagoList = new ArrayList<>();
        ordenPagoList.add(new OrdenPago("Marzo", "Pagado"));
        ordenPagoList.add(new OrdenPago("Abril", "Pagado"));
        ordenPagoList.add(new OrdenPago("Mayo", "Pendiente"));

        OrdenPagoAdapter adapter = new OrdenPagoAdapter(ordenPagoList);
        rvOrdenesPago.setAdapter(adapter);
    }
}
