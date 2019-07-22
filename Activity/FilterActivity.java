package id.meteor.alfamind.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaygoo.widget.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import id.meteor.alfamind.Model.ProductModel;
import id.meteor.alfamind.Model.RewardModel;
import id.meteor.alfamind.Model.SubCategoryModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;
import id.meteor.alfamind.extra.ProductHolder;
import id.meteor.alfamind.extra.RewardHolder;
import id.meteor.alfamind.extra.SubCatHolder;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class FilterActivity extends AppCompatActivity {

    TextView range, next, price_range, category, title;
    RangeSeekBar rangeBar;
    EditText brand, subKategori;
    String min_price, max_price, TITLE, alertDialogTitle, BRAND_IDS_NUMBERS = "", SUBCATEGORY_IDS_NUMBERS = "";
    int cid;
    private HashSet<String> Brand_List;
    private HashSet<String> SubCategory_List;
    private HashMap<String, String> hashMapBrandID;
    private HashMap<String, String> hashMapSubCategoriesID;
    private LinearLayout linearLayout;
    private ArrayList<String> List;
    private ArrayList<CheckBox> check_box_list;
    private String Selected_list;
    private String Selected_Brand_list;
    private String Selected_SubCategory_list;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        title = findViewById(R.id.title);
        range = findViewById(R.id.p_range);
        rangeBar = findViewById(R.id.rangeBar);
        brand = findViewById(R.id.etBrand);
        subKategori = findViewById(R.id.etSubKategori);
        ImageView back_btn = findViewById(R.id.back_btn);
        next = findViewById(R.id.next);
        price_range = findViewById(R.id.price_range);
        category = findViewById(R.id.category);
        RelativeLayout lay1 = findViewById(R.id.lay1);
        Brand_List = new HashSet<>();
        SubCategory_List = new HashSet<>();
        hashMapBrandID = new HashMap<>();
        hashMapSubCategoriesID = new HashMap<>();
        List = new ArrayList();

        if (MyApplication.isNetworkAvailable(this)) {
            getBrandList();
        } else {
            showSnackBar();
        }

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final AlertDialog alertDialog = dialogBuilder.create();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_layout,null);
        alertDialog.setView(dialogView);
        linearLayout = dialogView.findViewById(R.id.linear_layout);
        final TextView title = dialogView.findViewById(R.id.title);
        final TextView Cancel = dialogView.findViewById(R.id.Btn1);
        final TextView Ok = dialogView.findViewById(R.id.Btn2);

        TITLE = getIntent().getStringExtra("DATA");
        title.setText(TITLE);

        if (TITLE.equalsIgnoreCase("Filter Belanjaan")) {
            min_price = getIntent().getStringExtra("MIN");
            max_price = getIntent().getStringExtra("MAX");
            cid = Integer.parseInt(getIntent().getStringExtra("cid"));
            Log.e("MIN", min_price + "");
            Log.e("MAX", max_price + "");
            setData1();
        }

        if (TITLE.equalsIgnoreCase("Filter Reward")) {
            lay1.setVisibility(View.GONE);
            category.setText("Kategori");
            setData2();
        }

        rangeBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                price_range.setText("Rp " + getFormat((int) min + "") + " - Rp " + getFormat((int) max + ""));
                AllProductActivity.minimum_rp = (int) min + "";
                AllProductActivity.maximum_rp = (int) max + "";

                Log.e("ASDSSDSdSD", getFormat((int) min + "") + "<><><><>");
                Log.e("ASDSSDSdSD", AllProductActivity.minimum_rp + "");
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });


        subKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SubCategory_List != null) {
                    title.setText("Sub kategori");
                    alertDialogTitle = "Sub kategori";
                    Iterator iterator = SubCategory_List.iterator();
                    List.clear();
                    while (iterator.hasNext()) {
                        List.add(iterator.next() + "");
                    }
                    linearLayout.removeAllViews();
                    showAlertDialog(List, alertDialog);
                }
            }
        });

        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Brand");
                alertDialogTitle = "Brand";
                Iterator iterator = Brand_List.iterator();
                List.clear();
                while (iterator.hasNext()) {
                    List.add(iterator.next() + "");
                }
                linearLayout.removeAllViews();
                showAlertDialog(List, alertDialog);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("sadsadsad--BrandID", "" + BRAND_IDS_NUMBERS + "");
                progressDialog.show();
                if (TITLE.equalsIgnoreCase("Filter Belanjaan"))
                    if (BRAND_IDS_NUMBERS.length() > 0 && BRAND_IDS_NUMBERS != null && !BRAND_IDS_NUMBERS.equalsIgnoreCase("null")) {
                        if (SUBCATEGORY_IDS_NUMBERS.length() > 0 && SUBCATEGORY_IDS_NUMBERS != null && !SUBCATEGORY_IDS_NUMBERS.equalsIgnoreCase("null")) {
                            filterData();
                        } else {
                            SUBCATEGORY_IDS_NUMBERS = "";
                            Log.e("sadsadsad", "" + SUBCATEGORY_IDS_NUMBERS);
                            Iterator iterator = SubCategory_List.iterator();
                            List.clear();
                            while (iterator.hasNext()) {
                                SUBCATEGORY_IDS_NUMBERS = SUBCATEGORY_IDS_NUMBERS + hashMapSubCategoriesID.get(iterator.next()) + ",";
                            }
                            if (SUBCATEGORY_IDS_NUMBERS != null && SUBCATEGORY_IDS_NUMBERS.length() > 1) {
                                SUBCATEGORY_IDS_NUMBERS = SUBCATEGORY_IDS_NUMBERS.substring(0, SUBCATEGORY_IDS_NUMBERS.length() - 1);
                            }
                            filterData();
                        }
                    } else {
                        filterData2();
                    }
                if (TITLE.equalsIgnoreCase("Filter Reward")) {
                    if (Selected_list != null && Selected_list.length() > 0 && !Selected_list.equalsIgnoreCase("null")) {
                        Intent i = new Intent();
                        i.putExtra("Categori", Selected_list);
                        setResult(Activity.RESULT_OK, i);
                        progressDialog.dismiss();
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    } else {
                        TukarPointActivity.suvCategory_list = null;
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    }
                }
            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selected_list = "";
                for (int i = 0; i < check_box_list.size(); i++) {
                    CheckBox checkBox = check_box_list.get(i);
                    if (checkBox.isChecked()) {
                        if (i == check_box_list.size() - 1) {
                            Selected_list += checkBox.getText().toString() + ">";
                        } else {
                            Selected_list += checkBox.getText().toString() + ">";
                        }
                    }
                }
                Log.e("BRANDLNL", Selected_list);
                parseCategoriList(Selected_list);
                alertDialog.dismiss();


                if (alertDialogTitle.equalsIgnoreCase("Brand")) {
                    AllProductActivity.brand_list = Selected_list;
                    Selected_Brand_list = Selected_list;
                    getBrandIDs(Selected_Brand_list);
                } else {
                    AllProductActivity.suvCategory_list = Selected_list;
                    Selected_SubCategory_list = Selected_list;
                    getSubCategoryIDs(Selected_SubCategory_list);
                }
            }
        });
    }

    public void parseCategoriList(String str) {
        Log.e("VALUES12", str);
        if (str != null && !str.equalsIgnoreCase("null") && !str.equalsIgnoreCase("")) {
            String arr[] = str.split(">");
            if (arr.length > 0)
                if (alertDialogTitle.equals("Brand")) {
                    Log.e("VALUES12", arr.length + "");
                    brand.setHint("Semua Brand (" + arr.length + ")");
                } else {
                    Log.e("VALUES12", arr.length + "");
                    subKategori.setHint("Semua Kategori (" + arr.length + ")");
                }
        }
    }

    public void getBrandIDs(String str) {
        BRAND_IDS_NUMBERS = "";
        String arr[] = str.split(">");
        for (int i = 0; i < arr.length; i++) {
            Log.e("BRAND132", arr[i]);
            Log.e("BARNDIDS", hashMapBrandID.get(arr[i]) + "");
            BRAND_IDS_NUMBERS = BRAND_IDS_NUMBERS + hashMapBrandID.get(arr[i]) + ",";
        }
        if (BRAND_IDS_NUMBERS != null && BRAND_IDS_NUMBERS.length() > 1) {
            BRAND_IDS_NUMBERS = BRAND_IDS_NUMBERS.substring(0, BRAND_IDS_NUMBERS.length() - 1);
        }
    }

    public void getSubCategoryIDs(String str) {
        SUBCATEGORY_IDS_NUMBERS = "";
        String arr[] = str.split(">");
        for (int i = 0; i < arr.length; i++) {
            Log.e("BRAND132", arr[i]);
            Log.e("BARNDIDS", hashMapSubCategoriesID.get(arr[i]) + "");
            SUBCATEGORY_IDS_NUMBERS = SUBCATEGORY_IDS_NUMBERS + hashMapSubCategoriesID.get(arr[i]) + ",";
        }
        if (SUBCATEGORY_IDS_NUMBERS != null && SUBCATEGORY_IDS_NUMBERS.length() > 1) {
            SUBCATEGORY_IDS_NUMBERS = SUBCATEGORY_IDS_NUMBERS.substring(0, SUBCATEGORY_IDS_NUMBERS.length() - 1);
        }
    }


    public void setData1() {

        rangeBar.setRange(Float.parseFloat(min_price), Float.parseFloat(max_price));

        if (AllProductActivity.minimum_rp != null && !AllProductActivity.minimum_rp.equalsIgnoreCase("null") && !AllProductActivity.minimum_rp.equalsIgnoreCase("")) {
            Log.e("ASDSDDSSDDSd", "" + AllProductActivity.minimum_rp);
            price_range.setText("Rp " + getFormat(AllProductActivity.minimum_rp) + " - Rp " + getFormat(AllProductActivity.maximum_rp));
            rangeBar.setValue(Float.parseFloat(AllProductActivity.minimum_rp), Float.parseFloat(AllProductActivity.maximum_rp));
        } else {
            price_range.setText("Rp " + getFormat(min_price) + " - Rp " + getFormat(max_price));
        }


        ArrayList<ProductModel> productModels = ProductHolder.getInstance().getProductList(cid);
        ArrayList<SubCategoryModel> subCategoryModels = SubCatHolder.getInstance().getSubCatList(cid + "");

        if (productModels != null) {
            for (int i = 0; i < productModels.size(); i++) {
                Brand_List.add(productModels.get(i).getBrandName());
            }
        }

        if (subCategoryModels != null) {
            for (int i = 0; i < subCategoryModels.size(); i++) {
                SubCategory_List.add(subCategoryModels.get(i).getName());
                hashMapSubCategoriesID.put(subCategoryModels.get(i).getName(), subCategoryModels.get(i).getId() + "");
            }
        }

        if (AllProductActivity.brand_list != null && !AllProductActivity.brand_list.equalsIgnoreCase("null") && !AllProductActivity.brand_list.equalsIgnoreCase("")) {
            //  Log.e("SADSDSDSD",AllProductActivity.brand_list.length()+"");
            Selected_Brand_list = AllProductActivity.brand_list;
            alertDialogTitle = "Brand";
            parseCategoriList(Selected_Brand_list);
        } else {
//            Log.e("SADSDSDSD--",AllProductActivity.brand_list.length()+"");
            brand.setHint("Semua Brand (" + Brand_List.size() + ")");
        }

        if (AllProductActivity.suvCategory_list != null && !AllProductActivity.suvCategory_list.equalsIgnoreCase("null") && !AllProductActivity.suvCategory_list.equalsIgnoreCase("")) {
            Selected_SubCategory_list = AllProductActivity.suvCategory_list;
            alertDialogTitle = "Sub kategori";
            parseCategoriList(Selected_SubCategory_list);
        } else {
            if (SubCategory_List != null)
                subKategori.setHint("Semua Kategori (" + SubCategory_List.size() + ")");
            else
                subKategori.setHint("Semua Kategori (0)");
        }
    }

    public void setData2() {
        ArrayList<RewardModel> rewardModels = RewardHolder.getInstance().getProductList(Integer.parseInt(Constant.FILTER_ID));
        for (int i = 0; i < rewardModels.size(); i++) {
            SubCategory_List.add(rewardModels.get(i).getCattegoryName());
        }
        if (TukarPointActivity.suvCategory_list != null && !TukarPointActivity.suvCategory_list.equalsIgnoreCase("null") && !TukarPointActivity.suvCategory_list.equalsIgnoreCase("")) {
            Selected_SubCategory_list = TukarPointActivity.suvCategory_list;
            alertDialogTitle = "Sub kategori";
            parseCategoriList(Selected_SubCategory_list);
        } else {
            if (SubCategory_List != null)
                subKategori.setHint("Semua Kategori (" + SubCategory_List.size() + ")");
            else
                subKategori.setHint("Semua Kategori (0)");

        }
    }

    public void showAlertDialog(ArrayList<String> list, AlertDialog alertDialog) {
        Log.e("LIST_SIZE", list.size() + "");
        check_box_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.check_box_layout, null);
            Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Reg.otf");
            CheckBox check_box = view.findViewById(R.id.check_box);
            check_box.setTypeface(font);
            linearLayout.addView(view);
            CheckBox checkBox = view.findViewById(R.id.check_box);
            checkBox.setText(list.get(i));
            if (alertDialogTitle.equalsIgnoreCase("Brand")) {
                if (Selected_Brand_list != null) {
                    Log.e("Selected_Brand_list", "" + Selected_Brand_list);
                    String[] divide = Selected_Brand_list.split(">");
                    for (int j = 0; j < divide.length; j++) {
                        if (checkBox.getText().toString().equals(divide[j]))
                            checkBox.setChecked(true);
                    }
                }
                check_box_list.add(checkBox);
            } else {
                if (Selected_SubCategory_list != null) {
                    String[] divide = Selected_SubCategory_list.split(">");
                    for (int j = 0; j < divide.length; j++) {
                        if (checkBox.getText().toString().equals(divide[j]))
                            checkBox.setChecked(true);
                    }
                }
                check_box_list.add(checkBox);
            }
        }
        alertDialog.show();
    }

    public void filter() {
        Intent i = new Intent();
        i.putExtra("cat_id", Integer.parseInt(Constant.FILTER_ID));
        setResult(Activity.RESULT_OK, i);
        progressDialog.dismiss();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    private void filterData() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FILTER_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("FILTERDATAD", response + "");
                try {

                    ArrayList<ProductModel> productList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("total")) {
                            Log.e("TOTAL_PRODUCT", jsonObject.getString("total"));
                            if (Integer.parseInt(jsonObject.getString("total")) < 1) {
                                getErrorDialog("Tidak ada data ditemukan");
                                progressDialog.dismiss();
                            } else {


                                if (jsonObject.has("data")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ProductModel productModel = new ProductModel();
                                        ArrayList<String> imageList = new ArrayList<>();
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        productModel.setCattegoryName(AllProductActivity.Catergory_name + "");
                                        productModel.setCatId(Constant.FILTER_ID);

                                        if (object.has("plu")) {
                                            productModel.setPlu(object.getString("plu"));
                                        }
                                        if (object.has("id_product")) {
                                            productModel.setPruductId(object.getString("id_product"));
                                        }
                                        if (object.has("id_product_alfacart")) {
                                            productModel.setId_product_alfacart(object.getString("id_product_alfacart"));
                                        }
                                        if (object.has("name")) {
                                            productModel.setName(object.getString("name"));
                                        }
                                        if (object.has("id_subcategory")) {
                                            productModel.setSubCatId(object.getString("id_subcategory"));
                                        }
                                        if (object.has("type")) {
                                            productModel.setType(object.getString("type"));
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
                                        if (object.has("stock")) {
                                            productModel.setStock(object.getString("stock"));
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
                                        if (object.has("weight")) {

                                            productModel.setWeight(object.getString("weight"));
                                        }
                                        if (object.has("margin_header_human")) {

                                            productModel.setMargin_header_human(object.getString("margin_header_human"));
                                        }
                                        if (object.has("margin_human")) {

                                            productModel.setMargin_human(object.getString("margin_human"));
                                        }
                                        if (object.has("margin_tambahan_header_human")) {

                                            productModel.setMargin_tambahan_header_human(object.getString("margin_tambahan_header_human"));
                                        }
                                        if (object.has("margin_tambahan_human")) {

                                            productModel.setMargin_tambahan_human(object.getString("margin_tambahan_human"));
                                        }
                                        if (object.has("points")) {

                                            productModel.setPoints(object.getString("points"));
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

                                        if (object.has("product_images")) {
                                            if (!TextUtils.isEmpty(object.get("product_images").toString())) {
                                                JSONArray imageArr = object.getJSONArray("product_images");

                                                for (int j = 0; j < imageArr.length(); j++) {
                                                    imageList.add(productModel.getImagePath() + imageArr.getString(j));
                                                }
                                            }
                                            productModel.setImageList(imageList);
                                        }

                                        productList.add(productModel);

                                    }
                                }
                                Log.e("SOIJOM", productList.size() + "");
                                ProductHolder.getInstance().putProductList(Integer.parseInt(Constant.FILTER_ID), productList);
                                filter();
                            }

                        }
                    }
                    progressDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                float[] SEND = rangeBar.getCurrentRange();
                min_price = SEND[0] + "";
                max_price = SEND[1] + "";

                Log.e("MIN-SEND", min_price);
                Log.e("MIN-SEND", max_price);
                Log.e("MIN-SEND", BRAND_IDS_NUMBERS);
                //Log.e("id_brand", BRAND_IDS_NUMBERS);
                Log.e("MIN-SEND", SUBCATEGORY_IDS_NUMBERS + "");

                param.put("access_token", MyApplication.getInstance().getPrefManager().getAccessToken() + "");
                param.put("id_brand", BRAND_IDS_NUMBERS + "");
                param.put("min_price", min_price + "");
                param.put("max_price", max_price + "");
                // param.put("id_category", max_price + "");
                param.put("id_subcategory", SUBCATEGORY_IDS_NUMBERS + "");
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void filterData2() {

        float[] SEND = rangeBar.getCurrentRange();
        int min_price = (int) SEND[0];
        int max_price = (int) SEND[1];
        ArrayList<ProductModel> productModels = ProductHolder.getInstance().getProductList(cid);
        ArrayList<ProductModel> productList = new ArrayList<>();
        if (SUBCATEGORY_IDS_NUMBERS.length() > 0 && SUBCATEGORY_IDS_NUMBERS != null && !SUBCATEGORY_IDS_NUMBERS.equalsIgnoreCase("null")) {
            String arr[] = SUBCATEGORY_IDS_NUMBERS.split(",");
            Log.e("sadsadsad--SUBCAT", "" + SUBCATEGORY_IDS_NUMBERS + "----------------->" + arr.length);
            for (int i = 0; i < arr.length; i++) {
                Log.e("sadsadsad--SUBCAT12", "" + SUBCATEGORY_IDS_NUMBERS + "--pro-------->" + productModels.size());
                for (int j = 0; j < productModels.size(); j++) {
                    Log.e("sadsadsad--SUBCAT13", "---arr[i]--->" + arr[i] + "=======>" + productModels.get(j).getSubCatId());
                    if (arr[i].equalsIgnoreCase(productModels.get(j).getSubCatId())) {
                        Log.e("jashgdj", arr[i] + "             " + productModels.get(j).getSubCatId() + "");
                        if (Integer.parseInt(productModels.get(j).getBestPrice()) > min_price && Integer.parseInt(productModels.get(j).getBestPrice()) < max_price) {
                            productList.add(productModels.get(j));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < productModels.size(); i++) {
                if (Integer.parseInt(productModels.get(i).getBestPrice()) > min_price && Integer.parseInt(productModels.get(i).getBestPrice()) < max_price) {
                    productList.add(productModels.get(i));
                }

            }
        }
        ProductHolder.getInstance().putProductList(Integer.parseInt(Constant.FILTER_ID), productList);
        filter();
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

            } else {
                NUM = NUM + "" + arr[j];
            }
        }

        stringBuffer = new StringBuffer(NUM);
        stringBuffer.reverse();
        Log.e("RESULT-DONE", String.valueOf(stringBuffer));


        Log.e("RESULT*", NUM);
        return String.valueOf(stringBuffer);


    }


    /*public String getFormat(String str) {
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
                NUM = NUM + "," + arr[j];
            } else {
                NUM = NUM + "" + arr[j];
            }
        }

        stringBuffer = new StringBuffer(NUM);
        stringBuffer.reverse();
        return String.valueOf(stringBuffer);
    }*/

    public void getErrorDialog(final String str) {
        final String[] list = {str};
        final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(FilterActivity.this, list, null);
        dialog.title("");
        dialog.cancelText("Ok");
        dialog.show();
    }

    public void getBrandList() {
        Log.d("CaptchaResponse", " call ");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_BRAND_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CaptchaResponse", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        if (jsonObject.has("data")) {
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                if (object.has("label")) {
                                    hashMapBrandID.put(object.getString("label"), object.getString("id"));
                                    Log.e("DATATATA", object.getString("label"));
                                }
                            }
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
                //getErrorDialog("Something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                String token = MyApplication.getInstance().getPrefManager().getAccessToken();
                param.put("access_token", token + "");
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void showSnackBar() {
        RelativeLayout main_parent_layout = findViewById(R.id.main_layout);
        Snackbar snackbar = Snackbar.make(main_parent_layout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
