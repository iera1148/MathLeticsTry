package com.starla.mathletics;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starla.mathletics.adapters.DashboardAdapter;
import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.QuizResult;
import com.starla.mathletics.utils.SessionManager;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true) ;

        SessionManager session = new SessionManager(this);
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        //miza


        List<QuizResult> results = db.getResultsByAdmin(session.getUserId());
        ListView lv = findViewById(R.id.lvDashboard);

        if (results.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No student quiz results yet. Create quizzes and wait for students to complete them.");
            empty.setPadding(32, 32, 32, 32);
            lv.setEmptyView(empty);
        }

        lv.setAdapter(new DashboardAdapter(this, results));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
