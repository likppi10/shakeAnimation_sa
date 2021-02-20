package com.example.shakeanimation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/* 기본적으로 ExampleFragment를 통짜로 옮겼다 생각하면 됨*/
public class CustomLayout extends FrameLayout {

    public CustomLayout(@NonNull Context context) {
        super(context);
    }

    public CustomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initChildViews(context);
    }

    @IntDef({NONE, CUBE})
    public @interface AnimationStyle {}
    public static final int NONE     = 0;
    public static final int CUBE     = 2;

    @IntDef({NODIR, UP, DOWN, LEFT, RIGHT})
    public @interface AnimationDirection {}
    public static final int NODIR = 0;
    public static final int UP    = 1;
    public static final int DOWN  = 2;
    public static final int LEFT  = 3;
    public static final int RIGHT = 4;

    private static final long DURATION = 500;

    /* Fragment를 우선 선언함*/
    /* getArguments라는 것을 아래에서 많이 쓰는데 ExampleFragment에서 컨트롤 클릭해보면 Fragment꺼임  */
    private static Fragment ff = new Fragment();


    //시작은 큐브로 세팅함
    @AnimationStyle
    private static int sAnimationStyle = CUBE;
    @BindView(R.id.textAnimationStyle)
    TextView mTextAnimationStyle;


    /* 이게 안되서 프레그먼트 문제인것 같은게 ExampleFragment에서는 newInstance가 ExampleFragment 본인을 가리킴*/
    /* 근데 이거는 본인 자체가 프레그먼트가 아니라서 가상?으로 fragment를 선언해서 newInstance를 만들어줬음*/
    public static Fragment newInstance(@AnimationDirection int direction) {
        Fragment f = new Fragment();
        f.setArguments(new Bundle());
        f.getArguments().putInt("direction", direction);
        return f;
    }

    private void initChildViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_anim, this, true);

        int color = Color.rgb((int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64);
        view.setBackgroundColor(color);
        ButterKnife.bind(this, view);
        setAnimationStyleText();

    }


    /* ExampleFragment에서는 onCreateAnimation가 회색 글시가 아님 */
    /* 이것도 프레그먼트가 아니라 그런듯*/
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        switch (sAnimationStyle) {
            case CUBE:
                Log.d("파이어어어", " 확인1 : " + ff.getArguments().getInt("direction"));
                switch (ff.getArguments().getInt("direction")) {
                    case UP:
                        return CubeAnimation.create(CubeAnimation.UP, enter, DURATION);
                    case DOWN:
                        return CubeAnimation.create(CubeAnimation.DOWN, enter, DURATION);
                    case LEFT:

                        return CubeAnimation.create(CubeAnimation.LEFT, enter, DURATION);
                    case RIGHT:
                        return CubeAnimation.create(CubeAnimation.RIGHT, enter, DURATION);
                }
                break;
            case NONE:
                break;
        }
        return null;
    }
    @SuppressWarnings("unused")
    @OnClick(R.id.buttonUp)
    void onButtonUp() {
        ff.getArguments().putInt("direction", UP);
        FragmentTransaction ft = ff.getFragmentManager().beginTransaction();
        ft.replace(R.id.layout_fragment, CustomLayout.newInstance(UP));
        ft.commit();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.buttonDown)
    void onButtonDown() {
        ff.getArguments().putInt("direction", DOWN);
        FragmentTransaction ft = ff.getFragmentManager().beginTransaction();
        ft.replace(R.id.layout_fragment, CustomLayout.newInstance(DOWN));
        ft.commit();
    }

    /* 왼쪽 버튼 눌렀을 때 오류가 나는 부분이 134줄 저긴데 보면 뭐가 널이라 안됨 이라는데 이 역시 프레그먼트가 아니라 그런듯*/
    @SuppressWarnings("unused")
    @OnClick(R.id.buttonLeft)
    void onButtonLeft() {
        ff.getArguments().putInt("direction", LEFT);
        FragmentTransaction ft = ff.getFragmentManager().beginTransaction();
        ft.replace(R.id.layout_fragment, CustomLayout.newInstance(LEFT));
        ft.commit();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.buttonRight)
    void onButtonRight() {
        ff.getArguments().putInt("direction", RIGHT);
        FragmentTransaction ft = ff.getFragmentManager().beginTransaction();
        ft.replace(R.id.layout_fragment, CustomLayout.newInstance(RIGHT));
        ft.commit();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.textAnimationStyle)
    public void switchAnimationStyle(View view) {
        @AnimationStyle int[] styles;
        styles = new int[]{CUBE};
        for (int i = 0; i<styles.length-1; ++i) {
            if (styles[i] == sAnimationStyle) {
                setAnimationStyle(styles[i+1]);
                return;
            }
        }
        setAnimationStyle(CUBE);
    }

    public void setAnimationStyle(@AnimationStyle int style) {
        if (sAnimationStyle != style) {
            sAnimationStyle = style;
            setAnimationStyleText();  //이름 적기
            Snackbar.make(ff.getView(), "Animation Style is Changed", Snackbar.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setAnimationStyleText() {
        switch (sAnimationStyle) {
            case NONE:
                mTextAnimationStyle.setText("None");
                break;
            case CUBE:
                mTextAnimationStyle.setText("Cube");
                break;
        }
    }

    public CustomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
