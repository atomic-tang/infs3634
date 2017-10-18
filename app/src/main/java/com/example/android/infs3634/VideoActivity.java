package com.example.android.infs3634;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    Week week;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        final VideoActivity activity = this;

        Button mNextBtn = findViewById(R.id.nextBtn);
        VideoView mVideoView = findViewById(R.id.videoView);
        TextView mWeekProgress = findViewById(R.id.textView3);
        ProgressBar mProgressBar = findViewById(R.id.progressBar2);
        TextView mTitle = findViewById(R.id.title);
        TextView mDescription = findViewById(R.id.description);
        titleTextView = findViewById(R.id.videoWeekTitle);

        final ArrayList<Task> tasks = (ArrayList<Task>) getIntent().getSerializableExtra("Tasks");
        final int taskIndex = (int) getIntent().getSerializableExtra("Task Index") + 1;
        final int totalTasks = (int) getIntent().getSerializableExtra("Total Tasks");
        week = (Week) getIntent().getSerializableExtra("Week");

        titleTextView.setText("Week " + week.getWeekNumber() + ": " + week.getWeekTopic());

        int progress = Math.round(100 * taskIndex / totalTasks);
        Video video = (Video) tasks.get(0);
        Uri videoUri = Uri.parse(video.getUrl());

        mWeekProgress.setText(taskIndex + " / " + totalTasks);
        mProgressBar.setProgress(progress);
        mTitle.setText(video.getTitle());
        mDescription.setText(video.getDescription());

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoURI(videoUri);
        mVideoView.requestFocus();

        tasks.remove(0);
        if (tasks.size() > 0) {
            mNextBtn.setText("Next");
        } else {
            mNextBtn.setText("Finish Lesson");
        }

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tasks.size() > 0) {
                    Intent intent;
                    if (tasks.get(0).getType().equalsIgnoreCase("quiz")) {
                        intent = new Intent(activity, QuizActivity.class);
                    } else  {
                        intent = new Intent(activity, VideoActivity.class);
                    }
                    intent.putExtra("Tasks", tasks);
                    intent.putExtra("Task Index", taskIndex);
                    intent.putExtra("Total Tasks", totalTasks);
                    intent.putExtra("Week", week);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, ResultActivity.class);
                    intent.putExtra("Week", week);
                    startActivity(intent);
                }
            }
        });

    }
}
