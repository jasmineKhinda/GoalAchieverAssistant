package com.example.jasmine.goalachieverassistant.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.content.Context;


import java.util.List;
/**
 * Created by jasmine on 18/01/18.
 */

public class LinearLayoutManagerWrapper extends LinearLayoutManager {

    public LinearLayoutManagerWrapper(Context context) {
        super(context);
    }

    public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}