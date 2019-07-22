package id.meteor.alfamind.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.meteor.alfamind.Adapter.ImageAdapter;
import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Model.ProductModel;
import id.meteor.alfamind.Model.SubProductModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.extra.ImagesHolder;
import id.meteor.alfamind.extra.ProductHolder;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class ProductDetailActivity extends BaseActivity {

    private TextView holdText;
    ProductModel model;
    TextView product_name, n_price, discount_price, bandName, description, it_discount, category, plu, kode_plu, kategory, out_of_stock;
    ImageView firstImage;
    ViewPager viewPager;
    ImageAdapter imageAdapter;
    boolean isLogged = false;

    ArrayList<Bitmap> imageList;
    ArrayList<String> imageList2 = new ArrayList<>();
    private ProgressBar process_bar;
    private int mDotsCount;
    private static TextView[] mDotsText;
    private TextView quantity;
    private LinearLayout add_quantity;
    private List<String> sizeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        int id = Integer.parseInt(getIntent().getStringExtra("product_id"));

        Log.d("ProductId", id + "   ---  ");
        model = ProductHolder.getInstance().getProductDetail(id);

        if (model != null) {
            Log.e("MODEL_DEATILS", "" + model);
            Log.d("ProductId", model.getName() + "    " + model.getStock() + "");
            if (getIntent().hasExtra("DATA")) {
                if (getIntent().getStringExtra("DATA").equalsIgnoreCase("420")) {
                    getProductDetails();
                }
            } else
                loadLayout();
        }

        isLogged = MyApplication.getInstance().getPrefManager().getIsLogged();
    }

    @SuppressLint("SetTextI18n")
    private void loadLayout() {

        product_name = findViewById(R.id.product_name);
        n_price = findViewById(R.id.n_price);
        discount_price = findViewById(R.id.discount_price);
        description = findViewById(R.id.text_description);
        it_discount = findViewById(R.id.it_discount);
        bandName = findViewById(R.id.brand);
        quantity = findViewById(R.id.quantity);
        firstImage = findViewById(R.id.first_image);
        viewPager = findViewById(R.id.image_viewPager);
        process_bar = findViewById(R.id.process_bar);
        LinearLayout mDotsLayout = findViewById(R.id.image_count);
        ImageView previous_image = findViewById(R.id.previous_image);
        ImageView nextImage = findViewById(R.id.nextImage);
        ImageView stock_plus = findViewById(R.id.stock_plus);
        ImageView stock_minus = findViewById(R.id.stock_minus);
        LinearLayout size = findViewById(R.id.size);
        category = findViewById(R.id.category);
        plu = findViewById(R.id.plu);
        kategory = findViewById(R.id.kategory);
        kode_plu = findViewById(R.id.kode_plu);
        TextView discount_text = findViewById(R.id.discount_text);
        ImageView share = findViewById(R.id.share);
        add_quantity = findViewById(R.id.add_quantity);
        out_of_stock = findViewById(R.id.out_of_stock);
        Spinner size_spinner = findViewById(R.id.size_spinner);

        size_spinner.setVisibility(View.GONE);
        out_of_stock.setVisibility(View.GONE);

        if (model.getSubProduct()) {
            size.setVisibility(View.VISIBLE);
        }

        if (model.getCattegoryName() != null && !model.getCattegoryName().equals("") && !model.getCattegoryName().equals("null")) {
            kategory.setVisibility(View.VISIBLE);
            category.setVisibility(View.VISIBLE);
            category.setText(model.getCattegoryName() + "");
        }

        if (model.getPlu() != null && !model.getPlu().equals("") && !model.getPlu().equals("null")) {
            kode_plu.setVisibility(View.VISIBLE);
            plu.setVisibility(View.VISIBLE);
            plu.setText(model.getPlu() + "");
        }

        imageList = new ArrayList<>();

        downloadImage(model.getImagePath() + model.getImage(), firstImage);
        product_name.setText(model.getName().trim());
        n_price.setText("Rp " + getFormat(model.getBestPrice()));
        description.setText(model.getDescription());
        bandName.setText(model.getBrandName());

        if (model.getDiscountPercent() != null && !model.getDiscountPercent().equals("null") && !model.getDiscountPercent().equals("") && !model.getDiscountPercent().equals("0") && !model.getDiscountPercent().equals("%")) {
            it_discount.setVisibility(View.VISIBLE);
            it_discount.setText(model.getDiscountPercent() + "");
        } else {
            it_discount.setVisibility(View.GONE);
        }

        String Discount_Price = model.getDiscount() + "";
        Log.e("Discount_Price", "+++" + Discount_Price);
        Log.e("STOCKKK123", "+++" + model.getStock());

        if ((model.getStock() + "").equalsIgnoreCase("0")) {
            out_of_stock.setVisibility(View.VISIBLE);
            add_quantity.setVisibility(View.GONE);
        } else
            add_quantity.setVisibility(View.VISIBLE);

        if (Discount_Price != null && !Discount_Price.equals("") && !Discount_Price.equals("0") && !Discount_Price.equals("null"))
            discount_price.setText("Rp " + getFormat(Discount_Price));

        else {
            discount_text.setVisibility(View.GONE);
            discount_price.setVisibility(View.GONE);
            n_price.setBackground(null);
            n_price.setTextSize(22);
        }


        ArrayList<String> urlList = model.getImageList();
        if (urlList != null && urlList.size() > 0) {

            process_bar.setVisibility(View.VISIBLE);
            for (int i = 0; i < urlList.size(); i++) {
                if (MyApplication.isNetworkAvailable(ProductDetailActivity.this)) {
                    downloadImage(urlList.get(i), null);
                } else {
                    showSnackBar();
                }

            }
            process_bar.setVisibility(View.GONE);
            ImagesHolder.getInstance().putProductDetail(imageList, Integer.parseInt(model.getPruductId()));

            imageAdapter = new ImageAdapter(this, urlList);
            viewPager.setAdapter(imageAdapter);
            process_bar.setVisibility(View.GONE);

            mDotsCount = viewPager.getAdapter().getCount();
            mDotsText = new TextView[mDotsCount];

            for (int i = 0; i < mDotsCount; i++) {
                mDotsText[i] = new TextView(this);
                mDotsText[i].setText("•");
                mDotsText[i].setTextSize(40);
                mDotsText[i].setTypeface(null, Typeface.BOLD);
                mDotsText[i].setTextColor(android.graphics.Color.GRAY);
                mDotsLayout.addView(mDotsText[i]);
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    for (int j = 0; j < mDotsCount; j++) {
                        ProductDetailActivity.mDotsText[j].setTextColor(Color.GRAY);
                    }
                    ProductDetailActivity.mDotsText[position].setTextColor(Color.RED);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MyApplication.getInstance().getPrefManager().getIsLogged()) {
                        showProgress("Please wait...", false);
                        shareProduct();
                    } else
                        showPopUp("Silakan login terlebih dahulu");
                }
            });


            previous_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

                }
            });

            nextImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            });

            stock_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tempQuantity = Integer.parseInt(quantity.getText().toString());
                    if (tempQuantity > 1) {
                        quantity.setText((tempQuantity - 1) + "");
                    }

                }
            });
            stock_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tempQuantity = Integer.parseInt(quantity.getText().toString());
                    if (tempQuantity < model.getStock()) {
                        quantity.setText((tempQuantity + 1) + "");
                    } else {
                        getErrorDialog("Stok Tidak Mencukupi"); //Stok Tidak Mencukupi
                    }
                }
            });


        }
        process_bar.setVisibility(View.GONE);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.isNetworkAvailable(ProductDetailActivity.this)) {
                    if (MyApplication.getInstance().getPrefManager().getIsLogged()) {
                        if (!(model.getStock() + "").equalsIgnoreCase("0")) {
                            itemAddToCart(model.getPruductId());
                        } else
                            getErrorDialog("Stock tidak tersedia");
                    } else {
                        startActivity(new Intent(ProductDetailActivity.this, ChekOutErrorActivity.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                } else {
                    showSnackBar();
                }

            }
        });
        final ArrayList<SubProductModel> List = model.getSubProductList();

        if (List != null && List.size() > 0) {
            for (int i = 0; i < List.size(); i++) {

                final SubProductModel subModel = List.get(i);
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.size_layout, null);
                //size.addView(view);
                final TextView textView = view.findViewById(R.id.size_s);
                String Size_Name = subModel.getName() + "  ";
                Size_Name = Size_Name.toLowerCase();
                Size_Name = Size_Name.replace("size", "");
                Size_Name = Size_Name.replace(" ", "");
                textView.setText(Size_Name);
                sizeList.add(Size_Name);


                if (i == 0) {
                    model.setStock(subModel.getStock());
                    model.setPruductId(subModel.getProductId());
                    model.setDiscounted_price(subModel.getDiscounted_price());
                    model.setBestPrice(subModel.getBase_price());
                    model.setPlu(subModel.getPlu() + "");
                    model.setId_product_alfacart(subModel.getId_product_alfacart() + "");
                    textView.setTextColor(Color.RED);
                    if (holdText != null) {
                        holdText.setTextColor(Color.GRAY);
                    }
                    holdText = textView;
                    setSizeData();
                }

                final int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("IMAGEsad", finalI + "");
                        textView.setTextColor(Color.RED);

                        if (holdText != null) {
                            holdText.setTextColor(Color.GRAY);
                        }
                        holdText = textView;
                        model.setStock(subModel.getStock());
                        model.setPruductId(subModel.getProductId());

                        if (subModel.getDiscounted_price().equalsIgnoreCase("0") || subModel.getDiscounted_price().equals("")) {
                            n_price.setBackgroundResource(0);
                        }

                        model.setDiscounted_price(subModel.getDiscounted_price());
                        model.setBestPrice(subModel.getBase_price());
                        model.setPlu(subModel.getPlu() + "");
                        model.setId_product_alfacart(subModel.getId_product_alfacart() + "");

                        Log.e("ListSize", subModel.getName() + "  ");
                        Log.e("ListSize", subModel.getDiscounted_price() + "  dis");
                        Log.e("ListSize", subModel.getStock() + "  ");
                        Log.e("ListSize", subModel.getBase_price() + "  base");
                        Log.e("ListSize", subModel.getProductId() + "  ");

                        setSizeData();

                    }
                });
            }
            if (sizeList.size() > 0) {
                size_spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sizeList);
                size_spinner.setAdapter(dataAdapter);
            }
        }

        size_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                final SubProductModel subModel = List.get(i);
                Log.e("SELECTED_ITEM", "" + subModel.getName());

                model.setStock(subModel.getStock());
                model.setPruductId(subModel.getProductId());
                if (subModel.getDiscounted_price().equalsIgnoreCase("0") || subModel.getDiscounted_price().equals("")) {
                    n_price.setBackgroundResource(0);
                }
                model.setDiscounted_price(subModel.getDiscounted_price());
                model.setBestPrice(subModel.getBase_price());
                model.setPlu(subModel.getPlu() + "");
                model.setId_product_alfacart(subModel.getId_product_alfacart() + "");

                Log.e("ListSize", subModel.getName() + "  ");
                Log.e("ListSize", subModel.getDiscounted_price() + "  dis");
                Log.e("ListSize", subModel.getStock() + "  ");
                Log.e("ListSize", subModel.getBase_price() + "  base");
                Log.e("ListSize", subModel.getProductId() + "  ");

                setSizeData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }


    public void downloadImage(final String url, final ImageView imageView) {
        Log.d("ImageUrl", "  " + url + "");
            Glide.with(getApplicationContext())
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>(1000, 1000) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                            if (imageView != null) {
                                imageView.setImageBitmap(resource);
                                process_bar.setVisibility(View.GONE);
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new Intent(ProductDetailActivity.this, FullScreenImageActivity.class);
                                        intent.putExtra("url", url);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    }
                                });
                            } else {
                                imageList.add(resource);

                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });
    }

    // by mantri....
    public String getFormat(String str) {
        str = str.replaceAll("\\.", "");
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.reverse();
        char arr[] = stringBuffer.toString().toCharArray();
        StringBuilder NUM = new StringBuilder();
        int count = 0;
        for (int j = 0; j < arr.length; j++) {
            ++count;
            if (count == 4) {
                count = 1;
                NUM.append(".").append(arr[j]);
                Log.e("HELLO-WORD", NUM + " " + j + " count=" + count + " " + arr[arr.length - (j + 1)]);
            } else {
                NUM.append("").append(arr[j]);
                Log.e("HELLO-WORD", NUM + " " + j + " count=" + count + " " + arr[arr.length - (j + 1)]);
            }
        }

        stringBuffer = new StringBuffer(NUM.toString());
        stringBuffer.reverse();
        Log.e("RESULT-DONE", String.valueOf(stringBuffer));

        Log.e("RESULT*", NUM.toString());
        return String.valueOf(stringBuffer);
    }

    ///////  Item Added To Cart....
    public void itemAddToCart(final String pruductId) {
        showProgress("Please wait...", false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("Highlight", Constant.ADD_TO_CART + "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTRESPONSE", response + "");
                closeProgress();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        if (jsonObject.has("id_cart")) {
                            MyApplication.getInstance().getPrefManager().setCartId(jsonObject.getString("id_cart"));
                        }
                        startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();
                    } else {
                        getErrorDialog(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    closeProgress();
                    e.printStackTrace();
                    getErrorDialog("Something went wrong");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();

                int tempQuantity = Integer.parseInt(quantity.getText().toString());
                String idCart = MyApplication.getInstance().getPrefManager().getCartID();
                if (toktn == null || toktn.equals("null") || toktn.equals("")) {
                    toktn = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }

                Log.d("PRODUCT_ID", pruductId + " " + toktn + "   idcart_>" + idCart + "    ty  " + tempQuantity + "");
                param.put("id_cart", idCart + "");
                param.put("access_token", toktn + "");
                param.put("id_product", pruductId + "");
                param.put("quantity", tempQuantity + "");
                param.put("operation", "up");
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    public void setSizeData() {
        it_discount.setVisibility(View.GONE);
        n_price.setText("Rp " + getFormat(model.getBestPrice()));
        discount_price.setText("Rp " + getFormat(model.getDiscounted_price() + ""));
        plu.setText("" + model.getPlu());

        if ((model.getStock() + "").equalsIgnoreCase("0")) {
            out_of_stock.setVisibility(View.VISIBLE);
            add_quantity.setVisibility(View.GONE);
        } else {
            quantity.setText("1");
            add_quantity.setVisibility(View.VISIBLE);
            out_of_stock.setVisibility(View.GONE);
        }
    }

    // product details
    public void getProductDetails() {

        final String url = Constant.GET_PRODUCT + model.getDepartment_id() + "/" + model.getSubdepartment_id() + "/" + model.getCatId() + "/" + model.getSubCatId();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", url + "      " + response);
                try {
                    Log.e("YES", "url----------" + url);
                    JSONObject jsonObject = new JSONObject(response);
                    //    progressDialog.dismiss();
                    if (jsonObject.getString("status").equals("success")) {

                        Log.e("YES", "ENTE KN12");
                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);

                            if (object.getString("id_product").equalsIgnoreCase(model.getPruductId() + "")) {
                                Log.e("YES", "ENTE KN");
                                if (object.has("id_product")) {
                                    model.setPruductId(object.getString("id_product"));
                                }
                                if (object.has("name")) {
                                    model.setName(object.getString("name"));
                                }
                                if (object.has("brand_name")) {
                                    model.setBrandName(object.getString("brand_name"));
                                }
                                if (object.has("base_price")) {
                                    model.setBestPrice(object.getString("base_price"));
                                }
                                if (object.has("description")) {
                                    model.setDescription(object.getString("description"));
                                }

                                if (object.has("product_image_path")) {
                                    model.setImagePath(object.getString("product_image_path"));
                                }
                                if (object.has("product_image")) {
                                    model.setImage(object.getString("product_image"));
                                }
                                if (object.has("discounted_price")) {
                                    model.setDiscount(object.getString("discounted_price"));
                                }
                                if (object.has("discount_percentage")) {
                                    model.setDiscountPercent(object.getString("discount_percentage"));
                                }
                                if (object.has("stock")) {
                                    Log.e("STOCKKK", object.getString("stock"));
                                    model.setStock(object.getString("stock"));
                                }
                                if (object.has("begin_discount")) {

                                    model.setBegin_discount(object.getString("begin_discount"));
                                }
                                if (object.has("end_discount")) {

                                    model.setEnd_discount(object.getString("end_discount"));
                                }
                                if (object.has("discounted_price")) {

                                    model.setDiscounted_price(object.getString("discounted_price"));
                                }
                                if (object.has("discount_percentage")) {

                                    model.setDiscount_percentage(object.getString("discount_percentage"));
                                }
                                if (object.has("margin_human")) {

                                    model.setMargin_human(object.getString("margin_human"));
                                }
                                if (object.has("margin_tambahan_human")) {

                                    model.setMargin_tambahan_human(object.getString("margin_tambahan_human"));
                                }
                                if (object.has("margin_tambahan_header_human")) {

                                    model.setMargin_tambahan_header_human(object.getString("margin_tambahan_header_human"));
                                }
                                if (object.has("margin_group_margin")) {

                                    model.setMargin_group_margin(object.getString("margin_group_margin"));
                                }
                                if (object.has("points")) {

                                    model.setPoints(object.getString("points"));
                                }

                                if (object.has("product_images")) {
                                    if (!TextUtils.isEmpty(object.get("product_images").toString())) {
                                        JSONArray imageArr = object.getJSONArray("product_images");

                                        for (int j = 0; j < imageArr.length(); j++) {
                                            imageList2.add(model.getImagePath() + imageArr.getString(j));
                                        }
                                    }
                                    model.setImageList(imageList2);
                                }

                                if (object.has("id_group")) {

                                    model.setPruductId(object.getString("id_group"));
                                }

                                model.setSubProduct(false);
                                if (object.has("group_members")) {
                                    JSONArray objArr = object.getJSONArray("group_members");

                                    model.setSubProduct(objArr.length() > 1);

                                    ArrayList<SubProductModel> subProductList = new ArrayList<>();

                                    for (int j = 0; j < objArr.length(); j++) {
                                        JSONObject object1 = objArr.getJSONObject(j);
                                        SubProductModel subProductModel = new SubProductModel();
                                        if (object1.has("plu")) {
                                            subProductModel.setPlu(object1.getString("plu"));
                                        }
                                        if (object1.has("id_product_alfacart")) {
                                            subProductModel.setId_product_alfacart(object1.getString("id_product_alfacart"));
                                        }
                                        if (object1.has("name")) {
                                            subProductModel.setName(object1.getString("name"));
                                        }
                                        if (object1.has("base_price")) {
                                            subProductModel.setBase_price(object1.getString("base_price"));
                                        }
                                        if (object1.has("discounted_price")) {
                                            subProductModel.setDiscounted_price(object1.getString("discounted_price"));
                                        }
                                        if (object1.has("id_product")) {
                                            subProductModel.setProductId(object1.getString("id_product"));
                                        }
                                        if (object1.has("stock")) {
                                            subProductModel.setStock(object1.getString("stock"));
                                        }

                                        if (i == 0) {
                                            if (object1.has("plu")) {
                                                model.setPlu(object1.getString("plu"));
                                            }
                                            if (object1.has("id_product_alfacart")) {
                                                model.setId_product_alfacart(object1.getString("id_product_alfacart"));
                                            }
                                            if (object1.has("name")) {
                                                model.setName(object1.getString("name"));
                                            }
                                            if (object1.has("base_price")) {
                                                model.setBestPrice(object1.getString("base_price"));
                                            }
                                            if (object1.has("discounted_price")) {
                                                model.setDiscounted_price(object1.getString("discounted_price"));
                                            }
                                            if (object1.has("id_product")) {
                                                model.setPruductId(object1.getString("id_product"));
                                            }
                                            if (object1.has("stock")) {
                                                model.setStock(object1.getString("stock"));
                                            }
                                        }
                                        subProductList.add(subProductModel);

                                    }
                                    model.setSubProductList(subProductList);
                                }
                            }
                        }
                    }
                    loadLayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSnackBar();
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    public void shareProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.PRODUCT_SHARE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTLISTRESPONSECK", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            if (jsonObject.has("ck")) {
                                String ck = jsonObject.getString("ck");
                                Log.e("CK456456456", "" + ck);

                                String URL = Constant.PRODUCT_SHARE_URL + ck;
                                String Discount_Price = model.getDiscounted_price() + "";
                                if (Discount_Price.equals("") && Discount_Price.equals("0") && Discount_Price.equals("null"))
                                    Discount_Price = model.getBestPrice();
                                try {
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    String sAux = model.getName() + "\nRp " + getFormat(Discount_Price) + " - " + URL;
                                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                                    startActivity(Intent.createChooser(i, "choose one"));
                                } catch (Exception e) {
                                    //e.toString();
                                }
                                closeProgress();
                            }
                        }
                    } else
                        closeProgress();

                } catch (Exception e) {
                    closeProgress();
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();
                Log.d("PRODUCT_ID", toktn + "-----------" + model.getPruductId());
                param.put("access_token", toktn + "");
                param.put("id_product", model.getPruductId() + "");

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
}
