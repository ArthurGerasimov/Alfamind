package id.meteor.alfamind.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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

import id.meteor.alfamind.Activity.Bantuan_KontakLayananActivity;
import id.meteor.alfamind.Activity.Forgot_PasswordActivity;
import id.meteor.alfamind.Activity.MainActivity;
import id.meteor.alfamind.Activity.Pembelian_TopUpActivity;
import id.meteor.alfamind.Activity.SaldoWebViewActivity;
import id.meteor.alfamind.Activity.SignupActivity;
import id.meteor.alfamind.Base.BaseFragment;
import id.meteor.alfamind.Interface.FragmentListener;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;
import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.MyPreferenceManager;

public class LoginFragment extends BaseFragment implements FragmentListener {

    MainActivity mainActivity;
    View view;
    TextView txvLogin, txvSignup, txvForgotPass;
    EditText edtEmail, edtPass;
    private ProgressDialog progressDialog;
    private CheckBox check_box;
    private ImageView popup_menu;
    private String old_CartID;
    private ImageView visiblility;

    public FragmentListener getFragmentListener() {
        return  this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        popup_menu = view.findViewById(R.id.popup_menu);
        visiblility = view.findViewById(R.id.visiblility);

        old_CartID = MyApplication.getInstance().getPrefManager().getCartID() + "";
        loadLayout();


        if (!MyApplication.isNetworkAvailable(getActivity())) {
            ((MainActivity) getActivity()).showSnackBar();
        }

        popup_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), popup_menu);
                popup.getMenuInflater().inflate(R.menu.loin_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getTitle().toString()) {
                            case "Kontak Layanan":
                                Intent intent = new Intent(getActivity(), Bantuan_KontakLayananActivity.class);
                                intent.putExtra("DATA", "LOGIN");
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                break;
                            case "F.A.Q":
                                Intent intent1 = new Intent(getActivity(), SaldoWebViewActivity.class);
                                intent1.putExtra("DATA", "FAQ");
                                startActivity(intent1);
                                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        return view;
    }

    private void loadLayout() {

        mainActivity = (MainActivity) getActivity();
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        check_box = view.findViewById(R.id.check_box);
        txvLogin = view.findViewById(R.id.txv_login);
        txvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.hideKeyboard();
                if (isValidEmail()) {
                    if (check_box.isChecked()) {
                        MyApplication.getInstance().getPrefManager().setRememberEmail(edtEmail.getEditableText().toString());
                        MyApplication.getInstance().getPrefManager().setRememberPass(edtPass.getEditableText().toString());
                    } else {
                        MyApplication.getInstance().getPrefManager().setRememberEmail(null);
                        MyApplication.getInstance().getPrefManager().setRememberPass(null);
                    }
                    if (MyApplication.isNetworkAvailable(getActivity())) {

                        loginToUser();
                    } else {
                        ((MainActivity) getActivity()).showSnackBar();
                    }
                }
            }
        });

        txvSignup = view.findViewById(R.id.txv_signup);
        txvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignupActivity.class);
                intent.putExtra("DATA", "NEW_REGISTRATION");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        txvForgotPass = view.findViewById(R.id.txv_forgot_pass);
        txvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), Forgot_PasswordActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        edtEmail = view.findViewById(R.id.edt_email);
        edtPass = view.findViewById(R.id.edt_password);

        visiblility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visiblility.getTag().equals("off")) {
                    visiblility.setTag("on");
                    visiblility.setImageResource(R.drawable.ic_visibility_on);
                    edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    // edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    visiblility.setTag("off");
                    visiblility.setImageResource(R.drawable.ic_visibility_off);
                    edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // edtPass.setTransformationMethod(DoNothingTransformation.getInstance());
                }
            }
        });


        String temp = MyApplication.getInstance().getPrefManager().getRememberEmail();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            check_box.setChecked(true);
            edtEmail.setText(temp);
        }

        temp = MyApplication.getInstance().getPrefManager().getRememberPass();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            edtPass.setText(temp);
        }
    }

    private boolean isValidEmail() {

        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            getErrorDialog("Masukkan alamat email"); //enter email address

            return false;
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                getErrorDialog("Masukkan alamat email yang benar"); //enter valid email address
                return false;
            }
        }
        if (edtPass.getText().toString().length() == 0) {
            getErrorDialog("Masukkan kata kunci");
            return false;
        }

        return true;
    }


    private void loginToUser() {

        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.USER_AUTHENTICATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LOGINTOUSER", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        MainActivity m = (MainActivity) getActivity();
                        m.startTimer();
                        MyApplication.getInstance().getPrefManager().setIsLogged(true);

                        if (jsonObject.has("access_token")) {
                            MyApplication.getInstance().getPrefManager().setAccessToken(jsonObject.getString("access_token"));
                        }

                        ((MainActivity) getActivity()).getNotification();
                        getUserInfo();
                    } else {
                        progressDialog.dismiss();
                        if (jsonObject.getString("message").contains("title")){
                            JSONObject JESObject = jsonObject.getJSONObject("message");
                            showTopupDialog(JESObject.getString("title"),JESObject.getString("text"));
                        }
                        else if (jsonObject.getString("message").contains("Alfamind Anda belum aktif")){
                            showTopupDialog("Oops, akun Alfamind Anda belum aktif.",jsonObject.getString("message"));
                        }else {
                            showAlertDialog(jsonObject.getString("message"));
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    showAlertDialog("server error");
                    progressDialog.dismiss();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //  getErrorDialog("Something went wrong");
                //showAlertDialog("something went wrong");
                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((MainActivity) getActivity()).showSnackBar();
                }

                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("email", edtEmail.getEditableText().toString());
                param.put("passwd", edtPass.getEditableText().toString() + "");
                param.put("device_id", "na");

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getUserInfo() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LOGINTOUSER", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        manager.setPassword(edtPass.getText().toString());
                        if (jsonObject.has("user_data")) {
                            JSONObject object = jsonObject.getJSONObject("user_data");
                            if (object.has("avatar")) {
                                manager.setImage(object.getString("avatar"));
                            }
                            if (object.has("firstname")) {
                                manager.setFirstName(object.getString("firstname"));
                            }
                            if (object.has("lastname")) {
                                manager.setLastName(object.getString("lastname"));
                            }
                            if (object.has("store_name")) {
                                manager.setShopName(object.getString("store_name"));
                            }
                            if (object.has("id_customer")) {
                                manager.setCoustmerId(object.getString("id_customer"));
                            }
                            if (object.has("id_customer_ora")) {
                                manager.setCoustmerIdOra(object.getString("id_customer_ora"));
                            }
                            if (object.has("point_reward")) {
                                manager.setPoint(object.getString("point_reward"));
                            }
                            if (object.has("email")) {
                                manager.setEmail(object.getString("email"));
                            }
                            if (object.has("wallet_number")) {
                                manager.setWalletNo(object.getString("wallet_number"));
                            }
                            if (object.has("dob")) {
                                manager.setDOB(object.getString("dob"));
                            }
                            if (object.has("address")) {
                                manager.setAddress(object.getString("address"));
                            }
                            if (object.has("city")) {
                                manager.setCity(object.getString("city"));
                            }
                            if (object.has("phone_mobile")) {
                                manager.setPhoneNumber(object.getString("phone_mobile"));
                            }
                            if (object.has("postcode")) {
                                manager.setZipCode(object.getString("postcode"));
                            }
                            if (object.has("member_since")) {
                            }
                            if (object.has("gender")) {
                                manager.setGender(object.getString("gender"));
                            }

                            if (object.has("pic_lv1_province")) {
                                JSONObject pObject = object.getJSONObject("pic_lv1_province");
                                if (pObject.has("id")) {
                                    String pid1 = pObject.getString("id");
                                    if (pid1 != null) {
                                        //manager.setPid1(Integer.parseInt(pid1));
                                    }
                                }
                                if (pObject.has("label")) {
                                    manager.setState(pObject.getString("label"));
                                }
                            }

                            if (object.has("pic_lv2_kabupaten")) {
                                JSONObject pObject = object.getJSONObject("pic_lv2_kabupaten");
                                if (pObject.has("id")) {

                                    String pid2 = pObject.getString("id");
                                    if (pid2 != null) {
                                        //manager.setPid2(Integer.parseInt(pid2));
                                    }
                                }
                                if (pObject.has("label")) {
                                    manager.setDistrict(pObject.getString("label"));
                                }
                            }

                            if (object.has("pic_lv3_kecamatan")) {

                                JSONObject pObject = object.getJSONObject("pic_lv3_kecamatan");
                                if (pObject.has("id")) {
                                    String pid3 = pObject.getString("id");
                                }
                                if (pObject.has("label")) {

                                    manager.setkecamatan(pObject.getString("label"));
                                }
                            }
                            if (object.has("bank_account")){
                                manager.setBankName(object.getString("bank_account"));
                            }
                            if (object.has("bank_account_name")){
                                manager.setBankOwnerName(object.getString("bank_account_name"));
                            }
                            if (object.has("bank_account_number")){
                                manager.setBankAccountNumber(object.getString("bank_account_number"));
                            }
                            oldCartItemList();
                        }

                    } else {
                        showAlertDialog(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showAlertDialog("server error");
                    progressDialog.dismiss();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //  getErrorDialog("Something went wrong");
                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((MainActivity) getActivity()).showSnackBar();
                }
                // showAlertDialog("something went wrong");
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    public void getErrorDialog(String str) {
        final String[] list = {str};
        final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(getContext(), list, null);
        dialog.title("");
        dialog.cancelText("Ok");
        dialog.show();

    }

    private void showTopupDialog(String title,String message){

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.topup_dialog);

        TextView text1 = dialog.findViewById(R.id.title);
        TextView text2 = dialog.findViewById(R.id.body);
        text1.setText(title);
        text2.setText(message);

        dialog.findViewById(R.id.topup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(getActivity(), Pembelian_TopUpActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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

                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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


    public void addToNewCart(final ArrayList<String> old_CartProductID, final ArrayList<String> old_CartProductQuantity) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.d("Highlight", id.meteor.alfamind.helper.Constant.ADD_TO_CART + "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTRESPONSE", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        if (jsonObject.has("id_cart")) {
                            Log.e("id_cart_pppp", jsonObject.getString("id_cart"));
                            MyApplication.getInstance().getPrefManager().setCartId(jsonObject.getString("id_cart"));
                            Log.e("id_cart_pppp88", MyApplication.getInstance().getPrefManager().getCartID());
                        }
                        if (old_CartProductID.size() > 1) {
                            addToNewCartMore(old_CartProductID, old_CartProductQuantity);
                        } else {
                            progressDialog.dismiss();
                            mainActivity.getAccountFragment();
                        }
                    } else {
                        getErrorDialog("Something went wrong");
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
                getErrorDialog("Something went wrong");
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

                Log.d("PRODUCT_ID_LL", old_CartProductID.get(0) + " " + toktn + "   idcart_>" + old_CartProductQuantity.get(0) + "");
                param.put("id_cart", "0");
                param.put("access_token", toktn + "");
                param.put("id_product", old_CartProductID.get(0) + "");
                param.put("quantity", old_CartProductQuantity.get(0) + "");
                param.put("operation", "up");
                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void addToNewCartMore(ArrayList<String> old_CartProductID, ArrayList<String> old_CartProductQuantity) {

        for (int i = 1; i < old_CartProductID.size(); i++) {
            final String ProductID = old_CartProductID.get(i);
            final String ProductQuantity = old_CartProductQuantity.get(i);
            Log.e("ADDING", ProductID + "    --Q--" + ProductQuantity);

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            Log.d("Highlight", id.meteor.alfamind.helper.Constant.ADD_TO_CART + "");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.ADD_TO_CART, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("CARTRESPONSE", response + "");
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {


                            if (jsonObject.has("id_cart")) {
                                Log.e("id_cart_pppp", jsonObject.getString("id_cart"));
                                MyApplication.getInstance().getPrefManager().setCartId(jsonObject.getString("id_cart"));
                                Log.e("id_cart_pppp88", MyApplication.getInstance().getPrefManager().getCartID());
                            }
                        } else {
                            getErrorDialog("Something went wrong");
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
                    getErrorDialog("Something went wrong");
                    Log.d("VollyError", " something went wrong" + error.getMessage() + "");
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();

                    String toktn = MyApplication.getInstance().getPrefManager().getAccessToken();

                    String idCart12;
                    if (toktn == null || toktn.equals("null") || toktn.equals("")) {
                        toktn = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                    }

                    idCart12 = MyApplication.getInstance().getPrefManager().getCartID();

                    Log.d("PRODUCT_ID_LL", ProductID + " " + toktn + "   idcart_>" + idCart12 + "");
                    param.put("id_cart", idCart12 + "");
                    param.put("access_token", toktn + "");
                    param.put("id_product", ProductID + "");
                    param.put("quantity", ProductQuantity + "");
                    param.put("operation", "up");
                    return param;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
        progressDialog.dismiss();
        mainActivity.getAccountFragment();
    }


    public void oldCartItemList() {
        final ArrayList<String> old_CartProductID = new ArrayList<>();
        final ArrayList<String> old_CartProductQuantity = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        Log.e("OLD_CARTID", old_CartID);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.GET_CART_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CARTLISTRESPONSEOLD", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("cart_contents")) {
                        JSONArray array = jsonObject.getJSONArray("cart_contents");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject cartObject = array.getJSONObject(i);
                            if (cartObject.has("product_id")) {
                                old_CartProductID.add(cartObject.getString("product_id"));
                            }
                            if (cartObject.has("product_info")) {
                                JSONObject prodectObj = cartObject.getJSONObject("product_info");

                                if (prodectObj.has("cart_item_quantity")) {
                                    old_CartProductQuantity.add(prodectObj.getString("cart_item_quantity"));
                                }
                            }
                        }
                    }

                    Log.e("OLD_CART_SIZE", old_CartProductID.size() + "");
                    if (old_CartProductID.size() > 0) {
                        addToNewCart(old_CartProductID, old_CartProductQuantity);
                    } else {
                        progressDialog.dismiss();
                        mainActivity.getAccountFragment();
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
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                String toktn = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
                Log.d("PRODUCT_ID", toktn + "   -  " + old_CartID + "");
                param.put("id_cart", old_CartID);
                param.put("access_token", toktn);

                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPress() {
        ((MainActivity) getActivity()).getShoppingFragment();
    }
}
