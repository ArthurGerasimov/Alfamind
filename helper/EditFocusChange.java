package id.meteor.alfamind.helper;

import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by bodacious on 24/1/18.
 */

public class EditFocusChange implements View.OnFocusChangeListener {
    private String TAG = getClass().getName();
    private View view ;
    private ScrollView scrollView;

    public EditFocusChange(View view , final ScrollView scrollView){
        this.view =  view;
        this.scrollView = scrollView;
        view.setTag(false);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        Log.e(TAG," Focus");
        //if(!(boolean)editText.getTag()){
            int x  = scrollView.getScrollX();
            int y  =  scrollView.getScrollY();
            scrollView.smoothScrollTo(0,(y+200));
            view.setTag(true);
       // }
    }
}
