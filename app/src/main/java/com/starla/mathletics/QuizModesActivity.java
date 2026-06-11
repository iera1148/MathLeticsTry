package com.starla.mathletics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.Quiz;
import com.starla.mathletics.models.User;
import com.starla.mathletics.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class QuizModesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private List<Quiz> quizzes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_modes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);
        User user = db.getUserById(session.getUserId());

        CheckBox cbFlashcard = findViewById(R.id.cbFlashcard);
        CheckBox cbTimer = findViewById(R.id.cbTimer);
        Spinner spDuration = findViewById(R.id.spTimerDuration);
        Spinner spQuiz = findViewById(R.id.spQuiz);
        Button btnStart = findViewById(R.id.btnStartQuiz);

        String tier = user.isLowerPrimary() ? "Lower Primary" : "Upper Primary";
        quizzes = db.getQuizzesForStudent(tier, user.getGradeLevel());

        List<String> quizNames = new ArrayList<>();
        for (Quiz q : quizzes) {
            quizNames.add(q.getTitle());
        }
        if (quizNames.isEmpty()) {
            quizNames.add("No quizzes available");
        }

        spQuiz.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, quizNames));

        btnStart.setOnClickListener(v -> {
            if (quizzes.isEmpty()) {
                Toast.makeText(this, "No quizzes available for your grade", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean timerMode = cbTimer.isChecked();
            boolean flashcardMode = cbFlashcard.isChecked();
            int duration = Integer.parseInt(spDuration.getSelectedItem().toString());
            session.setQuizPreferences(timerMode, flashcardMode, duration);

            int selectedIndex = spQuiz.getSelectedItemPosition();
            Quiz quiz = quizzes.get(selectedIndex);

            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("quiz_id", quiz.getId());
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
