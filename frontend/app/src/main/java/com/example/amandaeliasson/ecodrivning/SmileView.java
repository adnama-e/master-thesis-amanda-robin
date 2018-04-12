package com.example.amandaeliasson.ecodrivning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
/**
 * Created by amandaeliasson on 2018-04-09.
 */

public class SmileView extends ImageSwitcher{
    Animation in;
    Animation out;
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

    public void init(){
        setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getContext());
            }
        });
        in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_view);
        out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_view);

        this.setInAnimation(in);
        this.setOutAnimation(out);
        images = new int[]{R.drawable.smiley_happy2,R.drawable.smiley_ok2, R.drawable.smiley_orange, R.drawable.smiley_sad2};
        index = 0;
    }
    public void change(){
        if(index<images.length-1){
            index++;
            this.setImageResource(images[index]);

        }
    }

    public void changeBack() {
        if(index !=0){
            index--;

            this.setImageResource(images[index]);

        }
    }
}
