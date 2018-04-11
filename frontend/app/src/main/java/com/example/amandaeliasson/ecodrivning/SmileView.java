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
    private  int[] colors;
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
        images = new int[]{R.drawable.smiley_happy,R.drawable.smiley_ok, R.drawable.smiley_ok, R.drawable.smiley_sad};
        colors = new int[]{R.color.greenS, R.color.LimeS, R.color.orange,R.color.red};
        index = 0;
    }
    public void change(){
        if(index<images.length-1){
            index++;
            setImageResource(images[index]);

        }
    }

    public void changeBack() {
        if(index !=0){
            index--;
            setImageResource(images[index]);

        }
    }
    public void changeBackgroundColor(){
        if(index ==0){
            setBackgroundColor(ContextCompat.getColor(getContext(), colors[0]));
        }else if(index ==1){
            setBackgroundColor(ContextCompat.getColor(getContext(), colors[1]));
        }else if(index ==2){
            setBackgroundColor(ContextCompat.getColor(getContext(), colors[2]));
        }else{
            setBackgroundColor(ContextCompat.getColor(getContext(), colors[3]));
        }
    }
}
