package id.meteor.alfamind.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;

public class Jaringan_Anggota_DetailActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaringan__anggota__detail);
        ImageView back_btn = findViewById(R.id.back_btn);

        TextView tx_name = findViewById(R.id.name);
        TextView tx_date = findViewById(R.id.date);
        TextView tx_phone = findViewById(R.id.phone);
        TextView tx_address = findViewById(R.id.address);
        TextView tx_email = findViewById(R.id.email);

        String name = getIntent().getStringExtra("name");
        String date = getIntent().getStringExtra("date");
        String address = getIntent().getStringExtra("address");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");

        tx_name.setText(name);
        tx_email.setText(email);
        tx_address.setText(address);
        tx_phone.setText(phone);
        tx_date.setText(date);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}
