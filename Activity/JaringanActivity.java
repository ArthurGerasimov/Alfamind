package id.meteor.alfamind.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class JaringanActivity extends BaseActivity {

    private boolean bankStatusFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaringan);

        TextView anggota = findViewById(R.id.anggota);
        ImageView back_btn = findViewById(R.id.back_btn);
        TextView performa = findViewById(R.id.performa);
        TextView pendaftaran = findViewById(R.id.pendaftaran);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        anggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JaringanActivity.this, Jaringan_AnggotaActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        performa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JaringanActivity.this, Jaringan_PerformaActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        pendaftaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!bankStatusFlag) {
                    showDialog();
                } else {
                    Intent intent = new Intent(JaringanActivity.this, SignupActivity.class);
                    intent.putExtra("DATA", "NEW_NETWORK");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            }
        });

        getBankStatus();
    }

    @SuppressLint("StaticFieldLeak")
    private void showDialog() {
        Log.d("Dialog", "Alert");
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Untuk melanjutkan, mohon melengkapi data akun bank Anda.");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("LENGKAPI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(JaringanActivity.this, ProfileActivity.class);
                intent.putExtra("OPEN","EDITPROFILE");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        dialogBuilder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
    }

    private void getBankStatus() {
        showProgress("Please wait...", true);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BANK_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BANK_STATUS_Response", response + "");
                closeProgress();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            if (jsonObject.has("ek_bank")) {
                                bankStatusFlag = jsonObject.getString("ek_bank").equalsIgnoreCase("valid");
                            }
                            if (jsonObject.has("edited")) {
                                bankStatusFlag = jsonObject.getString("edited").equalsIgnoreCase("1");
                            }
                        } else {
                            getErrorDialog(jsonObject.getString("message"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    closeProgress();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                String token = MyApplication.getInstance().getPrefManager().getAccessToken();
                Log.d("access_token", token);
                param.put("access_token", token);
                return param;
            }
        };
        ;
        requestQueue.add(stringRequest);
    }

}
