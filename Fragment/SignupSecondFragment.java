package id.meteor.alfamind.Fragment;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.meteor.alfamind.Activity.SignupActivity;
import id.meteor.alfamind.Model.ProvinciPozo;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetDialog;
import id.meteor.alfamind.customDialog.OnOperItemClickL;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.EditFocusChange;
import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.SignUpManager;

import static android.app.Activity.RESULT_OK;

public class SignupSecondFragment extends Fragment {

    private SignupActivity signupActivity;

    boolean back = true;
    private final int WRITE_EXTERNAL_STORAGE_REQUEST = 1002;
    private final int CAMERA_REQUEST = 1005;
    private final int PICK_IMAGE_REQUEST1 = 1003;
    private final int PICK_IMAGE_REQUEST2 = 10031;
    private final int TAKE_IMAGE_REQUEST1 = 1004;
    private final int TAKE_IMAGE_REQUEST2 = 10041;

    private int id1;
    private int id2;
    private int id3;

    private String name1, name2, name3, DATA, requestImage = "";
    public static String path1, path2;

    private ArrayList<ProvinciPozo> provinciList = new ArrayList<>();
    private ArrayList<ProvinciPozo> districtList = new ArrayList<>();
    private ArrayList<ProvinciPozo> kecamatanList = new ArrayList<>();

    private EditText et_ktpId, et_alamat, et_kota, et_provinsi, et_kabupaten, et_kecamatan, et_kode_pos;

    ImageView imageKtp, imageNpwr, close1, close2;

    public SignupSecondFragment() {
    }

    public void setActivity(SignupActivity signupActivity){this.signupActivity = signupActivity;}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_second, container, false);

        DATA = getArguments().getString("DATA");
        Log.e("BUNDLEDATA", getArguments().getString("DATA") + "");

        ScrollView scrollView = view.findViewById(R.id.scroll);
        TextView next_btn = view.findViewById(R.id.next);
        et_ktpId = view.findViewById(R.id.et_ktpId);
        et_alamat = view.findViewById(R.id.et_alamat);
        et_kota = view.findViewById(R.id.et_kota); //city
        et_provinsi = view.findViewById(R.id.et_provinsi);
        et_kabupaten = view.findViewById(R.id.et_kabupaten);//district
        et_kecamatan = view.findViewById(R.id.et_kecamatan);
        et_kode_pos = view.findViewById(R.id.et_kode_pos);//postalCode

        RelativeLayout upload_ktp = view.findViewById(R.id.rl_ktp);
        RelativeLayout upload_npwr = view.findViewById(R.id.rl_npwr);
        imageKtp = view.findViewById(R.id.image_ktp);
        imageNpwr = view.findViewById(R.id.image_npwr);
        TextView title = view.findViewById(R.id.title);
        close1 = view.findViewById(R.id.close1);
        close2 = view.findViewById(R.id.close2);

        if (DATA.equalsIgnoreCase("NEW_NETWORK")) {
            title.setText("Pendaftaran Anggota");
        }

        if (MyApplication.isNetworkAvailable(getActivity())) {
            getAddressList();
        } else {
            ((SignupActivity) getActivity()).showSnackBar();
        }

        et_alamat.setOnFocusChangeListener(new EditFocusChange(et_alamat, scrollView));
        et_kota.setOnFocusChangeListener(new EditFocusChange(et_alamat, scrollView));
        et_provinsi.setOnFocusChangeListener(new EditFocusChange(et_alamat, scrollView));
        et_kabupaten.setOnFocusChangeListener(new EditFocusChange(et_alamat, scrollView));
        et_kecamatan.setOnFocusChangeListener(new EditFocusChange(et_alamat, scrollView));

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MyApplication.isNetworkAvailable(getActivity())) {
                    if (validation()) {
                        saveValueInSharedPrefence();

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        Bundle bundle = new Bundle();
                        bundle.putString("DATA", DATA);
                        SignupThirdFragment signupThirdFragment = new SignupThirdFragment();
                        signupThirdFragment.setArguments(bundle);
                        signupThirdFragment.setActivity(signupActivity);
                        transaction.replace(R.id.frame, signupThirdFragment, "three");
                        transaction.commit();
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                } else {
                    signupActivity.getErrorDialog("Tidak ada koneksi internet");
                }
            }
        });

        // upload  ktp
        upload_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_ktp();
            }
        });

        //upload npwr
        upload_npwr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_npwr();
            }
        });

        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path1 = null;
                MyApplication.getInstance().getSignupManager().setKtpImage(null);
                imageKtp.setImageBitmap(null);
                close1.setVisibility(View.GONE);
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path2 = null;
                MyApplication.getInstance().getSignupManager().setNpwpImage(null);
                imageNpwr.setImageBitmap(null);
                close2.setVisibility(View.GONE);
            }
        });

        //et_provinci
        et_provinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provinciSelection(et_provinsi);
            }
        });
        //et_kabupaten
        et_kabupaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_provinsi.getEditableText().toString())) {

                    if (MyApplication.isNetworkAvailable(getActivity())) {
                        getDistrictList();
                    } else {
                        ((SignupActivity) getActivity()).showSnackBar();
                    }
                } else {
                    signupActivity.getErrorDialog("Pilih provinsi");
                }
            }
        });

        //et-kecamatan
        et_kecamatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_provinsi.getEditableText().toString())) {
                    if (!TextUtils.isEmpty(et_kabupaten.getEditableText().toString())) {

                        if (MyApplication.isNetworkAvailable(getActivity())) {
                            getKecamatanList();
                        } else {
                            ((SignupActivity) getActivity()).showSnackBar();
                        }
                    } else {

                        signupActivity.getErrorDialog("Pilih Kabupaten/Kota");
                    }
                } else {
                    signupActivity.getErrorDialog("Pilih Provinsi");
                }

            }
        });
        setData();
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {

        saveValueInSharedPrefence();
        super.onDestroy();
    }

    private void setData() {
        // MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();
        SignUpManager manager = MyApplication.getInstance().getSignupManager();

        // save nomer_Ktp
        String temp = manager.getNomor();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_ktpId.setText(temp);
        }

        // save ktp Image
        temp = manager.getKtpImage();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
        } else
            close1.setVisibility(View.GONE);

        // save Npwp Image
        temp = manager.getNpwpImage();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
        } else
            close2.setVisibility(View.GONE);

        temp = manager.getAddress();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {

            et_alamat.setText(temp);
        }
        temp = manager.getCity();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {

            et_kota.setText(temp);
        }

        temp = manager.getPName1();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_provinsi.setText(temp);
            name1 = temp;
            id1 = manager.getPid1();
        }
        temp = manager.getPName2();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_kabupaten.setText(temp);
            name2 = temp;
            id2 = manager.getPid2();
        }
        temp = manager.getPName3();

        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_kecamatan.setText(temp);
            name3 = temp;
            id3 = manager.getPid3();

        }
        temp = manager.getZipCode();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_kode_pos.setText(temp);
        }

        /// save ktp image
        temp = manager.getKtpImage();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            File file = new File(temp);
            if (file.exists()) {
                path1 = file.getPath();
                imageKtp.setImageBitmap(getBitmap(temp));
            }
        }

        // save npwp image;
        temp = manager.getNpwpImage();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            File file = new File(temp);
            if (file.exists()) {
                path2 = file.getPath();
                imageNpwr.setImageBitmap(getBitmap(temp));
            }

        }


    }

    // get Bitmap from file path................
    public Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {

            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            return bitmap;
        }
    }


    private void kecamatanSelection() {

        final String pNameArr[] = new String[kecamatanList.size()];
        for (int i = 0; i < kecamatanList.size(); i++) {
            pNameArr[i] = kecamatanList.get(i).getProvinciName();
        }
        Arrays.sort(pNameArr);

        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), pNameArr, null);
        dialog.title("Pilih Kecamatan");
        dialog.cancelText("Batal").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                //et_provinsi.setText(pNameArr[position]);

                et_kecamatan.setText(pNameArr[position]);

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


        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), pNameArr, null);
        dialog.title("Pilih Kabupaten/Kota");
        dialog.cancelText("Batal").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                //et_provinsi.setText(pNameArr[position]);
                et_kabupaten.setText(pNameArr[position]);
                et_kecamatan.setText("");

                name2 = pNameArr[position];
                id2 = Integer.parseInt(districtList.get(position).getProvinciId());
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

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

        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), pNameArr, null);
        dialog.title("Pilih Provinsi");
        dialog.cancelText("Batal").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {


                et_provinsi.setText(pNameArr[position]);
                et_kabupaten.setText("");
                et_kecamatan.setText("");

                name1 = pNameArr[position];
                id1 = Integer.parseInt(provinciList.get(position).getProvinciId());
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });


    }


    private boolean validation() {
        if (TextUtils.isEmpty(et_ktpId.getText().toString())) {
            signupActivity.getErrorDialog("Masukkan Nomor KTP");
            return false;
        }
        if (path1 == null) {
            signupActivity.getErrorDialog("Pilih gambar ktp");
            return false;
        }
        if (TextUtils.isEmpty(et_alamat.getText().toString())) {
            signupActivity.getErrorDialog("Masukkan alamat");
            return false;
        }
        if (TextUtils.isEmpty(et_kota.getText().toString())) {
            signupActivity.getErrorDialog("Masukkan kota");
            return false;
        }
        if (TextUtils.isEmpty(et_provinsi.getText().toString())) {
            signupActivity.getErrorDialog("Pilih provinsi");
            return false;
        }
        if (TextUtils.isEmpty(et_kabupaten.getText().toString())) {
            signupActivity.getErrorDialog("Pilih Kabupaten/Kota");
            return false;
        }
        if (TextUtils.isEmpty(et_kecamatan.getText().toString())) {
            signupActivity.getErrorDialog("Pilih kecamatan");
            return false;
        }
        if (TextUtils.isEmpty(et_kode_pos.getText().toString())) {
            signupActivity.getErrorDialog("Masukkan kode pos");
            return false;
        }
        return true;
    }

    private void upload_ktp() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                pictureRequest(1);
            }

        } else {
            pictureRequest(1);
        }
    }

    private void upload_npwr() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                pictureRequest(2);
            }

        } else {
            pictureRequest(2);
        }
    }


    /// save to pref.

    public void saveValueInSharedPrefence() {

        // MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();
        SignUpManager manager = MyApplication.getInstance().getSignupManager();
        manager.setPid1(id1);
        manager.setPid2(id2);
        manager.setPid3(id3);
        manager.setPName1(name1);
        manager.setPName2(name2);
        manager.setPName3(name3);
        manager.setKtpImage(path1);
        manager.setNpwpImage(path2);
        manager.setCity(et_kota.getText().toString());
        manager.setDistrict(id2 + "");
        manager.setNomor(et_ktpId.getText().toString());
        manager.setAddress(et_alamat.getText().toString());
        manager.setState(id1 + "");
        manager.setkecamatan(id3 + "");
        manager.setZipCode(et_kode_pos.getEditableText().toString() + "");
    }

    // get Services

    private void getAddressList() {
        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                signupActivity.getErrorDialog("Something went wrong");

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
        Log.d("District", " call " + id1);
        signupActivity.showProgress("Please wait...", false);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_KABUPATEN + id1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("District", response + "");
                districtParseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                signupActivity.closeProgress();


                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((SignupActivity) getActivity()).showSnackBar();
                } else {
                    signupActivity.getErrorDialog("Something went wrong");
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
        signupActivity.showProgress("Please wait...", false);
        Log.d("District", "----" + id2);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_KECAMATAN + id2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("District", response + "");
                kecamatanParseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((SignupActivity) getActivity()).showSnackBar();
                } else {
                    signupActivity.getErrorDialog("Something went wrong");
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
            signupActivity.closeProgress();
            districtSelection();

        } catch (JSONException e) {
            signupActivity.closeProgress();
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
            signupActivity.closeProgress();
            kecamatanSelection();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /// for  select image

    private void pictureRequest(final int val) {
        final String[] list = getResources().getStringArray(R.array.picture_options_list);

        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), list, null);
        dialog.cancelText("Batal").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission1())
                            takePicture(val);
                    } else {
                        takePicture(val);
                    }
                } else {
                    if (val == 1) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST1);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST2);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    Uri mImageUri;

    private void takePicture(final int val) {
        File photo = new File("");
        try {
            String name = System.currentTimeMillis() + "";
            photo = this.createTemporaryFile(name, ".jpg");
            photo.delete();
        } catch (Exception e) {
            signupActivity.getErrorDialog("Please check SD card! Image shot is impossible!");
        }
        if (Build.VERSION.SDK_INT >= 24)
            mImageUri = FileProvider.getUriForFile(getContext(), "id.meteor.AlfaMind.fileProvider", photo);
        else
            mImageUri = Uri.fromFile(photo);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (val == 1) {
            startActivityForResult(intent, TAKE_IMAGE_REQUEST1);
        } else {
            startActivityForResult(intent, TAKE_IMAGE_REQUEST2);
        }

    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    private boolean checkPermission() {
        int external = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permissions = new ArrayList<>();

        if (external != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissions.toArray(new String[permissions.size()]), WRITE_EXTERNAL_STORAGE_REQUEST);
            return false;
        }

        return true;
    }

    private boolean checkPermission1() {
        int camera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        List<String> permissions = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissions.toArray(new String[permissions.size()]), CAMERA_REQUEST);
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("IMAGEPICK", requestCode + "");

        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK) {
            Log.d("IMAGEPICK", " pick1");
            Uri filePath = data.getData();
            requestImage = "IMAGE1";
            CropImage.activity(filePath)
                    .start(getContext(), this);
        }

        if (requestCode == TAKE_IMAGE_REQUEST1 && resultCode == RESULT_OK) {
            Log.d("IMAGEPICK", " take1 ");
            requestImage = "IMAGE1";
            CropImage.activity(mImageUri)
                    .start(getContext(), this);
        }


        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK) {

            Log.d("IMAGEPICK", " pick2");
            requestImage = "IMAGE2";
            Uri filePath = data.getData();
            CropImage.activity(filePath)
                    .start(getContext(), this);
        }

        if (requestCode == TAKE_IMAGE_REQUEST2 && resultCode == RESULT_OK) {
            ContentResolver cr = getActivity().getContentResolver();
            Log.d("IMAGEPICK", " take2" + mImageUri);
            requestImage = "IMAGE2";
            CropImage.activity(mImageUri)
                    .start(getContext(), this);
        }

        /// after crop image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                if (requestImage.equalsIgnoreCase("IMAGE1")) {
                    Glide.with(getActivity())
                            .load(resultUri)
                            .asBitmap()
                            .placeholder(R.drawable.ic_camera_two)
                            .into(new SimpleTarget<Bitmap>(800, 800) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                    File file = saveImage(resource);
                                    path1 = file.getPath();
                                    MyApplication.getInstance().getSignupManager().setKtpImage(file.getPath());
                                    imageKtp.setImageBitmap(resource);
                                    close1.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadCleared(Drawable placeholder) {
                                    super.onLoadCleared(placeholder);
                                }
                            });
                }

                if (requestImage.equalsIgnoreCase("IMAGE2")) {
                    Glide.with(getActivity())
                            .load(resultUri)
                            .asBitmap()
                            .placeholder(R.drawable.ic_camera_two)
                            .into(new SimpleTarget<Bitmap>(800, 800) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                    File file = saveImage(resource);
                                    path2 = file.getPath();
                                    MyApplication.getInstance().getSignupManager().setNpwpImage(file.getPath());
                                    imageNpwr.setImageBitmap(resource);
                                    close2.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadCleared(Drawable placeholder) {
                                    super.onLoadCleared(placeholder);
                                }
                            });
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public File saveImage(Bitmap bitmap) {
        File filename;
        String imageName = System.currentTimeMillis() + "";
        try {
            String path = Environment.getExternalStorageDirectory().toString();

            File file = new File(path + "/AlfaMind/Media/");
            file.mkdirs();
            filename = new File(path + "/AlfaMind/Media/" + "IMG_" + imageName + ".jpg");

            FileOutputStream out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return filename;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}