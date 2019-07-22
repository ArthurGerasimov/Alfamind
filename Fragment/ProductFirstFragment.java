package id.meteor.alfamind.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import id.meteor.alfamind.Activity.AllProductActivity;
import id.meteor.alfamind.Activity.MainActivity;
import id.meteor.alfamind.Activity.Pembelian_TopUpActivity;
import id.meteor.alfamind.Activity.ProductDetailActivity;
import id.meteor.alfamind.Activity.SaldoWebViewActivity;
import id.meteor.alfamind.Interface.AddToCartListiner;
import id.meteor.alfamind.Model.CategoryModel;
import id.meteor.alfamind.Model.ProductModel;
import id.meteor.alfamind.Model.SubCategoryModel;
import id.meteor.alfamind.Model.SubProductModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;
import id.meteor.alfamind.extra.CategoryHolder;
import id.meteor.alfamind.extra.ProductHolder;
import id.meteor.alfamind.extra.SubCatHolder;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFirstFragment extends Fragment {

    private final String TAG = "PRODUCT_FRAGMENT";

    private HashMap<Integer, ArrayList<TextView>> mapHoldImage = new HashMap<>();
    private HashMap<Integer, Integer> mapProductCount = new HashMap<>();
    private ArrayList<TextView> tempImageList = new ArrayList<>();
    private HashMap<Integer, TextView> mapItemCount = new HashMap<>();

    private AddToCartListiner cartListiner;
    private LinearLayout main_layout;
    private HashMap<Integer, HashMap<Integer, ArrayList<ProductModel>>> catHashMap = new HashMap<>();
    private HashMap<Integer, ArrayList<SubCategoryModel>> catHashMap2 = new HashMap<>();

    private SwipeRefreshLayout swipe_refresh;
    public boolean topView;
    private int department_id;
    private int sub_department_id;

    public ProductFirstFragment() {
    }

    public void setListiner(AddToCartListiner listiner) {
        this.cartListiner = listiner;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        /*LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));*/
    }

    /*private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.e("DEP_ID_LABLE", val2 + "");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<TextView> temp = mapHoldImage.get(val);
                    if (temp != null) {
                        if (MyApplication.getInstance().getPrefManager().getIntipMargin()) {

                            for (TextView imageView : temp) {
                                imageView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            for (TextView imageView : temp) {
                                imageView.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
        }
    };


    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "createView");
        View view = inflater.inflate(R.layout.fragment_product_first, container, false);
        main_layout = view.findViewById(R.id.main_layout);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);

        department_id = getArguments().getInt("department_id");
        sub_department_id = getArguments().getInt("sub_department_id");

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCategeoryList(department_id, sub_department_id);
            }
        });

        if (MyApplication.isNetworkAvailable(getActivity())) {
            showCategeoryList(department_id, sub_department_id);
        } else {
            ((MainActivity) getActivity()).showSnackBar();
        }

        return view;
    }

    private void showCategeoryList(final int department_id, final int sub_department_id) {

        if (getActivity() == null)
            return;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = Constant.GET_DEP_ID + "/" + department_id + "/" + sub_department_id;
        Log.d(TAG,"showCategeoryList   -->  "+url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response --> " + response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        main_layout.removeAllViews();

                        if (topView) {

                            Log.e(TAG,"TOP_VIEW");
                            LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View default_view = inflater1.inflate(R.layout.list_default_view, null);

                            default_view.findViewById(R.id.topup).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(), Pembelian_TopUpActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                }
                            });

                            default_view.findViewById(R.id.tagihan).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showAlertDialog("Akan segera hadir");
                                }
                            });

                            default_view.findViewById(R.id.topupcashless).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getActivity(), SaldoWebViewActivity.class);
                                    if (MyApplication.getInstance().getPrefManager().getIsLogged()) {
                                        i.putExtra("DATA", "TOP-UP SALADO VIA DOKU");
                                        startActivity(i);
                                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    } else {
                                        i.putExtra("DATA", "TOP-UP CASHLESS");
                                        startActivity(i);
                                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    }
                                }
                            });

                            main_layout.addView(default_view);
                        }

                        //ArrayList<CategoryModel> catList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            //CategoryModel categoryModel = new CategoryModel();
                            JSONObject object = data.getJSONObject(i);

                            // Retrive Data from Json
                            String catId = object.getString("id_category");
                            String catName = object.getString("label");
                            String subDepartment = object.getString("id_subdepartment");

                            // Add to Category Model
                            //categoryModel.setCatName(catName);
                            //categoryModel.setCatId(catId);
                            //categoryModel.setDepId(subDepartment);

                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View tempView = inflater.inflate(R.layout.list_categeory_detail, null);
                            TextView catNameTextview = tempView.findViewById(R.id.cat_name);
                            catNameTextview.setText(catName);
                            main_layout.addView(tempView); // view add to main layout

                            //catList.add(categoryModel);
                            showSubCatageory(department_id, sub_department_id, Integer.parseInt(catId), tempView, catName);

                        }
                        //CategoryHolder.getInstance().putCategoryList((sub_department_id + ""), catList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Server error "+e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((MainActivity) getActivity()).showSnackBar();
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void showSubCatageory(final int department_id, final int sub_department_id, final int catId, final View tempView, final String caName) {

        if (getActivity() == null)
            return;

        final String url = Constant.GET_DEP_ID + "/" + department_id + "/" + sub_department_id + "/" + catId;
        Log.e(TAG," showSubCatageoryURL "+url);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int total_count = 0;
                Log.d(TAG, "showSubCatageory --> "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {

                        if (jsonObject.has("data")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            ArrayList<SubCategoryModel> subArrayList = new ArrayList<>();
                            int len = data.length();

                            for (int i = 0; i < len; i++) {
                                JSONObject object = data.getJSONObject(i);
                                SubCategoryModel subCategoryModel = new SubCategoryModel();
                                subCategoryModel.setId(Integer.parseInt(object.getString("id_subcategory")));
                                subCategoryModel.setName(object.getString("label"));
                                subCategoryModel.setImage(object.getString("image"));
                                subCategoryModel.setCattegoryName(caName);
                                subCategoryModel.setProduct_count(object.getString("product_count"));

                                total_count = Integer.parseInt(subCategoryModel.getProduct_count()) + total_count;
                                mapProductCount.put(catId, total_count);

                                String tempUrl = url + "/" + subCategoryModel.getId();
                                if (i == 0) {
                                    showSubCatageoryItem(catId, tempUrl, subCategoryModel, true, tempView);
                                }

                                TextView view = mapItemCount.get(catId);

                                if (view != null) {
                                    String txt = "Lihat Semua ";
                                    view.setText(txt + "(" + total_count + ")\n" + caName);
                                }
                                subCategoryModel.setUrl(tempUrl);
                                subArrayList.add(subCategoryModel);
                            }

                            catHashMap2.put(catId, subArrayList);
                            SubCatHolder.getInstance().putSubCatList(catId + "", subArrayList);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Error : "+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (MyApplication.isNetworkAvailable(getActivity())) {
                    getErrorDialog("Something went wrong");
                } else {
                    ((MainActivity) getActivity()).showSnackBar();
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void showSubCatageoryItem(final int catId, final String url, final SubCategoryModel model,
                                      final boolean flag, final View tempView) {
        Log.d(TAG, "COUNT_COME "+ model.getCattegoryName());
        if (getActivity() == null) {
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SUB_CAT_ITEM " + url + " " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        ArrayList<ProductModel> productList = ProductHolder.getInstance().getProductList(catId);
                        if (productList == null) {
                            productList = new ArrayList<>();
                        }

                        for (int i = 0; i < data.length(); i++) {
                            ProductModel productModel = new ProductModel();
                            ArrayList<String> imageList = new ArrayList<>();
                            JSONObject object = data.getJSONObject(i);

                            /*if (object.getString("id").equalsIgnoreCase("8152")){
                                Log.e("HAHAHAHAHAHHA",caName);
                                Log.e("HAHAHAHAHAHHA",catId+"");
                            }

                            productModel.setCattegoryName(caName);
                            productModel.setCatId(catId + "");
*/
                            if (object.has("category_label")){
                                productModel.setCattegoryName(object.getString("category_label"));
                            }
                            if (object.has("id_category")){
                                productModel.setCatId(object.getString("id_category"));
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

                                if (productModel.getName().equalsIgnoreCase("Marvel Comic Spiderman Shortsleeve T-Shirt Black CL00317117-6082N"))
                                    Log.e("LENGRTRHRGRB", "" + objArr.toString() + "-------->" + productModel.getName());

                                productModel.setSubProduct(objArr.length() > 1);
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
                                productModel.setSubProductList(subProductList);
                            }

                            productList.add(productModel);
                            if (flag && i < 4) {
                                showProduct(productModel, catId, tempView, productModel.getCattegoryName());
                            }
                        }
                        ProductHolder.getInstance().putProductList(catId, productList);

                        Log.d(TAG, "LIST_SIZE" + productList.size());

                        showProduct(null, catId, tempView, model.getCattegoryName());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //showSubCatageoryItem(catId, url, model, flag, tempView);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((MainActivity) getActivity()).showSnackBar();
                }
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    private void showProduct(final ProductModel productModel, final int catId, View tempView, String caName) {
        final LinearLayout main_layout = tempView.findViewById(R.id.root_layout);
        final LinearLayout layout_cat = tempView.findViewById(R.id.lay_sub_cat);
        final RelativeLayout lay_progressBar = tempView.findViewById(R.id.lay_progressBar);
        final TextView viewAll = tempView.findViewById(R.id.viewAll);

        if (getActivity() == null)
            return;

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View subCatView = inflater.inflate(R.layout.list_sub_categeory, null);
        RelativeLayout layout_details = subCatView.findViewById(R.id.lay_detail);
        RelativeLayout layout_item = subCatView.findViewById(R.id.layout_item);
        LinearLayout layout_viewAll = subCatView.findViewById(R.id.layout_viewAll);
        RelativeLayout layout_add_to_cart = subCatView.findViewById(R.id.layout_add_to_cart);

        final TextView it_name = subCatView.findViewById(R.id.it_name);
        TextView it_price = subCatView.findViewById(R.id.it_price);
        TextView it_last_price = subCatView.findViewById(R.id.it_last_price);
        ImageView it_image = subCatView.findViewById(R.id.it_image);
        final TextView it_discount = subCatView.findViewById(R.id.it_discount); //layout_item
        final TextView intip_margin = subCatView.findViewById(R.id.intip_margin); //layout_item
        final TextView intip_margin2 = subCatView.findViewById(R.id.intip_margin2); //layout_item
        final ImageView image_wobbler0 = subCatView.findViewById(R.id.image_wobbler0); //layout_item
        final ImageView image_wobbler1 = subCatView.findViewById(R.id.image_wobbler1); //layout_item
        final ImageView image_wobbler2 = subCatView.findViewById(R.id.image_wobbler2); //layout_item
        final ImageView image_wobbler3 = subCatView.findViewById(R.id.image_wobbler3); //layout_item


        if (MyApplication.getInstance().getPrefManager().getIntipMargin()) {
            intip_margin.setVisibility(View.VISIBLE);
            intip_margin2.setVisibility(View.VISIBLE);
        } else {
            intip_margin.setVisibility(View.GONE);
            intip_margin2.setVisibility(View.GONE);
        }
        // set listener to parent fragment...
        tempImageList.add(intip_margin);
        tempImageList.add(intip_margin2);
        mapHoldImage.put(department_id, tempImageList);

        //mapTempHoldImage.put(val,intip_margin);

        if (productModel == null) {
            layout_viewAll.setVisibility(View.VISIBLE);
            layout_item.setVisibility(View.GONE);
            TextView text_viewAll = subCatView.findViewById(R.id.text_viewAll);
            ArrayList<SubCategoryModel> sList = catHashMap2.get(catId);

            if (sList != null) {
                int COUNT = mapProductCount.get(catId);
                String txt = text_viewAll.getText().toString();
                text_viewAll.setText(txt + "(" + COUNT + ")\n" + caName);
                mapItemCount.put(catId, text_viewAll);
            }
            layout_cat.addView(subCatView);


            layout_viewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (MyApplication.isNetworkAvailable(getActivity())) {
                        Intent intent = new Intent(getActivity(), AllProductActivity.class);
                        // hold sub cat list...
                        SubCatHolder.getInstance().putSubCatList(catId + "", catHashMap2.get(catId));
                        intent.putExtra("cat_id", catId);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    } else {
                        ((MainActivity) getActivity()).showSnackBar();
                    }

                }
            });

        } else {

            if (productModel.getDiscountPercent() != null && !productModel.getDiscountPercent().equals("null") && !productModel.getDiscountPercent().equals("") && !productModel.getDiscountPercent().equals("0") && !productModel.getDiscountPercent().equals("%")) {
                it_discount.setText(productModel.getDiscountPercent());
            } else {
                it_discount.setVisibility(View.GONE);
            }

            if (!productModel.getPruductId().equals("")) {

                ArrayList<String> listtemp = productModel.getWobblerImageList();
                if (listtemp != null && listtemp.size() > 0) {
                    Log.e(TAG, "LIST_TEMP " + listtemp.size());

                    for (int i = 0; i < listtemp.size(); i++) {
                        if (View.VISIBLE == it_discount.getVisibility()) {
                            switch (i) {
                                case 0:
                                    downloadImage(listtemp.get(i), image_wobbler1, 100);
                                    ((RelativeLayout) subCatView.findViewById(R.id.wobber_lay1)).setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    downloadImage(listtemp.get(i), image_wobbler2, 100);
                                    ((RelativeLayout) subCatView.findViewById(R.id.wobber_lay2)).setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    downloadImage(listtemp.get(i), image_wobbler3, 100);
                                    ((RelativeLayout) subCatView.findViewById(R.id.wobber_lay3)).setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                        if (View.GONE == it_discount.getVisibility()) {
                            switch (i) {
                                case 0:
                                    downloadImage(listtemp.get(i), image_wobbler0, 100);
                                    ((RelativeLayout) subCatView.findViewById(R.id.wobber_lay0)).setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    downloadImage(listtemp.get(i), image_wobbler2, 100);
                                    ((RelativeLayout) subCatView.findViewById(R.id.wobber_lay2)).setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    downloadImage(listtemp.get(i), image_wobbler1, 100);
                                    ((RelativeLayout) subCatView.findViewById(R.id.wobber_lay1)).setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    }
                }

                downloadImage(productModel.getImagePath() + productModel.getImage(), it_image, 500);

                String marginHuman = getMarginHuman(productModel.getMargin_human());
                String marginTambahanHeaderHuman = productModel.getMargin_tambahan_header_human();
                String points = productModel.getPoints();
                String marginGroup = productModel.getMargin_group_margin();
                if (!marginHuman.equalsIgnoreCase("0"))
                    intip_margin.setText("  Margin: Rp " + marginHuman + "\n  Margin+: " + marginTambahanHeaderHuman + "\n  Poin: " + points + "\n  Margin Grup: " + marginGroup);
                else
                    intip_margin.setText("");

                it_name.setText(productModel.getName());

                String temp_discount = productModel.getDiscount() + "";

                if (!temp_discount.equalsIgnoreCase("") && !temp_discount.equalsIgnoreCase("null") && !temp_discount.equalsIgnoreCase("0") && temp_discount != null) {
                    it_price.setText("Rp " + getFormat(getFormat(temp_discount)));
                    it_last_price.setText("Rp " + getFormat(productModel.getBestPrice()));
                } else {
                    it_last_price.setVisibility(View.GONE);
                    it_price.setText("Rp " + getFormat(productModel.getBestPrice()));
                }
                lay_progressBar.setVisibility(View.GONE);
                layout_cat.addView(subCatView);

            } else {
                main_layout.setVisibility(View.GONE);
            }
        }

        // add to layout
        layout_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productModel != null) {

                    if (MyApplication.isNetworkAvailable(getActivity())) {
                        Log.d("PRODUCT_ID", productModel.getPruductId() + " ");
                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        ProductHolder.getInstance().putProductDetail(productModel);
                        intent.putExtra("product_id", productModel.getPruductId());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    } else {
                        ((MainActivity) getActivity()).showSnackBar();
                    }
                }
            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MyApplication.isNetworkAvailable(getActivity())) {
                    Intent intent = new Intent(getActivity(), AllProductActivity.class);
                    // hold ub cat list...
                    SubCatHolder.getInstance().putSubCatList(catId + "", catHashMap2.get(catId));
                    intent.putExtra("cat_id", catId);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                } else {
                    ((MainActivity) getActivity()).showSnackBar();
                }

            }
        });


        /// add to cart....

        layout_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartListiner.addToCart(productModel.getPruductId(), productModel.getStock() + "", productModel.getSubProduct());

            }
        });

        swipe_refresh.setRefreshing(false);

    }


    public void downloadImage(String url, final ImageView imageView, int width) {
        Log.d(TAG, "ImageUrl  " + url + "");
        Glide.with(getContext())
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(width, width) {
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

            } else {
                NUM.append("").append(arr[j]);

            }
        }

        stringBuffer = new StringBuffer(NUM.toString());
        stringBuffer.reverse();
        return String.valueOf(stringBuffer);
    }


    public void getErrorDialog(final String str) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String[] list = {str};
                final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(getContext(), list, null);
                dialog.title("");
                dialog.cancelText("Baik");
                dialog.show();
            }
        });
    }

    public String getMarginHuman(String str) {
        str = str.replace(" ", "");
        str = str.replace("IDR", "");
        str = str.replace(",", "");
        str = getFormat(str);
        return str;
    }

    @SuppressLint("StaticFieldLeak")
    private void showAlertDialog(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("Dialog", "Alert");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setMessage(msg);
                dialogBuilder.setCancelable(true);


                dialogBuilder.setPositiveButton("baik", new DialogInterface.OnClickListener() {
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
}
