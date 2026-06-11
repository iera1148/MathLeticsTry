package com.starla.mathletics;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starla.mathletics.adapters.QuizAdminAdapter;
import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.Quiz;
import com.starla.mathletics.models.User;
import com.starla.mathletics.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity implements QuizAdminAdapter.QuizActionListener {

    private SessionManager session;
    private DatabaseHelper db;
    private QuizAdminAdapter adapter;
    private RecyclerView rvQuizzes;
    private TextView tvTotalQuizzes, tvTotalStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        session = new SessionManager(this);
        db = DatabaseHelper.getInstance(this);
        User admin = db.getUserById(session.getUserId());

        TextView tvWelcome = findViewById(R.id.tvAdminWelcome);
        tvTotalQuizzes = findViewById(R.id.tvTotalQuizzes);
        tvTotalStudents = findViewById(R.id.tvTotalStudents);
        rvQuizzes = findViewById(R.id.rvQuizzes);
        Button btnCreate = findViewById(R.id.btnCreateQuiz);
        Button btnDashboard = findViewById(R.id.btnDashboard);
        Button btnProfile = findViewById(R.id.btnAdminProfile);

        tvWelcome.setText("Hello, " + admin.getUsername() + " (" + admin.getPosition() + ")");

        rvQuizzes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuizAdminAdapter(this, new ArrayList<>(), this);
        rvQuizzes.setAdapter(adapter);

        btnCreate.setOnClickListener(v -> startActivity(new Intent(this, CreateQuizActivity.class)));
        btnDashboard.setOnClickListener(v -> startActivity(new Intent(this, AdminDashboardActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, AdminProfileActivity.class)));

        refreshStats();
    }

    private void refreshStats() {
        tvTotalQuizzes.setText(getString(R.string.total_quizzes, db.getQuizCountByAdmin(session.getUserId())));
        tvTotalStudents.setText(getString(R.string.total_students, db.getStudentCount()));
        List<Quiz> quizzes = db.getQuizzesByAdmin(session.getUserId());
        adapter.updateList(quizzes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add_quiz) {
            startActivity(new Intent(this, CreateQuizActivity.class));
            return true;
        } else if (id == R.id.menu_report_bugs) {
            new AlertDialog.Builder(this)
                    .setTitle("Report Bugs?")
                    .setMessage("Report bugs to developer?")
                    .setPositiveButton("Yes", (d, w) -> Toast.makeText(this, "Thank you!", Toast.LENGTH_SHORT).show())
                    .setNegativeButton("No", null)
                    .show();
            return true;
        } else if (id == R.id.menu_about) {
            new AlertDialog.Builder(this)
                    .setTitle("About MathLetics")
                    .setMessage("Admin Module - STARLA.2.0\nCSC557 Mobile Programming")
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        } else if (id == R.id.menu_logout) {
            session.clearSession();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQuizDeleted() {
        refreshStats();
    }

    @Override
    public void onQuizUpdated(Quiz quiz) {
        Toast.makeText(this, "Edit quiz: " + quiz.getTitle() + " (use Create Quiz to add new)", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStats();
    }
}
