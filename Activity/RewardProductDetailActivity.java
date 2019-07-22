package id.meteor.alfamind.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Adapter.ImageAdapter;
import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Model.RewardModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.extra.ImagesHolder;
import id.meteor.alfamind.extra.RewardHolder;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class RewardProductDetailActivity extends BaseActivity {

    private RewardModel model;
    private TextView quantity;
    private ProgressBar process_bar;
    private ViewPager viewPager;
    private int mDotsCount;
    private static TextView[] mDotsText;
    private int Product_id;
    ArrayList<Bitmap> imageList;
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_product_detail);

        Product_id = Integer.parseInt(getIntent().getStringExtra("id"));

        model = RewardHolder.getInstance().getProductDetail(Product_id);
        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        loadLayout();
    }


    private void loadLayout() {
        TextView product_name = findViewById(R.id.product_name);
        TextView total_inventory = findViewById(R.id.total_inventory);
        TextView point = findViewById(R.id.point);
        TextView text_description = findViewById(R.id.text_description);
        TextView kategori = findViewById(R.id.kategori);
        TextView offer_period = findViewById(R.id.offer_period);
        ImageView image_plus = findViewById(R.id.image_plus);
        ImageView image_minus = findViewById(R.id.image_minus);
        ImageView previous_image = findViewById(R.id.previous_image);
        ImageView nextImage = findViewById(R.id.nextImage);
        LinearLayout mDotsLayout = findViewById(R.id.image_count);
        TextView next = findViewById(R.id.next);

        quantity = findViewById(R.id.quantity);
        process_bar = findViewById(R.id.process_bar);
        viewPager = findViewById(R.id.image_viewPager);
        imageList = new ArrayList<>();

        ArrayList<String> urlList = model.getImageList();
        if (urlList != null && urlList.size() > 0) {

            process_bar.setVisibility(View.VISIBLE);
            for (int i = 0; i < urlList.size(); i++) {
                if (MyApplication.isNetworkAvailable(RewardProductDetailActivity.this)) {
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
                        RewardProductDetailActivity.mDotsText[j].setTextColor(Color.GRAY);
                    }
                    RewardProductDetailActivity.mDotsText[position].setTextColor(Color.RED);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

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

            product_name.setText(model.getTitle());
            total_inventory.setText(model.getStock() + "");
            point.setText(model.getRequired_points() + "");
            text_description.setText(model.getDescription() + "");
            kategori.setText(model.getCattegoryName() + "");
            offer_period.setText(dateFormat(model.getOffer_validity_start()) + " - " + dateFormat(model.getOffer_validity_end()));

            image_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tempQuantity = Integer.parseInt(quantity.getText().toString());

                    if (tempQuantity > 1) {
                        quantity.setText((tempQuantity - 1) + "");
                    }

                }
            });
            image_plus.setOnClickListener(new View.OnClickListener() {
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

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress("Please wait...",false);
                    redeemReward();
                }
            });
        }
    }

    public void redeemReward() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.REWARDS_REDEEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                closeProgress();
                Log.d("RewardHISTORY", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            if (jsonObject.has("message")) {
                                showPopUp(jsonObject.getString("message"));
                            }
                        } else
                            showPopUp(jsonObject.getString("message"));
                    } else {
                        closeProgress();
                        showPopUp(jsonObject.getString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    showPopUp("server error");
                    closeProgress();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getErrorDialog("Something went wrong");
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                String token = MyApplication.getInstance().getPrefManager().getAccessToken();

                param.put("access_token", token + "");
                Log.e("ACCESS_TOKEN", token + "");
                param.put("reward_id", Product_id + "");
                Log.e("ACCESS_TOKEN", Product_id + "");
                param.put("alt_firstnamelastname", MyApplication.getInstance().getPrefManager().getFirstName() + " " + MyApplication.getInstance().getPrefManager().getLastName());
                Log.e("ACCESS_TOKEN", MyApplication.getInstance().getPrefManager().getFirstName() + " " + MyApplication.getInstance().getPrefManager().getLastName());
                param.put("alt_phone_mobile", MyApplication.getInstance().getPrefManager().getPhoneNumber() + "");
                Log.e("ACCESS_TOKEN", MyApplication.getInstance().getPrefManager().getPhoneNumber() + "");
                param.put("alt_address", MyApplication.getInstance().getPrefManager().getAddress() + "");
                Log.e("ACCESS_TOKEN", MyApplication.getInstance().getPrefManager().getAddress() + "");
                param.put("alt_email", MyApplication.getInstance().getPrefManager().getEmail() + "");
                Log.e("ACCESS_TOKEN", MyApplication.getInstance().getPrefManager().getEmail() + "");
                param.put("quantity", quantity.getText().toString() + "");
                Log.e("ACCESS_TOKEN", quantity.getText().toString() + "");

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void downloadImage(final String url, final ImageView imageView) {
        Log.d("ImageUrl", "  " + url + "");
        Glide.with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(false)
                .into(new SimpleTarget<Bitmap>(1000, 1000) {


                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                        if (imageView != null) {
                            imageView.setImageBitmap(resource);
                            process_bar.setVisibility(View.GONE);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(RewardProductDetailActivity.this, FullScreenImageActivity.class);
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


    public String dateFormat(String str) {

        String divide[] = str.split(" ");

        String[] year = divide[0].split("-");
        str = year[2];
        switch (year[1]) {
            case "01":
                str = str + " Januari ";
                break;
            case "02":
                str = str + " Februari ";
                break;
            case "03":
                str = str + " Maret ";
                break;
            case "04":
                str = str + " April ";
                break;
            case "05":
                str = str + " Mei ";
                break;
            case "06":
                str = str + " Juni ";
                break;
            case "07":
                str = str + " Juli ";
                break;
            case "08":
                str = str + " Agustus ";
                break;
            case "09":
                str = str + " September ";
                break;
            case "10":
                str = str + " Oktober ";
                break;
            case "11":
                str = str + " November ";
                break;
            case "12":
                str = str + " Desember ";
                break;
        }

        str = str + year[0];
        return str;

    }
}
