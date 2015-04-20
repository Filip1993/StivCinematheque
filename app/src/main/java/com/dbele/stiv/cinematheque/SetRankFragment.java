package com.dbele.stiv.cinematheque;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dbele.stiv.utitlities.AnimationHandler;


/**
 * Created by dbele on 4/15/2015.
 */
public class SetRankFragment extends DialogFragment implements SensorEventListener {

    private TextView tvRankDone;
    private ImageView ivRankDone;
    private MovieFragment movieFragment;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float lastY;
    private float deltaY = 0;
    float currentDegree = 1;

    private String[] ranks;
    private String rank;

    private boolean landscape = false;

    public static SetRankFragment newInstance(MovieFragment movieFragment) {
        SetRankFragment frag = new SetRankFragment();
        frag.movieFragment = movieFragment;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_set_rank, container);
        getDialog().setTitle(R.string.set_rank_title);

        ivRankDone = (ImageView) view.findViewById(R.id.ivRankDone);

        ranks = getResources().getStringArray(R.array.ranks);

        sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(getActivity(),R.string.cannot_use_accelerometer, Toast.LENGTH_LONG).show();
        }
        tvRankDone = (TextView) view.findViewById(R.id.tvRankDone);
        if (movieFragment.getMovieRank()!=null) {
            tvRankDone.setText(movieFragment.getMovieRank());
        }


        ivRankDone = (ImageView) view.findViewById(R.id.ivRankDone);
        ivRankDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieFragment.setMovieRank(tvRankDone.getText().toString().length() > 0 ? tvRankDone.getText().toString() : null);
                movieFragment.setMovieDegree(currentDegree);
                getDialog().dismiss();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            landscape = true;
        }
        else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            landscape = false;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometer);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float newY = event.values[1];
        if (!landscape) {
            newY = - event.values[0];
        }
        deltaY = lastY + newY;
        lastY = newY;

        float nextDegree = currentDegree + deltaY;

        if (nextDegree > 180) {
            nextDegree = 180;
        } else if (nextDegree < 1) {
            nextDegree = 1;
        }

        AnimationHandler.startRotatingAnimation(currentDegree, nextDegree, 250, ivRankDone);

        currentDegree = nextDegree;

        int rankIndex = (int)currentDegree/40;
        rank = ranks[rankIndex];
        tvRankDone.setText(rank);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}