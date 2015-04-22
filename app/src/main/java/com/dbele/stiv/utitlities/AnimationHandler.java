package com.dbele.stiv.utitlities;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import com.dbele.stiv.cinematheque.R;

public class AnimationHandler {

    public static void startRotatingAnimation(Context context, View view) {
        final Animation myRotation = AnimationUtils.loadAnimation(context, R.anim.rotation);
        view.startAnimation(myRotation);
    }

    public static void startRotatingAnimation(float currentDegree, float nextDegree, int duration, View view) {
        final RotateAnimation rotateAnimation = new RotateAnimation(
                currentDegree,
                nextDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);
        view.startAnimation(rotateAnimation);
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
