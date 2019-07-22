package id.meteor.alfamind.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;
import uk.co.senab.photoview.PhotoViewAttacher;

public class FullScreenImageActivity extends BaseActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView imageView = findViewById(R.id.image);
        ImageView close = findViewById(R.id.close);
        progressBar = findViewById(R.id.bar);
        String url = getIntent().getStringExtra("url");

        if (MyApplication.isNetworkAvailable(FullScreenImageActivity.this)) {
            downloadImage(url, imageView);
        } else {
            showSnackBar();
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public void downloadImage(String url, final ImageView imageView) {
        Log.d("ImageUrl", "  " + url + "");
        Glide.with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(1500, 1500) {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        progressBar.setVisibility(View.GONE);
                        if (imageView != null) {
                            imageView.setImageBitmap(resource);
                            PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
                            attacher.update();
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
    }

    public void showSnackBar() {
        RelativeLayout main_parent_layout = findViewById(R.id.main_layout);
        Snackbar snackbar = Snackbar.make(main_parent_layout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
