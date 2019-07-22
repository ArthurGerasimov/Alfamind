package id.meteor.alfamind.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import id.meteor.alfamind.Activity.RewardProductDetailActivity;
import id.meteor.alfamind.Interface.AddToCartListiner;
import id.meteor.alfamind.Model.RewardModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;

/**
 * Created by bodacious on 22/12/17.
 */

public class RewardsAlbumAdapter extends RecyclerView.Adapter<RewardsAlbumAdapter.MyViewHolder>{

    private Context mContext;
    private List<RewardModel> productList;



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, point;
        ImageView product_image;
        public RelativeLayout main_layout;

        public MyViewHolder(View view) {
            super(view);
            setIsRecyclable(false);
            title = view.findViewById(R.id.it_name);
            point = view.findViewById(R.id.it_point);
            main_layout = view.findViewById(R.id.main_layout);
            product_image = view.findViewById(R.id.it_image);
        }
    }

    public RewardsAlbumAdapter(Context mContext, List<RewardModel> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public RewardsAlbumAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_rewards_item, parent, false);

        return new RewardsAlbumAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RewardsAlbumAdapter.MyViewHolder holder, int position) {
        final RewardModel model = productList.get(position);


        if (model.getImageList()!=null && model.getImageList().size()>0){
            String URL = model.getImageList().get(0);
            Log.e("URL",URL);
            downloadImage(URL,holder.product_image,model);
        }
        holder.title.setText(model.getTitle());
        holder.point.setText(model.getRequired_points()+" Points");
        holder.product_image.setImageBitmap(null);

        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RewardProductDetailActivity.class);
                intent.putExtra("id",model.getPruductId());
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });


    }


    public void downloadImage(String url, final ImageView imageView, final RewardModel model){
        Log.d("ImageUrl", "  "+url + "");
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(500, 500) {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                        if(imageView!=null){
                            imageView.setImageBitmap(resource);
                            model.setBitmap(resource);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
    }

    public void getErrorDialog(final String str) {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String[] list = {str};
                final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(mContext, list, null);
                dialog.title("");
                dialog.cancelText("Ok");
                dialog.show();
            }
        });

    }

    public String getFormat(String str) {

        str = str.replaceAll("\\.", "");
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.reverse();
        char arr[] = stringBuffer.toString().toCharArray();
        StringBuilder NUM = new StringBuilder();
        int count = 0;
        for (char anArr : arr) {
            ++count;
            if (count == 4) {
                count = 1;
                NUM.append(".").append(anArr);

            } else {
                NUM.append("").append(anArr);
            }
        }
        stringBuffer = new StringBuffer(NUM.toString());
        stringBuffer.reverse();
        return String.valueOf(stringBuffer);
    }

}
