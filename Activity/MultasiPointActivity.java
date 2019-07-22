package id.meteor.alfamind.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
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

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetDialog;
import id.meteor.alfamind.customDialog.OnOperItemClickL;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;


public class MultasiPointActivity extends BaseActivity {

    private RelativeLayout lay_progressBar;
    private LinearLayout history_data;
    private String datetime, action, addition_point, subtraction_point, title, thisYear = "", thisMonth = "";
    private LayoutInflater inflate;
    private EditText et_month;
    private String lastSevenMonth[];
    private TextView akhir, debit, kredit, awal;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multasi_point);

        inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        history_data = findViewById(R.id.history_data);
        lay_progressBar = findViewById(R.id.layout_progressBar);
        et_month = findViewById(R.id.et_month);
        awal = findViewById(R.id.awal);
        kredit = findViewById(R.id.kredit);
        debit = findViewById(R.id.debit);
        akhir = findViewById(R.id.akhir);


        Calendar calendar = Calendar.getInstance();
        thisYear = calendar.get(Calendar.YEAR) + "";

        if (calendar.get(Calendar.MONTH) < 10) {
            thisMonth = "0" + (calendar.get(Calendar.MONTH) + 1);
        } else {
            thisMonth = (calendar.get(Calendar.MONTH) + 1) + "";
        }

        et_month.setText(getThisMonth((calendar.get(Calendar.MONTH) + 1) + "") + thisYear);

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
        lastSixMonth((calendar.get(Calendar.MONTH) + 1) + "", thisYear);
        getMultasiHistory();

    }

    public void getMultasiHistory() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_REWARD_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RewardHISTORY", response + "");
                try {
                    history_data.removeAllViews();
                    lay_progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("point_reward_log")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("point_reward_log");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                // if (jsonObject1.has("remaining_points"));
                                // if (jsonObject1.has("reference"));
                                // if (jsonObject1.has("expiration"));
                                // if (jsonObject1.has("category"));

                                if (jsonObject1.has("datetime")) {
                                    datetime = jsonObject1.getString("datetime");
                                }
                                if (jsonObject1.has("action")) {
                                    action = jsonObject1.getString("action");
                                }
                                if (jsonObject1.has("addition_point")) {
                                    addition_point = getFormat(jsonObject1.getString("addition_point"));
                                }
                                if (jsonObject1.has("subtraction_point")) {
                                    subtraction_point = getFormat(jsonObject1.getString("subtraction_point"));
                                }
                                if (jsonObject1.has("title")) {
                                    title = jsonObject1.getString("title");
                                }

                                View view = inflate.inflate(R.layout.mutasi_point_history, null);
                                history_data.addView(view);

                                TextView date = view.findViewById(R.id.date);
                                TextView name = view.findViewById(R.id.name);
                                TextView point = view.findViewById(R.id.point);
                                ImageView img = view.findViewById(R.id.img);

                                date.setText(dateFormat(datetime));
                                name.setText(title);

                                if (action.equalsIgnoreCase("Reward")) {
                                    img.setImageResource(R.drawable.ic_up);
                                    point.setText(addition_point + "");
                                } else {
                                    img.setImageResource(R.drawable.ic_down);
                                    point.setText(subtraction_point + "");
                                }
                            }
                        }

                        if (jsonObject.has("point_reward_rekap")) {
                            Log.e("HEELO", "adadasd");
                            JSONObject object = jsonObject.getJSONObject("point_reward_rekap");

                            for (int i = 0; i < object.length(); i++) {

                                if (object.has("point_reward_awal")) {
                                    if (object.getString("point_reward_awal").equalsIgnoreCase("null"))
                                        awal.setText("0 Points");
                                    else
                                        awal.setText(getFormat(object.getString("point_reward_awal")) + " Points");
                                }
                                if (object.has("mutasi_kredit")) {
                                    if (object.getString("mutasi_kredit").equalsIgnoreCase("null"))
                                        kredit.setText("0 Points");
                                    else
                                        kredit.setText(getFormat(object.getString("mutasi_kredit")) + " Points");
                                }
                                if (object.has("mutasi_debit")) {
                                    if (object.getString("mutasi_debit").equalsIgnoreCase("null"))
                                        debit.setText("0 Points");
                                    else
                                        debit.setText(getFormat(object.getString("mutasi_debit")) + " Points");
                                }
                                if (object.has("point_reward_akhir")) {
                                    if (object.getString("point_reward_akhir").equalsIgnoreCase("null"))
                                        akhir.setText("0 Points");
                                    else
                                        akhir.setText(getFormat(object.getString("point_reward_akhir")) + " Points");
                                }
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
                param.put("access_token", token + "");
                param.put("year", thisYear);
                param.put("month", thisMonth);

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
        Log.e("RESULT-DONE", String.valueOf(stringBuffer));

        Log.e("RESULT*", NUM.toString());
        return String.valueOf(stringBuffer);


    }

    public String dateFormat(String str) {

        String[] divide = str.split(" ");
        String[] year = divide[0].split("-");

        str = year[0];
        switch (year[1]) {
            case "01":
                str = str + " Januari ";
                break;
            case "02":
                str = str + " Februari ";
                break;
            case "03":
                str = str + " Maret ";
                break;
            case "04":
                str = str + " April ";
                break;
            case "05":
                str = str + " Mei ";
                break;
            case "06":
                str = str + " Juni ";
                break;
            case "07":
                str = str + " Juli ";
                break;
            case "08":
                str = str + " Agustus ";
                break;
            case "09":
                str = str + " September ";
                break;
            case "10":
                str = str + " Oktober ";
                break;
            case "11":
                str = str + " November ";
                break;
            case "12":
                str = str + " Desember ";
                break;
        }

        str = str + year[2] + ", ";
        String[] time = divide[1].split(":");
        str = str + time[0] + ":" + time[1];

        return str;

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
                getMultasiHistory();
            }

            @Override
            public void onCancel() {

            }
        });
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
