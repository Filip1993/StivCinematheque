package com.dbele.stiv.utitlities;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dbele.stiv.cinematheque.R;

/**
 * Created by dbele on 4/17/2015.
 */
public class AnimationHandler {

    public static void startRotatingAnimation(Context context, ImageView myImage) {
        final Animation myRotation = AnimationUtils.loadAnimation(context, R.anim.rotation);
        myImage.startAnimation(myRotation);
    }

    public static void startOutAnimation(Context context, View view) {
        Animation out = AnimationUtils.makeOutAnimation(context, true);
        view.startAnimation(out);
        view.setVisibility(View.INVISIBLE);
    }

    public static void startFadeInAnimation(Context context, View view) {
        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        view.startAnimation(in);
        view.setVisibility(View.VISIBLE);
    }

    public static void startBlinkAnimation(Context context, View view) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.blink));
    }



}
