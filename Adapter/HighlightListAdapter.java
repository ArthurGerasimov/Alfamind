package id.meteor.alfamind.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import id.meteor.alfamind.Activity.HighlightDetailActivity;
import id.meteor.alfamind.Model.HighlightModel;
import id.meteor.alfamind.R;

public class HighlightListAdapter extends ArrayAdapter<HighlightModel> {

    Context context;
    ArrayList<HighlightModel> highlightList;

    public HighlightListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<HighlightModel> highlights) {
        super(context, resource, highlights);

        this.context = context;
        highlightList = highlights;
    }

    @Override
    public int getCount() {

        return highlightList.size();
    }

    @Nullable
    @Override
    public HighlightModel getItem(int position) {
        return highlightList.get(position);
    }

    public void setData(ArrayList<HighlightModel> highlights) {

        highlightList = new ArrayList<>(highlights);
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView txvType, txvAmount, txvTitle, txvDescription;
        ImageView imageView;
        ProgressBar process_bar;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder vHolder;


        if (convertView == null) {

            vHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_highlight, parent, false);

            vHolder.process_bar = convertView.findViewById(R.id.process_bar);
            vHolder.txvType = convertView.findViewById(R.id.txv_type);
            vHolder.txvAmount = convertView.findViewById(R.id.txv_amount);
            vHolder.txvTitle = convertView.findViewById(R.id.txv_title);
            vHolder.txvDescription = convertView.findViewById(R.id.txv_description);
            vHolder.imageView = convertView.findViewById(R.id.h_image);
            vHolder.imageView.setImageBitmap(null);

            convertView.setTag(vHolder);
        } else {

            vHolder = (ViewHolder) convertView.getTag();
        }

        final HighlightModel model = getItem(position);
        vHolder.txvType.setText(model.getType());
        vHolder.txvAmount.setText(model.getAmount());
        vHolder.txvTitle.setText(model.getTitle());
        vHolder.txvDescription.setText(model.getDescription());
        vHolder.imageView.setImageBitmap(null);

        if (model.getBitmap() != null) {
            vHolder.imageView.setImageBitmap(model.getBitmap());
        } else {
            downloadModelImage(model, vHolder.imageView, vHolder.process_bar);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HighlightDetailActivity.class);
                intent.putExtra("DATA", model.getId());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        return convertView;
    }


    private void downloadModelImage(final HighlightModel model, final ImageView imageView, final ProgressBar process_bar) {
        Log.d("CaptchaUrl", model.getImage() + "");
        Glide.with(getContext())
                .load(model.getImage())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(300, 300) {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        process_bar.setVisibility(View.GONE);
                        imageView.setImageBitmap(resource);
                        model.setBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Log.d("CaptchaUrl", "Image downloading failed");
                    }
                });
    }

}
