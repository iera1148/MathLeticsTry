package com.starla.mathletics.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.starla.mathletics.R;
import com.starla.mathletics.models.LeaderboardEntry;

import java.util.List;

public class LeaderboardAdapter extends ArrayAdapter<LeaderboardEntry> {
    private final Activity context;

    public LeaderboardAdapter(Activity context, List<LeaderboardEntry> entries) {
        super(context, R.layout.item_leaderboard, entries);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_leaderboard, parent, false);
        }

        LeaderboardEntry entry = getItem(position);
        if (entry == null) return view;

        TextView tvRank = view.findViewById(R.id.tvRank);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvGrade = view.findViewById(R.id.tvGrade);
        TextView tvPoints = view.findViewById(R.id.tvPoints);

        tvRank.setText("#" + entry.getRank());
        tvName.setText(entry.getUsername());
        tvGrade.setText("Year " + entry.getGradeLevel() + " | Badges: " + entry.getBadgeCount());
        tvPoints.setText(entry.getTotalPoints() + " pts");

        return view;
    }
}
