package id.meteor.alfamind.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class Bantuan_KontakLayananActivity extends BaseActivity {

    private EditText title,message;
    private LinearLayout lay_feedback;
    private TextView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan__kontak_layanan);
        lay_feedback = findViewById(R.id.lay_feedback);
        ImageView back_btn = findViewById(R.id.back_btn);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        next = findViewById(R.id.next);

        if (getIntent().getStringExtra("DATA").equals("LOGIN")){
            lay_feedback.setVisibility(View.GONE);
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation())
                sendFeedback();
            }
        });
    }

    public boolean validation() {
        if (TextUtils.isEmpty(title.getText().toString())) {
            getErrorDialog("Masukkan Judul Pesan");
            return false;
        }
        if (TextUtils.isEmpty(message.getText().toString())) {
            getErrorDialog("Masukkan Isi Pesan");
            return false;
        }
        if ((message.getText().toString().length())<25) {
            getErrorDialog("Message harus memiliki minimal jumlah 25 karakter.");
            return false;
        }
        return true;
    }

    private void sendFeedback(){

        showProgress("Please wait...", false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.CONTACT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CONTACT_USResponse", response + "");
                closeProgress();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.has("status")){
                        if (jsonObject.getString("status").equals("success")){
                            startActivity(new Intent(Bantuan_KontakLayananActivity.this, Bantuan_EmailBerhasilActivity.class));
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            finish();
                        }else {
                            showAlertDialog(jsonObject.getString("message"),true);
                        }
                    }
                }catch (Exception e){
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String token = MyApplication.getInstance().getPrefManager().getAccessToken();

                Log.d("SEND_DATA",token+"   "+title.getText().toString()+">"+message.getText().toString());

                param.put("access_token", token);
                param.put("subject", title.getText().toString());
                param.put("message", message.getText().toString());
                return param;
            }
        };;
        requestQueue.add(stringRequest);
    }

    /*private void getShop_owner_guide() {
        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.SHOP_OWNER_GUIDE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BankResponse", response + "");
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getString("title").equalsIgnoreCase(activityStart)) {
                            //textView.setText(jsonObject.getString("content"));
                            layout_progressBar.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
        });
        requestQueue.add(stringRequest);
    }*/

}
