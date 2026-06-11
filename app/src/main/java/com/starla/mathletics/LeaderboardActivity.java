package com.starla.mathletics;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starla.mathletics.adapters.LeaderboardAdapter;
import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.LeaderboardEntry;

import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        int quizId = getIntent().getIntExtra("quiz_id", -1);
        String quizTitle = getIntent().getStringExtra("quiz_title");

        List<LeaderboardEntry> entries;
        if (quizId > 0) {
            setTitle("Leaderboard - " + quizTitle);
            entries = db.getLeaderboardByQuiz(quizId);
        } else {
            entries = db.getLeaderboard();
        }

        TextView tvFirst = findViewById(R.id.tvFirst);
        TextView tvSecond = findViewById(R.id.tvSecond);
        TextView tvThird = findViewById(R.id.tvThird);
        ListView lv = findViewById(R.id.lvLeaderboard);

        if (entries.size() > 0) {
            LeaderboardEntry first = entries.get(0);
            tvFirst.setText("#1 " + first.getUsername() + "\n" + first.getTotalPoints() + " pts");
        }
        if (entries.size() > 1) {
            LeaderboardEntry second = entries.get(1);
            tvSecond.setText("#2 " + second.getUsername() + "\n" + second.getTotalPoints() + " pts");
        }
        if (entries.size() > 2) {
            LeaderboardEntry third = entries.get(2);
            tvThird.setText("#3 " + third.getUsername() + "\n" + third.getTotalPoints() + " pts");
        }

        List<LeaderboardEntry> rest = entries.size() > 3 ? entries.subList(3, entries.size()) : entries;
        lv.setAdapter(new LeaderboardAdapter(this, rest));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
