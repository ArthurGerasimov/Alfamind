package id.meteor.alfamind.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import id.meteor.alfamind.Activity.AllProductActivity;
import id.meteor.alfamind.Activity.ProductDetailActivity;
import id.meteor.alfamind.Activity.SearchActivity;
import id.meteor.alfamind.Interface.AddToCartListiner;
import id.meteor.alfamind.Model.ProductModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.extra.ProductHolder;

/**
 * Created by bodacious on 15/12/17.
 */

public class SearchListAdapter extends BaseAdapter {
    private ArrayList<ProductModel> productList;
    private Context context;
    private String title="";
    private final int REQUEST_CODE = 100;

    AddToCartListiner listiner;

    public SearchListAdapter(Context context, ArrayList<ProductModel> productList) {
        this.productList = productList;
        this.context = context;

    }
    public SearchListAdapter(Context context, ArrayList<ProductModel> productList, AddToCartListiner listiner,String title) {
        this.productList = productList;
        this.context = context;
        this.listiner = listiner;
        this.title = title;
        Log.e("PRODUCTLISTSIZE",productList.size()+"");
    }

    @Override
    public int getCount() {
        return productList.size()+1;
    }

    @Override
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_search_product, null);


        TextView  name = view.findViewById(R.id.name);
        TextView  price = view.findViewById(R.id.price);
        TextView  category = view.findViewById(R.id.category);
        ImageView  addImage = view.findViewById(R.id.image);


        if(i==productList.size()){
            name.setText("Tampilkan semua");
            price.setVisibility(View.GONE);
            category.setVisibility(View.GONE);
            addImage.setImageResource(R.drawable.ic_search_black);
        }else {
            final ProductModel model = productList.get(i);
            name.setText(model.getName());
            price.setText("Rp "+getFormat(model.getBestPrice()));
            category.setText(model.getCattegoryName()+"");
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==productList.size()){
                    Intent intent = new Intent(context, AllProductActivity.class);
                    // hold sub cat list...
                    //   SubCatHolder.getInstance().putSubCatList(catId + "", catHashMap2.get(catId));
                    intent.putExtra("cat_id", 420);
                    intent.putExtra("OPERATION", "SEARCH");
                    intent.putExtra("TITLE", title);
                    intent.putExtra("TOTAL", productList.size()+"");
                    ((SearchActivity)context).startActivityForResult(intent,REQUEST_CODE);
                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                }else {
                    final ProductModel model = productList.get(i);
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    ProductHolder.getInstance().putProductDetail(model);
                    intent.putExtra("DATA", 420+"");
                    intent.putExtra("product_id", model.getPruductId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            }
        });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProductModel model = productList.get(i);
                listiner.addToCart(model.getPruductId(),model.getStock()+"",model.getSubProduct());
            }
        });

       /* final ProductModel productModel = productList.get(i);
        String bestPrice = productModel.getBestPrice();
        final int l = bestPrice.indexOf('.');
        if (l > 0) {
            bestPrice = bestPrice.substring(0, l);
        }
        Log.d("BEST_PRICE",bestPrice+"");

        int tempQuantity = productModel.getQuantity();
        double tempPrice = Double.parseDouble(bestPrice);
        double totalPrice = tempPrice * tempQuantity;
        totalAmountText  =totalAmountText+totalPrice;


        final TextView name = (TextView) view.findViewById(R.id.product_name);
        final TextView price = (TextView) view.findViewById(R.id.product_price);
        final TextView total_price = (TextView) view.findViewById(R.id.total_price);
        final TextView quantity = (TextView) view.findViewById(R.id.p_quantity);
        final ImageView pImage = (ImageView) view.findViewById(R.id.p_image);
        final ImageView minus = (ImageView) view.findViewById(R.id.image_minus);
        final ImageView plus = (ImageView) view.findViewById(R.id.image_plus);
        final TextView delete_cart_item = (TextView) view.findViewById(R.id.delete_cart_item);
        quantity.setText(tempQuantity + "");
        name.setText(productModel.getName());
        price.setText("Rp " + getFormat(bestPrice) + " /pcs");

        total_price.setText("Total Rp " + getFormat(totalPrice+""));

        if (productModel.getBitmap() != null) {
            pImage.setImageBitmap(productModel.getBitmap());
        } else {
            downloadImage(productModel, pImage);
        }


        listiner.setTotalAmount(getFormat(totalAmountText + ""));

        final String tempBestPrice = bestPrice;
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = Integer.parseInt(quantity.getText().toString());

                temp = temp + 1;
                double tempP = Double.parseDouble(tempBestPrice);
                double totalPrice = tempP * temp;
                totalAmountText = totalAmountText + tempP;

                total_price.setText("Total Rp " + getFormat(totalPrice + ""));
                quantity.setText(temp + "");

                listiner.setTotalAmount(getFormat(totalAmountText + ""));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // int temp = Integer.parseInt(quantity.getText().toString());


              *//*  if (temp > 1) {
                    double tempP = Double.parseDouble(tempBestPrice);
                    double totalPrice = tempP * temp;
                    temp = temp - 1;
                    totalAmountText = totalAmountText - tempP;

                    total_price.setText("Total Rp " + getFormat(totalPrice + ""));
                    quantity.setText(temp + "");

                    listiner.setTotalAmount(totalAmountText + "");
                }*//*

                int temp = Integer.parseInt(quantity.getText().toString());
                double tempP = Double.parseDouble(tempBestPrice);
                double totalPrice = tempP * temp;

                if (temp <= 1) {

                } else {
                    temp = temp - 1;
                    totalAmountText = totalAmountText - tempP;
                }
                listiner.setTotalAmount(getFormat(totalAmountText + ""));
                Log.d("TEMPQUAN", temp + "");
                total_price.setText("Total Rp " + getFormat(totalPrice + ""));
                quantity.setText(temp + "");

            }
        });


        delete_cart_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listiner.deleteFromcart(i, productModel.getPruductId());
            }
        });
        //   if()*/
        return view;
    }


    public String getFormat(String str) {
        str = str.replaceAll("\\.", "");
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.reverse();
        char arr[] = stringBuffer.toString().toCharArray();
        String NUM = "";
        int count = 0;
        for (int j = 0; j < arr.length; j++) {
            ++count;
            if (count == 4) {
                count = 1;
                NUM = NUM + "." + arr[j];
                Log.e("HELLO-WORD", NUM + " " + j + " count=" + count + " " + arr[arr.length - (j + 1)]);
            } else {
                NUM = NUM + "" + arr[j];
                Log.e("HELLO-WORD", NUM + " " + j + " count=" + count + " " + arr[arr.length - (j + 1)]);
            }
        }

        stringBuffer = new StringBuffer(NUM);
        stringBuffer.reverse();
        Log.e("RESULT-DONE", String.valueOf(stringBuffer));

        Log.e("RESULT*", NUM);
        return String.valueOf(stringBuffer);


    }


}
