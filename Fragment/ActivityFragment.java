package id.meteor.alfamind.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Activity.MainActivity;
import id.meteor.alfamind.Base.BaseFragment;
import id.meteor.alfamind.Interface.FragmentListener;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;

public class ActivityFragment extends BaseFragment implements FragmentListener{

    private String webViewurl = "";
    private View view;
    private WebView webView;
    private RelativeLayout layout_progressBar;
    private SwipeRefreshLayout swipe_refresh;

    public FragmentListener getFragmentListener(){
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity, container, false);

        layout_progressBar = view.findViewById(R.id.layout_progressBar);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webViewurl = id.meteor.alfamind.helper.Constant.ACTIVITY_WEBVIEW_URL + MyApplication.getInstance().getPrefManager().getCoustmerIdOra() + "/s/alfamind/?ck=";
                layout_progressBar.setVisibility(View.VISIBLE);
                getCK();
            }
        });

        webViewurl = id.meteor.alfamind.helper.Constant.ACTIVITY_WEBVIEW_URL + MyApplication.getInstance().getPrefManager().getCoustmerIdOra() + "/s/alfamind/?ck=";
        getCK();
        return view;
    }

    private void getCK() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_CK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTLISTRESPONSECK", response + "");
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            if (jsonObject.has("ck")) {
                                Log.e("ck", jsonObject.getString("ck") + "");
                                webViewurl = webViewurl + jsonObject.getString("ck");
                                loadLayout();
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();
                Log.d("PRODUCT_ID", toktn + "0");
                param.put("id_cart", 0 + "");
                param.put("access_token", toktn + "");

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void loadLayout() {


        webView = view.findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
        ((MainActivity) getActivity()).setWebview(webView);
        // Enable java scipt........
        webView.getSettings().setJavaScriptEnabled(true);

        if (!MyApplication.isNetworkAvailable(getActivity())) {
            ((MainActivity) getActivity()).showSnackBar();
        } else {
            Log.e("DATDATADAsadsdsdadad", webViewurl);
            webView.loadUrl(webViewurl);

        }
    }

    @Override
    public void onBackPress() {
        ((MainActivity)getActivity()).getShoppingFragment();
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
            layout_progressBar.setVisibility(View.GONE);
            swipe_refresh.setRefreshing(false);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Error in loading page");
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}