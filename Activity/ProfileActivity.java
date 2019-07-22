package id.meteor.alfamind.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Fragment.ProfileFragment;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle bundle = new Bundle();

        if (getIntent().hasExtra("OPEN")){
            if (getIntent().getStringExtra("OPEN").equals("EDITPROFILE")){
                bundle.putString("OPEN","EDITPROFILE");
            }
        }else {
            bundle.putString("OPEN","PROFILE");
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        transaction.replace(R.id.profile_container, profileFragment).commit();
    }

    @Override
    public void onResume() {
        if (!MyApplication.getInstance().getPrefManager().getIsLogged())
            finish();
        super.onResume();
    }

}
