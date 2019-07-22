package id.meteor.alfamind.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Fragment.AccountFragment;
import id.meteor.alfamind.Fragment.ActivityFragment;
import id.meteor.alfamind.Fragment.LoginFragment;
import id.meteor.alfamind.Fragment.NewsFragment;
import id.meteor.alfamind.Fragment.NotificationFragment;
import id.meteor.alfamind.Fragment.ShoppingFragment;
import id.meteor.alfamind.Interface.FragmentListener;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.MyPreferenceManager;
import id.meteor.alfamind.timer.LoginTimer;

public class MainActivity extends BaseActivity {
    LinearLayout lytShopping, lytNews, lytActivity, lytNotification, lytAccount;
    ImageView imvShopping, imvNews, imvActivity, imvNotification, imvAccount;
    TextView txvShopping, txvNews, txvActivity, txvNotification, txvAccount, notification_count;
    MyPreferenceManager manager;
    LoginTimer loginTimer;
    private WebView webview;
    private int noti_count = 0;
    ShoppingFragment shoppingFragment = new ShoppingFragment();
    private String version = "", URL;
    private FragmentListener fragmentListener;
    LinearLayout main_parent_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.hasExtra("URL")) {
            URL = intent.getStringExtra("URL");
            Log.e("URL+++++++", "" + URL);
        }

        main_parent_layout = findViewById(R.id.main_parent_layout);
        notification_count = findViewById(R.id.notification_count);
        loginTimer = new LoginTimer(((60 * 60) * 1000), 1000, this);


        String action = intent.getStringExtra("action");
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            int versionCode = pInfo.versionCode;
            Log.e("VERSION", "" + version + "------------" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        requestForPermission();
        getAppVersion();
        loadLayout();
        getNotification();

        if (action != null && action.equalsIgnoreCase("login")) {
            setShoppingFragment(shoppingFragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, shoppingFragment, "shop").commit();

            getLoginFragment();

        } else {
            updateBottomTab(0);
            getShoppingFragment();

        }

        if (URL != null && !URL.equalsIgnoreCase("null") && !URL.equalsIgnoreCase("")) {
            Intent i = new Intent(MainActivity.this, SaldoWebViewActivity.class);
            i.putExtra("DATA", "MAIN_ACTIVITY");
            i.putExtra("VALUE", URL + "");
            startActivity(i);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

    }

    public void getShoppingFragment() {
        ShoppingFragment shoppingFragment = (ShoppingFragment) getSupportFragmentManager().findFragmentByTag("SHOPPING");
        if (shoppingFragment == null)
            shoppingFragment = new ShoppingFragment();
        if (shoppingFragment.isVisible())
            return;
        fragmentListener = shoppingFragment.getFragmentListener();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        updateBottomTab(0);
        transaction.replace(R.id.frame_container, shoppingFragment, "SHOPPING");
        transaction.addToBackStack("SHOPPING");
        transaction.commit();
    }

    public void getLoginFragment() {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LOGIN");
        if (loginFragment == null)
            loginFragment = new LoginFragment();
        if (loginFragment.isVisible())
            return;
        fragmentListener = loginFragment.getFragmentListener();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, loginFragment, "LOGIN");
        updateBottomTab(4);
        transaction.addToBackStack("LOGIN");
        transaction.commit();
    }

    public void getAccountFragment() {
        AccountFragment accountFragment = (AccountFragment) getSupportFragmentManager().findFragmentByTag("ACCOUNT");
        if (accountFragment == null)
            accountFragment = new AccountFragment();
        if (accountFragment.isVisible())
            return;
        fragmentListener = accountFragment.getFragmentListener();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, accountFragment, "ACCOUNT");
        updateBottomTab(4);
        transaction.addToBackStack("ACCOUNT");
        transaction.commit();
    }

    public void getNewsFragment() {
        NewsFragment newsFragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag("NEWS");
        if (newsFragment == null)
            newsFragment = new NewsFragment();
        if (newsFragment.isVisible())
            return;
        fragmentListener = newsFragment.getFragmentListener();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, newsFragment, "NEWS");
        updateBottomTab(1);
        transaction.addToBackStack("NEWS");
        transaction.commit();
    }

    public void getNotificationFragment() {
        NotificationFragment notificationFragment = (NotificationFragment) getSupportFragmentManager().findFragmentByTag("Notification");
        if (notificationFragment == null)
            notificationFragment = new NotificationFragment();
        if (notificationFragment.isVisible())
            return;
        fragmentListener = notificationFragment.getFragmentListener();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, notificationFragment, "Notification");
        updateBottomTab(3);
        transaction.addToBackStack("Notification");
        transaction.commit();
    }

    public void getActivtyFragment() {
        ActivityFragment activityFragment = (ActivityFragment) getSupportFragmentManager().findFragmentByTag("ACTIVITY");
        if (activityFragment == null)
            activityFragment = new ActivityFragment();
        if (activityFragment.isVisible())
            return;
        fragmentListener = activityFragment.getFragmentListener();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, activityFragment, "ACTIVITY");
        updateBottomTab(2);
        transaction.addToBackStack("ACTIVITY");
        transaction.commit();
    }

    private void loadLayout() {

        lytShopping = findViewById(R.id.lyt_shopping);
        lytShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShoppingFragment();
            }
        });

        manager = MyApplication.getInstance().getPrefManager();
        lytNews = findViewById(R.id.lyt_news);
        lytNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewsFragment();
            }
        });

        lytActivity = findViewById(R.id.lyt_activity);
        lytActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.getIsLogged()) {
                    getActivtyFragment();
                }
            }
        });

        lytNotification = findViewById(R.id.lyt_notification);
        lytNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.getIsLogged()) {
                    getNotificationFragment();
                }
            }
        });

        lytAccount = findViewById(R.id.lyt_account);
        lytAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.getIsLogged()) {
                    getAccountFragment();
                } else {
                    getLoginFragment();
                }
            }
        });

        txvShopping = findViewById(R.id.txv_shopping);
        txvNews = findViewById(R.id.txv_news);
        txvActivity = findViewById(R.id.txv_activity);
        txvNotification = findViewById(R.id.txv_notification);
        txvAccount = findViewById(R.id.txv_account);

        imvShopping = findViewById(R.id.imv_shopping);
        imvNews = findViewById(R.id.imv_news);
        imvActivity = findViewById(R.id.imv_activity);
        imvNotification = findViewById(R.id.imv_notification);
        imvAccount = findViewById(R.id.imv_account);
    }

    private void updateBottomTab(int index) {

        ArrayList<ImageView> imageList = new ArrayList<>();
        imageList.add(imvShopping);
        imageList.add(imvNews);
        imageList.add(imvActivity);
        imageList.add(imvNotification);
        imageList.add(imvAccount);

        ArrayList<TextView> textList = new ArrayList<>();
        textList.add(txvShopping);
        textList.add(txvNews);
        textList.add(txvActivity);
        textList.add(txvNotification);
        textList.add(txvAccount);

        int[] imageSelectedResources = {R.mipmap.ic_belanja_selected, R.mipmap.ic_highlight_selected, R.mipmap.ic_aktivitas_selected,
                R.mipmap.ic_notifikasi_selected, R.mipmap.ic_akun_selected};

        int[] imageResources = {R.mipmap.ic_belanja, R.mipmap.ic_highlight, R.mipmap.ic_aktivitas, R.mipmap.ic_notifikasi, R.mipmap.ic_account};

        for (int i = 0; i < imageList.size(); i++) {
            imageList.get(i).setImageResource(imageResources[i]);
            textList.get(i).setTextColor(0xFFBDBDBD);
        }

        imageList.get(index).setImageResource(imageSelectedResources[index]);
        textList.get(index).setTextColor(0xFFCE0017);
    }


    public void setShoppingFragment(ShoppingFragment fragment) {
        shoppingFragment = fragment;

    }

    public void hideKeyboard() {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    /// start auto logIn.............

    public void startTimer() {
        loginTimer.start();
    }

    // start auto logIn............
    public void stopTimer() {
        loginTimer.cancel();
    }


    @Override
    public void onBackPressed() {
        if (fragmentListener != null)
            fragmentListener.onBackPress();
    }

    public void setWebview(WebView webview) {
        this.webview = webview;
    }


    public void showSnackBar() {
        Snackbar snackbar = Snackbar.make(main_parent_layout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    public void requestForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                Log.e("per", "hiiii");
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1005);

                return;
            }
        }
        Log.e("per", "2");
        loadLayout();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1005) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.e("per", "4");
                    loadLayout();
                }
            }
        }
    }

    public void getNotification() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("NotificationResponse", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        noti_count = 0;

                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            if (jsonObject.has("notifications")) {
                                JSONArray array = jsonObject.getJSONArray("notifications");
                                for (int i = 0; i < array.length(); i++) {
                                    final JSONObject object = array.getJSONObject(i);
                                    if (object.has("read_status")) {
                                        String isRead = object.getString("read_status");
                                        if (isRead != null && isRead.equals("0")) {
                                            noti_count++;
                                        }
                                    }
                                }

                                if (noti_count > 0) {
                                    notification_count.setText(noti_count + "");
                                    notification_count.setVisibility(View.VISIBLE);
                                }
                            }
                        } else
                            notification_count.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VollyError", " something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                String access_token = MyApplication.getInstance().getPrefManager().getAccessToken();
                Log.d("UPDATEPARAM", access_token + "");
                param.put("access_token", access_token + "");

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void getAppVersion() {

        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, id.meteor.alfamind.helper.Constant.APP_VERSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("APP_VERSION", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // if (jsonObject.has("version"));

                    if (jsonObject.has("version_lite")) {

                        String NEW_VERSION = jsonObject.getString("version_lite");
                        String new1[] = NEW_VERSION.split("\\.");
                        String old1[] = version.split("\\.");

                        if (!new1[0].equalsIgnoreCase(old1[0])) {
                            showAlertDialog("Mengubah paksa", false);
                        } else if (!new1[1].equalsIgnoreCase(old1[1]))
                            showAlertDialog("Segera update applikasi anda", true);
                        else if (!new1[2].equalsIgnoreCase(old1[2]))
                            showAlertDialog("Ada pembaharuan", true);

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

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MAINACTIVITY", "ON_RESUME _CALL");
        getNotification();
    }
}
