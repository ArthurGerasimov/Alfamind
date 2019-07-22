package id.meteor.alfamind.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import id.meteor.alfamind.Activity.MainActivity;
import id.meteor.alfamind.Activity.NotificationDetailActivity;
import id.meteor.alfamind.Base.BaseFragment;
import id.meteor.alfamind.Interface.FragmentListener;
import id.meteor.alfamind.Model.NotificationModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.MyPreferenceManager;

public class NotificationFragment extends BaseFragment implements FragmentListener{

    View view;
    MainActivity mainActivity;
    LinearLayout listNoti;
    private SwipeRefreshLayout swipe_refresh;

    ArrayList<NotificationModel> notiList = new ArrayList<>();
    private ProgressDialog progressDialog;

    public FragmentListener getFragmentListener(){
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = (MainActivity) getActivity();
        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        listNoti = view.findViewById(R.id.list_noti);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);

        loadLayout();

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listNoti.removeAllViews();
                loadLayout();
            }
        });

        return view;
    }

    private void loadLayout() {

        if (MyApplication.isNetworkAvailable(getActivity())) {
            getNotificationList();
        } else {
            ((MainActivity) getActivity()).showSnackBar();
        }

        /*listNoti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), NotificationDetailActivity.class);
                intent.putExtra("message", notiList.get(i).getContent());
                intent.putExtra("title", notiList.get(i).getTitle());
                intent.putExtra("id",notiList.get(i).getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });*/
    }

    @Override
    public void onResume() {
        getNotificationList();
        super.onResume();
    }

    private void getNotificationList() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GET_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("NotificationResponse", response + "");
                try {
                    listNoti.removeAllViews();
                    progressDialog.dismiss();

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {

                        if (jsonObject.has("notifications")) {
                            JSONArray array = jsonObject.getJSONArray("notifications");
                            notiList.clear();
                            for (int i = 0; i < array.length(); i++) {
                                NotificationModel notificationModel = new NotificationModel();
                                final JSONObject object = array.getJSONObject(i);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View convertView = inflater.inflate(R.layout.list_noti, null);
                                listNoti.addView(convertView);

                                ImageView dot = convertView.findViewById(R.id.imv_dot);
                                final TextView txv_title = convertView.findViewById(R.id.txv_title);
                                TextView txv_date = convertView.findViewById(R.id.txv_date);
                                final TextView txv_content = convertView.findViewById(R.id.txv_content);
                                final TextView txt_image = convertView.findViewById(R.id.image);

                                if (object.has("datetime")) {
                                    txv_date.setText(getDateFormat(object.getString("datetime")));
                                    notificationModel.setDate(object.getString("datetime"));
                                }
                                if (object.has("gcm_message_id")) {
                                    notificationModel.setId(object.getString("gcm_message_id"));
                                }
                                if (object.has("title_text")) {
                                    notificationModel.setTitle(object.getString("title_text"));
                                    txv_title.setText(object.getString("title_text") + "");
                                }
                                if (object.has("message_text")) {
                                    notificationModel.setContent(object.getString("message_text"));
                                    txv_content.setText(object.getString("message_text").trim() + "");
                                }
                                if (object.has("image")){
                                    notificationModel.setImage(object.getString("image"));
                                    txt_image.setText(object.getString("image"));
                                }
                                if (object.has("read_status")) {
                                    String isRead = object.getString("read_status");
                                    if (isRead != null && isRead.equals("1")) {
                                        notificationModel.setRead(true);
                                        dot.setVisibility(View.GONE);
                                    } else {
                                        dot.setVisibility(View.VISIBLE);
                                        notificationModel.setRead(false);
                                    }
                                }

                                convertView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), NotificationDetailActivity.class);
                                        intent.putExtra("message", txv_content.getText().toString() + "");
                                        intent.putExtra("title", txv_title.getText().toString() + "");
                                        intent.putExtra("image",txt_image.getText().toString()+"");
                                        try {
                                            intent.putExtra("id", object.getString("gcm_message_id") + "");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    }
                                });

                                notiList.add(notificationModel);
                                swipe_refresh.setRefreshing(false);
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    swipe_refresh.setRefreshing(false);
                    getErrorDialog("Something went wrong" + e.getMessage());
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                swipe_refresh.setRefreshing(false);

                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    ((MainActivity) getActivity()).showSnackBar();
                } else {
                    getErrorDialog("Something went wrong");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

                String access_token = manager.getAccessToken();
                Log.d("UPDATEPARAM", access_token + "");
                if (access_token != null && !access_token.equals("null") && !access_token.equals("")) {
                    param.put("access_token", access_token);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("access_token", "");
                }
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public String getDateFormat(String str) {

        String[] divide = str.split(" ");
        String[] divide1 = divide[0].split("-");

        return divide1[2] + "/" + divide1[1] + "/" + divide1[0];
    }


    public void getErrorDialog(String str) {

        if (mainActivity.isFinishing()) {
            final String[] list = {str};
            final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(getContext(), list, null);
            dialog.title("");
            dialog.cancelText("Ok");
            dialog.show();
        }
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