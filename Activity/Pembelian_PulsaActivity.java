package id.meteor.alfamind.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Adapter.PulsaAdapter;
import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Model.PulsaModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class Pembelian_PulsaActivity extends BaseActivity {

    private ImageView number_icon;
    private EditText nomor_hp_number;
    private RecyclerView recylerView;
    private ArrayList<PulsaModel> pulsaModels;
    private PulsaAdapter adapter;
    private String TITLE = "";
    private RelativeLayout layout_progressBar;
    private int RQS_PICK_CONTACT = 101;
    private LinearLayout lay2,tac;
    AlertDialog.Builder dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian__pulsa);

        ImageView back_btn = findViewById(R.id.back_btn);
        TextView title = findViewById(R.id.title);
        nomor_hp_number = findViewById(R.id.nomor_hp_number);
        recylerView = findViewById(R.id.recylerView);
        layout_progressBar = findViewById(R.id.layout_progressBar);
        number_icon = findViewById(R.id.number_icon);
        lay2 = findViewById(R.id.lay2);
        tac = findViewById(R.id.tac);
        ImageView phonebook_icon = findViewById(R.id.phonebook_icon);

        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final AlertDialog alertDialog = dialogBuilder.create();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_tac,null);
        alertDialog.setView(dialogView);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setItemAnimator(new DefaultItemAnimator());
        final TextView title_text = dialogView.findViewById(R.id.title);

        title_text.setText("Syarat dan Ketentuan");
        final TextView Ok = dialogView.findViewById(R.id.Btn2);
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        final WebView webView = dialogView.findViewById(R.id.webView);
        final WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFontSize(14);
        webView.setWebViewClient(new Pembelian_PulsaActivity.MyBrowser());
        webView.loadUrl("file:///android_asset/tac_syarat.html");

        if (getIntent().hasExtra("TITLE"))
            TITLE = getIntent().getStringExtra("TITLE");

        title.setText(TITLE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        phonebook_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, RQS_PICK_CONTACT);
            }
        });


        nomor_hp_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = nomor_hp_number.getText().toString();
                setNumberIcon(number);
                if (!number.equalsIgnoreCase("") && number.length() > 3) {
                    MyApplication.getInstance().getPrefManager().setNomorHP(nomor_hp_number.getText().toString() + "");
                    getPulsaList(number);
                } else
                    recylerView.setAdapter(null);
            }
        });

        tac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

    }

    public void getPulsaList(final String number) {
        layout_progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.PULSA_FILTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("NotificationResponse", response + "");
                try {
                    pulsaModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            if (jsonObject.has("Data")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("Data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    PulsaModel model = new PulsaModel();
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    if (object.has("PRODUCT_NAME")) {
                                        model.setPRODUCT_NAME(object.getString("PRODUCT_NAME"));
                                    }
                                    if (object.has("HARGA")) {
                                        model.setHARGA(object.getString("HARGA"));
                                    }
                                    if (object.has("DESK_PROD")) {
                                        model.setDESK_PROD(object.getString("DESK_PROD"));
                                    }
                                    if (object.has("PLU")) {
                                        model.setPLU(object.getString("PLU"));
                                    }
                                    if (object.has("GROUP_NAME")) {
                                        model.setGROUP_NAME(object.getString("GROUP_NAME"));
                                    }
                                    if (object.has("MARGIN")) {
                                        model.setMARGIN(object.getString("MARGIN"));
                                    }
                                    if (object.has("JENIS")) {
                                        model.setJENIS(object.getString("JENIS"));
                                    }
                                    if (TITLE.equalsIgnoreCase("Pembelian Pulsa")) {
                                        if (model.getJENIS().equalsIgnoreCase("REGULER")) {
                                            pulsaModels.add(model);
                                        }
                                    } else {
                                        if (model.getJENIS().equalsIgnoreCase("DATA")) {
                                            pulsaModels.add(model);
                                        }
                                    }
                                }
                                adapter = new PulsaAdapter(Pembelian_PulsaActivity.this, pulsaModels);
                                recylerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                layout_progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            recylerView.setAdapter(null);
                            layout_progressBar.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    recylerView.setAdapter(null);
                    layout_progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recylerView.setAdapter(null);
                getErrorDialog("Something went wrong");
                Log.d("VollyError", " something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                Log.d("UPDATEPARAM", number + "");
                param.put("phone_number", number + "");
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void setNumberIcon(String number) {
        String s = "";
        if (number.length() > 3)
            s = number.substring(0, 4);
        Log.e("DATA", "" + s);

        switch (s) {
            case "0811":
            case "0812":
            case "0813":
            case "0821":
            case "0822":
            case "0823":
            case "0852":
            case "0853":
            case "0851":
                number_icon.setImageResource(R.drawable.ic_24_logo_telkomsel);
                break;

            case "0855":
            case "0856":
            case "0857":
            case "0858":
            case "0814":
            case "0815":
            case "0816":
                number_icon.setImageResource(R.drawable.ic_24_logo_indosat);
                break;

            case "0817":
            case "0818":
            case "0819":
            case "0859":
            case "0877":
            case "0878":
                number_icon.setImageResource(R.drawable.ic_24_logo_xl);
                break;

            case "0896":
            case "0897":
            case "0898":
            case "0899":
                number_icon.setImageResource(R.drawable.ic_24_logo_3);
                break;

            case "0838":
            case "0831":
            case "0832":
            case "0833":
                number_icon.setImageResource(R.drawable.ic_24_logo_axis);
                break;

            case "0881":
            case "0882":
            case "0883":
            case "0884":
            case "0885":
            case "0886":
            case "0887":
            case "0888":
            case "0889":
                number_icon.setImageResource(R.drawable.ic_24_logo_smartfren);
                break;

            default:
                number_icon.setImageBitmap(null);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RQS_PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();
                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                number = number.replace(" ", "");
                number = number.replace("-", "");
                number = number.replace("+", "");
                number = number.replace("(", "");
                number = number.replace(")", "");
                if (number.substring(0, 2).equalsIgnoreCase("62"))
                    number = "0" + number.substring(2, number.length());

                setNumberIcon(number);
                MyApplication.getInstance().getPrefManager().setNomorHP(number + "");
                nomor_hp_number.setText(number);
                lay2.setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}
