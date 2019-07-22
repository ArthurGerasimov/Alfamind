package id.meteor.alfamind.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.makeramen.roundedimageview.RoundedImageView;
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
import java.util.Set;
import java.util.concurrent.TimeUnit;

import id.meteor.alfamind.Activity.Bantuan_EmailBerhasilActivity;
import id.meteor.alfamind.Activity.Bantuan_KontakLayananActivity;
import id.meteor.alfamind.Activity.MainActivity;
import id.meteor.alfamind.Activity.ProfileActivity;
import id.meteor.alfamind.Activity.SignupActivity;
import id.meteor.alfamind.Model.ProvinciPozo;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetDialog;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;
import id.meteor.alfamind.customDialog.OnOperItemClickL;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.EditFocusChange;
import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.MyPreferenceManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileUpdateFragment extends Fragment {

    private TextView name, customer_id, email, gender, dateOfBirth, phone, text_bankOwnerName, text_bankName, text_bankAccountNumber;
    private RoundedImageView profile_image;
    private EditText address, provinsi, zipcode, et_city, kecamatan, store, et_kabupaten, et_bankName, et_bankAccountNumber, et_bankOwnerName;
    private ArrayList<ProvinciPozo> provinciList = new ArrayList<>();
    private ArrayList<ProvinciPozo> districtList = new ArrayList<>();
    private ArrayList<ProvinciPozo> kecamatanList = new ArrayList<>();
    private String name1, name2, name3;
    private int id1 = 0, id2, id3;
    private final int CAMERA_REQUEST = 1005;
    private final int PICK_IMAGE_REQUEST1 = 1003;
    private final int TAKE_IMAGE_REQUEST1 = 1004;
    private Uri mImageUri;

    private String bankNameArr[];
    private String bankCodeArr[];
    private ArrayList<String> bankNameList = new ArrayList<>();
    private ArrayList<String> bankCodeList = new ArrayList<>();
    private String bankCode = "NA";
    private ArrayList<ProvinciPozo> allBankList = new ArrayList<>();

    private HashMap<String, String> bankHashMap = new HashMap<>();
    private LinearLayout bank_text, bank_edit;

    String path = "";

    private View view;
    private ProgressDialog progressDialog;

    public ProfileUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile_update, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        name = view.findViewById(R.id.name);
        customer_id = view.findViewById(R.id.customer_id);
        profile_image = view.findViewById(R.id.profile_image);
        email = view.findViewById(R.id.et_email);
        phone = view.findViewById(R.id.et_number);
        gender = view.findViewById(R.id.et_gender);
        dateOfBirth = view.findViewById(R.id.et_dob);
        provinsi = view.findViewById(R.id.et_provinsi);
        zipcode = view.findViewById(R.id.et_kode_pos);
        et_city = view.findViewById(R.id.et_kota);
        kecamatan = view.findViewById(R.id.et_kecamatan);
        address = view.findViewById(R.id.et_alamat);
        store = view.findViewById(R.id.et_storename);
        et_kabupaten = view.findViewById(R.id.et_kabupaten);
        ImageView upload_ktp = view.findViewById(R.id.upload_ktp);

        bank_text = view.findViewById(R.id.bank_text);
        bank_edit = view.findViewById(R.id.bank_edit);

        et_bankName = view.findViewById(R.id.et_bankName);
        et_bankAccountNumber = view.findViewById(R.id.et_bankAccountNumber);
        et_bankOwnerName = view.findViewById(R.id.et_bankOwnerName);

        text_bankName = view.findViewById(R.id.text_bankName);
        text_bankOwnerName = view.findViewById(R.id.text_bankOwnerName);
        text_bankAccountNumber = view.findViewById(R.id.text_bankAccountNumber);

        ScrollView scrollView = view.findViewById(R.id.scrollView);
        store.setOnFocusChangeListener(new EditFocusChange(store, scrollView));
        phone.setOnFocusChangeListener(new EditFocusChange(phone, scrollView));
        address.setOnFocusChangeListener(new EditFocusChange(address, scrollView));
        et_city.setOnFocusChangeListener(new EditFocusChange(et_city, scrollView));
        zipcode.setOnFocusChangeListener(new EditFocusChange(zipcode, scrollView));

        if (!MyApplication.isNetworkAvailable(getActivity())) {
            ((ProfileActivity) getActivity()).showSnackBar();
        }
        getAddressList();


        provinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                provinciSelection(provinsi);
            }
        });

        et_kabupaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(provinsi.getEditableText().toString())) {
                    if (!MyApplication.isNetworkAvailable(getActivity())) {
                        ((ProfileActivity) getActivity()).showSnackBar();
                    }
                    getDistrictList();
                } else {
                    getErrorDialog("Pilih provinsi");
                }
            }
        });

        kecamatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(provinsi.getEditableText().toString())) {
                    if (!TextUtils.isEmpty(et_kabupaten.getEditableText().toString())) {
                        if (!MyApplication.isNetworkAvailable(getActivity())) {
                            ((ProfileActivity) getActivity()).showSnackBar();
                        }
                        getKecamatanList();
                    } else {
                        getErrorDialog("Pilih Kabupaten/Kota");
                    }
                } else {
                    getErrorDialog("Pilih Provinsi");
                }

            }
        });

        upload_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_ktp();
            }
        });


        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    if (!MyApplication.isNetworkAvailable(getActivity())) {
                        ((ProfileActivity) getActivity()).showSnackBar();
                    }
                } else {
                    if (validation()) {
                        if (path != null && !path.equals("null") && !path.equals("")) {
                            doFileUpload(path);
                        } else {
                            String imPath = MyApplication.getInstance().getPrefManager().getImagePath();
                            if (imPath != null && !imPath.equals("null") && !imPath.equals("")) {
                                doFileUpload(imPath);
                            } else {
                                resetpassword();
                            }
                        }
                    }
                }

            }
        });

        et_bankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bankSelection(et_bankName);
            }
        });

        setData();
        getBankStatus();
        getBankNameList();
        return view;
    }

    public boolean validation() {
        if (id1 != 0) {
            if (id2 == 0) {
                getErrorDialog("Pilih Kabupaten/Kota");
                return false;
            }
            if (id3 == 0) {
                getErrorDialog("Pilih kecamatan");
                return false;
            }
        }
        if (!bankCode.equalsIgnoreCase("NA")) {
            if (TextUtils.isEmpty(et_bankAccountNumber.getText().toString())) {
                getErrorDialog("Pilih Nomor Rekening");
                return false;
            }
            if (TextUtils.isEmpty(et_bankOwnerName.getText().toString())) {
                getErrorDialog("Pilih Nama Permilik Rekening");
                return false;
            }
        }
        return true;
    }

    // Bank selection;
    private void bankSelection(final EditText et) {
        Log.d("BankSelect", "bankselection");
        if (bankNameArr != null && bankCodeArr != null) {

            final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), bankNameArr, null);
            dialog.title("Pilih Bank");
            dialog.cancelText("Batal");
            dialog.show();

            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.e("BANKCODE", bankCode);
                    et.setText(bankNameArr[position]);
                    bankCode = bankCodeArr[position] + "";
                    dialog.dismiss();

                    et_bankAccountNumber.setText("");
                    et_bankOwnerName.setText("");
                }

                @Override
                public void onCancel() {
                    Log.e("ONCANCEL", "AAHAHAHHAHAHA");
                    et.setText("");
                    bankCode = "NA";
                    et_bankAccountNumber.setText("");
                    et_bankOwnerName.setText("");
                }
            });
        }

    }

    // hold bank name list
    private void getBankNameList() {
        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GET_BANK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BankResponse", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("message")) {
                        JSONArray bankArray = jsonObject.getJSONArray("message");

                        bankCodeArr = new String[bankArray.length()];
                        bankNameArr = new String[bankArray.length()];
                        for (int i = 0; i < bankArray.length(); i++) {
                            JSONObject bankObj = bankArray.getJSONObject(i);
                            ProvinciPozo pozo = new ProvinciPozo();
                            if (bankObj.has("bank_name")) {
                                bankNameList.add(bankObj.getString("bank_name"));
                                bankNameArr[i] = bankObj.getString("bank_name");
                                pozo.setProvinciName(bankObj.getString("bank_name"));
                            }
                            if (bankObj.has("bank_code")) {
                                bankCodeList.add(bankObj.getString("bank_code"));
                                bankCodeArr[i] = bankObj.getString("bank_code");
                                pozo.setProvinciId(bankObj.getString("bank_code"));
                            }
                            allBankList.add(pozo);
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
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        });
        requestQueue.add(stringRequest);
    }

   /* private void getBankNameList() {
        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GET_BANK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BankResponse", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("message")) {
                        JSONArray bankArray = jsonObject.getJSONArray("message");

                        for (int i = 0; i <bankArray.length(); i++) {
                            JSONObject bankData = bankArray.getJSONObject(i);

                            if (bankData.has("bank_name")){
                                if (bankData.has("bank_code")){
                                    bankHashMap.put(bankData.getString("bank_name"),bankData.getString("bank_code"));
                                }
                            }
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
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        });
        requestQueue.add(stringRequest);
    }

    private void bankSelection(final EditText et) {
        Log.d("BankSelect", "bankselection");
        if (bankHashMap != null && bankHashMap != null) {

            final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), bankNameArr, null);
            dialog.title("Pilih Bank");
            dialog.cancelText("Batal");
            dialog.show();

            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {



                    Log.e("BANKCODE",bankCode);
                    et.setText(bankNameArr[position]);
                    bankCode = bankCodeArr[position] + "";
                    dialog.dismiss();

                    et_bankAccountNumber.setText("");
                    et_bankOwnerName.setText("");
                }

                @Override
                public void onCancel() {
                    Log.e("ONCANCEL","AAHAHAHHAHAHA");
                    et.setText("");
                    bankCode="";
                    et_bankAccountNumber.setText("");
                    et_bankOwnerName.setText("");
                }
            });
        }

    }*/


    private void setData() {
        MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

        String temp = manager.getImage();
        String imPath = manager.getImagePath();
        if (imPath != null && !imPath.equals("null") && !imPath.equals("")) {
            if (new File(imPath).exists()) {
                Bitmap bitmap = getBitmap(imPath);
                Log.d("bitmapPATH", bitmap + "");
                profile_image.setImageBitmap(bitmap);
                // profile_image.setImageBitmap(getBitmap(new File(imPath)));
            } else {
                downloadImage(temp, profile_image);
            }
        } else {
            if (temp != null && !temp.equals("null") && !temp.equals("")) {
                downloadImage(temp, profile_image);
            }
        }

        temp = manager.getFirstName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            name.setText(temp);
        }

        temp = manager.getCoustmerId();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            customer_id.setText("Customer " + temp);
        }


        temp = manager.getEmail();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            email.setText(temp);
        }

        temp = manager.getPhoneNumber();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            phone.setText(temp);
        }

        temp = manager.getGender();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            if (temp.equalsIgnoreCase("M") || temp.equalsIgnoreCase("F"))
                if (temp.equalsIgnoreCase("M") || temp.equalsIgnoreCase("F")) {
                    if (temp.equalsIgnoreCase("M"))
                        gender.setText("Laki-laki");
                    else
                        gender.setText("Perempuan");
                } else
                    gender.setText(temp);
        }

        temp = manager.getDOB();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            dateOfBirth.setText(temp);
        }

        temp = manager.getAddress();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            address.setText(temp);
        }

        temp = manager.getCity();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_city.setText(temp);
        }
        temp = manager.getZipCode();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            zipcode.setText(temp);
        }

        temp = manager.getState();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            provinsi.setText(temp);
        }

        temp = manager.getkecamatan();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            kecamatan.setText(temp);
        }

        temp = manager.getShopName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            store.setText(temp);
        }

        temp = manager.getDistrict();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_kabupaten.setText(temp);
        }

        temp = manager.getBankName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            text_bankName.setText(temp);
        }

        temp = manager.getBankAccountNumber();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            text_bankAccountNumber.setText(temp);
        }

        temp = manager.getBankOwnerName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            text_bankOwnerName.setText(temp);
        }


    }

    public File saveImage(Bitmap bitmap, boolean flag) {
        File filename;
        String imageName = System.currentTimeMillis() + "";
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            File file = new File(path + "/AlfaMind/User/");
            file.mkdirs();
            if (!flag) {
                File temp = new File(path + "/AlfaMind/temp/");
                temp.mkdirs();
                filename = new File(path + "/AlfaMind/temp/" + "IMG_" + imageName + ".jpg");
            } else {
                filename = new File(path + "/AlfaMind/User/" + "IMG_PROFILE" + ".jpg");
            }
            FileOutputStream out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            if (!flag) {
                profile_image.setImageBitmap(bitmap);
                this.path = filename.getPath();
            } else {
                MyApplication.getInstance().getPrefManager().setImagePath(filename.getPath());
            }
            return filename;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void provinciSelection(final EditText et_provinsi) {
        Log.d("BankSelect", "bankselection");
        final String pNameArr[] = new String[provinciList.size()];
        for (int i = 0; i < provinciList.size(); i++) {
            pNameArr[i] = provinciList.get(i).getProvinciName();
        }
        Arrays.sort(pNameArr);

        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), pNameArr, null);
        dialog.title("Pilih Provinsi");
        dialog.cancelText("Membatalkan").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                et_provinsi.setText(pNameArr[position]);
                et_kabupaten.setText("");
                kecamatan.setText("");

                name1 = pNameArr[position];
                id1 = Integer.parseInt(provinciList.get(position).getProvinciId());
                id2 = 0;
                id3 = 0;
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

        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), pNameArr, null);
        dialog.title("Pilih Kabupaten/Kota");
        dialog.cancelText("Membatalkan").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                //et_provinsi.setText(pNameArr[position]);
                et_kabupaten.setText(pNameArr[position]);
                kecamatan.setText("");

                name2 = pNameArr[position];
                id2 = Integer.parseInt(districtList.get(position).getProvinciId());
                id3 = 0;
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

        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), pNameArr, null);
        dialog.title("Pilih Kecamatan");
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

    private void upload_ktp() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                pictureRequest(1);
            }
        } else {
            pictureRequest(1);
        }
    }

    private void pictureRequest(final int val) {
        final String[] list = getResources().getStringArray(R.array.picture_options_list);

        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), list, null);
        dialog.title("");
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
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST1);
                }
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("IMAGEPICK", requestCode + "");
        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == getActivity().RESULT_OK) {
            Uri filePath = data.getData();
            Log.d("IMAGEPICK", mImageUri + "");
            CropImage.activity(filePath)
                    .start(getContext(), this);
        }

        if (requestCode == TAKE_IMAGE_REQUEST1 && resultCode == getActivity().RESULT_OK) {
            CropImage.activity(mImageUri)
                    .start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(getActivity())
                        .load(resultUri)
                        .asBitmap()
                        .placeholder(R.drawable.ic_camera_two)
                        .into(new SimpleTarget<Bitmap>(800, 800) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                Bitmap bitmap = resource;

                                bitmap = getResizedBitmap(bitmap, 500);
                                File file = saveImage(bitmap, false);
                                path = file.getPath();
                            }

                            @Override
                            public void onLoadCleared(Drawable placeholder) {
                                super.onLoadCleared(placeholder);
                            }
                        });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private boolean checkPermission() {
        int external = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permissions = new ArrayList<>();

        if (external != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            int WRITE_EXTERNAL_STORAGE_REQUEST = 1002;
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

    private void takePicture(final int val) {
        File photo = new File("");
        try {
            String name = System.currentTimeMillis() + "";
            photo = this.createTemporaryFile(name, ".jpg");
            photo.delete();
        } catch (Exception e) {
            //  Toast.makeText(context, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT);
            getErrorDialog("Please check SD card! Image shot is impossible!");
        }
        if (Build.VERSION.SDK_INT >= 24)
            mImageUri = FileProvider.getUriForFile(getActivity(), "id.meteor.AlfaMind.fileProvider", photo);
        else
            mImageUri = Uri.fromFile(photo);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, TAKE_IMAGE_REQUEST1);


    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public void getErrorDialog(final String str) {
        final String[] list = {str};
        final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(getActivity(), list, null);
        dialog.title("");
        dialog.cancelText("Ok");
        dialog.show();
    }


    /////////////////////////////   Update Profile ................

    @SuppressLint("StaticFieldLeak")
    private void doFileUpload(final String filePath) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(200, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .build();

                MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
                //MediaType mediaType2 = MediaType.parse("image/jpeg");


                MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

                String access_token = manager.getAccessToken();
                Log.d("UPDATEPARAM", access_token + "");
                if (access_token != null && !access_token.equals("null") && !access_token.equals("")) {

                } else {
                    access_token = "";
                    Log.d("UPDATEPARAM", " els");

                }

                String store_name = store.getText().toString();
                Log.d("UPDATEPARAM", store_name + "");
                if (store_name != null && !store_name.equals("null") && !store_name.equals("")) {

                } else {
                    store_name = "";
                    Log.d("UPDATEPARAM", " els");

                }

                String email = manager.getEmail();
                Log.d("UPDATEPARAM", email + "");
                if (email != null && !email.equals("null") && !email.equals("")) {

                } else {
                    email = "";
                    Log.d("UPDATEPARAM", " els");

                }

                String address1 = address.getText().toString();
                Log.d("UPDATEPARAM", address1 + "");
                if (address1 != null && !address1.equals("null") && !address1.equals("")) {

                } else {
                    address1 = "";
                    Log.d("UPDATEPARAM", " els");

                }

                String city = et_city.getText().toString();
                Log.d("UPDATEPARAM", city + "");
                if (city != null && !city.equals("null") && !city.equals("")) {

                } else {
                    city = "";
                    Log.d("UPDATEPARAM", " els");

                }

                String postcode = zipcode.getText().toString();
                Log.d("UPDATEPARAM", postcode + "");
                if (postcode != null && !postcode.equals("null") && !postcode.equals("")) {

                } else {
                    postcode = "";
                    Log.d("UPDATEPARAM", " els");

                }

                String phone_mobile = phone.getText().toString();
                Log.d("UPDATEPARAM", phone_mobile + "");
                if (phone_mobile != null && !phone_mobile.equals("null") && !phone_mobile.equals("")) {

                } else {
                    phone_mobile = "";
                    Log.d("UPDATEPARAM", " els");

                }

                File file = new File(filePath);

                String password = manager.getPassword();

                MultipartBody.Builder multipartBody = new MultipartBody.Builder();


                multipartBody.setType(MultipartBody.FORM);

                multipartBody.addFormDataPart("avatar", file.getName(),
                        RequestBody.create(mediaType, file));

                multipartBody.addFormDataPart("old_passwd", password + "");
                multipartBody.addFormDataPart("passwd", password + "");
                multipartBody.addFormDataPart("confirm_passwd", password + "");
                multipartBody.addFormDataPart("access_token", access_token + "");
                multipartBody.addFormDataPart("store_name", store_name + "");
                multipartBody.addFormDataPart("email", email + "");
                multipartBody.addFormDataPart("address1", address1 + "");
                multipartBody.addFormDataPart("phone_mobile", phone_mobile + "");
                multipartBody.addFormDataPart("city", city + "");
                multipartBody.addFormDataPart("postcode", postcode + "");

                if (!bankCode.equalsIgnoreCase("NA")) {
                    multipartBody.addFormDataPart("rek_bank", bankCode + "");
                    multipartBody.addFormDataPart("rek_number", et_bankAccountNumber.getText().toString() + "");
                    multipartBody.addFormDataPart("rek_name", et_bankOwnerName.getText().toString() + "");
                }

                if (id1 != 0) {
                    multipartBody.addFormDataPart("pic_lv1_province", id1 + "");
                    multipartBody.addFormDataPart("pic_lv2_kabupaten", id2 + "");
                    multipartBody.addFormDataPart("pic_lv3_kecamatan", id3 + "");
                }

                RequestBody formBody = multipartBody.build();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(Constant.UPDATE_USER_INFO)
                        .post(formBody)
                        .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "4f0ea30d-fb70-c2f4-14c8-b58743be20f4")
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.d("fileResponse", result + "");
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equals("success")) {

                            // showAlertDialog(jsonObject.getString("message"));
                            showAlertDialog("Perubahan Profie berhasil", true);


                        } else {
                            showAlertDialog(jsonObject.getString("message"), false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void resetpassword() {

        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.UPDATE_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ForgotPassword", response + "");
                try {
                    progressDialog.dismiss();

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equals("11") || jsonObject.getString("status").equalsIgnoreCase("success")) {
                            //MyApplication.getInstance().getPrefManager().setPassword(.getText().toString());

                            loginToUser();
                        } else {
                            showAlertDialog(jsonObject.getString("message"), false);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // getErrorDialog("Something went wrong");
                Log.d("VollyError", " something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

                String access_token = manager.getAccessToken();
                Log.d("UPDATEPARAM", access_token + "");
                if (access_token != null && !access_token.equals("null") && !access_token.equals("")) {
                    param.put("access_token", access_token);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("access_token", "");
                }

                String store_name = manager.getShopName();
                Log.d("UPDATEPARAM", store_name + "");
                if (store_name != null && !store_name.equals("null") && !store_name.equals("")) {
                    param.put("store_name", store_name);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("store_name", "Ab");
                }

                String email = manager.getEmail();
                Log.d("UPDATEPARAM", email + "");
                if (email != null && !email.equals("null") && !email.equals("")) {
                    param.put("email", email);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("email", "");
                }

                String address1 = manager.getAddress();
                Log.d("UPDATEPARAM", address1 + "");
                if (address1 != null && !address1.equals("null") && !address1.equals("")) {
                    param.put("address1", address1);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("address1", "");
                }

                String city = manager.getCity();
                Log.d("UPDATEPARAM", city + "");
                if (city != null && !city.equals("null") && !city.equals("")) {
                    param.put("city", city);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("city", "");
                }

                String postcode = manager.getZipCode();
                Log.d("UPDATEPARAM", postcode + "");
                if (postcode != null && !postcode.equals("null") && !postcode.equals("")) {
                    param.put("postcode", postcode);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("postcode", "");
                }

                String phone_mobile = manager.getPhoneNumber();
                Log.d("UPDATEPARAM", phone_mobile + "");
                if (phone_mobile != null && !phone_mobile.equals("null") && !phone_mobile.equals("")) {
                    param.put("phone_mobile", phone_mobile);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("phone_mobile", "");
                }

                String passwd = manager.getPassword();
                Log.d("UPDATEPARAM", passwd + "");
                if (passwd != null && !passwd.equals("null") && !passwd.equals("")) {

                    param.put("old_passwd", passwd);
                    param.put("passwd", passwd);
                    param.put("confirm_passwd", passwd);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("old_passwd", "");
                    param.put("passwd", "");
                    param.put("confirm_passwd", "");
                }

                param.put("avatar", "");


                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    ////// login user..........
    private void loginToUser() {

        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.USER_AUTHENTICATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LOGIN_TO_USER_TIMER", response + "");
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        MyApplication.getInstance().getPrefManager().setIsLogged(true);
                        //saveValue();
                        if (jsonObject.has("access_token")) {
                            MyApplication.getInstance().getPrefManager().setAccessToken(jsonObject.getString("access_token"));
                        }
                        getUserInfo();
                    } else {
                        showAlertDialog(jsonObject.getString("message"), false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //showAlertDialog("server error");
                    progressDialog.dismiss();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((ProfileActivity) getActivity()).showSnackBar();
                }
                progressDialog.dismiss();
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

                String email = manager.getEmail();
                String password = manager.getPassword();
                Log.d("LOGINPARAM", email + "  -  " + password + "");

                param.put("email", email + "");
                param.put("passwd", password + "");
                param.put("device_id", "na");

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    //////////////////////// get User Info.......

    private void getUserInfo() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LOGINTOUSER", response + "");
                try {
                    progressDialog.dismiss();

                    JSONObject jsonObject = new JSONObject(response);

                    MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("user_data")) {
                            JSONObject object = jsonObject.getJSONObject("user_data");
                            if (object.has("avatar")) {
                                manager.setImage(object.getString("avatar"));
                                downloadImage(manager.getImage(), null);
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
                            if (object.has("bank_account")){
                                if (object.getString("bank_account").equalsIgnoreCase("none"))
                                    manager.setBankName("");
                                else
                                    manager.setBankName(object.getString("bank_account"));
                            }
                            if (object.has("bank_account_name")){
                                manager.setBankOwnerName(object.getString("bank_account_name"));
                            }
                            if (object.has("bank_account_number")){
                                manager.setBankAccountNumber(object.getString("bank_account_number"));
                            }
                        }

                    } else {
                        showAlertDialog(jsonObject.getString("message"), false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getErrorDialog("server error");
                    progressDialog.dismiss();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((MainActivity) getActivity()).showSnackBar();
                }
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


    @SuppressLint("StaticFieldLeak")
    private void showAlertDialog(final String msg, final boolean b) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("Dialog", "Alert");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setMessage(msg);
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (b) {
                            loginToUser();
                        }
                        dialog.cancel();
                    }
                });
                AlertDialog alert = dialogBuilder.create();
                alert.show();
            }
        });
    }


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

    public void downloadImage(String url, final RoundedImageView imageView) {
        Log.d("ImageUrl", "  " + url + "");
        Glide.with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(100, 100) {


                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                        if (imageView != null) {
                            imageView.setImageBitmap(resource);

                        } else {
                            saveImage(resource, true);
                            getActivity().onBackPressed();
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        if (!MyApplication.isNetworkAvailable(getActivity())) {
                            ((ProfileActivity) getActivity()).showSnackBar();
                        }

                    }
                });
    }

    // get Services

    private void getAddressList() {
        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GET_AREA, new Response.Listener<String>() {
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
        });
        requestQueue.add(stringRequest);
    }

    private void getDistrictList() {
        Log.d("District", " call " + id1);
        progressDialog.show();
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
                progressDialog.dismiss();


                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((SignupActivity) getActivity()).showSnackBar();
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
        progressDialog.show();
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
            progressDialog.dismiss();
            districtSelection();

        } catch (JSONException e) {
            progressDialog.dismiss();
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
            progressDialog.dismiss();
            kecamatanSelection();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getBankStatus() {

        ((ProfileActivity) getActivity()).showProgress("Please wait...", false);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BANK_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BANK_STATUS_Response", response + "");
                ((ProfileActivity) getActivity()).closeProgress();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            if (jsonObject.has("ek_bank")) {
                                if (jsonObject.getString("ek_bank").equalsIgnoreCase("valid"))
                                    bank_text.setVisibility(View.VISIBLE);
                                else
                                    bank_edit.setVisibility(View.VISIBLE);
                            }
                            if (jsonObject.has("edited")) {
                                if (jsonObject.getString("edited").equalsIgnoreCase("1"))
                                    bank_text.setVisibility(View.VISIBLE);
                                else
                                    bank_edit.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ((ProfileActivity) getActivity()).getErrorDialog(jsonObject.getString("message"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((ProfileActivity) getActivity()).closeProgress();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((ProfileActivity) getActivity()).closeProgress();
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                String token = MyApplication.getInstance().getPrefManager().getAccessToken();
                Log.d("access_token", token);
                param.put("access_token", token);
                return param;
            }
        };
        ;
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        File dir = new File(Environment.getExternalStorageDirectory() + "/AlfaMind/temp/");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }
}
