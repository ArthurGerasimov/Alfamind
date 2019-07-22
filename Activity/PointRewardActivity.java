package id.meteor.alfamind.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import id.meteor.alfamind.helper.MyApplication;

public class PointRewardActivity extends BaseActivity {

    private TextView reward_point;
    private RelativeLayout lay_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_reward);

        LinearLayout mutasi_point = findViewById(R.id.mutasi_point);
        LinearLayout tukar_point = findViewById(R.id.tukar_point);
        ImageView back_btn = findViewById(R.id.back_btn);
        reward_point = findViewById(R.id.reward_point);
        lay_progressBar = findViewById(R.id.layout_progressBar);
        lay_progressBar.setVisibility(View.VISIBLE);

        mutasi_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PointRewardActivity.this, MultasiPointActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        tukar_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PointRewardActivity.this, TukarPointActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setRewardPoint();
    }

    private void setRewardPoint() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RewardPoint", response + "");
                try {
                    lay_progressBar.setVisibility(View.GONE);

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("user_data")) {
                            JSONObject object = jsonObject.getJSONObject("user_data");
                            if (object.has("point_reward")) {
                                String POINT = object.getString("point_reward");
                                MyApplication.getInstance().getPrefManager().setPoint(object.getString("point_reward"));
                                reward_point.setText(POINT + " Points");
                            }
                        }
                    } else {
                        showPopUp(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showPopUp("server error");
                    lay_progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lay_progressBar.setVisibility(View.GONE);
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
