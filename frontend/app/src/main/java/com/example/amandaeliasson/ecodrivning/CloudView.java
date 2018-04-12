package com.example.amandaeliasson.ecodrivning;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by amandaeliasson on 2018-04-10.
 */

public class CloudView extends ImageView {
    private CloudView after;
    private CloudView before;

    public CloudView(Context context) {
        super(context);
        init();
    }

    public CloudView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CloudView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CloudView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {

        setImageResource(R.drawable.cloud);
        setVisibility(INVISIBLE);

    }

    public void setFlowerToWaitFor(CloudView fv) {
        after = fv;
        fv.before = this;
    }

    public void grow() {
        if (after == null || after.getVisibility() == VISIBLE) {
            setVisibility(VISIBLE);
            animate().alpha(1.0f);
        }
    }
    public void unGrow(){
        if (before == null|| before.getVisibility() == INVISIBLE) {

            animate().alpha(0.0f);
            setVisibility(INVISIBLE);

        }
    }

}
