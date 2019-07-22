package id.meteor.alfamind.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

import id.meteor.alfamind.Adapter.RewardsAlbumAdapter;
import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Interface.AddToCartListiner;
import id.meteor.alfamind.Model.RewardModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.extra.RewardHolder;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class TukarPointActivity extends BaseActivity {

    private boolean go = false;
    private RelativeLayout layout_progressBar;
    private RecyclerView recyclerView;
    private ArrayList<RewardModel> rewardModels;
    private int REQUESTCODE102 = 102;
    private int REQUESTCODE101 = 101;
    ArrayList<RewardModel> temp;

    //save data for filter activity
    public static String suvCategory_list = null;
    public static String sort_data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_point);

        ImageView back_btn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.recylerView);
        layout_progressBar = findViewById(R.id.layout_progressBar);
        LinearLayout sort = findViewById(R.id.sort);
        LinearLayout filter = findViewById(R.id.filter);
        temp = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (go) {
                    Intent intent = new Intent(TukarPointActivity.this, SortActivity.class);
                    intent.putExtra("DATA", "Sortir Reward");
                    startActivityForResult(intent, REQUESTCODE102);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (go) {
                    Intent intent = new Intent(TukarPointActivity.this, FilterActivity.class);
                    intent.putExtra("DATA", "Filter Reward");
                    startActivityForResult(intent, REQUESTCODE101);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getRewardList();
    }

    private void getRewardList() {
        showProgress("Please wait...", false);
        rewardModels = new ArrayList<>();
        final ArrayList<RewardModel> rewardModelArrayList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.REWARDS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("REWARDS", "AllP - " + response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //    progressDialog.dismiss();
                    if (jsonObject.getString("status").equals("success")) {

                        if (jsonObject.has("reward_categories")) {
                            JSONArray data = jsonObject.getJSONArray("reward_categories");
                            for (int i = 0; i < data.length(); i++) {

                                JSONObject object = data.getJSONObject(i);
                                RewardModel rewardModel = new RewardModel();

                                if (object.has("id")) {
                                    rewardModel.setPruductId(object.getString("id"));
                                }
                                if (object.has("category")) {
                                    rewardModel.setCattegoryName(object.getString("category"));
                                }
                                if (object.has("title")) {
                                    rewardModel.setTitle(object.getString("title"));
                                }
                                if (object.has("description")) {
                                    rewardModel.setDescription(object.getString("description"));
                                }
                                if (object.has("required_points")) {
                                    rewardModel.setRequired_points(object.getString("required_points"));
                                }
                                if (object.has("offer_validity_start")) {
                                    rewardModel.setOffer_validity_start(object.getString("offer_validity_start"));
                                }
                                if (object.has("offer_validity_end")) {
                                    rewardModel.setOffer_validity_end(object.getString("offer_validity_end"));
                                }
                                if (object.has("stock")) {
                                    rewardModel.setStock(object.getString("stock"));
                                }
                                if (object.has("status")) {
                                    rewardModel.setStatus(object.getString("status"));
                                }

                                if (object.has("image")) {
                                    if (!TextUtils.isEmpty(object.get("image").toString())) {
                                        JSONArray imageArr = object.getJSONArray("image");
                                        final ArrayList<String> imageList = new ArrayList<>();
                                        for (int j = 0; j < imageArr.length(); j++) {
                                            imageList.add(imageArr.getString(j));
                                        }
                                        rewardModel.setImageList(imageList);
                                    }
                                }
                                rewardModels.add(rewardModel);
                                temp.add(rewardModel);
                                RewardHolder.getInstance().putProductDetail(rewardModel);
                                rewardModelArrayList.add(rewardModel);
                            }
                            RewardHolder.getInstance().putProductList(Integer.parseInt(Constant.FILTER_ID), rewardModelArrayList);
                        }
                        showAllItem(rewardModelArrayList);
                    }
                } catch (JSONException e) {

                    closeProgress();
                    e.printStackTrace();
                    layout_progressBar.setVisibility(View.GONE);
                    getErrorDialog("Something went wrong");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getErrorDialog("Something went wrong");
                closeProgress();
                layout_progressBar.setVisibility(View.GONE);
                error.printStackTrace();
                Log.d("SubCatageoryVolly", " something went wrong" + error.getMessage() + "");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void showAllItem(ArrayList<RewardModel> productModelArrayList) {

        layout_progressBar.setVisibility(View.GONE);
        RewardsAlbumAdapter adapter = new RewardsAlbumAdapter(this, productModelArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        closeProgress();
        go = true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE102 && resultCode == RESULT_OK) {
            Log.e("SORT_DATA", data.getStringExtra("SORT_DATA"));
            sort_data = data.getStringExtra("SORT_DATA") + "";
            Collections.sort(temp, new TukarPointActivity.CommentComparator(Integer.parseInt(data.getStringExtra("SORT_DATA"))));
            Log.e("dsnfhgdfff", "" + temp.get(0));
            Log.e("dsnfhgdfff", "" + RewardHolder.getInstance().getProductList(Integer.parseInt(Constant.FILTER_ID)).get(0));
            showAllItem(temp);
        }
        if (requestCode == REQUESTCODE101 && resultCode == RESULT_OK) {
            parseCategoriList(data.getStringExtra("Categori"));
            suvCategory_list = data.getStringExtra("Categori");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class CommentComparator implements Comparator<RewardModel> {

        private int SORT;

        public CommentComparator(int type) {
            SORT = type;
        }

        public int compare(RewardModel o1, RewardModel o2) {

            Log.d("CommentComparator", o1.getRequired_points() + " -- " + o2.getRequired_points() + "-------->" + SORT);
            String x = o1.getTitle(), y = o2.getTitle();
            x = x.toLowerCase();
            y = y.toLowerCase();
            int point1 = Integer.parseInt(o1.getRequired_points()), point2 = Integer.parseInt(o2.getRequired_points());

            switch (SORT) {
                case 1:
                    if (point1 == point2)
                        return 0;
                    else if (point1 > point2)
                        return 1;
                    else
                        return -1;

                case 2:
                    if (point1 == point2)
                        return 0;
                    else if (point1 < point2)
                        return 1;
                    else
                        return -1;

                case 3:
                    return x.compareTo(y);

                case 4:
                    return y.compareTo(x);

                default:
                    return x.compareTo(y);

            }
        }}

    public void parseCategoriList(String str) {

        ArrayList<String> categori_list = new ArrayList<>();

        String arr[] = str.split(">");

        for (String anArr : arr) {
            Log.e("VALUES", anArr);
            categori_list.add(anArr);
        }

        Log.e("TEMP_SIZE", "" + temp.size());
        temp = new ArrayList<>();
        for (int i = 0; i < rewardModels.size(); i++) {
            for (int j = 0; j < categori_list.size(); j++) {
                if (rewardModels.get(i).getCattegoryName().equalsIgnoreCase(categori_list.get(j))) {
                    temp.add(rewardModels.get(i));
                }
            }
        }
        Log.e("TEMP_SIZE", "" + temp.size());
        showAllItem(temp);
    }

    @Override
    protected void onDestroy() {
        suvCategory_list = null;
        sort_data = null;
        super.onDestroy();
    }
}
