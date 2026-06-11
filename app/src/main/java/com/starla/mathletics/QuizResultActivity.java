package com.starla.mathletics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        int timeTaken = getIntent().getIntExtra("time_taken", 0);
        String quizTitle = getIntent().getStringExtra("quiz_title");

        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvPoints = findViewById(R.id.tvPointsEarned);
        Button btnBack = findViewById(R.id.btnBackHome);

        tvScore.setText(getString(R.string.your_score, score, total));
        tvPoints.setText("+" + (score * 10) + " points | Time: " + timeTaken + "s | " + quizTitle);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
