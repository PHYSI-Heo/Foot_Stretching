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

        animator = ObjectAnimator.ofInt(ivUp, "imageResource",
                R.drawable.ic_up_enable, R.drawable.ic_up_disable);
        animator.setDuration(500);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(Animation.RESTART);

    }

    private void setTypeArray(TypedArray typedArray) {
        imgResourceID = typedArray.getResourceId(R.styleable.MoveWinker_img_resid, R.drawable.img_left_foot);
        typedArray.recycle();
    }

    private void setTargetWidget(String direction){
        if(animator == null)
            return;

        switch (direction){
            case "1":
                animator.setIntValues(R.drawable.ic_up_enable, R.drawable.ic_up_disable);
                animator.setTarget(ivUp);
                break;
            case "2":
                animator.setIntValues(R.drawable.ic_down_enable, R.drawable.ic_down_disable);
                animator.setTarget(ivDown);
                break;
            case "3":
                animator.setIntValues(R.drawable.ic_left_enable, R.drawable.ic_left_disable);
                animator.setTarget(ivLeft);
                break;
            default:
                animator.setIntValues(R.drawable.ic_right_enable, R.drawable.ic_right_disable);
                animator.setTarget(ivRight);
                break;
        }
    }

    public void startBlink(String data){
        try{
            String dir = data.split(",")[0];
            if(animator.isStarted())
                animator.end();
            setTargetWidget(dir);
            animator.start();
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public void stopBlink(){
        if(animator.isStarted())
            animator.end();
    }
}
