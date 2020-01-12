package com.express.tsuexpress;

import android.graphics.Bitmap;
import android.view.View;


public class ScreenShot {
    public static Bitmap takescreenshot(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap takescreenshotOfrootView(View v){
        return takescreenshot(v.getRootView());

    }
}
