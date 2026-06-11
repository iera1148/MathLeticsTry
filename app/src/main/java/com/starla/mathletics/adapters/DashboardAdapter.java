package com.starla.mathletics.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.starla.mathletics.LeaderboardActivity;
import com.starla.mathletics.R;
import com.starla.mathletics.models.QuizResult;

import java.util.List;

public class DashboardAdapter extends ArrayAdapter<QuizResult> {
    private final Activity context;

    public DashboardAdapter(Activity context, List<QuizResult> results) {
        super(context, R.layout.item_dashboard, results);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        }

        QuizResult result = getItem(position);
        if (result == null) return view;

        TextView tvQuiz = view.findViewById(R.id.tvQuizTitle);
        TextView tvStudent = view.findViewById(R.id.tvStudentName);
        TextView tvScore = view.findViewById(R.id.tvScore);
        Button btnView = view.findViewById(R.id.btnViewProgress);

        tvQuiz.setText(result.getQuizTitle());
        tvStudent.setText("Student: " + result.getStudentName());
        tvScore.setText("Score: " + result.getScore() + " / " + result.getTotalQuestions());

        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LeaderboardActivity.class);
            intent.putExtra("quiz_id", result.getQuizId());
            intent.putExtra("quiz_title", result.getQuizTitle());
            context.startActivity(intent);
        });

        return view;
    }
}
