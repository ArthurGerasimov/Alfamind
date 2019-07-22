package id.meteor.alfamind.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import id.meteor.alfamind.Activity.SaldoWalletActivity;
import id.meteor.alfamind.Activity.SaldoWebViewActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;

public class SaldoOneFragment extends Fragment {


    private SaldoWalletActivity saldoWalletActivity;
    private TextView rp_amount, rp_active_until;
    private RelativeLayout layout_progressBar;
    private String current_balance, active_until;

    public SaldoOneFragment() {
        // Required empty public constructor
    }

    public void setActivity(SaldoWalletActivity saldoWalletActivity) {
        this.saldoWalletActivity = saldoWalletActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saldo_one, container, false);
        View layout = view.findViewById(R.id.lay1);
        View layout2 = view.findViewById(R.id.lay2);
        rp_amount = view.findViewById(R.id.rp_amount);
        layout_progressBar = view.findViewById(R.id.layout_progressBar);
        rp_active_until = view.findViewById(R.id.rp_active_until);

        setDataRp();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.isNetworkAvailable(getActivity())) {
                    Intent i = new Intent(getActivity(), SaldoWebViewActivity.class);
                    i.putExtra("DATA", "MUTASI SALADO");
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SaldoWebViewActivity.class);
                i.putExtra("DATA", "TOP-UP SALADO VIA DOKU");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        return view;
    }

    private void setDataRp() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_USER_BALANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    layout_progressBar.setVisibility(View.GONE);

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")){
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            if (jsonObject.has("balance")) {
                                JSONObject object = jsonObject.getJSONObject("balance");

                                // if (object.has("current_balance_human"));
                                // if (object.has("maximum_balance")) ;

                                if (object.has("current_balance")) {
                                    current_balance = object.getString("current_balance");
                                    rp_amount.setText("Rp " + getFormat(current_balance + ""));
                                }
                                if (object.has("active_until")) {
                                    active_until = object.getString("active_until");
                                    rp_active_until.setText("Topup terakhir tanggal " + active_until);
                                }
                            }

                        } else {
                            saldoWalletActivity.showPopUp(jsonObject.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    saldoWalletActivity.showPopUp("server error");
                    layout_progressBar.setVisibility(View.GONE);
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                saldoWalletActivity.showPopUp("something went wrong");
                layout_progressBar.setVisibility(View.GONE);
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

    public String getFormat(String str) {
        str = str.replaceAll("\\.", "");
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.reverse();
        char arr[] = stringBuffer.toString().toCharArray();
        StringBuilder NUM = new StringBuilder();
        int count = 0;
        for (int j = 0; j < arr.length; j++) {
            ++count;
            if (count == 4) {
                count = 1;
                NUM.append(".").append(arr[j]);
                Log.e("HELLO-WORD", NUM + " " + j + " count=" + count + " " + arr[arr.length - (j + 1)]);
            } else {
                NUM.append("").append(arr[j]);
                Log.e("HELLO-WORD", NUM + " " + j + " count=" + count + " " + arr[arr.length - (j + 1)]);
            }
        }
        stringBuffer = new StringBuffer(NUM.toString());
        stringBuffer.reverse();

        return String.valueOf(stringBuffer);
    }
}
