package com.example.amandaeliasson.ecodrivning;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by amandaeliasson on 2018-04-11.
 */

public class WarningView extends ImageView {
    int alpha;

    public WarningView(Context context) {
        super(context);
        init();
    }

    public WarningView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WarningView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public WarningView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public void init(){
        alpha =0;
    }
    public void increaseAlpha(){
        if(alpha < 255){
            alpha += 10;
        }
    }
    public void reduceAlpha(){
        if(alpha != 0){
            alpha -= 10;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setAlpha(){
        setImageAlpha(alpha);
    }
    public int alpha(){
        return alpha;
    }
}
