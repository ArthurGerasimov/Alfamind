package id.meteor.alfamind.timer;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by bodacious on 6/1/18.
 */

public class IntipMarzinTimer extends CountDownTimer {

    Context context;
    ArrayList<ImageView> temp;



    public IntipMarzinTimer(long millisInFuture, long countDownInterval,Context context,ArrayList<ImageView> temp) {
        super(millisInFuture, countDownInterval);
        this.context =context;
        this.temp =temp ;
    }

    @Override
    public void onTick(long l) {

    }

    @Override
    public void onFinish() {


    }
}
