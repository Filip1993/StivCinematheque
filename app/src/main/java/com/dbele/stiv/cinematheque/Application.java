package com.dbele.stiv.cinematheque;

import android.os.Build;

/**
 * Created by dbele on 3/30/2015.
 */
public class Application extends android.app.Application{
    public void onCreate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Foreground.init(this);
        }
    }


}
