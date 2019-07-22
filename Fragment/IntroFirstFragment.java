package id.meteor.alfamind.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import id.meteor.alfamind.Activity.BoardingActivity;
import id.meteor.alfamind.Base.BaseFragment;
import id.meteor.alfamind.R;

public class IntroFirstFragment extends BaseFragment {

    ImageView rigthBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intro_first, container, false);
        rigthBtn = view.findViewById(R.id.rightBtn);
        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BoardingActivity.fragNo = 2;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                fragmentTransaction.replace(R.id.frameLayout, new IntroSecondFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
