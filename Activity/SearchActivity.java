package id.meteor.alfamind.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Adapter.SearchListAdapter;
import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Interface.AddToCartListiner;
import id.meteor.alfamind.Model.ProductModel;
import id.meteor.alfamind.Model.SubProductModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.extra.ProductHolder;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class SearchActivity extends BaseActivity {


    final ArrayList<ProductModel> tempProductModel = new ArrayList<>();
    String str;
    EditText et_search;
    ListView listView;
    private AddToCartListiner listiner;
    private ImageView cross;
    private ProgressBar loader;
    private final int REQUEST_CODE = 100;
    private boolean isTyping;

    long delay = 500; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    private Runnable input_finish_checker = new Runnable() {
        public void run() {

            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                searchProductAPI(et_search.getText().toString(), false);
                Log.e("HAHAHAHAHA", "run");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_fullscreen);
        setContentView(R.layout.activity_search);

        cross = findViewById(R.id.cross);
        loader = findViewById(R.id.loader);
        et_search = findViewById(R.id.et_search);
        listView = findViewById(R.id.searchListView);
        handler = new Handler();

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                et_search.setText("");
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("Enter pressed", "Enter pressed");
                    searchProductAPI(str, true);
                }
                return false;
            }
        });

        addListiner();
    }


    private void addListiner() {
        listiner = new AddToCartListiner() {
            @Override
            public void addToCart(String productId, String Stock, boolean Category) {
                if (MyApplication.isNetworkAvailable(SearchActivity.this)) {
                    itemAddToCart(productId);
                } else {
                    showSnackBar();
                }
            }
        };


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("AAAAAAASSAAS", "onBTC");
                isTyping = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listView.setAdapter(null);
                loader.setVisibility(View.VISIBLE);
                cross.setVisibility(View.VISIBLE);
                handler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable editable) {

                str = et_search.getText().toString();

                if (str.equalsIgnoreCase("") || str.equalsIgnoreCase("null") || str == null) {
                    cross.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                }

                if (editable.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                }
            }
        });
    }

    ///////  Item Added To Cart....
    public void itemAddToCart(final String pruductId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTRESPONSE", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        if (jsonObject.has("message")) {
                            Toast.makeText(SearchActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                        }
                        if (jsonObject.has("id_cart")) {
                            MyApplication.getInstance().getPrefManager().setCartId(jsonObject.getString("id_cart"));
                        }
                    } else {
                        getErrorDialog(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getErrorDialog("Server error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();

                String idCart = MyApplication.getInstance().getPrefManager().getCartID();
                if (toktn == null || toktn.equals("null") || toktn.equals("")) {
                    toktn = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }

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


    ////////////    SearchingProduct......
    private void searchProductAPI(final String str, final boolean value) {

        tempProductModel.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SEARCH_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTDEleteLISTRESPONSE", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equals("success")) {
                            if (jsonObject.has("data")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                if (jsonObject1.has("search_result")) {
                                    JSONArray array = jsonObject1.getJSONArray("search_result");

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);

                                        ProductModel model = new ProductModel();
                                        if (object.has("name")) {
                                            model.setName(object.getString("name"));
                                        }
                                        if (object.has("type")) {
                                            model.setType(object.getString("type"));
                                        }
                                        if (object.has("category_id")) {
                                            model.setCatId(object.getString("category_id"));
                                        }
                                        if (object.has("subcategory_id")) {
                                            model.setSubCatId(object.getString("subcategory_id"));
                                        }
                                        if (object.has("category_label")) {
                                            model.setCattegoryName(object.getString("category_label"));
                                        }
                                        if (object.has("subcategory_id")) {
                                            model.setSubCatId(object.getString("subcategory_id"));
                                        }
                                        if (object.has("subcategory_label")) {
                                            model.setSubCatName(object.getString("subcategory_label"));
                                        }
                                        if (object.has("department_id")) {
                                            model.setDepartment_id(object.getString("department_id"));
                                        }
                                        if (object.has("subdepartment_id")) {
                                            model.setSubdepartment_id(object.getString("subdepartment_id"));
                                        }
                                        if (object.has("plu")) {
                                            model.setPlu(object.getString("plu"));
                                        }
                                        if (object.has("id_product")) {
                                            model.setPruductId(object.getString("id_product"));
                                        }
                                        if (object.has("id_product_alfacart")) {
                                            model.setId_product_alfacart(object.getString("id_product_alfacart"));
                                        }
                                        if (object.has("base_price")) {
                                            model.setBestPrice(object.getString("base_price"));
                                        }
                                        if (object.has("stock")) {
                                            model.setStock(object.getString("stock"));
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
                                        if (object.has("point")) {
                                            model.setPoints(object.getString("point"));
                                        }
                                        //set wobbler_data...
                                        if (object.has("wobbler_data")) {
                                            if (!object.getString("wobbler_data").equalsIgnoreCase("null") && !object.getString("wobbler_data").equalsIgnoreCase("") && !object.getString("wobbler_data").equalsIgnoreCase("")) {
                                                JSONArray wobblerArray = object.getJSONArray("wobbler_data");
                                                ArrayList<String> wobblerImages = new ArrayList<>();
                                                for (int j = 0; j < wobblerArray.length(); j++) {
                                                    String image = wobblerArray.getString(j);
                                                    if (!TextUtils.isEmpty(image) && wobblerImages.size() < 3) {
                                                        wobblerImages.add(image);
                                                    }
                                                }
                                                model.setWobblerImageList(wobblerImages);
                                            }
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

                                                subProductList.add(subProductModel);
                                            }
                                            model.setSubProductList(subProductList);
                                        }
                                        model.setImagePath("");
                                        model.setBrandName(object.getString("brand_label"));
                                        ArrayList<String> img = new ArrayList<>();
                                        img.add(object.getString("product_image"));
                                        model.setImageList(img);
                                        tempProductModel.add(model);
                                    }
                                }
                            }
                            ProductHolder.getInstance().putProductList(420, tempProductModel);
                            if (value) {
                                Intent intent = new Intent(SearchActivity.this, AllProductActivity.class);
                                intent.putExtra("cat_id", 420);
                                intent.putExtra("OPERATION", "SEARCH");
                                intent.putExtra("TITLE", str);
                                intent.putExtra("TOTAL", tempProductModel.size() + "");
                                startActivityForResult(intent, REQUEST_CODE);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            }
                            SearchListAdapter listAdapter = new SearchListAdapter(SearchActivity.this, tempProductModel, listiner, str);
                            listView.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
                            loader.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("q", str + "");
                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {

            String Value = data.getStringExtra("DATA");
            if (Value.equalsIgnoreCase("HOME"))
                finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showSnackBar() {
        LinearLayout main_parent_layout = findViewById(R.id.main_layout);
        Snackbar snackbar = Snackbar.make(main_parent_layout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
