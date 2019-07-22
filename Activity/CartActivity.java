package id.meteor.alfamind.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Model.ProductModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class CartActivity extends BaseActivity {

    private TextView next;
    private LinearLayout layout_cart;
    private boolean isLogged;
    private long totalAmountText = 0;
    private TextView total_amount, empty;
    private LinearLayout total_layout;
    private int REQUEST_CODE = 100;
    private ProgressBar pBar;

    private String cartId;
    private ImageView trash_btn;
    ArrayList<ProductModel> list;
    HashMap<String, ArrayList<ProductModel>> cartMap = new HashMap<>();
    HashMap<String, ProductModel> cartItems = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        next = findViewById(R.id.next);
        pBar = findViewById(R.id.pBar);
        total_amount = findViewById(R.id.total_amount);
        ImageView back_btn = findViewById(R.id.back_btn);
        trash_btn = findViewById(R.id.trash_btn);
        layout_cart = findViewById(R.id.layout_cart);
        total_layout = findViewById(R.id.total_layout);
        empty = findViewById(R.id.empty);

        cartId = MyApplication.getInstance().getPrefManager().getCartID();

        if (MyApplication.isNetworkAvailable(CartActivity.this)) {
            getCartList();
        } else {
            showSnackBar();
        }


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // next btn
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isLogged) {
                    startActivity(new Intent(CartActivity.this, ChekOutErrorActivity.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else {
                    if (MyApplication.isNetworkAvailable(CartActivity.this)) {
                        getCkFromService();
                    } else {
                        showSnackBar();
                    }
                }
            }
        });

        trash_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogDeleteAll();
            }
        });


        isLogged = MyApplication.getInstance().getPrefManager().getIsLogged();

    }

    private void getCkFromService() {

        showProgress("Please wait...", false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_CK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTLISTRESPONSECK", response + "");
                try {

                    closeProgress();
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            if (jsonObject.has("ck")) {
                                Log.e("ck", jsonObject.getString("ck") + "");
                                String ck = jsonObject.getString("ck");
                                Intent i = new Intent(CartActivity.this, SaldoWebViewActivity.class);
                                i.putExtra("DATA", "CHECK_OUT");
                                i.putExtra("ck", ck);
                                startActivityForResult(i, REQUEST_CODE);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    pBar.setVisibility(View.GONE);
                    closeProgress();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pBar.setVisibility(View.GONE);
                if (!MyApplication.isNetworkAvailable(CartActivity.this)) {
                    getErrorDialog("Tidak ada koneksi internet");
                }
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();

                if (toktn == null || toktn.equals("null") || toktn.equals("")) {
                    toktn = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }

                Log.d("PRODUCT_ID", toktn + "   -  " + cartId + "");
                param.put("id_cart", cartId + "");
                param.put("access_token", toktn + "");

                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void getCartList() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        list = new ArrayList<>();
        totalAmountText = 0;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_CART_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTLISTRESPONSE8787", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("cart_contents")) {

                            if (!jsonObject.getString("cart_contents").equalsIgnoreCase("null")) {

                                JSONArray array = jsonObject.getJSONArray("cart_contents");
                                for (int i = 0; i < array.length(); i++) {
                                    ProductModel productModel = new ProductModel();
                                    JSONObject cartObject = array.getJSONObject(i);
                                    if (cartObject.has("product_id")) {

                                        productModel.setPruductId(cartObject.getString("product_id"));
                                    }
                                    if (cartObject.has("product_info")) {
                                        JSONObject prodectObj = cartObject.getJSONObject("product_info");

                                        if (prodectObj.has("name")) {

                                            productModel.setName(prodectObj.getString("name"));
                                        }
                                        if (prodectObj.has("id_subcategory")) {
                                            productModel.setSubCatId(prodectObj.getString("id_subcategory"));
                                        }
                                        if (prodectObj.has("price")) {

                                            productModel.setBestPrice(prodectObj.getString("price"));
                                        }
                                        if (prodectObj.has("price_discounted")) {
                                            productModel.setDiscounted_price(prodectObj.getString("price_discounted"));

                                        }
                                        if (prodectObj.has("cart_item_quantity")) {

                                            if (!prodectObj.getString("cart_item_quantity").equalsIgnoreCase("null") && !prodectObj.getString("cart_item_quantity").equalsIgnoreCase(""))
                                                productModel.setQuantity(Integer.parseInt(prodectObj.getString("cart_item_quantity")));
                                        }
                                        if (prodectObj.has("image_url")) {

                                            productModel.setImage(prodectObj.getString("image_url"));
                                        }
                                        cartItems.put(productModel.getPruductId(), productModel);
                                        list.add(productModel);
                                    }
                                }

                                cartMap.put(cartId, list);
                                pBar.setVisibility(View.GONE);

                                showCartListitem();
                            } else {
                                showCartListitem();
                            }

                        } else {
                            pBar.setVisibility(View.GONE);
                        }
                    } else {
                        showCartListitem();
                    }
                    // progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    pBar.setVisibility(View.GONE);
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pBar.setVisibility(View.GONE);
                if (!MyApplication.isNetworkAvailable(CartActivity.this)) {
                    getErrorDialog("Tidak ada koneksi internet");
                }
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();

                if (toktn == null || toktn.equals("null") || toktn.equals("")) {
                    toktn = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }

                Log.d("PRODUCT_ID", toktn + "   -  " + cartId + "");
                param.put("id_cart", cartId + "");
                param.put("access_token", toktn + "");

                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    @SuppressLint("SetTextI18n")
    private void showCartList(final ProductModel productModel, final int position) {
        String bestPrice = productModel.getBestPrice();
        String discountPrice = productModel.getDiscounted_price();

        if (Integer.parseInt(discountPrice) > 0) {
            bestPrice = productModel.getDiscounted_price();
        }
        final int l = bestPrice.indexOf('.');
        if (l > 0) {
            bestPrice = bestPrice.substring(0, l);
        }
        Log.d("BEST_PRICE", bestPrice + "");

        int tempQuantity = productModel.getQuantity();
        long tempPrice = Long.parseLong(bestPrice);
        long totalPrice = tempPrice * tempQuantity;

        totalAmountText = totalAmountText + totalPrice;
        total_amount.setText(getFormat(totalAmountText + ""));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.list_cart_item, null);

        final TextView name = view.findViewById(R.id.product_name);
        final TextView price = view.findViewById(R.id.product_price);
        final TextView total_price = view.findViewById(R.id.total_price);
        final TextView quantity = view.findViewById(R.id.p_quantity);
        final ImageView pImage = view.findViewById(R.id.p_image);
        final ImageView minus = view.findViewById(R.id.image_minus);
        final ImageView plus = view.findViewById(R.id.image_plus);
        final TextView delete_cart_item = view.findViewById(R.id.delete_cart_item);
        quantity.setText(tempQuantity + "");
        name.setText(productModel.getName());

        price.setText("Rp " + getFormat(bestPrice) + " /pcs");
        total_price.setText("Total Rp " + getFormat(totalPrice + ""));

        if (productModel.getBitmap() != null) {
            pImage.setImageBitmap(productModel.getBitmap());
        } else {
            downloadImage(productModel, pImage);
        }

        final String tempBestPrice = bestPrice;

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.isNetworkAvailable(CartActivity.this)) {
                    itemAddToCart(productModel.getPruductId(), "up", tempBestPrice, quantity, total_price);
                } else {
                    showSnackBar();
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.isNetworkAvailable(CartActivity.this)) {
                    int temp = Integer.parseInt(quantity.getText().toString());
                    if (temp > 1) {
                        itemAddToCart(productModel.getPruductId(), "down", tempBestPrice, quantity, total_price);
                    }
                } else {
                    showSnackBar();
                }
            }
        });

        delete_cart_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = getResources().getString(R.string.delete_from_cart);
                showAlertDialog(msg, productModel.getPruductId(), position);
            }
        });
        layout_cart.addView(view);
    }

    private void showCartListitem() {

        layout_cart.removeAllViews();

        if (list.size() <= 0) {
            trash_btn.setVisibility(View.GONE);
            total_layout.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            pBar.setVisibility(View.GONE);

        } else {
            next.setVisibility(View.VISIBLE);
            total_layout.setVisibility(View.VISIBLE);

            for (int i = 0; i < list.size(); i++) {
                Log.e("COUNT", "" + i);
                trash_btn.setVisibility(View.VISIBLE);
                showCartList(list.get(i), i);
            }
        }
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
        Log.e("RESULT-DONE", String.valueOf(stringBuffer));

        Log.e("RESULT*", NUM.toString());
        return String.valueOf(stringBuffer);
    }

    public void downloadImage(final ProductModel productModel, final ImageView imageView) {
        Log.d("ImageUrl", "  " + productModel.getImage() + "  - " + imageView);
        if (!CartActivity.this.isFinishing()) {
            Glide.with(getApplicationContext())
                    .load(productModel.getImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>(300, 300) {

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            imageView.setImageBitmap(resource);
                            productModel.setBitmap(resource);
                            // process_bar.setVisibility(View.GONE);
                            Log.d("ImageUrl", " success");
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            Log.d("ImageUrl", " failed");
                            e.printStackTrace();
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });
        }

    }


    @SuppressLint("StaticFieldLeak")
    private void showAlertDialog(final String msg, final String pId, final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("Dialog", "Alert");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CartActivity.this);
                dialogBuilder.setMessage(msg);
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFromCart(pId, position);
                        dialog.cancel();
                    }
                });
                dialogBuilder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = dialogBuilder.create();
                alert.show();

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void showAlertDialogDeleteAll() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("Dialog", "Alert");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CartActivity.this);
                dialogBuilder.setMessage("Apakah anda yakin ingin membuang semua item dari keranjang belanja anda?");
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (list != null) {
                            String tempProductId = "";
                            for (int i = 0; i < list.size(); i++) {
                                ProductModel productModel = list.get(i);
                                deleteFromCart(productModel.getPruductId(), -1);
                            }
                            getCartList();
                            Log.d("TestingPurpose", "value" + " " + tempProductId + "");
                        }
                        dialog.cancel();
                    }
                });
                dialogBuilder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = dialogBuilder.create();
                alert.show();

            }
        });
    }


    /// delete Item....................
    private void deleteFromCart(final String productId, final int position) {
        showProgress("Please wait...", false);
        final String cartId = MyApplication.getInstance().getPrefManager().getCartID();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("ADDTOCART_URL", Constant.GET_CART_COUNT + cartId);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.DELETE_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTDEleteLISTRESPONSE", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        if (!jsonObject.getString("status").equals("failed")) {
                            totalAmountText = 0;
                            layout_cart.removeAllViews();
                            if (position == -1) {
                                list.clear();
                            } else {
                                list.remove(position);
                            }
                            showCartListitem();
                            closeProgress();
                        } else {
                            closeProgress();
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
                closeProgress();
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();
                if (toktn == null || toktn.equals("null") || toktn.equals("")) {
                    toktn = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }
                Log.d("PRODUCT_ID", toktn + "   " + productId + "");
                param.put("id_cart", cartId + "");
                param.put("access_token", toktn + "");
                param.put("id_product", productId + "");

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    ///////  Item Added To Cart....
    public void itemAddToCart(final String pruductId, final String action, final String tempBestPrice, final TextView quantity, final TextView total_price) {
        showProgress("Please wait...", false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("Highlight", Constant.ADD_TO_CART + "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTRESPONSE", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        ////   down action.......
                        if (action.equals("up")) {
                            int temp = Integer.parseInt(quantity.getText().toString());

                            temp = temp + 1;
                            long tempPriceperitem = Long.parseLong(tempBestPrice);
                            long totalPrice = tempPriceperitem * temp;
                            totalAmountText = totalAmountText + tempPriceperitem;

                            total_price.setText("Total Rp " + getFormat(totalPrice + ""));
                            quantity.setText(temp + "");
                            total_amount.setText(getFormat(totalAmountText + ""));
                        } else {
                            int temp = Integer.parseInt(quantity.getText().toString());

                            long tempPricePerItem = Long.parseLong(tempBestPrice);
                            long totalPrice = tempPricePerItem * temp;

                            temp = temp - 1;
                            totalAmountText = totalAmountText - tempPricePerItem;
                            totalPrice = tempPricePerItem * temp;
                            quantity.setText(temp + "");

                            total_price.setText("Total Rp " + getFormat(totalPrice + ""));
                            quantity.setText(temp + "");
                            total_amount.setText(getFormat(totalAmountText + ""));
                        }

                        //   if (jsonObject.has("message"));
                        if (jsonObject.has("id_cart")) {
                            MyApplication.getInstance().getPrefManager().setCartId(jsonObject.getString("id_cart"));
                        }
                        closeProgress();

                        //'artment
                    } else {
                        getErrorDialog(jsonObject.getString("message"));
                        closeProgress();
                    }
                    // progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgress();
                    getErrorDialog("Server error");
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

                String idCart = MyApplication.getInstance().getPrefManager().getCartID();
                if (toktn == null || toktn.equals("null") || toktn.equals("")) {
                    toktn = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }

                Log.d("PRODUCT_ID", pruductId + " " + toktn + "   idcart_>" + idCart + "");
                param.put("id_cart", idCart + "");
                param.put("access_token", toktn + "");
                param.put("id_product", pruductId + "");
                param.put("quantity", 1 + "");
                param.put("operation", action);
                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            getCartList();
        }
    }
}

