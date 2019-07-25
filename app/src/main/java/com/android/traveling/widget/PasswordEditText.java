package com.android.traveling.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.android.traveling.R;
import com.android.traveling.util.LogUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget.gridview
 * 文件名：PasswordEditText
 * 创建者：HY
 * 创建时间：2019/4/6 16:27
 * 描述：  带有可见/不可见图标的密码输入框
 */

public class PasswordEditText extends AppCompatEditText {

    /**
     * 当前密码可见
     */
    private static final int VISIBLE = 0;
    private static final int INVISIBLE = 1;

    @IntDef({VISIBLE, INVISIBLE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Visible {
    }

    /**
     * 密码可见性
     */
    @Visible
    private int visible = INVISIBLE;
    private int left = 0;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*
                  判断点击的是否是右边的按钮
                 */
                if (left == 0) {
                    left = getWidth() - getTotalPaddingRight() - getCompoundDrawablePadding();
                }
                boolean touchable = event.getX() > left && event.getX() < getWidth();
                LogUtil.d("left=" + left + ",x = " + event.getX() + ",touchable=" + touchable);
                if (touchable) {
                    changeVisible();
                } else {
                    performClick();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 改变密码可见性
     */
    private void changeVisible() {
        Drawable left;
        switch (visible) {
            case VISIBLE:
                LogUtil.d("变为不可见");
                visible = INVISIBLE;
                //设置密码不可见
                setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                left = getContext().getResources().getDrawable(R.drawable.ic_eye_slash, null);
                // 这一步必须要做，否则不会显示。
                left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());// 设置图片宽高
                setCompoundDrawables(null, null, left, null);
                break;
            case INVISIBLE:
                LogUtil.d("变为可见");
                visible = VISIBLE;
                setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                left = getContext().getResources().getDrawable(R.drawable.ic_eye, null);
                // 这一步必须要做，否则不会显示。
                left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());// 设置图片宽高
                setCompoundDrawables(null, null, left, null);
                break;
        }
        setSelection(getText().toString().length());
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}