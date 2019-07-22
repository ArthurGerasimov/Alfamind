package id.meteor.alfamind.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import id.meteor.alfamind.Activity.BoardingActivity;
import id.meteor.alfamind.Activity.MainActivity;
import id.meteor.alfamind.Base.BaseFragment;
import id.meteor.alfamind.R;

public class IntroThirdFragment extends BaseFragment {

    ImageView leftBtn;
    Button button;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_third, container, false);

        button = view.findViewById(R.id.button);
        leftBtn = view.findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BoardingActivity.fragNo=2;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
                fragmentTransaction.replace(R.id.frameLayout, new IntroSecondFragment());
                fragmentTransaction.commit();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                getActivity().finish();
            }
        });
        return view;
    }


}

