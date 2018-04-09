package com.example.amandaeliasson.ecodrivning;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by amandaeliasson on 2018-04-07.
 */

public class FlowerView extends ImageView {
    private int[] images;
    private int index;
    private FlowerView waitFor;


    public FlowerView(Context context) {
        super(context);
        init();
    }

    public FlowerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlowerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FlowerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public void init(){
        images = new int[]{R.drawable.growing1,R.drawable.growing2, R.drawable.growing3, R.drawable.growing4, R.drawable.growing5,R.drawable.growing6, R.drawable.growing7};
        index = -1;
    }
    public void setFlowerToWaitFor(FlowerView fv){
        waitFor = fv;
    }
    public void grow(){
        if(waitFor==null||((waitFor !=null) && waitFor.fullGrown())){
            if(index ==-1){
                setVisibility(VISIBLE);
            }
            if(index<images.length-1){
                index++;
                setImageResource(images[index]);
            }
        }

    }
    public boolean fullGrown(){
        return (index == images.length-1);
    }
}
