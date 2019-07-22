package id.meteor.alfamind.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class PembayaranActivity extends BaseActivity {

    private TextView balance;
    private String plu = "", type = "";
    private RelativeLayout layout_progressBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        ImageView back_btn = findViewById(R.id.back_btn);
        TextView next = findViewById(R.id.next);
        TextView txt = findViewById(R.id.txt);
        balance = findViewById(R.id.balance);
        TextView total_amount = findViewById(R.id.total_amount);
        layout_progressBar = findViewById(R.id.layout_progressBar);
        setDataRp();

        if (getIntent().hasExtra("BALANCE")) {
            total_amount.setText("Rp " + getFormat(getIntent().getStringExtra("BALANCE")));
            plu = getIntent().getStringExtra("PLU");
            type = getIntent().getStringExtra("TYPE");
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (type.equalsIgnoreCase("DATA"))
            txt.setText("Total harga top-up paket data");


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress("Please wait...", false);
                buyBalance();
            }
        });

    }

    private void setDataRp() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_USER_BALANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RewardPoint", response + "");
                try {
                    layout_progressBar.setVisibility(View.GONE);

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("balance")) {
                            JSONObject object = jsonObject.getJSONObject("balance");

                            if (object.has("current_balance")) {
                                balance.setText("Rp " + getFormat(object.getString("current_balance") + ""));
                            }
                        }

                    } else {
                        showPopUp(jsonObject.getString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    showPopUp("server error");
                    layout_progressBar.setVisibility(View.GONE);
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                layout_progressBar.setVisibility(View.GONE);
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                String token = MyApplication.getInstance().getPrefManager().getAccessToken();
                Log.e("ACCESS_TOKEN", token + "");
                param.put("access_token", token + "");
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void buyBalance() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.PULSA_BUY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PULSA_BUY", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            if (jsonObject.has("confirm")) {
                                closeProgress();
                                Intent intent = new Intent(MyApplication.getInstance(), SaldoWebViewActivity.class);
                                intent.putExtra("DATA", "PEMBAYARAN");
                                intent.putExtra("VALUE", jsonObject.getString("confirm") + "");
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            }
                        } else {
                            closeProgress();
                            getErrorDialog("Something went wrong");
                        }
                    }
                } catch (Exception e) {
                    closeProgress();
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                getErrorDialog("Something went wrong");
                Log.d("VollyError", " something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                String access_token = MyApplication.getInstance().getPrefManager().getAccessToken();
                String NomorHP = MyApplication.getInstance().getPrefManager().getNomorHP();
                Log.d("UPDATEPARAM", access_token + "        " + plu + "        " + NomorHP);
                param.put("access_token", access_token + "");
                param.put("phone_number", NomorHP + "");
                param.put("plu", plu + "");
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public String getFormat(String str) {
        str = str.replaceAll("\\.", "");
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.reverse();
        char arr[] = stringBuffer.toString().toCharArray();
        StringBuilder NUM = new StringBuilder();
        int count = 0;
        for (char anArr : arr) {
            ++count;
            if (count == 4) {
                count = 1;
                NUM.append(".").append(anArr);
            } else {
                NUM.append("").append(anArr);
            }
        }
        stringBuffer = new StringBuffer(NUM.toString());
        stringBuffer.reverse();

        return String.valueOf(stringBuffer);
    }

}
