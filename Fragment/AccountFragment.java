package id.meteor.alfamind.Fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.meteor.alfamind.Activity.BantuanActivity;
import id.meteor.alfamind.Activity.JaringanActivity;
import id.meteor.alfamind.Activity.MainActivity;
import id.meteor.alfamind.Activity.PengaturanActivity;
import id.meteor.alfamind.Activity.PointRewardActivity;
import id.meteor.alfamind.Activity.ProfileActivity;
import id.meteor.alfamind.Activity.ReturnActivity;
import id.meteor.alfamind.Activity.SaldoWalletActivity;
import id.meteor.alfamind.Activity.SaldoWebViewActivity;
import id.meteor.alfamind.Base.BaseFragment;
import id.meteor.alfamind.Interface.FragmentListener;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;

public class AccountFragment extends BaseFragment implements FragmentListener {

    private View view;
    private TextView logout;

    public FragmentListener getFragmentListener(){
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        if (!MyApplication.getInstance().getPrefManager().getIsLogged()) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, new LoginFragment()).commit();
        } else {
            loadLayout();
        }

        return view;
    }

    @Override
    public void onResume() {
        if (!MyApplication.getInstance().getPrefManager().getIsLogged()) {
            logout.performClick();
        }
        super.onResume();
    }

    private void loadLayout() {
        logout = view.findViewById(R.id.logout);
        TextView profile = view.findViewById(R.id.profile);
        TextView saldo_e_wallet = view.findViewById(R.id.saldo_e_wallet);
        TextView history_transaksi = view.findViewById(R.id.history_transaksi);
        TextView point_reward = view.findViewById(R.id.point_reward);
        TextView jaringan = view.findViewById(R.id.jaringan);
        TextView bantuan = view.findViewById(R.id.bantuan);
        TextView pengaturan = view.findViewById(R.id.pengaturan);
        TextView retur = view.findViewById(R.id.retur);
        TextView app_version = view.findViewById(R.id.app_version);

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            int versionCode = pInfo.versionCode;
            app_version.setText("ver " + version);
            Log.e("VERSION", "" + version + "------------" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getPrefManager().setIsLogged(false);
                MyApplication.getInstance().getPrefManager().setAccessToken(null);
                MyApplication.getInstance().getPrefManager().setCartId(null);
                MyApplication.getInstance().getPrefManager().setImagePath(null);
                MyApplication.getInstance().getPrefManager().setIntipMargin(false);
                Intent intent = new Intent("UPDATE_COUNT");
                LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(intent);
                MainActivity m = (MainActivity) getActivity();
                m.stopTimer();
                ((MainActivity) getActivity()).getNotification();
                Intent intent1 = new Intent("custom-event-name");
                intent.putExtra("message", "YES");
                LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(intent1);
                LoginFragment fragment = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment).commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        saldo_e_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SaldoWalletActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        history_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getInstance(), SaldoWebViewActivity.class);
                intent.putExtra("DATA", "HISTORY");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        point_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getInstance(), PointRewardActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        jaringan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getInstance(), JaringanActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getInstance(), BantuanActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getInstance(), PengaturanActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getInstance(), ReturnActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    @Override
    public void onBackPress() {
        ((MainActivity)getActivity()).getShoppingFragment();
    }
}
