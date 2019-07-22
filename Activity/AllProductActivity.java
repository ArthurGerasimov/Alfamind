package id.meteor.alfamind.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Adapter.AlbumAdapter;
import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Interface.AddToCartListiner;
import id.meteor.alfamind.Model.ProductModel;
import id.meteor.alfamind.Model.SubCategoryModel;
import id.meteor.alfamind.Model.SubProductModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.extra.ProductHolder;
import id.meteor.alfamind.extra.SubCatHolder;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class AllProductActivity extends BaseActivity {
    String TAG = "AllProductActivity";

    private int catId, catIdAll;
    private SwipeRefreshLayout swiperefrest;
    private int REQUESTCODE101 = 101, REQUESTCODE102 = 102;
    private RecyclerView recyclerView;
    private ArrayList<ProductModel> productList;
    private ArrayList<ProductModel> productModelArrayList;
    private String OPERATION = "";

    //save data for filter activity
    public static String minimum_rp = null;
    public static String maximum_rp = null;
    public static String brand_list = null;
    public static String suvCategory_list = null;
    public static String sort = null;
    public static String Catergory_name;
    private ArrayList<SubCategoryModel> cList;
    private int flag = 0;
    private int counter = 0;
    private LinearLayout subcategori;
    private View holdsubcatoryView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);

        productList = new ArrayList<>();
        TextView text_header = findViewById(R.id.text_header);
        recyclerView = findViewById(R.id.recylerView);
        LinearLayout sortingBtn = findViewById(R.id.lyt_news);
        LinearLayout filterBtn = findViewById(R.id.lyt_account);
        LinearLayout total_product1 = findViewById(R.id.total_product);
        CardView lay_bottom = findViewById(R.id.lay_bottom);
        TextView total_product_text = findViewById(R.id.total_product_text);
        swiperefrest = findViewById(R.id.swiperefrest);
        subcategori = findViewById(R.id.subcategori);
        final HorizontalScrollView sv = findViewById(R.id.horizontalview);
        //sortingBtn.setEnabled(false);
        showProgress("Please wait...", false);
        sv.setSmoothScrollingEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Log.e("DATA", getIntent().getIntExtra("cat_id", 0) + "");

        catId = getIntent().getIntExtra("cat_id", 0);
        catIdAll = getIntent().getIntExtra("cat_id", 0);


        cList = SubCatHolder.getInstance().getSubCatList((catId + ""));

        if (catId == -1) {
            productModelArrayList = ProductHolder.getInstance().getProductList(1994);
        } else {
            productModelArrayList = ProductHolder.getInstance().getProductList(catId);
        }

        if ((catId + "").equalsIgnoreCase("420")) {
            swiperefrest.setEnabled(false);
            findViewById(R.id.subcategori_title).setVisibility(View.GONE);
            total_product1.setVisibility(View.VISIBLE);
            String total_product = getIntent().getStringExtra("TOTAL");
            total_product_text.setText(total_product + " produk ditemukan dari semua kategori");
            lay_bottom.setVisibility(View.GONE);
            text_header.setText(getIntent().getStringExtra("TITLE"));
            showAllItem(productModelArrayList, "search");
            if (getIntent().hasExtra("OPERATION")) {
                OPERATION = getIntent().getStringExtra("OPERATION");
            }
        }

        if (cList != null) {
            flag = 0;
            counter = cList.size();
            for (int i = 0; i < counter; i++) {

                SubCategoryModel scm = cList.get(i);
                final int position = i;

                // subcatgory  view
                final View subcatgoryView = getLayoutInflater().inflate(R.layout.view_subcatgory_bubble, null);
                subcategori.addView(subcatgoryView);
                final TextView textView = subcatgoryView.findViewById(R.id.text);
                textView.setText(scm.getName());
                subcatgoryView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holdsubcatoryView != null)
                            holdsubcatoryView.setBackground(getResources().getDrawable(R.drawable.subcatogry_back_gray));

                        holdsubcatoryView = textView;
                        textView.setBackground(getResources().getDrawable(R.drawable.subcatogry_back_red));

                        ArrayList<ProductModel> productModelArrayList = ProductHolder.getInstance().getProductList(catIdAll);
                        ArrayList<ProductModel> newListSubcategory = new ArrayList<>();

                        if (productModelArrayList != null)
                            for (int j = 0; j < productModelArrayList.size(); j++) {

                                if (productModelArrayList.get(j).getSubCategoryName().equalsIgnoreCase(textView.getText().toString())) {
                                    newListSubcategory.add(productModelArrayList.get(j));
                                    Log.e("AAASSSAASASASAS", productModelArrayList.get(j).getSubCategoryName());
                                }
                            }
                        showAllItem(newListSubcategory, "all");
                    }
                });
                Log.e("testing", "i = " + scm.getName());
                new DowloadProduct(i, scm.getUrl() + "/" + scm.getId(), scm.getCattegoryName()).startRequest();
            }
        }


        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.image_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OPERATION.equalsIgnoreCase("SEARCH")) {
                    Intent i = new Intent();
                    i.putExtra("DATA", "HOME");
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
                onBackPressed();
            }
        });

        sortingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("testing", "flag = " + flag + "  counter = " + counter + "------>" + productModelArrayList.size());
                if (flag == counter) {
                    Intent intent = new Intent(AllProductActivity.this, SortActivity.class);
                    intent.putExtra("DATA", "Sortir Belanjaan");
                    startActivityForResult(intent, REQUESTCODE102);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else {
                    Toast.makeText(_context, "Please wait!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllProductActivity.this, FilterActivity.class);
                ArrayList<ProductModel> productModelArrayList = ProductHolder.getInstance().getProductList(catIdAll);
                ArrayList<Integer> Price_list = new ArrayList<>();

                if (productModelArrayList != null)
                    for (int i = 0; i < productModelArrayList.size(); i++)
                        Price_list.add(Integer.parseInt(productModelArrayList.get(i).getBestPrice()));

                intent.putExtra("DATA", "Filter Belanjaan");
                intent.putExtra("MIN", Collections.min(Price_list) + "");
                intent.putExtra("MAX", Collections.max(Price_list) + "");
                intent.putExtra("cid", catIdAll + "");
                startActivityForResult(intent, REQUESTCODE101);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        swiperefrest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sv.scrollTo(0, sv.getBottom());
                if (holdsubcatoryView!=null)
                holdsubcatoryView.setBackground(getResources().getDrawable(R.drawable.subcatogry_back_gray));
                assert cList != null;
                for (int i = 0; i < cList.size(); i++) {
                    showSubCatageoryItem(cList.get(i), i);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE101 && resultCode == RESULT_OK) {
            Log.e("RETURNDATA", "GO BACK 101");
            catId = data.getIntExtra("cat_id", 0);
            ArrayList<ProductModel> temp = ProductHolder.getInstance().getProductList(catId);
            showAllItem(temp, "all");
        }

        if (requestCode == REQUESTCODE102 && resultCode == RESULT_OK) {
            Log.e("SORT_DATA", data.getStringExtra("SORT_DATA"));
            sort = data.getStringExtra("SORT_DATA") + "";
            ArrayList<ProductModel> temp = new ArrayList<>();
            Log.e("DADATDAD", "" + productModelArrayList.size() + "------>" + productList.size());
            temp.addAll(productList);
            Collections.sort(temp, new CommentComparator(Integer.parseInt(data.getStringExtra("SORT_DATA"))));
            Log.e("dsnfhgdfff", "" + temp.size());
            //Log.e("dsnfhgdfff", "" + ProductHolder.getInstance().getProductList(catId).get(0));
            showAllItem(temp, "all");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showAllItem(ArrayList<ProductModel> productModelArrayList, String opration) {
        Log.e("CATID", catId + "");
        AlbumAdapter adapter = new AlbumAdapter(this, opration, productModelArrayList, new AddToCartListiner() {

            @Override
            public void addToCart(String productId, String Stock, boolean subProduct) {
                if (MyApplication.isNetworkAvailable(AllProductActivity.this)) {
                    if (MyApplication.getInstance().getPrefManager().getIsLogged()) {

                        if (subProduct) {
                            getErrorDialog("Silahkan memilih ukuran dahulu");
                        } else {
                            if (!Stock.equalsIgnoreCase("0")) {
                                itemAddToCart(productId);
                            } else
                                getErrorDialog("Stock tidak tersedia");
                        }

                    } else {
                        startActivity(new Intent(AllProductActivity.this, ChekOutErrorActivity.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                } else {
                    showSnackBar();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swiperefrest.setRefreshing(false);

        if (catId == 420) {
            closeProgress();
        }
    }


    public void itemAddToCart(final String pruductId) {
        showProgress("Please wait...", false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTRESPONSE", response + "");
                closeProgress();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        if (jsonObject.has("message")) {
                            Toast.makeText(AllProductActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                        }
                        if (jsonObject.has("id_cart")) {
                            MyApplication.getInstance().getPrefManager().setCartId(jsonObject.getString("id_cart"));
                        }
                    } else {
                        getErrorDialog(jsonObject.getString("message"));
                    }
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();
                String idCart = MyApplication.getInstance().getPrefManager().getCartID();
                Log.d("PRODUCT_ID", pruductId + " " + toktn + "   idcart_>" + idCart + "");
                param.put("id_cart", idCart + "");
                param.put("access_token", toktn + "");
                param.put("id_product", pruductId + "");
                param.put("quantity", 1 + "");
                param.put("operation", "up");
                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void showSnackBar() {
        RelativeLayout main_parent_layout = findViewById(R.id.main_layout);
        Snackbar snackbar = Snackbar.make(main_parent_layout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    public class CommentComparator implements Comparator<ProductModel> {

        private int SORT;

        CommentComparator(int type) {
            SORT = type;
        }

        public int compare(ProductModel o1, ProductModel o2) {

            Log.d("CommentComparator", o1.getBestPrice() + " -- " + o2.getBestPrice() + "");
            int a = Integer.parseInt(o1.getBestPrice()), b = Integer.parseInt(o2.getBestPrice());
            String x = o1.getName(), y = o2.getName();
            x = x.toLowerCase();
            y = y.toLowerCase();
            long stock1 = o1.getStock(), stock2 = o2.getStock();

            switch (SORT) {
                case 1:
                    if (a == b)
                        return 0;
                    else if (a > b)
                        return 1;
                    else
                        return -1;

                case 2:
                    if (a == b)
                        return 0;
                    else if (a < b)
                        return 1;
                    else
                        return -1;

                case 3:
                    return x.compareTo(y);

                case 4:
                    return y.compareTo(x);

                case 5:
                    if (stock1 == stock2)
                        return 0;
                    else if (stock1 < stock2)
                        return 1;
                    else
                        return -1;

                case 6:
                    if (stock1 == stock2)
                        return 0;
                    else if (stock1 > stock2)
                        return 1;
                    else
                        return -1;

                default:
                    return o2.getBestPrice().compareTo(o1.getBestPrice());

            }
        }
    }


    public class DowloadProduct {

        int holdValue;
        String url, categoryName;

        DowloadProduct(int val, String url, String categoryName) {
            this.holdValue = val;
            this.url = url;
            this.categoryName = categoryName;
        }

        void startRequest() {
            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            Log.e("VALUE_THREAD", holdValue + "  ");
                            for (int i = 0; i < data.length(); i++) {
                                ProductModel productModel = new ProductModel();
                                ArrayList<String> imageList = new ArrayList<>();
                                JSONObject object = data.getJSONObject(i);

                                productModel.setCattegoryName(categoryName);

                                if (i == 0)
                                    Catergory_name = categoryName;

                                productModel.setCatId(catId + "");
                                if (object.has("subcategory_label")) {
                                    productModel.setSubCategoryName(object.getString("subcategory_label"));
                                }
                                if (object.has("plu")) {
                                    productModel.setPlu(object.getString("plu"));
                                }
                                if (object.has("id_product")) {
                                    productModel.setPruductId(object.getString("id_product"));
                                }
                                if (object.has("name")) {
                                    productModel.setName(object.getString("name"));
                                }
                                if (object.has("id_subcategory")) {
                                    productModel.setSubCatId(object.getString("id_subcategory"));
                                }
                                if (object.has("brand_name")) {
                                    productModel.setBrandName(object.getString("brand_name"));
                                }
                                if (object.has("base_price")) {
                                    productModel.setBestPrice(object.getString("base_price"));
                                }
                                if (object.has("description")) {
                                    productModel.setDescription(object.getString("description"));
                                }

                                if (object.has("product_image_path")) {
                                    productModel.setImagePath(object.getString("product_image_path"));
                                }
                                if (object.has("product_image")) {
                                    productModel.setImage(object.getString("product_image"));
                                }
                                if (object.has("discounted_price")) {
                                    productModel.setDiscount(object.getString("discounted_price"));
                                }
                                if (object.has("discount_percentage")) {
                                    productModel.setDiscountPercent(object.getString("discount_percentage"));
                                }
                                if (object.has("stock")) {

                                    productModel.setStock(object.getString("stock"));
                                }
                                if (object.has("begin_discount")) {

                                    productModel.setBegin_discount(object.getString("begin_discount"));
                                }
                                if (object.has("end_discount")) {

                                    productModel.setEnd_discount(object.getString("end_discount"));
                                }
                                if (object.has("discounted_price")) {

                                    productModel.setDiscounted_price(object.getString("discounted_price"));
                                }
                                if (object.has("discount_percentage")) {

                                    productModel.setDiscount_percentage(object.getString("discount_percentage"));
                                }
                                if (object.has("margin_human")) {

                                    productModel.setMargin_human(object.getString("margin_human"));
                                }
                                if (object.has("margin_tambahan_human")) {

                                    productModel.setMargin_tambahan_human(object.getString("margin_tambahan_human"));
                                }
                                if (object.has("margin_tambahan_header_human")) {

                                    productModel.setMargin_tambahan_header_human(object.getString("margin_tambahan_header_human"));
                                }
                                if (object.has("margin_group_margin")) {

                                    productModel.setMargin_group_margin(object.getString("margin_group_margin"));
                                }
                                if (object.has("points")) {

                                    productModel.setPoints(object.getString("points"));
                                }

                                if (object.has("product_images")) {
                                    if (!TextUtils.isEmpty(object.get("product_images").toString())) {
                                        JSONArray imageArr = object.getJSONArray("product_images");

                                        for (int j = 0; j < imageArr.length(); j++) {
                                            imageList.add(productModel.getImagePath() + imageArr.getString(j));
                                        }
                                    }
                                    productModel.setImageList(imageList);
                                }

                                if (object.has("id_group")) {

                                    productModel.setPruductId(object.getString("id_group"));
                                }

                                //set wobbler_data...
                                if (object.has("wobbler_data")) {
                                    JSONArray wobblerArray = object.getJSONArray("wobbler_data");
                                    ArrayList<String> wobblerImages = new ArrayList<>();
                                    for (int j = 0; j < wobblerArray.length(); j++) {
                                        String image = wobblerArray.getString(j);
                                        if (!TextUtils.isEmpty(image) && wobblerImages.size() < 3) {
                                            wobblerImages.add(image);
                                        }
                                    }
                                    productModel.setWobblerImageList(wobblerImages);
                                }
                                productModel.setSubProduct(false);

                                if (object.has("group_members")) {
                                    JSONArray objArr = object.getJSONArray("group_members");
                                    ArrayList<SubProductModel> subProductList = new ArrayList<>();

                                    productModel.setSubProduct(objArr.length() > 1);
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
                                        subProductList.add(subProductModel);
                                    }
                                    productModel.setSubProductList(subProductList);
                                }
                                productList.add(productModel);

                            }

                            ProductHolder.getInstance().putProductList(catId, productList);
                            showAllItem(productList, "all");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        flag++;
                        if (flag == counter) {
                            closeProgress();
                        }
                        Log.e("testing", "flat = " + flag);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (!MyApplication.isNetworkAvailable(getApplicationContext())) {
                        showSnackBar();
                    }
                    error.printStackTrace();
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }


    private void showSubCatageoryItem(final SubCategoryModel model, final int val) {
        productList.clear();
        final String url = model.getUrl() + "/" + model.getId();

        Log.e("DADDADADADAD", model.getUrl() + "--------->" + model.getId());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SUB_CAT_ITEM " + url + " " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //    progressDialog.dismiss();
                    if (jsonObject.getString("status").equals("success")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        Log.e("VALUE_THREAD", val + "  ");
                        for (int i = 0; i < data.length(); i++) {
                            ProductModel productModel = new ProductModel();
                            ArrayList<String> imageList = new ArrayList<>();
                            JSONObject object = data.getJSONObject(i);

                            productModel.setCattegoryName(model.getCattegoryName());

                            if (i == 0)
                                Catergory_name = model.getCattegoryName();

                            productModel.setCatId(catId + "");
                            if (object.has("subcategory_label")) {
                                productModel.setSubCategoryName(object.getString("subcategory_label"));
                            }
                            if (object.has("plu")) {
                                productModel.setPlu(object.getString("plu"));
                            }
                            if (object.has("id_product")) {
                                productModel.setPruductId(object.getString("id_product"));
                            }
                            if (object.has("name")) {
                                productModel.setName(object.getString("name"));
                            }
                            if (object.has("id_subcategory")) {
                                productModel.setSubCatId(object.getString("id_subcategory"));
                            }
                            if (object.has("brand_name")) {
                                productModel.setBrandName(object.getString("brand_name"));
                            }
                            if (object.has("base_price")) {
                                productModel.setBestPrice(object.getString("base_price"));
                            }
                            if (object.has("description")) {
                                productModel.setDescription(object.getString("description"));
                            }
                            if (object.has("product_image_path")) {
                                productModel.setImagePath(object.getString("product_image_path"));
                            }
                            if (object.has("product_image")) {
                                productModel.setImage(object.getString("product_image"));
                            }
                            if (object.has("discounted_price")) {
                                productModel.setDiscount(object.getString("discounted_price"));
                            }
                            if (object.has("discount_percentage")) {
                                productModel.setDiscountPercent(object.getString("discount_percentage"));
                            }
                            if (object.has("stock")) {

                                productModel.setStock(object.getString("stock"));
                            }
                            if (object.has("begin_discount")) {

                                productModel.setBegin_discount(object.getString("begin_discount"));
                            }
                            if (object.has("end_discount")) {

                                productModel.setEnd_discount(object.getString("end_discount"));
                            }
                            if (object.has("discounted_price")) {

                                productModel.setDiscounted_price(object.getString("discounted_price"));
                            }
                            if (object.has("discount_percentage")) {

                                productModel.setDiscount_percentage(object.getString("discount_percentage"));
                            }
                            if (object.has("margin_human")) {

                                productModel.setMargin_human(object.getString("margin_human"));
                            }
                            if (object.has("margin_tambahan_human")) {

                                productModel.setMargin_tambahan_human(object.getString("margin_tambahan_human"));
                            }
                            if (object.has("margin_tambahan_header_human")) {

                                productModel.setMargin_tambahan_header_human(object.getString("margin_tambahan_header_human"));
                            }
                            if (object.has("margin_group_margin")) {

                                productModel.setMargin_group_margin(object.getString("margin_group_margin"));
                            }
                            if (object.has("points")) {

                                productModel.setPoints(object.getString("points"));
                            }

                            if (object.has("product_images")) {
                                if (!TextUtils.isEmpty(object.get("product_images").toString())) {
                                    JSONArray imageArr = object.getJSONArray("product_images");

                                    for (int j = 0; j < imageArr.length(); j++) {
                                        imageList.add(productModel.getImagePath() + imageArr.getString(j));
                                    }
                                }
                                productModel.setImageList(imageList);
                            }

                            if (object.has("id_group")) {

                                productModel.setPruductId(object.getString("id_group"));
                            }

                            //set wobbler_data...
                            if (object.has("wobbler_data")) {
                                JSONArray wobblerArray = object.getJSONArray("wobbler_data");
                                ArrayList<String> wobblerImages = new ArrayList<>();
                                for (int j = 0; j < wobblerArray.length(); j++) {
                                    String image = wobblerArray.getString(j);
                                    if (!TextUtils.isEmpty(image) && wobblerImages.size() < 3) {
                                        wobblerImages.add(image);
                                    }
                                }
                                productModel.setWobblerImageList(wobblerImages);
                            }
                            productModel.setSubProduct(false);

                            if (object.has("group_members")) {
                                JSONArray objArr = object.getJSONArray("group_members");
                                ArrayList<SubProductModel> subProductList = new ArrayList<>();

                                productModel.setSubProduct(objArr.length() > 1);
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
                                    subProductList.add(subProductModel);
                                }
                                productModel.setSubProductList(subProductList);
                            }
                            productList.add(productModel);

                        }

                        ProductHolder.getInstance().putProductList(catId, productList);
                        showAllItem(productList, "all");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!MyApplication.isNetworkAvailable(getApplicationContext())) {
                    showSnackBar();
                }
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onDestroy() {
        minimum_rp = null;
        maximum_rp = null;
        brand_list = null;
        suvCategory_list = null;
        sort = null;
        Catergory_name = null;
        super.onDestroy();
    }
}
