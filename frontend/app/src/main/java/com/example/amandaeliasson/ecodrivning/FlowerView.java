package com.example.amandaeliasson.ecodrivning;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by amandaeliasson on 2018-04-07.
 */

public class FlowerView extends ImageSwitcher {
    private int[] images;
    private int index;
    Animation in;
    Animation out;
    private FlowerView waitFor;


    public FlowerView(Context context) {
        super(context);
        init();
    }

    public FlowerView(Context context, @Nullable AttributeSet attrs) {
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
        images = new int[]{R.drawable.flower1,R.drawable.flower2, R.drawable.flower3, R.drawable.flower4, R.drawable.flower5,R.drawable.flower6, R.drawable.flower7};
        index = -1;
    }
    public void setFlowerToWaitFor(FlowerView fv){
        waitFor = fv;
    }
    public void grow(){
        if(waitFor==null||waitFor.fullGrown()){
            if(index ==-1){
                setVisibility(VISIBLE);
            }
            if(index<images.length-1){
                index++;
                this.setImageResource(images[index]);
            }
        }
    }
    public void unGrow(){
        if(index>0){
            index--;
            this.setImageResource(images[index]);
        }
    }
    public boolean fullGrown(){
        return (index == images.length-1);
    }
}
