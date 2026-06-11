package com.starla.mathletics;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.User;
import com.starla.mathletics.utils.SessionManager;

public class AdminProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SessionManager session = new SessionManager(this);
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        User admin = db.getUserById(session.getUserId());

        TextView tvName = findViewById(R.id.tvAdminName);
        TextView tvOrg = findViewById(R.id.tvAdminOrg);
        TextView tvPosition = findViewById(R.id.tvAdminPosition);
        ProgressBar pbJourney = findViewById(R.id.pbCreatorJourney);
        TextView tvStats = findViewById(R.id.tvCreatorStats);
        TextView tvAchievements = findViewById(R.id.tvAchievements);

        int quizCount = db.getQuizCountByAdmin(admin.getId());
        int studentCount = db.getStudentCount();

        tvName.setText(admin.getUsername());
        tvOrg.setText(admin.getOrganizationName());
        tvPosition.setText(admin.getPosition());

        int journeyProgress = Math.min(100, quizCount * 20);
        pbJourney.setProgress(journeyProgress);

        tvStats.setText("Quizzes Created: " + quizCount +
                "\nStudents Managed: " + studentCount +
                "\nEmail: " + admin.getEmail());

        StringBuilder achievements = new StringBuilder();
        if (quizCount >= 1) achievements.append("📝 First Quiz Creator\n");
        if (quizCount >= 3) achievements.append("📚 Quiz Master\n");
        if (quizCount >= 5) achievements.append("🏆 Curriculum Champion\n");
        if (studentCount >= 5) achievements.append("👥 Student Mentor\n");
        if (achievements.length() == 0) {
            achievements.append("Create your first quiz to earn achievements!");
        }
        tvAchievements.setText(achievements.toString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
