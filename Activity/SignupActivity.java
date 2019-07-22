package id.meteor.alfamind.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Fragment.SignupFirstFragment;
import id.meteor.alfamind.Fragment.SignupSecondFragment;
import id.meteor.alfamind.Fragment.SignupThirdFragment;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;

public class SignupActivity extends BaseActivity {

    public static boolean isRegister = false;
    public TextView title;
    public ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        title = findViewById(R.id.title);
        back = findViewById(R.id.back_btn);

        if (!MyApplication.getInstance().getSignupManager().getSignUpData().equalsIgnoreCase(getIntent().getStringExtra("DATA") + "")) {
            Log.e("SHAREDPRECLEAR", "YES");
            MyApplication.getInstance().getSignupManager().getEditor().clear().apply();
        }

        String DATA = getIntent().getStringExtra("DATA");
        MyApplication.getInstance().getSignupManager().setSignUpData(DATA + "");
        Log.e("DAATATATA", getIntent().getStringExtra("DATA") + "");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("DATA", DATA);
        SignupFirstFragment signupFirstFragment = new SignupFirstFragment();
        signupFirstFragment.setArguments(bundle);
        signupFirstFragment.setActivity(this);
        transaction.replace(R.id.frame, signupFirstFragment, "one");
        transaction.commit();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupFirstFragment myFragment = (SignupFirstFragment) getSupportFragmentManager().findFragmentByTag("one");
                if (myFragment != null && myFragment.isVisible()) {
                    myFragment.saveValueInSharedPrefence();
                }

                SignupSecondFragment myFragment2 = (SignupSecondFragment) getSupportFragmentManager().findFragmentByTag("two");
                if (myFragment2 != null && myFragment2.isVisible()) {
                    myFragment2.saveValueInSharedPrefence();
                }

                SignupThirdFragment myFragment3 = (SignupThirdFragment) getSupportFragmentManager().findFragmentByTag("three");
                if (myFragment3 != null && myFragment3.isVisible()) {
                    myFragment3.saveValueInSharedPrefence();
                }
                onBackPressed();
            }
        });
    }


    @Override
    protected void onResume() {

        if (isRegister) {
            MyApplication.getInstance().getSignupManager().getEditor().clear().apply();
            isRegister = false;
        }
        super.onResume();
    }


}