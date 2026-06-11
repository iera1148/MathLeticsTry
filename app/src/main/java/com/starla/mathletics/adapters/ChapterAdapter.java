package com.starla.mathletics.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.starla.mathletics.R;
import com.starla.mathletics.models.ChapterProgress;

import java.util.List;

public class ChapterAdapter extends ArrayAdapter<ChapterProgress> {
    private final Activity context;
    private final List<ChapterProgress> chapters;

    public ChapterAdapter(Activity context, List<ChapterProgress> chapters) {
        super(context, R.layout.item_chapter, chapters);
        this.context = context;
        this.chapters = chapters;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false);
        }

        ChapterProgress chapter = chapters.get(position);

        TextView tvName = view.findViewById(R.id.tvChapterName);
        TextView tvStatus = view.findViewById(R.id.tvChapterStatus);
        ProgressBar pb = view.findViewById(R.id.pbChapter);
        ImageView ivIcon = view.findViewById(R.id.ivChapterIcon);
        ImageView ivLock = view.findViewById(R.id.ivLock);

        tvName.setText(chapter.getChapterName());
        pb.setProgress(chapter.getProgressPercent());

        if (chapter.isLocked()) {
            tvStatus.setText("Locked - Complete previous chapters");
            tvStatus.setTextColor(context.getColor(R.color.locked_gray));
            ivLock.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(R.drawable.ic_lock);
        } else if (chapter.isCompleted()) {
            tvStatus.setText("Completed! Trophy earned");
            tvStatus.setTextColor(context.getColor(R.color.sketch_green));
            ivLock.setVisibility(View.GONE);
            ivIcon.setImageResource(R.drawable.ic_trophy);
        } else {
            tvStatus.setText("In Progress - " + chapter.getProgressPercent() + "%");
            tvStatus.setTextColor(context.getColor(R.color.sketch_blue));
            ivLock.setVisibility(View.GONE);
            ivIcon.setImageResource(R.drawable.ic_trophy);
        }

        return view;
    }
}
