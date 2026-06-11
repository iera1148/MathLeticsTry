package com.starla.mathletics;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.starla.mathletics.adapters.ChapterAdapter;
import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.ChapterProgress;
import com.starla.mathletics.models.Quiz;
import com.starla.mathletics.models.User;
import com.starla.mathletics.utils.SessionManager;

import java.util.List;

public class StudentHomeActivity extends AppCompatActivity {

    private SessionManager session;
    private DatabaseHelper db;
    private User currentUser;
    private List<ChapterProgress> chapters;
    private ListView lvChapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        session = new SessionManager(this);
        db = DatabaseHelper.getInstance(this);
        currentUser = db.getUserById(session.getUserId());

        if (currentUser == null) {
            finish();
            return;
        }

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvTier = findViewById(R.id.tvTierLabel);
        TextView tvQuickStats = findViewById(R.id.tvQuickStats);
        lvChapters = findViewById(R.id.lvChapters);
        Button btnLeaderboard = findViewById(R.id.btnLeaderboard);
        Button btnQuizModes = findViewById(R.id.btnQuizModes);
        Button btnProfile = findViewById(R.id.btnProfile);

        tvWelcome.setText("Welcome, " + currentUser.getUsername() + "!");
        tvQuickStats.setText("⭐ " + currentUser.getStars() + " Stars  |  🪙 " + currentUser.getCoins() + " Coins  |  " + currentUser.getTotalPoints() + " pts");
        if (currentUser.isLowerPrimary()) {
            tvTier.setText(getString(R.string.lower_primary));
            setTitle("Lower Primary Home");
        } else {
            tvTier.setText(getString(R.string.upper_primary));
            setTitle("Upper Primary Home");
        }

        db.initChapterProgressForStudent(currentUser.getId(), currentUser.isLowerPrimary());
        chapters = db.getChapterProgress(currentUser.getId());
        ChapterAdapter adapter = new ChapterAdapter(this, chapters);
        lvChapters.setAdapter(adapter);
        registerForContextMenu(lvChapters);

        lvChapters.setOnItemClickListener((parent, view, position, id) -> {
            ChapterProgress chapter = chapters.get(position);
            if (chapter.isLocked()) {
                Toast.makeText(this, "Complete previous chapters to unlock!", Toast.LENGTH_SHORT).show();
                return;
            }
            launchQuizForChapter(chapter.getChapterName());
        });

        btnLeaderboard.setOnClickListener(v -> startActivity(new Intent(this, LeaderboardActivity.class)));
        btnQuizModes.setOnClickListener(v -> startActivity(new Intent(this, QuizModesActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, StudentProfileActivity.class)));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvChapters) {
            getMenuInflater().inflate(R.menu.chapter_context_menu, menu);
            menu.setHeaderTitle("Chapter Action");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        ChapterProgress chapter = chapters.get(position);

        if (item.getItemId() == R.id.chapter_details) {
            new AlertDialog.Builder(this)
                    .setTitle(chapter.getChapterName())
                    .setMessage("Status: " + chapter.getStatus() + "\nProgress: " + chapter.getProgressPercent() + "%")
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        } else if (item.getItemId() == R.id.chapter_start) {
            if (chapter.isLocked()) {
                Toast.makeText(this, "Chapter is locked!", Toast.LENGTH_SHORT).show();
            } else {
                launchQuizForChapter(chapter.getChapterName());
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void launchQuizForChapter(String chapterName) {
        String tier = currentUser.isLowerPrimary() ? "Lower Primary" : "Upper Primary";
        List<Quiz> quizzes = db.getQuizzesForStudent(tier, currentUser.getGradeLevel());

        if (quizzes.isEmpty()) {
            Toast.makeText(this, "No quiz available for this chapter yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        Quiz quiz = quizzes.get(0);
        for (Quiz q : quizzes) {
            if (q.getTopic().equalsIgnoreCase(chapterName) || q.getTitle().contains(chapterName.split(" ")[0])) {
                quiz = q;
                break;
            }
        }

        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("quiz_id", quiz.getId());
        intent.putExtra("chapter_name", chapterName);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            Toast.makeText(this, "Settings - STARLA.2.0 MathLetics", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_report_bugs) {
            showReportBugsDialog();
            return true;
        } else if (id == R.id.menu_about) {
            showAboutDialog();
            return true;
        } else if (id == R.id.menu_logout) {
            session.clearSession();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showReportBugsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Report Bugs?")
                .setMessage("Report bugs to STARLA.2.0 developers?")
                .setPositiveButton("Yes", (d, w) -> Toast.makeText(this, "Thank you for reporting!", Toast.LENGTH_SHORT).show())
                .setNegativeButton("No", (d, w) -> Toast.makeText(this, "Reporting helps us improve MathLetics.", Toast.LENGTH_SHORT).show())
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About MathLetics")
                .setMessage("MathLetics - Gamified Learning\nTeam: STARLA.2.0\nCSC557 Mobile Programming")
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUser != null) {
            currentUser = db.getUserById(session.getUserId());
            TextView tvQuickStats = findViewById(R.id.tvQuickStats);
            tvQuickStats.setText("⭐ " + currentUser.getStars() + " Stars  |  🪙 " + currentUser.getCoins() + " Coins  |  " + currentUser.getTotalPoints() + " pts");
            chapters = db.getChapterProgress(currentUser.getId());
            lvChapters.setAdapter(new ChapterAdapter(this, chapters));
        }
    }
}
