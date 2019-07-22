package id.meteor.alfamind.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;

public class ForgotPasswordSecondFragment extends Fragment {

    public ForgotPasswordSecondFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forgot_password_second, container, false);
        TextView next = view.findViewById(R.id.next);
        TextView text = view.findViewById(R.id.text);
        ImageView back_btn = view.findViewById(R.id.back_btn);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getPrefManager().setIsLogged(false);
                getActivity().finish();
            }
        });

        if (getArguments() != null) {
            boolean flag = getArguments().getBoolean("isFlag", true);
            if (!flag)
                text.setText("Kembali ke Profil");
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

            }
        });
        return view;
    }
}
