package com.starla.mathletics;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.Question;
import com.starla.mathletics.models.Quiz;
import com.starla.mathletics.models.QuizResult;
import com.starla.mathletics.models.User;
import com.starla.mathletics.utils.SessionManager;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private List<Question> questions;
    private Quiz quiz;
    private int currentIndex = 0;
    private int score = 0;
    private int timeTaken = 0;
    private CountDownTimer timer;
    private long startTime;

    private TextView tvQuestion, tvProgress, tvTimer, tvQuizTitle;
    private RadioGroup rgOptions;
    private RadioButton rbA, rbB, rbC, rbD;
    private ProgressBar pbQuiz;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);

        int quizId = getIntent().getIntExtra("quiz_id", -1);
        quiz = db.getQuizById(quizId);
        questions = db.getQuestionsByQuiz(quizId);

        if (quiz == null || questions.isEmpty()) {
            Toast.makeText(this, "No questions found for this quiz", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvQuizTitle = findViewById(R.id.tvQuizTitle);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvProgress = findViewById(R.id.tvProgress);
        tvTimer = findViewById(R.id.tvTimer);
        rgOptions = findViewById(R.id.rgOptions);
        rbA = findViewById(R.id.rbA);
        rbB = findViewById(R.id.rbB);
        rbC = findViewById(R.id.rbC);
        rbD = findViewById(R.id.rbD);
        pbQuiz = findViewById(R.id.pbQuiz);
        btnSubmit = findViewById(R.id.btnSubmit);

        tvQuizTitle.setText(quiz.getTitle());
        startTime = System.currentTimeMillis();

        if (session.isTimerMode()) {
            startTimer(session.getTimerDuration());
        } else {
            tvTimer.setVisibility(View.GONE);
        }

        if (session.isFlashcardMode()) {
            rgOptions.setVisibility(View.GONE);
            btnSubmit.setText(R.string.next_question);
        }

        loadQuestion();
        btnSubmit.setOnClickListener(v -> handleSubmit());
    }

    private void startTimer(int seconds) {
        timer = new CountDownTimer(seconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(getString(R.string.timer_label, millisUntilFinished / 1000 + "s"));
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                finishQuiz();
            }
        }.start();
    }

    private void loadQuestion() {
        Question q = questions.get(currentIndex);
        tvQuestion.setText(q.getQuestionText());
        rbA.setText("A. " + q.getOptionA());
        rbB.setText("B. " + q.getOptionB());
        rbC.setText("C. " + q.getOptionC());
        rbD.setText("D. " + q.getOptionD());
        rgOptions.clearCheck();

        tvProgress.setText(getString(R.string.progress_label, currentIndex + 1, questions.size()));
        pbQuiz.setProgress((currentIndex + 1) * 100 / questions.size());

        if (currentIndex == questions.size() - 1) {
            btnSubmit.setText(session.isFlashcardMode() ? R.string.quiz_complete : R.string.submit_answer);
        }
    }

    private void handleSubmit() {
        if (!session.isFlashcardMode()) {
            int selectedId = rgOptions.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            String selected = "";
            if (selectedId == R.id.rbA) selected = "A";
            else if (selectedId == R.id.rbB) selected = "B";
            else if (selectedId == R.id.rbC) selected = "C";
            else if (selectedId == R.id.rbD) selected = "D";

            Question q = questions.get(currentIndex);
            if (q.getCorrectAnswer().equalsIgnoreCase(selected)) {
                score++;
            }
        }

        currentIndex++;
        if (currentIndex >= questions.size()) {
            finishQuiz();
        } else {
            loadQuestion();
        }
    }

    private void finishQuiz() {
        if (timer != null) timer.cancel();
        timeTaken = (int) ((System.currentTimeMillis() - startTime) / 1000);

        if (!session.isFlashcardMode()) {
            QuizResult result = new QuizResult();
            result.setStudentId(session.getUserId());
            result.setQuizId(quiz.getId());
            result.setScore(score);
            result.setTotalQuestions(questions.size());
            result.setTimeTaken(timeTaken);
            result.setTimerMode(session.isTimerMode());
            result.setFlashcardMode(session.isFlashcardMode());
            db.insertQuizResult(result);

            User user = db.getUserById(session.getUserId());
            int pointsEarned = score * 10;
            int newPoints = user.getTotalPoints() + pointsEarned;
            int newCoins = user.getCoins() + score * 2;
            int newStars = user.getStars() + (score == questions.size() ? 3 : 1);
            db.updateUserPoints(user.getId(), newPoints, newCoins, newStars);

            if (score == questions.size()) {
                db.insertAchievement(user.getId(), "Perfect Score - " + quiz.getTitle());
            }

            String chapterName = getIntent().getStringExtra("chapter_name");
            if (chapterName != null) {
                int progress = Math.min(100, (score * 100) / questions.size());
                String status = progress >= 80 ? "completed" : "in_progress";
                db.updateChapterProgress(user.getId(), chapterName, status, progress);
            }
        }

        Intent intent = new Intent(this, QuizResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", questions.size());
        intent.putExtra("quiz_title", quiz.getTitle());
        intent.putExtra("time_taken", timeTaken);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
