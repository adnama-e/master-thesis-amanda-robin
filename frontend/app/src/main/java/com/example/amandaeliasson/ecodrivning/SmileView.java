package com.example.amandaeliasson.ecodrivning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * Created by amandaeliasson on 2018-04-09.
 */

public class SmileView extends ImageView {
    private int[] images;
    int index;
    public SmileView(Context context) {
        super(context);
        init();
    }

    public SmileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SmileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public void init(){
        images = new int[]{R.drawable.s1,R.drawable.s2, R.drawable.s3, R.drawable.s4};
        index = 0;
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.greenS));
    }
    public void change(){
        if(index<images.length-1){
            if (index == 0) {
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.LimeS));
            } else if (index == 1) {
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
            } else {
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
            }
            index++;
            setImageResource(images[index]);

        }
    }
}
