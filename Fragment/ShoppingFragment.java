package id.meteor.alfamind.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

import id.meteor.alfamind.Activity.CartActivity;
import id.meteor.alfamind.Activity.ChekOutErrorActivity;
import id.meteor.alfamind.Activity.MainActivity;
import id.meteor.alfamind.Activity.SearchActivity;
import id.meteor.alfamind.Base.BaseFragment;
import id.meteor.alfamind.Interface.AddToCartListiner;
import id.meteor.alfamind.Interface.FragmentListener;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

public class ShoppingFragment extends BaseFragment implements FragmentListener {
    View view;
    private ProgressDialog progressDialog;
    private TabLayout tabs_layout;
    private ViewPager view_pager;
    private ImageView show_cart;
    private TextView cart_count;
    boolean isLogged = false;
    private ProgressBar process_bar;
    ArrayList<Fragment> fList = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();
    private ImageView image_search;
    private String depList = "";

    public FragmentListener getFragmentListener() {
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_shopping, container, false);
            Log.d("SHOPPING_FRAGMENT", "OnCreateView");
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            tabs_layout = view.findViewById(R.id.tabs_layout);
            process_bar = view.findViewById(R.id.process_bar);
            view_pager = view.findViewById(R.id.view_pager);
            show_cart = view.findViewById(R.id.show_cart);
            image_search = view.findViewById(R.id.image_search);
            cart_count = view.findViewById(R.id.cart_count);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("UPDATE_COUNT"));
            isLogged = MyApplication.getInstance().getPrefManager().getIsLogged();
            loadLayout();
        }
        return view;
    }


    @Override
    public void onResume() {
        Log.d("SHOPPING_FRAGMENT", "Resume");
        getCartCount();
        super.onResume();
    }

    private void loadLayout() {
        Log.d("SHopping", "frg");
        if (MyApplication.isNetworkAvailable(getActivity())) {
            getDepartmentID();
        } else {
            process_bar.setVisibility(View.GONE);
            ((MainActivity) getActivity()).showSnackBar();
        }
        show_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CartActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }


    private void getDepartmentID() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Log.d("Highlight", Constant.GET_DEP_ID + "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GET_DEP_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Highlight", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {

                        process_bar.setVisibility(View.GONE);
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            if (object.has("id_department")) {
                                showTabs(Integer.parseInt(object.getString("id_department")));
                            }
                        }
                    } else {
                        process_bar.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    process_bar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                process_bar.setVisibility(View.GONE);
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
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


    private void showTabs(int i) {

        if (getActivity() == null)
            return;
        String url = Constant.GET_TABS_DETAIL + i;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TABSDETAILS", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        JSONArray data = jsonObject.getJSONArray("data");

                        fList.clear();
                        titleList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            int idSub = Integer.parseInt(object.getString("id_subdepartment"));
                            Bundle bundle = new Bundle();

                            bundle.putInt("department_id", Integer.parseInt(object.getString("id_department")));
                            bundle.putInt("sub_department_id", idSub);

                            if (i == data.length() - 1) {
                                depList += idSub;
                            } else {
                                depList += idSub + ",";
                            }

                            ProductFirstFragment fragment = new ProductFirstFragment();
                            AddToCartListiner listiner = new AddToCartListiner() {
                                @Override
                                public void addToCart(String productId, String Stock, boolean Category) {
                                    if (MyApplication.getInstance().getPrefManager().getIsLogged()) {
                                        if (Category) {
                                            getErrorDialog("Silahkan memilih ukuran dahulu");
                                        } else {
                                            if (!Stock.equalsIgnoreCase("0")) {
                                                itemAddToCart(productId);
                                            } else
                                                getErrorDialog("Stock tidak tersedia");
                                        }
                                    } else {
                                        startActivity(new Intent(getActivity(), ChekOutErrorActivity.class));
                                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    }
                                }
                            };

                            fragment.setListiner(listiner);
                            fragment.topView = i == 0;
                            // fragment.setProcessBar(process_bar);

                            fragment.setArguments(bundle);
                            tabs_layout.setVisibility(View.VISIBLE);
                            fList.add(fragment);
                            titleList.add(object.getString("label"));

                        }

                        tabs_layout.setupWithViewPager(view_pager);
                        Log.d("PAGER_LIST", fList.size() + "  -  " + titleList.size() + "");
                        setupViewPager(view_pager, fList, titleList);
                        view_pager.setOffscreenPageLimit(data.length());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getErrorDialog(final String str) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String[] list = {str};
                final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(getContext(), list, null);
                dialog.title("");
                dialog.cancelText("Ok");
                dialog.show();
            }
        });

    }


    // getCart count .......
    public void getCartCount() {
        final String cartId = MyApplication.getInstance().getPrefManager().getCartID();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Log.d("ADDTOCART_URL", Constant.GET_CART_COUNT + cartId);
        String url = Constant.GET_CART_COUNT + cartId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_CART_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTCountRESPONSE", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {

                        String temp = jsonObject.getString("cart_contents");

                        if (temp == null || temp.equals("null")) {
                            cart_count.setVisibility(View.GONE);
                        } else {

                            if (jsonObject.has("cart_contents")) {

                                JSONArray array = jsonObject.getJSONArray("cart_contents");

                                if (array != null && array.length() > 0) {
                                    cart_count.setText(array.length() + "");
                                    cart_count.setVisibility(View.VISIBLE);
                                } else {
                                    cart_count.setVisibility(View.GONE);
                                }
                            }
                        }

                    } else {
                        cart_count.setVisibility(View.GONE);
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
                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    //  getErrorDialog("Tidak ada koneksi internet");
                    ((MainActivity) getActivity()).showSnackBar();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();
                if (toktn == null || toktn.equals("null") || toktn.equals("")) {
                    toktn = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                }

                Log.d("PRODUCT_ID", toktn + "   cartid->" + cartId);
                param.put("id_cart", cartId + "");
                param.put("access_token", toktn + "");

                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    ///////  Item Added To Cart....
    public void itemAddToCart(final String pruductId) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTRESPONSE", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        if (jsonObject.has("message")) {
                            Toast.makeText(getActivity(), jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();

                        }
                        if (jsonObject.has("id_cart")) {
                            MyApplication.getInstance().getPrefManager().setCartId(jsonObject.getString("id_cart"));
                        }
                        getCartCount();
                        progressDialog.dismiss();
                    } else {
                        getErrorDialog(jsonObject.getString("message"));
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    getErrorDialog("Server error");
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                getErrorDialog("Something went wrong");
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


    private void setupViewPager(ViewPager viewPager, ArrayList<Fragment> fList, ArrayList<String> tabNameList) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragmentList(fList, tabNameList);

        viewPager.setAdapter(adapter);
        Log.e("COUCNJNS", "" + tabs_layout.getTabCount() + "");
        for (int i = 0; i < tabs_layout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
            tabs_layout.getTabAt(i).setCustomView(tv);
        }

    }

    @Override
    public void onBackPress() {
        showAlertDialogExit("Anda yakin ingin keluar?");
    }

    ///////// class view pager...............

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void addFragmentList(List<Fragment> fragment, List<String> title) {
            mFragmentList = fragment;
            mFragmentTitleList = title;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position).toUpperCase();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getCartCount();
        }
    };

    @Override
    public void onDetach() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        super.onDetach();
    }
    
    @SuppressLint("StaticFieldLeak")
    private void showAlertDialogExit(final String msg) {
        Log.d("Dialog", "Alert");
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(msg);
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
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
}
