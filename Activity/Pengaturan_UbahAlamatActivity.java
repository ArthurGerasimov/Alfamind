package id.meteor.alfamind.Activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Database.Address;
import id.meteor.alfamind.Database.MySQLiteHelper;
import id.meteor.alfamind.Database.TableAddress;
import id.meteor.alfamind.Model.ProvinciPozo;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetDialog;
import id.meteor.alfamind.customDialog.OnOperItemClickL;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class Pengaturan_UbahAlamatActivity extends BaseActivity {

    private TextView next;
    private ArrayList<ProvinciPozo> provinciList = new ArrayList<>();
    private ArrayList<ProvinciPozo> districtList = new ArrayList<>();
    private ArrayList<ProvinciPozo> kecamatanList = new ArrayList<>();
    private int id1, id2, id3;
    private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase db;
    private EditText name, number, address, rt, rw, provinsi, kabupaten, kode_pos, kecamatan;
    private String NEXT;
    private String ADDRESS_ID;
    private String name1;
    private String name2;
    private String name3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan__ubah_alamat);

        if (MyApplication.isNetworkAvailable(this)) {
            getAddressList();
        } else {
            showSnackBar();
        }

        mySQLiteHelper = new MySQLiteHelper(this);
        db = mySQLiteHelper.getWritableDatabase();
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        address = findViewById(R.id.address);
        rt = findViewById(R.id.rt);
        rw = findViewById(R.id.rw);
        provinsi = findViewById(R.id.provinsi);
        kabupaten = findViewById(R.id.kabupaten);
        kecamatan = findViewById(R.id.kecamatan);
        kode_pos = findViewById(R.id.kode_pos);
        ImageView back_btn = findViewById(R.id.back_btn);
        next = findViewById(R.id.next);
        TextView title = findViewById(R.id.title);

        String TITLE = getIntent().getStringExtra("TITLE");
        NEXT = getIntent().getStringExtra("NEXT");

        if (TITLE.equalsIgnoreCase("Tambah Alamat")) {
            title.setText(TITLE);
            next.setText(NEXT);
        }

        if (TITLE.equalsIgnoreCase("Ubah Alamat")) {
            ADDRESS_ID = getIntent().getStringExtra("ID");
            title.setText(TITLE);
            next.setText(NEXT);
            setData(ADDRESS_ID);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (next.getText().toString().equalsIgnoreCase("TAMBAH")) {
                    if (validation()) {
                        TableAddress.saveAddress(name.getText().toString(), number.getText().toString(), address.getText().toString(), rt.getText().toString(), rw.getText().toString(), provinsi.getText().toString(), kabupaten.getText().toString(), kecamatan.getText().toString(), kode_pos.getText().toString(), db);
                        Toast.makeText(Pengaturan_UbahAlamatActivity.this, "TAMBAH", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    }
                }
                if (next.getText().toString().equalsIgnoreCase("UBAH")) {
                    if (validation()) {
                        TableAddress.updateAddress(ADDRESS_ID, name.getText().toString(), number.getText().toString(), address.getText().toString(), rt.getText().toString(), rw.getText().toString(), provinsi.getText().toString(), kabupaten.getText().toString(), kabupaten.getText().toString(), kode_pos.getText().toString(), db);
                        Toast.makeText(Pengaturan_UbahAlamatActivity.this, "UBAH", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    }
                }
            }
        });

        provinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provinciSelection(provinsi);
            }
        });

        kabupaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(provinsi.getEditableText().toString())) {

                    if (MyApplication.isNetworkAvailable(Pengaturan_UbahAlamatActivity.this)) {
                        getDistrictList();
                    } else {
                        showSnackBar();
                    }
                } else {
                    getErrorDialog("Pilih provinsi");
                }
            }
        });

        kecamatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(provinsi.getEditableText().toString())) {
                    if (!TextUtils.isEmpty(kabupaten.getEditableText().toString())) {

                        if (MyApplication.isNetworkAvailable(Pengaturan_UbahAlamatActivity.this)) {
                            getKecamatanList();
                        } else {
                            showSnackBar();
                        }
                    } else {

                        getErrorDialog("Pilih Kabupaten/Kota");
                    }
                } else {
                    getErrorDialog("Pilih Provinsi");
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void provinciSelection(final EditText et_provinsi) {
        Log.d("BankSelect", "bankselection");
        final String pNameArr[] = new String[provinciList.size()];
        for (int i = 0; i < provinciList.size(); i++) {
            pNameArr[i] = provinciList.get(i).getProvinciName();
        }
        Arrays.sort(pNameArr);

        final ActionSheetDialog dialog = new ActionSheetDialog(this, pNameArr, null);
        dialog.title("Pilih Provinsi");
        dialog.cancelText("Membatalkan").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                et_provinsi.setText(pNameArr[position]);
                kabupaten.setText("");
                kecamatan.setText("");
                name1 = pNameArr[position];
                id1 = Integer.parseInt(provinciList.get(position).getProvinciId());
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void kecamatanSelection() {

        final String pNameArr[] = new String[kecamatanList.size()];
        for (int i = 0; i < kecamatanList.size(); i++) {
            pNameArr[i] = kecamatanList.get(i).getProvinciName();
        }
        Arrays.sort(pNameArr);

        final ActionSheetDialog dialog = new ActionSheetDialog(this, pNameArr, null);
        dialog.title("Pilih provinsi");
        dialog.cancelText("Membatalkan").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                kecamatan.setText(pNameArr[position]);

                name3 = pNameArr[position];
                id3 = Integer.parseInt(kecamatanList.get(position).getProvinciId());
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void districtSelection() {

        final String pNameArr[] = new String[districtList.size()];
        Log.d("LIStSIZE", districtList.size() + "");
        for (int i = 0; i < districtList.size(); i++) {
            pNameArr[i] = districtList.get(i).getProvinciName();
        }
        Arrays.sort(pNameArr);

        final ActionSheetDialog dialog = new ActionSheetDialog(this, pNameArr, null);
        dialog.title("Pilih provinsi");
        dialog.cancelText("Membatalkan").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                //et_provinsi.setText(pNameArr[position]);
                kabupaten.setText(pNameArr[position]);
                kecamatan.setText("");
                name2 = pNameArr[position];
                id2 = Integer.parseInt(districtList.get(position).getProvinciId());
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });
    }


    public boolean validation() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            getErrorDialog("Masukkan nama depan");
            return false;
        }
        if (TextUtils.isEmpty(number.getText().toString()) || number.getText().toString().length()<8) {
            getErrorDialog("Masukkan nomor ponsel");
            return false;
        }
        if (TextUtils.isEmpty(address.getText().toString())) {
            getErrorDialog("Masukkan Nomor HP Konsumen");
            return false;
        }
        if (TextUtils.isEmpty(rt.getText().toString())) {
            getErrorDialog("Masukkan RT");
            return false;
        }
        if (TextUtils.isEmpty(rw.getText().toString())) {
            getErrorDialog("Masukkan RW");
            return false;
        }
        if (TextUtils.isEmpty(provinsi.getText().toString())) {
            getErrorDialog("Pilih provinsi");
            return false;
        }
        if (TextUtils.isEmpty(kode_pos.getText().toString())) {
            getErrorDialog("Masukkan kode pos");
            return false;
        }
        if (TextUtils.isEmpty(kabupaten.getText().toString())) {
            getErrorDialog("Pilih Kabupaten/Kota");
            return false;
        }
        if (TextUtils.isEmpty(kecamatan.getText().toString())) {
            getErrorDialog("Pilih Kecamatan");
            return false;
        }

        return true;
    }

    public void setData(String id) {
        db = mySQLiteHelper.getReadableDatabase();
        Address addresss = TableAddress.getAddress(id, db);
        assert addresss != null;
        name.setText(addresss.getName());
        number.setText(addresss.getNumber());
        provinsi.setText(addresss.getProvinsi());
        kabupaten.setText(addresss.getKabupaten());
        kode_pos.setText(addresss.getKode());
        rw.setText(addresss.getRw());
        rt.setText(addresss.getRt());
        address.setText(addresss.getAddress());
        kecamatan.setText(addresss.getKecamatan());
    }


    // get Services

    private void getAddressList() {
        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_AREA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CaptchaResponse", response + "");
                provinciParseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
                getErrorDialog("Something went wrong");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                String token = MyApplication.getInstance().getPrefManager().getAccessToken();
                if (token == null || token.equals("null") || token.equals("")) {
                    token = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }
                param.put("access_token", token + "");

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getDistrictList() {
        showProgress("Please wait...",false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_KABUPATEN + id1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("District", response + "");
                districtParseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                if (!MyApplication.isNetworkAvailable(Pengaturan_UbahAlamatActivity.this)) {
                    showSnackBar();
                } else {
                    getErrorDialog("Something went wrong");
                }
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                String token = MyApplication.getInstance().getPrefManager().getAccessToken();
                if (token == null || token.equals("null") || token.equals("")) {
                    token = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }
                Log.d("id_prov", id1 + "  " + token);
                param.put("access_token", token);
                param.put("id_prov", id1 + "");

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getKecamatanList() {
        showProgress("Please wait...",false);
        Log.d("District", "----" + id2);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_KECAMATAN + id2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("District", response + "");
                kecamatanParseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!MyApplication.isNetworkAvailable(Pengaturan_UbahAlamatActivity.this)) {
                    showSnackBar();
                } else {
                    getErrorDialog("Something went wrong");
                }
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                String token = MyApplication.getInstance().getPrefManager().getAccessToken();
                if (token == null || token.equals("null") || token.equals("")) {
                    token = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }
                Log.d("id_prov", id1 + "");
                param.put("access_token", token);
                param.put("id_prov", id1 + "");
                param.put("id_kab", id2 + "");

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    // hold response

    private void provinciParseJson(String response) {
        provinciList.clear();
        try {

            JSONArray provinciArr = new JSONArray(response);
            Log.e("LENGHT", provinciArr.length() + "");
            for (int i = 0; i < provinciArr.length(); i++) {
                JSONObject object = provinciArr.getJSONObject(i);
                ProvinciPozo provinciPozo = new ProvinciPozo();
                if (object.has("id")) {
                    provinciPozo.setProvinciId(object.getString("id"));
                }
                if (object.has("label")) {
                    provinciPozo.setProvinciName(object.getString("label"));
                }
                provinciList.add(provinciPozo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void districtParseJson(String response) {

        districtList.clear();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                ProvinciPozo provinciPozo = new ProvinciPozo();
                if (object.has("id")) {
                    provinciPozo.setProvinciId(object.getString("id"));
                }
                if (object.has("label")) {
                    provinciPozo.setProvinciName(object.getString("label"));
                }
                districtList.add(provinciPozo);
            }
            closeProgress();
            districtSelection();

        } catch (JSONException e) {
            closeProgress();
            e.printStackTrace();
        }
    }

    private void kecamatanParseJson(String response) {

        kecamatanList.clear();
        try {
            JSONArray provinciArr = new JSONArray(response);
            for (int i = 0; i < provinciArr.length(); i++) {
                JSONObject object = provinciArr.getJSONObject(i);
                ProvinciPozo provinciPozo = new ProvinciPozo();
                if (object.has("id")) {
                    provinciPozo.setProvinciId(object.getString("id"));
                }
                if (object.has("label")) {
                    provinciPozo.setProvinciName(object.getString("label"));
                }
                kecamatanList.add(provinciPozo);
            }
            closeProgress();
            kecamatanSelection();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


