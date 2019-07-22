package id.meteor.alfamind.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;

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
import id.meteor.alfamind.helper.MyPreferenceManager;

public class SplashActivity extends BaseActivity {

    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();

        assert receivedAction != null;
        if (receivedAction.equals(Intent.ACTION_VIEW)) {
            URL = receivedIntent.getDataString();
        }

        if (MyApplication.getInstance().getPrefManager().getIsLogged()) {
            loginToUser();
        }

        new CountDownTimer(2000, 1) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                gotoIntro();
            }
        }.start();
    }

    private void gotoIntro() {
        if (MyApplication.getInstance().getPrefManager().getIsFirstTime()) {
            MyApplication.getInstance().getPrefManager().setIsFirstTime(false);
            Intent intent = new Intent(SplashActivity.this, BoardingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            finish();
        } else {
            MyApplication.getInstance().getPrefManager().setIsFirstTime(false);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);

            if (URL != null && !URL.equalsIgnoreCase("null") && !URL.equalsIgnoreCase(""))
                intent.putExtra("URL", URL + "");

            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            finish();
        }
    }


    private void loginToUser() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.USER_AUTHENTICATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        MyApplication.getInstance().getPrefManager().setIsLogged(true);
                        if (jsonObject.has("access_token")) {
                            MyApplication.getInstance().getPrefManager().setAccessToken(jsonObject.getString("access_token"));
                            getUserInfo();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VollyError", "something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email", MyApplication.getInstance().getPrefManager().getEmail() + "");
                param.put("passwd", MyApplication.getInstance().getPrefManager().getPassword() + "");
                param.put("device_id", "na");

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getUserInfo() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LOGINTOUSER", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("user_data")) {
                            JSONObject object = jsonObject.getJSONObject("user_data");
                            if (object.has("avatar")) {
                                manager.setImage(object.getString("avatar"));
                            }
                            if (object.has("firstname")) {
                                manager.setFirstName(object.getString("firstname"));
                            }
                            if (object.has("lastname")) {
                                manager.setLastName(object.getString("lastname"));
                            }
                            if (object.has("store_name")) {
                                manager.setShopName(object.getString("store_name"));
                            }
                            if (object.has("id_customer")) {
                                manager.setCoustmerId(object.getString("id_customer"));
                            }
                            if (object.has("id_customer_ora")) {
                                manager.setCoustmerIdOra(object.getString("id_customer_ora"));
                            }
                            if (object.has("point_reward")) {
                                manager.setPoint(object.getString("point_reward"));
                            }
                            if (object.has("email")) {
                                manager.setEmail(object.getString("email"));
                            }
                            if (object.has("wallet_number")) {
                                manager.setWalletNo(object.getString("wallet_number"));
                            }
                            if (object.has("dob")) {
                                manager.setDOB(object.getString("dob"));
                            }
                            if (object.has("address")) {
                                manager.setAddress(object.getString("address"));
                            }
                            if (object.has("city")) {
                                manager.setCity(object.getString("city"));
                            }
                            if (object.has("phone_mobile")) {
                                manager.setPhoneNumber(object.getString("phone_mobile"));
                            }
                            if (object.has("postcode")) {
                                manager.setZipCode(object.getString("postcode"));
                            }
                            if (object.has("member_since")) {
                            }
                            if (object.has("gender")) {
                                manager.setGender(object.getString("gender"));
                            }
                            if (object.has("pic_lv1_province")) {
                                Log.e("DAADTAD", "FOUNT321321586");

                                JSONObject pObject = object.getJSONObject("pic_lv1_province");
                                if (pObject.has("id")) {
                                    Log.e("DAADTAD", "ENETER");

                                    String pid1 = pObject.getString("id");
                                    if (pid1 != null) {
                                        Log.e("DAADTAD", "FOUNTID");
                                        //manager.setPid1(Integer.parseInt(pid1));
                                    }
                                }
                                if (pObject.has("label")) {
                                    Log.e("DAADTAD", "" + pObject.getString("label"));
                                    manager.setState(pObject.getString("label"));
                                }
                            }

                            if (object.has("pic_lv2_kabupaten")) {
                                JSONObject pObject = object.getJSONObject("pic_lv2_kabupaten");
                                if (pObject.has("id")) {

                                    String pid2 = pObject.getString("id");
                                    if (pid2 != null) {
                                        // manager.setPid2(Integer.parseInt(pid2));
                                    }
                                }
                                if (pObject.has("label")) {
                                    manager.setDistrict(pObject.getString("label"));
                                }
                            }

                            if (object.has("pic_lv3_kecamatan")) {

                                JSONObject pObject = object.getJSONObject("pic_lv3_kecamatan");
                                if (pObject.has("id")) {
                                    String pid3 = pObject.getString("id");
                                }
                                if (pObject.has("label")) {

                                    manager.setkecamatan(pObject.getString("label"));
                                }
                            }
                            if(object.has("bank_account_number")){
                                manager.setBankAccountNumber(object.getString("bank_account_number"));
                            }
                            if(object.has("bank_account")){
                                if (object.getString("bank_account").equalsIgnoreCase("none"))
                                    manager.setBankName("");
                                    else
                                        manager.setBankName(object.getString("bank_account"));
                            }
                            if(object.has("bank_account_name")){
                                manager.setBankOwnerName(object.getString("bank_account_name"));
                            }
                        }

                    } else {
                        showAlertDialog(jsonObject.getString("message"), false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getErrorDialog("server error");
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                String token = MyApplication.getInstance().getPrefManager().getAccessToken();
                param.put("access_token", token + "");
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

}
