package id.meteor.alfamind.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.WindowManager;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Fragment.IntroFirstFragment;
import id.meteor.alfamind.Fragment.IntroSecondFragment;
import id.meteor.alfamind.Fragment.IntroThirdFragment;
import id.meteor.alfamind.R;

public class BoardingActivity extends BaseActivity {

    private float x1;
    static public int fragNo = 1;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);
        setFragment(new IntroFirstFragment(), "RightToLeft");
    }

    protected void setFragment(Fragment fragment, String swipe) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        if (swipe.equals("RightToLeft")) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (swipe.equals("LeftToRight")) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        }
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        //Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
                        if (fragNo == 2) {
                            setFragment(new IntroFirstFragment(), "LeftToRight");
                            fragNo = 1;
                        } else if (fragNo == 3) {
                            setFragment(new IntroSecondFragment(), "LeftToRight");
                            fragNo = 2;
                        }
                    }

                    // Right to left swipe action
                    else {
                        //Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                        if (fragNo == 1) {
                            setFragment(new IntroSecondFragment(), "RightToLeft");
                            fragNo = 2;
                        } else if (fragNo == 2) {
                            setFragment(new IntroThirdFragment(), "RightToLeft");
                            fragNo = 3;
                        }
                    }

                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
