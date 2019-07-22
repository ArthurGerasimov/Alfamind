package id.meteor.alfamind.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import id.meteor.alfamind.Activity.FullScreenImageActivity;
import id.meteor.alfamind.R;

/**
 * Created by bodacious on 14/12/17.
 */

public class ImageAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> imgBitmap;
    private ProgressBar process_bar;

    public ImageAdapter(Context context, ArrayList imageListForAdapter){
        mContext = context;
        imgBitmap = imageListForAdapter;
    }

    @Override
    public int getCount() {
        return imgBitmap.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView =  layoutInflater.inflate(R.layout.images_gallary_layout,container,false);

        process_bar = itemView.findViewById(R.id.process_bar);
        ImageView image_gallery = itemView.findViewById(R.id.image_gallery);
        downloadImage(imgBitmap.get(position),image_gallery);

        image_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mContext,FullScreenImageActivity.class);
                intent.putExtra("url",imgBitmap.get(position));
                mContext.startActivity(intent);
                ((Activity)mContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    public void downloadImage(final String url,final ImageView imageView){
        Log.d("ImageUrl", "  "+url + "");
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(1000, 1000) {


                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation glideAnimation) {
                        process_bar.setVisibility(View.GONE);
                        if(imageView!=null){
                            imageView.setImageBitmap(resource);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        process_bar.setVisibility(View.GONE);
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
    }
}