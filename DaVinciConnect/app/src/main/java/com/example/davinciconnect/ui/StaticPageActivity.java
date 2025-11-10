package com.example.davinciconnect.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.davinciconnect.R;

public class StaticPageActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_CONTENT = "EXTRA_CONTENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_page);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String content = getIntent().getStringExtra(EXTRA_CONTENT);

        TextView tvTitle = findViewById(R.id.tvStaticPageTitle);
        TextView tvContent = findViewById(R.id.tvStaticPageContent);

        tvTitle.setText(title);
        tvContent.setText(content);
    }
}
