package id.meteor.alfamind.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class NotificationDetailActivity extends BaseActivity {

    private ImageView image;
    private String msg_id;
    private ProgressBar process_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        TextView noti_msg = findViewById(R.id.noti_msg);
        TextView noti_title = findViewById(R.id.noti_title);
        image = findViewById(R.id.image);
        process_bar = findViewById(R.id.process_bar);

        msg_id = getIntent().getStringExtra("id");
        String message = getIntent().getStringExtra("message");
        String title = getIntent().getStringExtra("title");
        String image_url = getIntent().getStringExtra("image");

        downloadModelImage(image_url);
        markAsRead();

        noti_msg.setText(message);
        noti_title.setText(title);
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }

    public void markAsRead() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_READ_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", response + "");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();
                param.put("message_id", msg_id + "");
                param.put("access_token", toktn + "");
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void downloadModelImage(final String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(300, 300) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        process_bar.setVisibility(View.GONE);
                        image.setImageBitmap(resource);
                    }
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
    }
}
