package com.starla.mathletics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.Question;
import com.starla.mathletics.models.Quiz;
import com.starla.mathletics.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CreateQuizActivity extends AppCompatActivity {

    private static final int MAX_QUESTIONS = 20;

    private LinearLayout llQuestions;
    private TextView tvQuestionCount;
    private final List<View> questionViews = new ArrayList<>();
    private DatabaseHelper db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);

        EditText etTitle = findViewById(R.id.etQuizTitle);
        EditText etTopic = findViewById(R.id.etQuizTopic);
        Spinner spTier = findViewById(R.id.spGradeTier);
        Spinner spGrade = findViewById(R.id.spGradeLevel);
        llQuestions = findViewById(R.id.llQuestions);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        Button btnAdd = findViewById(R.id.btnAddQuestion);
        Button btnSave = findViewById(R.id.btnSaveQuiz);

        btnAdd.setOnClickListener(v -> {
            if (questionViews.size() >= MAX_QUESTIONS) {
                Toast.makeText(this, "Maximum 20 questions per quiz", Toast.LENGTH_SHORT).show();
                return;
            }
            addQuestionForm();
        });

        btnSave.setOnClickListener(v -> saveQuiz(etTitle, etTopic, spTier, spGrade));
        addQuestionForm();
    }

    private void addQuestionForm() {
        View form = LayoutInflater.from(this).inflate(R.layout.item_question_form, llQuestions, false);
        TextView tvNum = form.findViewById(R.id.tvQuestionNum);
        tvNum.setText("Question " + (questionViews.size() + 1));
        llQuestions.addView(form);
        questionViews.add(form);
        updateCount();
    }

    private void updateCount() {
        tvQuestionCount.setText("Questions: " + questionViews.size() + " / " + MAX_QUESTIONS);
    }

    private void saveQuiz(EditText etTitle, EditText etTopic, Spinner spTier, Spinner spGrade) {
        String title = etTitle.getText().toString().trim();
        String topic = etTopic.getText().toString().trim();
        String tier = spTier.getSelectedItem().toString();
        int gradeLevel = spGrade.getSelectedItemPosition() + 1;


        if (title.isEmpty() || topic.isEmpty()) {
            Toast.makeText(this, "Enter quiz title and topic", Toast.LENGTH_SHORT).show();
            return;
        }


        if (questionViews.isEmpty()) {
            Toast.makeText(this, "Add at least one question", Toast.LENGTH_SHORT).show();
            return;
        }

        if (questionViews.size() > MAX_QUESTIONS) {
            Toast.makeText(this, "Maximum 20 questions allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        Quiz quiz = new Quiz();
        quiz.setAdminId(session.getUserId());
        quiz.setTitle(title);
        quiz.setTopic(topic);
        quiz.setGradeTier(tier);
        quiz.setGradeLevel(gradeLevel);

        long quizId = db.insertQuiz(quiz);
        if (quizId <= 0) {
            Toast.makeText(this, "Failed to save quiz", Toast.LENGTH_SHORT).show();
            return;
        }

        int order = 1;
        for (View form : questionViews) {
            EditText etQ = form.findViewById(R.id.etQuestion);
            EditText etA = form.findViewById(R.id.etOptionA);
            EditText etB = form.findViewById(R.id.etOptionB);
            EditText etC = form.findViewById(R.id.etOptionC);
            EditText etD = form.findViewById(R.id.etOptionD);
            EditText etCorrect = form.findViewById(R.id.etCorrect);

            String qText = etQ.getText().toString().trim();
            String correct = etCorrect.getText().toString().trim().toUpperCase();

            if (qText.isEmpty() || correct.isEmpty()) {
                Toast.makeText(this, "Fill all question fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Question question = new Question();
            question.setQuizId((int) quizId);
            question.setQuestionText(qText);
            question.setOptionA(etA.getText().toString().trim());
            question.setOptionB(etB.getText().toString().trim());
            question.setOptionC(etC.getText().toString().trim());
            question.setOptionD(etD.getText().toString().trim());
            question.setCorrectAnswer(correct);
            question.setOrderNum(order++);
            db.insertQuestion(question);
        }

        Toast.makeText(this, "Quiz created successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
