package com.example.tidy.pomodoro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tidy.R;


public class RelaxActivity extends AppCompatActivity implements PomodoroCountDownTimer.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro_rest);

        if (savedInstanceState != null) {
            initializeViews(savedInstanceState.getInt(SAVED_PROGRESS));
        } else {
            initializeViews(0);
        }

        initializeCountDownTimer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, _progressBar.getProgress());
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    private void initializeViews(int progress) {
        _progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        _countDownTextView = (TextView) findViewById(R.id.count_down);

        _progressBar.setMax(COUNTDOWN_SECONDS);
        _progressBar.setProgress(progress);
    }

    private void pause() {
        _countDownTimer.cancel();
        initializeCountDownTimer();
    }

    public void sendNotification(int icon, String title, String content) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this);

        final Intent notificationIntent = new Intent(this, PomodoroMain.class);
        notificationIntent
                .setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Notification pomodoro = notificationBuilder
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0))
                .build();

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, pomodoro);
    }

    private void start() {
        _countDownTimer.start();
        String title = getString(R.string.grab_coffee);
        String content = getString(R.string.relaxing_period);
        sendNotification(R.drawable.icon_coffee, title, content);
    }

    @Override
    public void onProgressChange(int progress) {
        _progressBar.setProgress(progress);
    }

    @Override
    public void onCountDownChange(String countDown) {
        _countDownTextView.setText(countDown);
    }

    @Override
    public void onFinish() {
        String title = getString(R.string.back_work);
        String content = getString(R.string.pomodoro_session);
        sendNotification(R.drawable.icon_work, title, content);
        finish();
    }

    private void initializeCountDownTimer() {
        int actualCountDown = _progressBar.getMax() - _progressBar.getProgress();
        _countDownTimer = new PomodoroCountDownTimer(COUNTDOWN_SECONDS, actualCountDown, this);
    }

    static final String SAVED_PROGRESS = "SAVED_PROGRESS";

    static final int COUNTDOWN_SECONDS = 300;

    public static final int NOTIFICATION_ID = 0;

    private PomodoroCountDownTimer _countDownTimer;

    private ProgressBar _progressBar;

    private TextView _countDownTextView;
}

