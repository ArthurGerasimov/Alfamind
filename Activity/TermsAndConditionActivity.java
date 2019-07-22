package id.meteor.alfamind.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;

public class TermsAndConditionActivity extends BaseActivity {

    private RelativeLayout lay_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        ImageView back_btn = findViewById(R.id.back_btn);
        lay_progressBar = findViewById(R.id.layout_progressBar);
        lay_progressBar.setVisibility(View.VISIBLE);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl("file:///android_asset/tac.html");
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
            lay_progressBar.setVisibility(View.GONE);
        }
    }
}
