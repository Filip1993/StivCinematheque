package com.dbele.stiv.utitlities;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import com.dbele.stiv.cinematheque.Foreground;
import java.util.List;

public class ActivityHandler {

    public static boolean applicationIsInForeground(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return Foreground.get().isForeground();
        } else {
            return isInForeground(context);
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean isInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
        return services.get(0).topActivity.getPackageName().equalsIgnoreCase(context.getPackageName());
    }
}
