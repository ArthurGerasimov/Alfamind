package id.meteor.alfamind.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetDialog;
import id.meteor.alfamind.customDialog.OnOperItemClickL;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class Jaringan_PerformaActivity extends AppCompatActivity {

    private RelativeLayout lay_progressBar;
    private LinearLayout history_data;
    private LayoutInflater inflate;
    private EditText et_month;
    private TextView total_net_sales;
    private String thisYear, thisMonth, group_margin = "", name = "", net_sales = "";
    private String lastSevenMonth[];

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaringan__performa);
        inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        history_data = findViewById(R.id.history_data);
        lay_progressBar = findViewById(R.id.layout_progressBar);
        et_month = findViewById(R.id.et_month);
        total_net_sales = findViewById(R.id.total_net_sales);

        Calendar calendar = Calendar.getInstance();
        thisYear = calendar.get(Calendar.YEAR) + "";
        thisMonth = (calendar.get(Calendar.MONTH) + 1) + "";

        et_month.setText(getThisMonth(thisMonth) + thisYear);

        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        et_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthSelection(et_month);
            }
        });

        getJaringanPerforma();
        lastSixMonth(thisMonth, thisYear);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void monthSelection(final EditText et) {

        final ActionSheetDialog dialog = new ActionSheetDialog(this, lastSevenMonth, null);
        dialog.title("Lihat daftar bulan");
        dialog.cancelText("Membatalkan").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                et.setText(lastSevenMonth[position]);
                String[] divide = lastSevenMonth[position].split(" ");
                thisYear = divide[1];
                switch (divide[0]) {
                    case "Januari":
                        thisMonth = "01";
                        break;
                    case "Februari":
                        thisMonth = "02";
                        break;
                    case "Maret":
                        thisMonth = "03";
                        break;
                    case "April":
                        thisMonth = "04";
                        break;
                    case "Mei":
                        thisMonth = "05";
                        break;
                    case "Juni":
                        thisMonth = "06";
                        break;
                    case "Juli":
                        thisMonth = "07";
                        break;
                    case "Agustus":
                        thisMonth = "08";
                        break;
                    case "September":
                        thisMonth = "09";
                        break;
                    case "Oktober":
                        thisMonth = "10";
                        break;
                    case "November":
                        thisMonth = "11";
                        break;
                    case "Desember":
                        thisMonth = "12";
                        break;
                }
                dialog.dismiss();
                lay_progressBar.setVisibility(View.VISIBLE);
                getJaringanPerforma();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void getJaringanPerforma() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_USER_NETWORK_PERFORMANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RewardHISTORY", response + "");
                try {
                    history_data.removeAllViews();
                    lay_progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("performance")) {

                            JSONObject object = jsonObject.getJSONObject("performance");
                            // if (object.has("downlink_performance"));

                            if (object.has("group_margin")) {
                                group_margin = object.getString("group_margin");
                            }

                            if (object.has("downlink_performance_list")) {

                                JSONArray jsonArray = object.getJSONArray("downlink_performance_list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);

                                    if (object1.has("name")) {
                                        name = object1.getString("name");
                                    }

                                    if (object1.has("net_sales")) {
                                        net_sales = object1.getString("net_sales");
                                    }

                                    // if (object1.has("net_sales_margin"));

                                    View view = inflate.inflate(R.layout.performa_jaringan_history, null);
                                    TextView NAME = view.findViewById(R.id.name);
                                    TextView NET_SALES = view.findViewById(R.id.net_sales);
                                    TextView NET_SALES_MARGIN = view.findViewById(R.id.net_sales_margin);

                                    NAME.setText(name);
                                    NET_SALES.setText("Net Sales Rp. " + getFormat(net_sales));
                                    NET_SALES_MARGIN.setText("Rewards 0 Points");
                                    history_data.addView(view);
                                    Log.e("YES", "HERE");
                                }
                            }
                        }
                        total_net_sales.setText(group_margin);


                    } else {
                        showAlertDialog(jsonObject.getString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    showAlertDialog("server error");
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
                Log.e("thisYear", thisYear + "");
                Log.e("thisMonth", thisMonth + "");
                param.put("access_token", token + "");
                param.put("year", thisYear);
                param.put("month", thisMonth);

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void showAlertDialog(final String msg) {

        if (!isFinishing()) {
            Log.d("Dialog", "Alert");
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage(msg);
            dialogBuilder.setCancelable(true);
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = dialogBuilder.create();
            alert.show();
        }
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
        Log.e("RESULT-DONE", String.valueOf(stringBuffer));

        Log.e("RESULT*", NUM.toString());
        return String.valueOf(stringBuffer);
    }

    public void lastSixMonth(String current_month, String current_year) {

        int month = Integer.parseInt(current_month);
        int year = Integer.parseInt(current_year);
        Log.e("MONTH", month + " " + year);
        lastSevenMonth = new String[6];
        for (int i = 0; i < 6; i++) {
            switch (month) {

                case 1:
                    lastSevenMonth[i] = "Januari " + year;
                    month = 12;
                    break;
                case 2:
                    lastSevenMonth[i] = "Februari " + year;
                    month = month - 1;
                    break;
                case 3:
                    lastSevenMonth[i] = "Maret " + year;
                    month = month - 1;
                    break;
                case 4:
                    lastSevenMonth[i] = "April " + year;
                    month = month - 1;
                    break;
                case 5:
                    lastSevenMonth[i] = "Mei " + year;
                    month = month - 1;
                    break;
                case 6:
                    lastSevenMonth[i] = "Juni " + year;
                    month = month - 1;
                    break;
                case 7:
                    lastSevenMonth[i] = "Juli " + year;
                    month = month - 1;
                    break;
                case 8:
                    lastSevenMonth[i] = "Agustus " + year;
                    month = month - 1;
                    break;
                case 9:
                    lastSevenMonth[i] = "September " + year;
                    month = month - 1;
                    break;
                case 10:
                    lastSevenMonth[i] = "Oktober " + year;
                    month = month - 1;
                    break;
                case 11:
                    lastSevenMonth[i] = "November " + year;
                    month = month - 1;
                    break;
                case 12:
                    year = (year - 1);
                    lastSevenMonth[i] = "Desember " + year;
                    month = month - 1;
                    break;
            }
        }
    }

    public String getThisMonth(String number) {
        switch (number) {
            case "1":
                number = "Januari ";
                break;
            case "2":
                number = "Februari ";
                break;
            case "3":
                number = "Maret ";
                break;
            case "4":
                number = "April ";
                break;
            case "5":
                number = "Mei ";
                break;
            case "6":
                number = "Juni ";
                break;
            case "7":
                number = "Juli ";
                break;
            case "8":
                number = "Agustus ";
                break;
            case "9":
                number = "September ";
                break;
            case "10":
                number = "Oktober ";
                break;
            case "11":
                number = "November ";
                break;
            case "12":
                number = "Desember ";
                break;
        }
        return number;
    }
}
