package id.meteor.alfamind.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.suke.widget.SwitchButton;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;

public class PengaturanActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);

        SwitchButton switchButton = findViewById(R.id.switch_button);
        ImageView back_btn = findViewById(R.id.back_btn);
        TextView atur_alamat = findViewById(R.id.atur_alamat);

        if (MyApplication.getInstance().getPrefManager().getIntipMargin())
            switchButton.setChecked(true);

        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                MyApplication.getInstance().getPrefManager().setIntipMargin(isChecked);
                Intent intent = new Intent("custom-event-name");
                intent.putExtra("message", "YES");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        });

        atur_alamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PengaturanActivity.this, Pengaturan_AturAlamatActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}
