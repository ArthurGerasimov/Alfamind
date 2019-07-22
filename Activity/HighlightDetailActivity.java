package id.meteor.alfamind.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;

public class HighlightDetailActivity extends BaseActivity {

    private ImageView image;
    private TextView highlighttitle, description;
    private String id;
    private ProgressBar process_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_detail);

        if (getIntent().hasExtra("DATA")){
            id = getIntent().getStringExtra("DATA");
        }

        ImageView back_btn = findViewById(R.id.back_btn);
        highlighttitle = findViewById(R.id.highlighttitle);
        description = findViewById(R.id.description);
        image = findViewById(R.id.image);
        process_bar = findViewById(R.id.process_bar);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setData();
    }

    private void setData() {
        showProgress("Please wait...",false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.HIGHLIGHT_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Highlight", response + "");
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        // if (object.has("id"));
                        // if (object.has("image_details"));
                        // if (object.has("related_product_id"));

                        if (object.has("title")) {
                            Log.e("TITLE", object.getString("title") + "");
                            highlighttitle.setText(object.getString("title"));
                        }
                        if (object.has("image")) {
                            downloadModelImage(object.getString("image"));
                        }
                        if (object.has("description")) {
                            description.setText(Html.fromHtml(object.getString("description")));
                        }
                    }
                    closeProgress();

                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgress();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                closeProgress();
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        });
        requestQueue.add(stringRequest);
    }

    private void downloadModelImage(final String url) {
        Log.d("CaptchaUrl", url + "");
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
                        Log.d("CaptchaUrl", "Image downloading failed");
                    }
                });
    }

}
