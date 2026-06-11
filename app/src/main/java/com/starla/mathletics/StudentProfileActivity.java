package com.starla.mathletics;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.Achievement;
import com.starla.mathletics.models.ChapterProgress;
import com.starla.mathletics.models.User;
import com.starla.mathletics.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SessionManager session = new SessionManager(this);
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        User user = db.getUserById(session.getUserId());

        TextView tvName = findViewById(R.id.tvProfileName);
        TextView tvStats = findViewById(R.id.tvProfileStats);
        ProgressBar pbJourney = findViewById(R.id.pbJourney);
        TextView tvPercent = findViewById(R.id.tvJourneyPercent);
        ListView lvBadges = findViewById(R.id.lvBadges);

        tvName.setText(user.getUsername() + " - Year " + user.getGradeLevel());
        tvStats.setText(getString(R.string.coins_stars, user.getCoins(), user.getStars()) +
                "\n" + getString(R.string.total_points, user.getTotalPoints()));

        List<ChapterProgress> chapters = db.getChapterProgress(user.getId());
        int totalProgress = 0;
        for (ChapterProgress cp : chapters) {
            totalProgress += cp.getProgressPercent();
        }
        int avgProgress = chapters.isEmpty() ? 0 : totalProgress / chapters.size();
        pbJourney.setProgress(avgProgress);
        tvPercent.setText(avgProgress + "% Complete");

        List<Achievement> achievements = db.getAchievementsByStudent(user.getId());
        List<String> badgeNames = new ArrayList<>();
        for (Achievement a : achievements) {
            badgeNames.add("🏅 " + a.getBadgeName());
        }
        if (badgeNames.isEmpty()) {
            badgeNames.add("No badges yet - keep playing!");
        }

        lvBadges.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, badgeNames));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
