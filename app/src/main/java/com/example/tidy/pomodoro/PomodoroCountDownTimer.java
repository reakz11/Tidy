package com.example.tidy.pomodoro;


import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;


class PomodoroCountDownTimer extends CountDownTimer {

    /**
     * Creates a new timer
     *
     * @param totalCountDown number of total seconds of this timer
     * @param actualCountDown number of seconds on which the count down must start. It must be equal
     * or less than totalCountDown
     * @param listener the listener that will receive events from this timer
     */
    PomodoroCountDownTimer(long totalCountDown, long actualCountDown, Listener listener) {
        super(actualCountDown * 1000, 1000);
        mListener = listener;
        mTotalCountDown = totalCountDown;

        mListener.onCountDownChange(getCountDownText(actualCountDown));
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int progress = (int) (millisUntilFinished / 1000);

        mListener.onCountDownChange(getCountDownText(progress));

        mListener.onProgressChange((int) (mTotalCountDown - progress));
    }

    @Override
    public void onFinish() {
        mListener.onFinish();
    }

    private String getCountDownText(long secondsUntilFinished) {
        int seconds = (int) (secondsUntilFinished % 60);
        int minutes = (int) (secondsUntilFinished / 60);

        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    interface Listener {
        /**
         * Called when the progress needs to be updated, like in a {@link ProgressBar}.
         *
         * @param progress number of seconds passed
         */
        void onProgressChange(int progress);

        /**
         * Called when the visual count down needs to be updated, like in a {@link TextView}.
         *
         * @param countDown String representation of the actual count down
         */
        void onCountDownChange(String countDown);

        /**
         * Called when the count down has reached 0.
         */
        void onFinish();
    }

    private final Listener mListener;

    private final long mTotalCountDown;
}
