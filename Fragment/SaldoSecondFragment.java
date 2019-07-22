package id.meteor.alfamind.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import id.meteor.alfamind.Activity.SaldoWalletActivity;
import id.meteor.alfamind.Activity.SaldoWebViewActivity;
import id.meteor.alfamind.Activity.TarikViaAlfamartActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaldoSecondFragment extends Fragment {

    private Button button;
    private TextView e_wallet;
    private ImageView rp;
    private boolean processKyc;
    private String state = "";
    private final int REQUEST_CODE = 100;
    private SaldoWalletActivity saldoWalletActivity;

    public SaldoSecondFragment() {
        // Required empty public constructor
    }

    public void setActivity(SaldoWalletActivity saldoWalletActivity) {
        this.saldoWalletActivity = saldoWalletActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saldo_second, container, false);

        button = view.findViewById(R.id.button);
        e_wallet = view.findViewById(R.id.e_wallet);
        rp = view.findViewById(R.id.rp);

        getWalletDetail();

        e_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equalsIgnoreCase("4")) {
                    saldoWalletActivity.showPopUp("Permintaan KYC Anda sedang diproses");
                } else {
                    Intent i = new Intent(getActivity(), SaldoWebViewActivity.class);
                    i.putExtra("DATA", "AKTIVASI E-WALLET");
                    startActivityForResult(i, REQUEST_CODE);
                    getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TarikViaAlfamartActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        return view;
    }

    private void getWalletDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_EWALLET_DETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LOGINTOUSER", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("output")) {
                            JSONObject object = jsonObject.getJSONObject("output");

                            if (object.has("customer")) {
                                JSONObject object1 = object.getJSONObject("customer");

                                if (object1.has("processKyc")) {
                                    Log.e("DATA", "" + object1.getBoolean("processKyc") + "------------" + object1.getString("state"));
                                    processKyc = object1.getBoolean("processKyc");
                                }
                                if (object1.has("state")) {
                                    state = object1.getString("state");
                                }
                                if (getActivity() == null)
                                    return;
                                setData();
                            }
                        }

                    } else {
                        saldoWalletActivity.showPopUp(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    saldoWalletActivity.showPopUp("server error");
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                saldoWalletActivity.getErrorDialog("Something went wrong");
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

    @SuppressLint("ResourceAsColor")
    private void setData() {
        if (processKyc) {
            switch (state) {
                case "4":
                    e_wallet.setEnabled(true);
                    button.setEnabled(false);
                    break;
            }

        } else {
            switch (state) {
                case "1":
                case "2":
                case "3":
                    e_wallet.setEnabled(true);
                    button.setEnabled(false);
                    break;
                case "5":
                    e_wallet.setVisibility(View.GONE);
                    rp.setVisibility(View.GONE);
                    button.setEnabled(true);
                    button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            getWalletDetail();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
