package id.meteor.alfamind.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;

public class Pembelian_TopUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian__top_up);

        MyApplication.getInstance().getPrefManager().setNomorHP(null);

        ImageView back_btn = findViewById(R.id.back_btn);
        TextView pulsa = findViewById(R.id.pulsa);
        TextView poket_data = findViewById(R.id.poket_data);
        TextView token_pln = findViewById(R.id.token_pln);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Pembelian_TopUpActivity.this, Pembelian_PulsaActivity.class);
                i.putExtra("TITLE", "Pembelian Pulsa");
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        poket_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Pembelian_TopUpActivity.this, Pembelian_PulsaActivity.class);
                i.putExtra("TITLE", "Pembelian Paket Data");
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        token_pln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}


