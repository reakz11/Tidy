package com.example.tidy.pomodoro;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tidy.R;

public class PomodoroMain extends AppCompatActivity implements View.OnClickListener,
        PomodoroCountDownTimer.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro_main);

        if (savedInstanceState != null) {
            initializeViews(savedInstanceState.getInt(SAVED_PROGRESS));
            initializeButtons(savedInstanceState.getBoolean(SAVED_STARTED));
        } else {
            initializeViews(0);
        }

        initializeCountDownTimer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, _progressBar.getProgress());
        outState.putBoolean(SAVED_STARTED, _started);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_REQUEST_CODE) {
            start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.cancel(RelaxActivity.NOTIFICATION_ID);

        _countDownTimer.cancel();
    }

    private void initializeViews(int progress) {
        _progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        _countDownTextView = (TextView) findViewById(R.id.count_down);
        _startButton = (Button) findViewById(R.id.start_button);
        _pauseButton = (Button) findViewById(R.id.pause_button);


        _progressBar.setMax(COUNTDOWN_SECONDS);
        _progressBar.setProgress(progress);
        _startButton.setOnClickListener(this);
        _pauseButton.setOnClickListener(this);
        findViewById(R.id.close_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                start();
                initializeButtons(true);
                break;
            case R.id.pause_button:
                pause();
                initializeButtons(false);
                break;
            case R.id.close_button:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void initializeButtons(boolean isStarted) {
        _started = isStarted;
        _pauseButton.setEnabled(isStarted);
        _pauseButton.setAlpha(isStarted ? 1.0f : 0.5f);
        _startButton.setEnabled(!isStarted);
        _startButton.setAlpha(isStarted ? 0.5f : 1.0f);
    }

    private void pause() {
        _countDownTimer.cancel();
        initializeCountDownTimer();
    }

    private void start() {
        _countDownTimer.start();
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
        _progressBar.setProgress(0);
        pause();
        Intent intent = new Intent(this, RelaxActivity.class);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    private void initializeCountDownTimer() {
        int actualCountDown = _progressBar.getMax() - _progressBar.getProgress();
        _countDownTimer = new PomodoroCountDownTimer(COUNTDOWN_SECONDS, actualCountDown, this);
    }

    static final String SAVED_PROGRESS = "SAVED_PROGRESS";

    static final String SAVED_STARTED = "SAVED_STARTED";

    static final int COUNTDOWN_SECONDS = 1500;

    private static final int ACTIVITY_REQUEST_CODE = 42;

    private PomodoroCountDownTimer _countDownTimer;

    private ProgressBar _progressBar;

    private TextView _countDownTextView;

    private Button _startButton;

    private Button _pauseButton;

    private boolean _started;
}
