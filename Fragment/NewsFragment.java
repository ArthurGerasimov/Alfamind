package id.meteor.alfamind.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import id.meteor.alfamind.Activity.MainActivity;
import id.meteor.alfamind.Adapter.HighlightListAdapter;
import id.meteor.alfamind.Base.BaseFragment;
import id.meteor.alfamind.Interface.FragmentListener;
import id.meteor.alfamind.Model.HighlightModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class NewsFragment extends BaseFragment implements FragmentListener{
    View view;
    MainActivity mainActivity;
    ListView listNews;

    HighlightListAdapter listAdapter;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipe_refresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = (MainActivity) getActivity();
        mainActivity = (MainActivity) getActivity();

    }

    public FragmentListener getFragmentListener(){
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        listNews = view.findViewById(R.id.list_news);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        //   listAdapter = new HighlightListAdapter(getActivity(), R.layout.list_highlight, highlightList);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        //showItemList();

        if (MyApplication.isNetworkAvailable(getActivity())) {
            showItemList();
        } else {
            ((MainActivity) getActivity()).showSnackBar();
        }

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showItemList();
            }
        });

        return view;
    }

    private void showItemList() {//HIGHLIGHT_URL
        progressDialog.show();
        listNews.setAdapter(null);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.HIGHLIGHT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Highlight", response + "");
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<HighlightModel> highlightList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        HighlightModel highlightModel = new HighlightModel();
                        if (object.has("id")) {
                            highlightModel.setId(object.getString("id"));
                        }
                        if (object.has("title")) {
                            highlightModel.setTitle(object.getString("title"));
                        }
                        if (object.has("image")) {
                            highlightModel.setImage(object.getString("image"));
                        }
                        if (object.has("related_product_id")) {
                            highlightModel.setProductId(object.getString("related_product_id"));
                        }
                        if (object.has("image_details")) {
                            highlightModel.setSetImageDetails(object.getString("related_product_id"));
                        }
                        highlightList.add(highlightModel);
                    }
                    progressDialog.dismiss();

                    if(highlightList.size() >0){
                        if(getActivity() == null)
                            return;
                        Log.e("CHECK_PRINT_I",R.layout.list_highlight+"");
                        Log.e("CHECK_PRINT_I",getActivity()+"");
                        Log.e("CHECK_PRINT_I",highlightList+"");
                        listAdapter = new HighlightListAdapter(getActivity(), R.layout.list_highlight, highlightList);
                        listNews.setAdapter(listAdapter);
                        listAdapter.notifyDataSetChanged();
                        swipe_refresh.setRefreshing(false);
                    }
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
                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((MainActivity) getActivity()).showSnackBar();
                }
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        });
        requestQueue.add(stringRequest);
    }

    public void getErrorDialog(String str) {
        final String[] list = {str};
        final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(getContext(), list, null);
        dialog.title("");
        dialog.cancelText("Ok");
        dialog.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progressDialog.dismiss();
    }

    @Override
    public void onBackPress() {
        ((MainActivity)getActivity()).getShoppingFragment();
    }
}