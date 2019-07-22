package id.meteor.alfamind.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import id.meteor.alfamind.Activity.Jaringan_AnggotaActivity;
import id.meteor.alfamind.Activity.SignupActivity;
import id.meteor.alfamind.Activity.TermsAndConditionActivity;
import id.meteor.alfamind.Model.ProvinciPozo;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetDialog;
import id.meteor.alfamind.customDialog.OnOperItemClickL;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.EditFocusChange;
import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.PatternEditableBuilder;
import id.meteor.alfamind.helper.SignUpManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupThirdFragment extends Fragment {

    private SignupActivity signupActivity;
    boolean back = true;
    private ArrayList<String> bankNameList;
    private ArrayList<String> bankCodeList;
    private ArrayList<ProvinciPozo> allBankList;

    private String bankNameArr[];
    private String bankCodeArr[];
    private String bankCode = "", DATA;

    private ImageView captchaImage;
    private EditText et_nama_bank, et_acount_number, et_owner_name, et_captcha, et_email;

    private CheckBox check_box;
    private SignUpManager signUpmanager;

    public SignupThirdFragment() {
        // Required empty public constructor
    }

    public void setActivity(SignupActivity signupActivity){this.signupActivity = signupActivity;}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_third, container, false);

        DATA = getArguments().getString("DATA");
        Log.e("BUNDLEDATA", getArguments().getString("DATA") + "");

        ScrollView scrollView = view.findViewById(R.id.scroll);
        check_box = view.findViewById(R.id.check_box);
        captchaImage = view.findViewById(R.id.image_captcha);
        ImageView refresh_captcha = view.findViewById(R.id.refresh_captcha);
        TextView next_btn = view.findViewById(R.id.next);
        et_nama_bank = view.findViewById(R.id.et_nama_bank);
        et_acount_number = view.findViewById(R.id.et_acount_number);
        et_owner_name = view.findViewById(R.id.et_owner_name);
        et_captcha = view.findViewById(R.id.et_captcha);
        et_email = view.findViewById(R.id.et_email);
        TextInputLayout ti_email = view.findViewById(R.id.ti_email);
        TextView termsAndCondition = view.findViewById(R.id.termsAndCondition);
        TextView referral = view.findViewById(R.id.referral);

        et_nama_bank.setKeyListener(null);

        et_nama_bank.setOnFocusChangeListener(new EditFocusChange(et_nama_bank, scrollView));
        et_acount_number.setOnFocusChangeListener(new EditFocusChange(et_nama_bank, scrollView));
        et_owner_name.setOnFocusChangeListener(new EditFocusChange(et_nama_bank, scrollView));
        et_email.setOnFocusChangeListener(new EditFocusChange(et_nama_bank, scrollView));

        new PatternEditableBuilder().
                addPattern(Pattern.compile("Syarat & Ketentuan"), new PatternEditableBuilder.SpannableClickedListener() {
                    @Override
                    public void onSpanClicked(String text) {
                        if (MyApplication.isNetworkAvailable(getActivity())) {

                            Intent intent = new Intent(MyApplication.getInstance(), TermsAndConditionActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        } else {
                            ((SignupActivity) getActivity()).showSnackBar();
                        }

                    }
                }).into(termsAndCondition);

        if (DATA.equalsIgnoreCase("NEW_NETWORK")) {
            referral.setVisibility(View.GONE);
            ti_email.setVisibility(View.GONE);
            et_email.setText(MyApplication.getInstance().getPrefManager().getEmail() + "");
        }

        bankNameList = new ArrayList<>();// hold bank name
        bankCodeList = new ArrayList<>(); // hold bank code
        allBankList = new ArrayList<>(); // hold bank code
        getBankNameList(); //call get bank service
        downloadCaptchaImage(0); // call getCaptcha service

        // next btn
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveValueInSharedPrefence();

                if (validation()) {

                    if (!check_box.isChecked()) {
                        String warningMsg = "Anda harus mencentang Syarat dan Ketentuan";
                        showAlertDialog(warningMsg, true);
                    } else {
                        if (MyApplication.isNetworkAvailable(getActivity())) {
                            doFileUpload();
                        } else {
                            signupActivity.getErrorDialog("Tidak ada koneksi internet");
                        }
                    }
                }
            }
        });

        // banmk name
        et_nama_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bankSelection(et_nama_bank);

            }
        });

        refresh_captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadCaptchaImage(1);

            }
        });

        // next_btn.setEnabled(false);
       /* check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    next_btn.setEnabled(true);
                    next_btn.setTextColor(getActivity().getResources().getColor(R.color.white));
                }else {
                    next_btn.setEnabled(false);
                    next_btn.setTextColor(getActivity().getResources().getColor(R.color.pager_color));
                }
            }
        });*/

        return view;
    }

    @Override
    public void onResume() {
        signUpmanager = MyApplication.getInstance().getSignupManager();
        setData(); // save from pref.


        Log.d("BANK_CODE", bankCode + "");
        super.onResume();
    }

    @Override
    public void onDestroy() {

        Log.d("onDestroy", back + "");
        if (back) {
            saveValueInSharedPrefence();
        }
        super.onDestroy();
    }

    private void setData() {
        // save bank name
        String temp = signUpmanager.getBankName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_nama_bank.setText(temp);
            et_acount_number.setFocusableInTouchMode(true);
            et_acount_number.setClickable(true);
            et_owner_name.setClickable(true);
            et_owner_name.setFocusableInTouchMode(true);
            bankCode = signUpmanager.getBankCode() + "";
        }
        // save account number
        temp = signUpmanager.getAccountNumber();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_acount_number.setText(temp);
        }
        // save owner name
        temp = signUpmanager.getBankOwnerName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_owner_name.setText(temp);
        }
        temp = signUpmanager.getRememberEmail();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_email.setText(temp);
        }


    }

    private boolean validation() {

        if (!TextUtils.isEmpty(et_nama_bank.getText().toString())) {
            if (TextUtils.isEmpty(et_acount_number.getText().toString())) {
                signupActivity.getErrorDialog("Masukkan nomor rekening");
                return false;
            } else {
                int len = et_acount_number.getText().toString().length();
                if (len < 5) {
                    signupActivity.getErrorDialog("Nomor acccount harus lebih besar dari 4 digit");
                    return false;
                }
            }
            if (TextUtils.isEmpty(et_owner_name.getText().toString())) {
                signupActivity.getErrorDialog("Masukkan nama pemilik");
                return false;
            }
        }

        if (TextUtils.isEmpty(et_captcha.getText().toString())) {
            signupActivity.getErrorDialog("Masukkan teks captcha");
            return false;
        }

        if (!TextUtils.isEmpty(et_email.getText().toString())) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
                signupActivity.getErrorDialog("Masukkan alamat email yang benar"); //enter valid email address
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



                    Log.e("BANKCODE",bankCode);
                    et.setText(bankNameArr[position]);
                    bankCode = bankCodeArr[position] + "";
                    dialog.dismiss();

                    et_acount_number.setFocusableInTouchMode(true);
                    et_acount_number.setClickable(true);
                    et_owner_name.setClickable(true);
                    et_owner_name.setFocusableInTouchMode(true);

                    et_acount_number.setText("");
                    et_owner_name.setText("");
                }

                @Override
                public void onCancel() {
                    Log.e("ONCANCEL","AAHAHAHHAHAHA");
                    et.setText("");
                    bankCode="";
                    et_acount_number.setText("");
                    et_owner_name.setText("");
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

    /// downloadCaptchaImage and show
    private void downloadCaptchaImage(final int val) {
        if (val == 1) {
            signupActivity.showProgress("Please wait...", false);
        }
        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GET_CAPTCHA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CaptchaResponse", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("captcha_url")) {
                        String cUrl = jsonObject.getString("captcha_url");
                        if (val == 1) {
                            signupActivity.closeProgress();
                        }
                        showCaptchaImage(cUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (val == 1) {
                        signupActivity.closeProgress();
                    }
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (val == 1) {
                    signupActivity.closeProgress();
                }
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        });
        requestQueue.add(stringRequest);
    }

    private void showCaptchaImage(String cUrl) {
        Log.d("CaptchaUrl", cUrl + "");
        Glide.with(getContext())
                .load(cUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(300, 300) {


                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                        captchaImage.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
    }


    // save to pref.

    public void saveValueInSharedPrefence() {
        //MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();
        //  SignUpManager manager = MyApplication.getInstance().getPrefManager();
        signUpmanager.setBankCode(bankCode + "");
        signUpmanager.setBankName(et_nama_bank.getText().toString());
        signUpmanager.setAccountNumber(et_acount_number.getText().toString());
        signUpmanager.setBankOwnerName(et_owner_name.getText().toString());
        signUpmanager.setRememberEmail(et_email.getText().toString());
    }


    ///////  submit details to server


    @SuppressLint("StaticFieldLeak")
    private void doFileUpload() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                signupActivity.showProgress("Please wait...", false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                signupActivity.closeProgress();
            }

            @Override
            protected String doInBackground(String... params) {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(200, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
                // RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"file\"; filename=\""+videoPath+"\"\r\nContent-Type: image/png\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");


                //final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

                String deviceId = "deviceid-blabla";
               /* final TelephonyManager mTelephony = (TelephonyManager) getActivity().getSystemService(
                        Context.TELEPHONY_SERVICE);
                if (mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId(); /*\ use for mobiles
                 } else {
                 deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                 Settings.Secure.ANDROID_ID); /* use for tablets
                 }
                 if (deviceId==null)
                 deviceId = "abcde";*/

                //       MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

                if (new File(SignupSecondFragment.path1).exists()) {
                    Log.d("FILE_PATH", "true");
                } else {
                    Log.d("FILE_PATH", "false");
                }

                String eEmail = "na";
                String acNo = "";
                String holdername = "";
                if (!TextUtils.isEmpty(et_email.getText().toString())) {
                    eEmail = et_email.getEditableText().toString();
                }
                if (!TextUtils.isEmpty(et_nama_bank.getText().toString())) {
                    acNo = et_acount_number.getEditableText().toString();
                    holdername = et_owner_name.getEditableText().toString();
                } else {
                    bankCode = "";
                }
                /*if(bankCode==null){
                    bankCode ="";
                }*/

                Log.e("BANK_CODE", bankCode);

                MultipartBody.Builder multipartBody = new MultipartBody.Builder();


                multipartBody.setType(MultipartBody.FORM);
                multipartBody.addFormDataPart("govt_id", "govt_id.jpg",
                        RequestBody.create(mediaType, new File(SignupSecondFragment.path1)));

                if (SignupSecondFragment.path2 != null) {
                    Log.e("HERE", "YES");
                    multipartBody.addFormDataPart("npwp", "npwp.jpg",
                            RequestBody.create(mediaType, new File(SignupSecondFragment.path2)));
                    Log.e("DADADADADAD",SignupSecondFragment.path2+"");
                }
                multipartBody.addFormDataPart("id_gender", signUpmanager.getGender().equalsIgnoreCase("Laki-laki") ? "1" : "2");
                multipartBody.addFormDataPart("firstname", signUpmanager.getFirstName());
                multipartBody.addFormDataPart("lastname", signUpmanager.getLastName());
                multipartBody.addFormDataPart("email", signUpmanager.getEmail());
                multipartBody.addFormDataPart("days", signUpmanager.getBirthDate());
                multipartBody.addFormDataPart("months", signUpmanager.getBirthMonth());
                multipartBody.addFormDataPart("years", signUpmanager.getBirthYear());
                multipartBody.addFormDataPart("address1", signUpmanager.getAddress());
                multipartBody.addFormDataPart("city", signUpmanager.getCity());
                multipartBody.addFormDataPart("postcode", signUpmanager.getZipCode());
                multipartBody.addFormDataPart("phone_mobile", signUpmanager.getPhoneNumber());
                multipartBody.addFormDataPart("referral", eEmail);
                multipartBody.addFormDataPart("reg_sauce", "alfamind-lite");
                multipartBody.addFormDataPart("pic_lv1_province", signUpmanager.getState());
                multipartBody.addFormDataPart("pic_lv2_kabupaten", signUpmanager.getDistrict());
                multipartBody.addFormDataPart("pic_lv3_kecamatan", signUpmanager.getkecamatan());
                multipartBody.addFormDataPart("captcha", et_captcha.getEditableText().toString());
                multipartBody.addFormDataPart("rek_bank", bankCode + "");
                multipartBody.addFormDataPart("rek_number", acNo);
                multipartBody.addFormDataPart("rek_name", holdername);
                multipartBody.addFormDataPart("govt_id_number", signUpmanager.getNomor());
                multipartBody.addFormDataPart("id_install", deviceId);
                multipartBody.addFormDataPart("tc_agree_status", 1 + "");


                RequestBody formBody = multipartBody.build();


                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(Constant.USER_REGISTER)
                        .post(formBody)
                        .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "dc3508a5-487a-8912-07a2-4fc1097a275c")
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.d("fileResponse", result + "");
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equals("success")) {

                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }
                                SignupActivity.isRegister = true;
                                back = false;
                                showAlertDialog(jsonObject.getString("message"), false);
                                Log.d("fileResponse", result + "");


                        } else {
                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }
                            showAlertDialog(jsonObject.getString("message"), true);
                            Log.d("fileResponse", result + "");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    private void showAlertDialog(final String msg, final boolean tag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("Dialog", "Alert");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setMessage(msg);
                dialogBuilder.setCancelable(tag);

                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (!tag) {
                            dialog.cancel();
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            if (DATA.equalsIgnoreCase("NEW_NETWORK"))
                                startActivity(new Intent(getActivity(), Jaringan_AnggotaActivity.class));
                        }

                    }
                });
                AlertDialog alert = dialogBuilder.create();
                alert.show();

            }
        });
    }
}