package id.meteor.alfamind.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Fragment.ForgotPasswordFirstFragment;
import id.meteor.alfamind.Fragment.UpdatePasswordFragment;
import id.meteor.alfamind.R;

public class Forgot_PasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        boolean flag = getIntent().getBooleanExtra("flag", true);
        if (!flag) {
            UpdatePasswordFragment updatePasswordFragment =  new UpdatePasswordFragment();
            updatePasswordFragment.setActivity(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,updatePasswordFragment).commit();
        } else {
            ForgotPasswordFirstFragment fragment = new ForgotPasswordFirstFragment();
            fragment.setActivity(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, fragment).commit();
        }
    }

}
