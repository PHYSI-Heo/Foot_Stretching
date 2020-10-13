package com.physis.foot.stretching.widget;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.physis.foot.stretching.R;

public class MoveWinker extends LinearLayout {

    private int imgResourceID;

    private ImageView ivFoot, ivUp, ivDown, ivLeft, ivRight;

    private ObjectAnimator animator;

    public MoveWinker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeArray(getContext().obtainStyledAttributes(attrs, R.styleable.MoveWinker));
        initView();
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_move_winker, this, false);
        addView(view);

        ivFoot = view.findViewById(R.id.iv_front_foot);
        ivFoot.setImageResource(imgResourceID);

        ivUp = view.findViewById(R.id.iv_move_up);
        ivDown = view.findViewById(R.id.iv_move_down);
        ivLeft = view.findViewById(R.id.iv_move_left);
        ivRight = view.findViewById(R.id.iv_move_right);

        animator = ObjectAnimator.ofInt(ivUp, "backgroundColor",
                getResources().getColor(R.color.colorAccent, null),
                getResources().getColor(R.color.colorHighlight, null),
                getResources().getColor(R.color.colorAccent, null));
        animator.setDuration(500);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(Animation.RESTART);

    }

    private void setTypeArray(TypedArray typedArray) {
        imgResourceID = typedArray.getResourceId(R.styleable.MoveWinker_img_resid, R.drawable.img_left_foot);
        typedArray.recycle();
    }

    private ImageView getTargetWidget(String direction){
        switch (direction){
            case "1":
                return ivUp;
            case "2":
                return ivDown;
            case "3":
                return ivLeft;
            default:
                return ivRight;
        }
    }

    public void startBlink(String direction){
        if(animator.isStarted())
            animator.end();
        animator.setTarget(getTargetWidget(direction));
        animator.start();
    }

    public void stopBlink(){
        if(animator.isStarted())
            animator.end();
    }
}
