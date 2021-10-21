package com.hipradeep.oauthenticationexample.utils;


import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

class SquareCardView extends CardView {
    Context context;

    public SquareCardView(@NonNull Context context, Context context1) {
        super(context);
        this.context = context1;
    }

    public SquareCardView(@NonNull Context context, @Nullable AttributeSet attrs, Context context1) {
        super(context, attrs);
        this.context = context1;
    }

    public SquareCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Context context1) {
        super(context, attrs, defStyleAttr);
        this.context = context1;
    }

    public void onMeasure(int widthMeasureSpec,int  ignoredHeightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
