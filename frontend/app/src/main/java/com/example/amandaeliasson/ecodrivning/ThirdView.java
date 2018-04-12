package com.example.amandaeliasson.ecodrivning;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by amandaeliasson on 2018-04-09.
 */

public class ThirdView extends ImageView {
    private int[] images;
    private int index;
    private ThirdView waitFor;


    public ThirdView(Context context) {
        super(context);
        init();
    }

    public ThirdView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThirdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ThirdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        images = new int[]{R.drawable.cloud, R.drawable.cloud, R.drawable.cloud, R.drawable.cloud, R.drawable.cloud};
        index = -1;


    }

    public void setFlowerToWaitFor(ThirdView fv) {
        waitFor = fv;
    }

    public void grow() {
        if (waitFor == null || waitFor.fullGrown()) {
            if (index == -1) {
                setVisibility(VISIBLE);
            }
            if (index < images.length - 1) {
                index++;
                setImageResource(images[index]);
            }
        }

    }

    public boolean fullGrown() {
        return (index == images.length - 1);
    }
}
