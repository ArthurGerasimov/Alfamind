package id.meteor.alfamind.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Fragment.SaldoOneFragment;
import id.meteor.alfamind.Fragment.SaldoSecondFragment;
import id.meteor.alfamind.R;

public class SaldoWalletActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo_wallet);

        ImageView back_btn = findViewById(R.id.back_btn);
        TabLayout tabs_layout = findViewById(R.id.tabs_layout);
        ViewPager view_pager = findViewById(R.id.view_pager);
        tabs_layout.setupWithViewPager(view_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ArrayList<Fragment> fList = new ArrayList<>();

        SaldoOneFragment saldoOneFragment = new SaldoOneFragment();
        SaldoSecondFragment saldoSecondFragment = new SaldoSecondFragment();
        saldoOneFragment.setActivity(this);
        saldoSecondFragment.setActivity(this);

        fList.add(saldoOneFragment);
        fList.add(saldoSecondFragment);

        ArrayList<String> tabNameList = new ArrayList<>();
        tabNameList.add("SALDO ALFAMIND");
        tabNameList.add("E-WALLET");

        adapter.addFragmentList(fList, tabNameList);
        view_pager.setAdapter(adapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragmentList(List<Fragment> fragment, List<String> title) {
            mFragmentList = fragment;
            mFragmentTitleList = title;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void showSnackBar() {
        LinearLayout main_parent_layout = findViewById(R.id.main_layout);
        Snackbar snackbar = Snackbar.make(main_parent_layout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
