package com.physis.foot.stretching.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physis.foot.stretching.R;

public class MenuButton extends RelativeLayout {

    private RelativeLayout menuBtnFrame;
    private TextView tvPointLine, tvBtnText;
    private ImageView ivMenuIcon;

    private int lineColor, iconResid;
    private String menuText;

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeArray(getContext().obtainStyledAttributes(attrs, R.styleable.MenuButton));
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_menu_button, this, false);
        addView(view);

        menuBtnFrame = view.findViewById(R.id.menu_btn_frame);

        tvPointLine = view.findViewById(R.id.tv_point_lien);
        tvPointLine.setBackgroundResource(lineColor);
        tvBtnText = view.findViewById(R.id.tv_menu_txt);
        tvBtnText.setText(menuText);
        ivMenuIcon = view.findViewById(R.id.iv_menu_icon);
        ivMenuIcon.setImageResource(iconResid);
    }

    private void setTypeArray(TypedArray typedArray) {
        lineColor = typedArray.getResourceId(R.styleable.MenuButton_bottom_lienColor, R.color.colorAccent);
        iconResid = typedArray.getResourceId(R.styleable.MenuButton_ic_resid, R.drawable.ic_launcher_foreground);
        menuText = typedArray.getString(R.styleable.MenuButton_menu_text);
        typedArray.recycle();
    }

}
