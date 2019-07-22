package id.meteor.alfamind.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;

public class SortActivity extends BaseActivity {

    private String radioButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        String TITLE = getIntent().getStringExtra("DATA");

        TextView title = findViewById(R.id.title);
        ImageView back_btn = findViewById(R.id.back_btn);
        TextView next = findViewById(R.id.next);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        RadioButton r1 = findViewById(R.id.r1);
        RadioButton r2 = findViewById(R.id.r2);
        RadioButton r3 = findViewById(R.id.r3);
        RadioButton r4 = findViewById(R.id.r4);
        RadioButton r5 = findViewById(R.id.r5);
        RadioButton r6 = findViewById(R.id.r6);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Reg.otf");
        r1.setTypeface(font);
        r2.setTypeface(font);
        r3.setTypeface(font);
        r4.setTypeface(font);
        r5.setTypeface(font);
        r6.setTypeface(font);

        title.setText(TITLE);

        if (!TITLE.equalsIgnoreCase("Sortir Belanjaan")) {
            r1.setText(R.string.Points_Terkecil);
            r2.setText(R.string.Points_Terbesar);
            r5.setVisibility(View.GONE);
            r6.setVisibility(View.GONE);
            if (TukarPointActivity.sort_data != null && !TukarPointActivity.sort_data.equalsIgnoreCase("null") && !TukarPointActivity.sort_data.equalsIgnoreCase(""))
                switch (TukarPointActivity.sort_data) {
                    case "1":
                        r1.setChecked(true);
                        break;
                    case "2":
                        r2.setChecked(true);
                        break;
                    case "3":
                        r3.setChecked(true);
                        break;
                    case "4":
                        r4.setChecked(true);
                        break;
                }
        } else {
            if (AllProductActivity.sort != null && !AllProductActivity.sort.equalsIgnoreCase("null") && !AllProductActivity.sort.equalsIgnoreCase(""))
                switch (AllProductActivity.sort) {
                    case "1":
                        r1.setChecked(true);
                        break;
                    case "2":
                        r2.setChecked(true);
                        break;
                    case "3":
                        r3.setChecked(true);
                        break;
                    case "4":
                        r4.setChecked(true);
                        break;
                    case "5":
                        r5.setChecked(true);
                        break;
                    case "6":
                        r6.setChecked(true);
                        break;
                }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButton = radioGroup.getCheckedRadioButtonId();
                Log.e("IDDDDDDD", radioButton + "");
                RadioButton button = findViewById(radioButton);
                switch (button.getText().toString()) {
                    case "Harga Termurah":
                        radioButtonId = 1 + "";
                        break;
                    case "Harga Termahal":
                        radioButtonId = 2 + "";
                        break;
                    case "Points Terkecil":
                        radioButtonId = 1 + "";
                        break;
                    case "Points Terbesar":
                        radioButtonId = 2 + "";
                        break;
                    case "A - Z":
                        radioButtonId = 3 + "";
                        break;
                    case "Z - A":
                        radioButtonId = 4 + "";
                        break;
                    case "Stok Terbanyak":
                        radioButtonId = 5 + "";
                        break;
                    case "Stok Tersedikit":
                        radioButtonId = 6 + "";
                        break;
                }
                Log.e("IDDDDDDD", button.getText().toString());
            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioButtonId != null && !radioButtonId.equals("null") && !radioButtonId.equals("")) {
                    Intent intent = new Intent();
                    intent.putExtra("SORT_DATA", radioButtonId + "");
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }
}
