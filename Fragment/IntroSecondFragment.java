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

public class IntroSecondFragment extends BaseFragment {

    ImageView rigthBtn, leftBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intro_second, container, false);

        leftBtn = view.findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BoardingActivity.fragNo = 1;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
                fragmentTransaction.replace(R.id.frameLayout, new IntroFirstFragment());
                fragmentTransaction.commit();
            }
        });


        rigthBtn = view.findViewById(R.id.rightBtn);
        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BoardingActivity.fragNo = 3;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                fragmentTransaction.replace(R.id.frameLayout, new IntroThirdFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
