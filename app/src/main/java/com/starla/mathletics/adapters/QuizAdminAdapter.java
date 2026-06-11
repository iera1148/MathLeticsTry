package com.starla.mathletics.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starla.mathletics.R;
import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.Quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizAdminAdapter extends RecyclerView.Adapter<QuizAdminAdapter.ViewHolder> {

    public interface QuizActionListener {
        void onQuizDeleted();
        void onQuizUpdated(Quiz quiz);
    }

    private final Context context;
    private final List<Quiz> list;
    private final DatabaseHelper db;
    private final QuizActionListener listener;

    public QuizAdminAdapter(Context context, List<Quiz> list, QuizActionListener listener) {
        this.context = context;
        this.list = list;
        this.db = DatabaseHelper.getInstance(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_quiz_admin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz quiz = list.get(position);
        holder.tvTitle.setText(quiz.getTitle());
        holder.tvTopic.setText(quiz.getTopic() + " | " + quiz.getGradeTier() + " Year " + quiz.getGradeLevel());
        holder.tvStats.setText("Questions: " + quiz.getQuestionCount() +
                " | Avg Score: " + String.format("%.0f%%", quiz.getAvgScore()) +
                " | Completion: " + String.format("%.0f%%", quiz.getCompletionRate()));

        holder.btnDelete.setOnClickListener(v -> showDeleteDialog(quiz, position));
        holder.btnUpdate.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQuizUpdated(quiz);
            }
        });
    }

    private void showDeleteDialog(Quiz quiz, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Are you sure to delete?")
                .setMessage("Quiz and all questions will be permanently deleted.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteQuiz(quiz.getId());
                    list.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Quiz deleted.", Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onQuizDeleted();
                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        Toast.makeText(context, "Delete cancelled.", Toast.LENGTH_SHORT).show())
                .show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Quiz> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public List<Quiz> getList() {
        return list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTopic, tvStats;
        Button btnUpdate, btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvQuizTitle);
            tvTopic = itemView.findViewById(R.id.tvQuizTopic);
            tvStats = itemView.findViewById(R.id.tvQuizStats);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
