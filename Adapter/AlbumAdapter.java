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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import id.meteor.alfamind.Activity.ProductDetailActivity;
import id.meteor.alfamind.Interface.AddToCartListiner;
import id.meteor.alfamind.Model.ProductModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;
import id.meteor.alfamind.extra.ProductHolder;
import id.meteor.alfamind.helper.MyApplication;

/**
 * Created by bodacious on 12/12/17.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductModel> productList;
    private AddToCartListiner addToCartListiner;
    private String operation;


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, it_price, it_last_price, it_discount, intip_margin;
        public ImageView product_image, image_wobbler0, image_wobbler1, image_wobbler2, image_wobbler3;
        public RelativeLayout wobber_lay0, wobber_lay1, wobber_lay2, wobber_lay3;
        public RelativeLayout itemLayout,
                layout_details,
                layout_item,
                layout_viewAll,
                layout_add_to_cart;


        public MyViewHolder(View view) {
            super(view);
            setIsRecyclable(false);
            title = view.findViewById(R.id.it_name);
            it_price = view.findViewById(R.id.it_price);
            it_last_price = view.findViewById(R.id.it_last_price);
            it_discount = view.findViewById(R.id.it_discount);
            intip_margin = view.findViewById(R.id.intip_margin);
            product_image = view.findViewById(R.id.it_image);
            layout_details = view.findViewById(R.id.lay_detail);
            layout_add_to_cart = view.findViewById(R.id.lay_botom);
            image_wobbler0 = view.findViewById(R.id.image_wobbler0);
            image_wobbler1 = view.findViewById(R.id.image_wobbler1);
            image_wobbler2 = view.findViewById(R.id.image_wobbler2);
            image_wobbler3 = view.findViewById(R.id.image_wobbler3);
            wobber_lay0 = view.findViewById(R.id.wobber_lay0);
            wobber_lay1 = view.findViewById(R.id.wobber_lay1);
            wobber_lay2 = view.findViewById(R.id.wobber_lay2);
            wobber_lay3 = view.findViewById(R.id.wobber_lay3);
        }
    }

    public AlbumAdapter(Context mContext, List<ProductModel> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    public AlbumAdapter(Context mContext, String operation, List<ProductModel> productList, AddToCartListiner addToCartListiner) {
        this.mContext = mContext;
        this.productList = productList;
        this.addToCartListiner = addToCartListiner;
        this.operation = operation;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sub_categeory_all, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ProductModel model = productList.get(position);
        holder.product_image.setTag(model.getPruductId());
        holder.product_image.setImageBitmap(null);
        holder.title.setText(model.getName());


        DecimalFormat df = new DecimalFormat("##,##,##0");
        String numberAsString = df.format(Double.parseDouble(model.getBestPrice()));
        numberAsString.replaceAll(",", ".");
        holder.it_price.setText("Rp " + getFormat(model.getBestPrice()));

        if (MyApplication.getInstance().getPrefManager().getIntipMargin()) {

            if (model.getMargin_human() != null && !model.getMargin_human().equalsIgnoreCase("null") && !model.getMargin_human().equalsIgnoreCase("")) {
                String marginHuman = getMarginHuman(model.getMargin_human());
                String marginTambahanHuman = model.getMargin_tambahan_header_human();
                String points = model.getPoints();
                String marginGroup = model.getMargin_group_margin();
                if (!marginHuman.equalsIgnoreCase("0"))
                    holder.intip_margin.setText("  Margin: Rp " + marginHuman + "\n  Margin+: " + marginTambahanHuman + "\n  Poin: " + points + "\n  Margin Grup: " + marginGroup);
                else
                    holder.intip_margin.setText("");
            } else
                holder.intip_margin.setVisibility(View.GONE);
        } else
            holder.intip_margin.setVisibility(View.GONE);

        if (model.getDiscountPercent() != null && !model.getDiscountPercent().equals("null") && !model.getDiscountPercent().equals("") && !model.getDiscountPercent().equals("0") && !model.getDiscountPercent().equals("%")) {
            Log.e("DISCOUNT_PER", "" + model.getDiscountPercent());
            holder.it_discount.setText(model.getDiscountPercent());
        } else {
            holder.it_discount.setVisibility(View.GONE);
        }

        if (model != null && !model.getPruductId().equals("")) {

            ArrayList<String> listtemp = model.getWobblerImageList();
            if (listtemp != null && listtemp.size() > 0) {
                Log.e("LIST_TEMP", "---------LIST_TEMP " + listtemp.size());

                for (int i = 0; i < listtemp.size(); i++) {
                    if (View.VISIBLE == holder.it_discount.getVisibility()) {
                        switch (i) {
                            case 0:
                                downloadWobblerImage(listtemp.get(i), holder.image_wobbler1);
                                holder.wobber_lay1.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                downloadWobblerImage(listtemp.get(i), holder.image_wobbler2);
                                holder.wobber_lay2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                downloadWobblerImage(listtemp.get(i), holder.image_wobbler3);
                                holder.wobber_lay3.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                    if (View.GONE == holder.it_discount.getVisibility()) {

                        Log.e("DADADADADAD", "" + listtemp.get(i));
                        switch (i) {
                            case 0:
                                downloadWobblerImage(listtemp.get(i), holder.image_wobbler0);
                                holder.wobber_lay0.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                downloadWobblerImage(listtemp.get(i), holder.image_wobbler2);
                                holder.wobber_lay2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                downloadWobblerImage(listtemp.get(i), holder.image_wobbler1);
                                holder.wobber_lay1.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
            }

            String temp_discount = model.getDiscount() + "";

            if (!temp_discount.equalsIgnoreCase("") && !temp_discount.equalsIgnoreCase("null") && !temp_discount.equalsIgnoreCase("0") && temp_discount != null) {
                holder.it_price.setText("Rp " + getFormat(getFormat(temp_discount)));
                holder.it_last_price.setText("Rp " + getFormat(model.getBestPrice()));
            } else {
                holder.it_last_price.setVisibility(View.GONE);
                holder.it_price.setText("Rp " + getFormat(model.getBestPrice()));
            }


            if (model.getBitmap() != null) {
                holder.product_image.setImageBitmap(model.getBitmap());
            } else {
                downloadImage(model.getImagePath() + model.getImage(), holder.product_image, model);
            }


            holder.layout_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    ProductHolder.getInstance().putProductDetail(model);
                    intent.putExtra("product_id", model.getPruductId());
                    if (operation.equalsIgnoreCase("search"))
                        intent.putExtra("DATA", 420 + "");
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                }
            });
            holder.layout_add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCartListiner.addToCart(model.getPruductId(), model.getStock() + "", model.getSubProduct());
                }
            });
        }
    }


    public void downloadImage(String url, final ImageView imageView, final ProductModel model) {
        Log.d("ImageUrl", "  " + url + "");
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .into(new SimpleTarget<Bitmap>(1000, 1000) {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        if (imageView != null) {
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
        ((Activity) mContext).runOnUiThread(new Runnable() {
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

    public String getMarginHuman(String str) {
        str = str.replace(" ", "");
        str = str.replace("IDR", "");
        str = str.replace(",", "");
        str = getFormat(str);
        return str;
    }

    public void downloadWobblerImage(String url, final ImageView imageView) {
        // Log.d(TAG, "ImageUrl  " + url + "");
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
    }

    /*private String roundOffPercent(String value){
        return (Math.round(Float.valueOf((value.replace("%",""))) * 10) / 10)+"%";
    }*/
}
