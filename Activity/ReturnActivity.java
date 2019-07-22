package id.meteor.alfamind.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetDialog;
import id.meteor.alfamind.customDialog.OnOperItemClickL;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.EditFocusChange;
import id.meteor.alfamind.helper.MyApplication;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ReturnActivity extends BaseActivity {

    private EditText et_produk, et_shipment, et_jumlah, et_detail, et_konsumen_nama, et_konsumen_alamat, et_resi, et_konsumen_nomor, et_konsumen_email, et_ekspedisi;
    private Uri mImageUri;
    private ArrayList<String> ekspedisiList = new ArrayList<>();
    private final int WRITE_EXTERNAL_STORAGE_REQUEST = 1002;
    private final int CAMERA_REQUEST = 1005;
    private final int PICK_IMAGE_REQUEST1 = 100;
    private final int PICK_IMAGE_REQUEST2 = 200;
    private final int PICK_IMAGE_REQUEST3 = 300;
    private final int PICK_IMAGE_REQUEST4 = 400;
    private final int TAKE_IMAGE_REQUEST1 = 101;
    private final int TAKE_IMAGE_REQUEST2 = 201;
    private final int TAKE_IMAGE_REQUEST3 = 301;
    private final int TAKE_IMAGE_REQUEST4 = 401;
    public static String path1, path2, path3, path4;
    private ImageView photo1_img2, photo2_img2, photo3_img2, photo4_img2;
    private int ekspedisiValue;
    private String[] photoArray = new String[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        ImageView back_btn = findViewById(R.id.back_btn);
        et_produk = findViewById(R.id.et_produk);
        et_shipment = findViewById(R.id.et_shipment);
        et_jumlah = findViewById(R.id.et_jumlah);
        et_detail = findViewById(R.id.et_detail);
        et_konsumen_nama = findViewById(R.id.et_konsumen_nama);
        et_konsumen_alamat = findViewById(R.id.et_konsumen_alamat);
        et_konsumen_nomor = findViewById(R.id.et_konsumen_nomor);
        et_konsumen_email = findViewById(R.id.et_konsumen_email);
        et_ekspedisi = findViewById(R.id.et_ekspedisi);
        et_resi = findViewById(R.id.et_resi);
        RelativeLayout photo1_lay = findViewById(R.id.photo1_lay);
        RelativeLayout photo2_lay = findViewById(R.id.photo2_lay);
        RelativeLayout photo3_lay = findViewById(R.id.photo3_lay);
        RelativeLayout photo4_lay = findViewById(R.id.photo4_lay);
        photo1_img2 = findViewById(R.id.photo1_img2);
        photo2_img2 = findViewById(R.id.photo2_img2);
        photo3_img2 = findViewById(R.id.photo3_img2);
        photo4_img2 = findViewById(R.id.photo4_img2);
        ScrollView scrollView = findViewById(R.id.scrollView);
        TextView next = findViewById(R.id.next);

        et_jumlah.setOnFocusChangeListener(new EditFocusChange(et_jumlah, scrollView));
        et_resi.setOnFocusChangeListener(new EditFocusChange(et_resi, scrollView));
        et_ekspedisi.setOnFocusChangeListener(new EditFocusChange(et_ekspedisi, scrollView));
        et_konsumen_nama.setOnFocusChangeListener(new EditFocusChange(et_konsumen_nama, scrollView));
        et_konsumen_alamat.setOnFocusChangeListener(new EditFocusChange(et_konsumen_alamat, scrollView));
        et_konsumen_nomor.setOnFocusChangeListener(new EditFocusChange(et_konsumen_nomor, scrollView));
        et_konsumen_email.setOnFocusChangeListener(new EditFocusChange(et_konsumen_email, scrollView));

        for (int i = 0; i < photoArray.length; i++) {
            photoArray[i] = "";
        }


        if (MyApplication.isNetworkAvailable(this)) {
            getEkspedisiList();
        } else {
            showSnackBar();
        }


        et_ekspedisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ekspedisiSelection();
            }
        });

        photo1_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_photo_1();
            }
        });

        photo2_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_photo_2();
            }
        });

        photo3_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_photo_3();
            }
        });

        photo4_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_photo_4();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    showProgress("Please wait...",false);
                    doFileUpload();
                }
            }
        });


    }

    private void upload_photo_1() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                pictureRequest(1);
            }

        } else {
            pictureRequest(1);
        }
    }

    private void upload_photo_2() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                pictureRequest(2);
            }

        } else {
            pictureRequest(2);
        }
    }

    private void upload_photo_3() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                pictureRequest(3);
            }

        } else {
            pictureRequest(3);
        }
    }

    private void upload_photo_4() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                pictureRequest(4);
            }

        } else {
            pictureRequest(4);
        }
    }

    private void ekspedisiSelection() {
        Log.d("BankSelect", "bankselection");
        final String pNameArr[] = new String[ekspedisiList.size()];
        for (int i = 0; i < ekspedisiList.size(); i++) {
            pNameArr[i] = ekspedisiList.get(i);
        }

        final ActionSheetDialog dialog = new ActionSheetDialog(this, pNameArr, null);
        dialog.title("Pilih Ekspedisi");
        dialog.cancelText("Membatalkan").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_ekspedisi.setText(pNameArr[position]);
                ekspedisiValue = position + 1;
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void pictureRequest(final int val) {
        final String[] list = getResources().getStringArray(R.array.picture_options_list);

        final ActionSheetDialog dialog = new ActionSheetDialog(this, list, null);
        dialog.title("Options");
        dialog.cancelText("Cancel").show();

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
                    }
                    if (val == 2) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST2);
                    }
                    if (val == 3) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST3);
                    }
                    if (val == 4) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST4);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });
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
            mImageUri = FileProvider.getUriForFile(this, "id.meteor.AlfaMind.fileProvider", photo);
        else
            mImageUri = Uri.fromFile(photo);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (val == 1) {
            startActivityForResult(intent, TAKE_IMAGE_REQUEST1);
        }
        if (val == 2) {
            startActivityForResult(intent, TAKE_IMAGE_REQUEST2);
        }
        if (val == 3) {
            startActivityForResult(intent, TAKE_IMAGE_REQUEST3);
        }
        if (val == 4) {
            startActivityForResult(intent, TAKE_IMAGE_REQUEST4);
        }

    }

    private boolean checkPermission() {
        int external = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permissions = new ArrayList<>();

        if (external != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), WRITE_EXTERNAL_STORAGE_REQUEST);
            return false;
        }

        return true;
    }


    private boolean checkPermission1() {
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> permissions = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), CAMERA_REQUEST);
            return false;
        }

        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("IMAGEPICK", requestCode + "");
        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK) {
            Log.d("IMAGEPICK", " pick1");
            Uri filePath = data.getData();
            Glide.with(this)
                    .load(filePath)
                    .asBitmap()
                    .placeholder(R.drawable.ic_camera_two)
                    .into(new SimpleTarget<Bitmap>(800, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            File file = saveImage(resource);
                            path1 = file.getPath();
                            photoArray[0] = file.getPath() + "";
                            photo1_img2.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            super.onLoadCleared(placeholder);
                        }
                    });
        }

        if (requestCode == TAKE_IMAGE_REQUEST1 && resultCode == RESULT_OK) {
            Log.d("IMAGEPICK", " take1 ");
            ContentResolver cr = this.getContentResolver();
            Glide.with(this)
                    .load(mImageUri)
                    .asBitmap()
                    .placeholder(R.drawable.ic_camera_two)
                    .into(new SimpleTarget<Bitmap>(800, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            File file = saveImage(resource);
                            path1 = file.getPath();
                            photoArray[0] = file.getPath() + "";
                            photo1_img2.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            super.onLoadCleared(placeholder);
                        }
                    });


        }


        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK) {

            Log.d("IMAGEPICK", " pick2");
            Uri filePath = data.getData();
            Glide.with(this)
                    .load(filePath)
                    .asBitmap()
                    .placeholder(R.drawable.ic_camera_two)
                    .into(new SimpleTarget<Bitmap>(800, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            File file = saveImage(resource);
                            path2 = file.getPath();
                            photoArray[1] = file.getPath() + "";
                            photo2_img2.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            super.onLoadCleared(placeholder);
                        }
                    });
        }

        if (requestCode == TAKE_IMAGE_REQUEST2 && resultCode == RESULT_OK) {
            ContentResolver cr = this.getContentResolver();
            Log.d("IMAGEPICK", " take2");
            Glide.with(this)
                    .load(mImageUri)
                    .asBitmap()
                    .placeholder(R.drawable.ic_camera_two)
                    .into(new SimpleTarget<Bitmap>(800, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            File file = saveImage(resource);
                            path2 = file.getPath();
                            photoArray[1] = file.getPath() + "";
                            photo2_img2.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            super.onLoadCleared(placeholder);
                        }
                    });
        }
        if (requestCode == PICK_IMAGE_REQUEST3 && resultCode == RESULT_OK) {
            Log.d("IMAGEPICK", " pick1");
            Uri filePath = data.getData();
            Glide.with(this)
                    .load(filePath)
                    .asBitmap()
                    .placeholder(R.drawable.ic_camera_two)
                    .into(new SimpleTarget<Bitmap>(800, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            File file = saveImage(resource);
                            path3 = file.getPath();
                            photoArray[2] = file.getPath() + "";
                            photo3_img2.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            super.onLoadCleared(placeholder);
                        }
                    });
        }

        if (requestCode == TAKE_IMAGE_REQUEST3 && resultCode == RESULT_OK) {
            Log.d("IMAGEPICK", " take1 ");
            ContentResolver cr = this.getContentResolver();
            Glide.with(this)
                    .load(mImageUri)
                    .asBitmap()
                    .placeholder(R.drawable.ic_camera_two)
                    .into(new SimpleTarget<Bitmap>(800, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            File file = saveImage(resource);
                            path3 = file.getPath();
                            photoArray[2] = file.getPath() + "";
                            photo3_img2.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            super.onLoadCleared(placeholder);
                        }
                    });


        }
        if (requestCode == PICK_IMAGE_REQUEST4 && resultCode == RESULT_OK) {
            Log.d("IMAGEPICK", " pick1");
            Uri filePath = data.getData();
            Glide.with(this)
                    .load(filePath)
                    .asBitmap()
                    .placeholder(R.drawable.ic_camera_two)
                    .into(new SimpleTarget<Bitmap>(800, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            File file = saveImage(resource);
                            path4 = file.getPath();
                            photoArray[3] = file.getPath() + "";
                            photo4_img2.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            super.onLoadCleared(placeholder);
                        }
                    });
        }

        if (requestCode == TAKE_IMAGE_REQUEST4 && resultCode == RESULT_OK) {
            Log.d("IMAGEPICK", " take1 ");
            ContentResolver cr = this.getContentResolver();
            Glide.with(this)
                    .load(mImageUri)
                    .asBitmap()
                    .placeholder(R.drawable.ic_camera_two)
                    .into(new SimpleTarget<Bitmap>(800, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            File file = saveImage(resource);
                            path4 = file.getPath();
                            photoArray[3] = file.getPath() + "";
                            photo4_img2.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            super.onLoadCleared(placeholder);
                        }
                    });


        }


        /// after crop image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (result != null) {
                Uri resultUri = result.getUri();

            }
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

    public boolean validation() {
        if (TextUtils.isEmpty(et_produk.getText().toString())) {
            getErrorDialog("Masukkan Nama Produk");
            return false;
        }
        if (TextUtils.isEmpty(et_shipment.getText().toString())) {
            getErrorDialog("Masukkan Nomor Shipment");
            return false;
        }
        if (TextUtils.isEmpty(et_jumlah.getText().toString())) {
            getErrorDialog("Masukkan Jumlah Produk");
            return false;
        }
        if (TextUtils.isEmpty(et_resi.getText().toString())) {
            getErrorDialog("Masukkan Nomor Resi");
            return false;
        }
        if (TextUtils.isEmpty(et_ekspedisi.getText().toString())) {
            getErrorDialog("Pilih Ekspedisi");
            return false;
        }
        if (TextUtils.isEmpty(et_detail.getText().toString())) {
            getErrorDialog("Masukkan Penjelasan detail untuk retur");
            return false;
        }
        if (TextUtils.isEmpty(et_konsumen_nama.getText().toString())) {
            getErrorDialog("Masukkan Nama Konsumen");
            return false;
        }
        if (TextUtils.isEmpty(et_konsumen_alamat.getText().toString())) {
            getErrorDialog("Masukkan Alamat Konsumen");
            return false;
        }
        if (TextUtils.isEmpty(et_konsumen_nomor.getText().toString())) {
            getErrorDialog("Masukkan Nomor HP Konsumen");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_konsumen_email.getText().toString()).matches()) {
            getErrorDialog("Masukkan alamat email yang benar");
            return false;
        } else
            return true;
    }

    private void getEkspedisiList() {
        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_EKSPEDIS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CaptchaResponse", response + "");
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("status")) {
                        if (object.getString("status").equalsIgnoreCase("success")) {
                            if (object.has("message")) {
                                JSONArray jsonArray = object.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if (jsonObject.has("id_ekspedisi")) {
                                        ekspedisiList.add(jsonObject.getString("nama_ekspedisi"));
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    @SuppressLint("StaticFieldLeak")
    private void doFileUpload() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(200, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

                MultipartBody.Builder multipartBody = new MultipartBody.Builder();

                multipartBody.setType(MultipartBody.FORM);
                multipartBody.addFormDataPart("access_token", MyApplication.getInstance().getPrefManager().getAccessToken());
                multipartBody.addFormDataPart("shipment", et_shipment.getText().toString());
                multipartBody.addFormDataPart("nama_penerima", et_konsumen_nama.getText().toString());
                multipartBody.addFormDataPart("email_penerima", et_konsumen_email.getText().toString());
                multipartBody.addFormDataPart("phone_penerima", et_konsumen_nomor.getText().toString());
                multipartBody.addFormDataPart("alamat_penerima", et_konsumen_alamat.getText().toString());
                multipartBody.addFormDataPart("plu", "123456");
                multipartBody.addFormDataPart("ekspedisi", ekspedisiValue + "");
                multipartBody.addFormDataPart("resi", et_resi.getText().toString());
                multipartBody.addFormDataPart("alasan", et_konsumen_alamat.getText().toString());
                multipartBody.addFormDataPart("nama_product", et_konsumen_nama.getText().toString());
                multipartBody.addFormDataPart("jml_product", et_jumlah.getText().toString());

                Log.e("ekspedisiValue", ekspedisiValue + "");


                for (int i = 0; i < photoArray.length; i++) {
                    if (!photoArray[i].equals("")) {
                        String param = "img" + i;
                        Log.e("IMAGERE", photoArray[i]);
                        multipartBody.addFormDataPart(param, param + ".jpg",
                                RequestBody.create(mediaType, new File(photoArray[i])));

                    }
                }

                RequestBody formBody = multipartBody.build();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(Constant.PRODUCT_RETURN)
                        .post(formBody)
                        .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "dc3508a5-487a-8912-07a2-4fc1097a275c")
                        .build();


                try {
                    Log.e("PHOTODATAD", photoArray[0]);
                    okhttp3.Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.d("fileResponse", result + "");
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equals("success")) {

                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                                showPopUp(jsonObject.getString("message"));
                            }

                        } else {
                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                                showPopUp(jsonObject.getString("message"));
                            }
                        }
                    }
                } catch (Exception e) {
                    closeProgress();
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void showAlertDialog(final String msg, final boolean tag) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgress();
                Log.d("Dialog", "Alert");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ReturnActivity.this);
                dialogBuilder.setMessage(msg);
                dialogBuilder.setCancelable(tag);

                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tag) {
                            dialog.cancel();
                            startActivity(new Intent(ReturnActivity.this, ReturnSuccessActivity.class));
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            finish();
                        }
                    }
                });
                AlertDialog alert = dialogBuilder.create();
                alert.show();
            }
        });


    }

}
