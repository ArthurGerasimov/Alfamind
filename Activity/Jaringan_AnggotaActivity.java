package id.meteor.alfamind.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Adapter.AnggotaAdapter;
import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Model.DownlinksModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class Jaringan_AnggotaActivity extends BaseActivity {

    private RelativeLayout lay_progressBar;
    private ArrayList<DownlinksModel> list = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaringan__anggota);

        listView = findViewById(R.id.listView);
        lay_progressBar = findViewById(R.id.layout_progressBar);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        getAnggotaDetails();

    }

    public void getAnggotaDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_USER_DOWNLINKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RewardHISTORY", response + "");
                try {
                    lay_progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("downlinks")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("downlinks");

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    DownlinksModel downlinksModel = new DownlinksModel();

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    if (object.has("id_customer_ora")){
                                        downlinksModel.setId_customer_ora(object.getString("id_customer_ora"));
                                    }
                                    if (object.has("avatar")){
                                        downlinksModel.setAvatar(object.getString("avatar"));
                                    }
                                    if (object.has("name")) {
                                        downlinksModel.setName(object.getString("name"));
                                    }
                                    if (object.has("email")) {
                                        downlinksModel.setEmail(object.getString("email"));
                                    }
                                    if (object.has("status")) {
                                        downlinksModel.setStatus(object.getString("status"));
                                    }
                                    if (object.has("member_since")) {
                                        downlinksModel.setMember_since(object.getString("member_since"));
                                    }
                                    if (object.has("phone_mobile")) {
                                        downlinksModel.setPhone_mobile(object.getString("phone_mobile"));
                                    }
                                    if (object.has("address")) {
                                        downlinksModel.setAddress(object.getString("address"));
                                    }
                                    list.add(downlinksModel);
                                }
                                listView.setAdapter(new AnggotaAdapter(Jaringan_AnggotaActivity.this,list));
                            } else {
                                findViewById(R.id.no_data).setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        showPopUp(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showPopUp("server error");
                    lay_progressBar.setVisibility(View.GONE);
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lay_progressBar.setVisibility(View.GONE);
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



}
