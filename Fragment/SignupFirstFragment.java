package id.meteor.alfamind.Fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import id.meteor.alfamind.Activity.SignupActivity;
import id.meteor.alfamind.Base.BaseFragment;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetDialog;
import id.meteor.alfamind.customDialog.OnOperItemClickL;
import id.meteor.alfamind.helper.EditFocusChange;
import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.SignUpManager;


public class SignupFirstFragment extends BaseFragment {

    private SignupActivity signupActivity;

    public SignupFirstFragment() {
    }

    private String holdDate, DATA;
    private Calendar calendar;
    private String[] list;
    private EditText et_fname, et_lname, et_gender, et_email, et_date, et_month, et_year, et_phone_number;

    public void setActivity(SignupActivity signupActivity) {
        this.signupActivity = signupActivity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_first, container, false);
        DATA = getArguments().getString("DATA");
        Log.e("BUNDLEDATA", getArguments().getString("DATA") + "");

        TextView next = view.findViewById(R.id.next);
        // back_btn = view.findViewById(R.id.back_btn);
        et_fname = view.findViewById(R.id.et_fname);
        et_lname = view.findViewById(R.id.et_lname);
        et_gender = view.findViewById(R.id.et_gender);
        et_email = view.findViewById(R.id.et_email);
        et_date = view.findViewById(R.id.date);
        et_month = view.findViewById(R.id.month);
        et_year = view.findViewById(R.id.year);
        TextView title = view.findViewById(R.id.title);
        et_phone_number = view.findViewById(R.id.et_number);
        ScrollView scroll_view = view.findViewById(R.id.scroll);
        scroll_view.scrollTo(0, view.getBottom());
        next.setMovementMethod(new ScrollingMovementMethod());
        SignUpManager manager = MyApplication.getInstance().getSignupManager();

        calendar = Calendar.getInstance();
        et_date.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
        et_month.setText((calendar.get(Calendar.MONTH) + 1) + "");
        et_year.setText(calendar.get(Calendar.YEAR) + "");

        if (DATA.equalsIgnoreCase("NEW_NETWORK")) {
            title.setText("Pendaftaran Anggota");
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.isNetworkAvailable(getActivity())) {
                    if (validation()) {
                        saveValueInSharedPrefence();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        Bundle bundle = new Bundle();
                        bundle.putString("DATA", DATA);
                        SignupSecondFragment signupSecondFragment = new SignupSecondFragment();
                        signupSecondFragment.setArguments(bundle);
                        signupSecondFragment.setActivity(signupActivity);
                        transaction.replace(R.id.frame, signupSecondFragment, "two");
                        transaction.commit();
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                } else {
                    signupActivity.getErrorDialog("Tidak ada koneksi internet");
                }


            }
        });
        et_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderSelection(et_gender);
            }
        });
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daySelection(et_date);
            }
        });
        et_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthSelection(et_month);
            }
        });
        et_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearSelection(et_year);
            }
        });


        et_lname.setOnFocusChangeListener(new EditFocusChange(et_lname, scroll_view));
        et_gender.setOnFocusChangeListener(new EditFocusChange(et_gender, scroll_view));
        et_email.setOnFocusChangeListener(new EditFocusChange(et_email, scroll_view));
        et_date.setOnFocusChangeListener(new EditFocusChange(et_date, scroll_view));
        et_month.setOnFocusChangeListener(new EditFocusChange(et_month, scroll_view));
        et_year.setOnFocusChangeListener(new EditFocusChange(et_year, scroll_view));

        return view;
    }

    @Override
    public void onDestroy() {
        saveValueInSharedPrefence();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        setData();
        super.onResume();
    }

    private void setData() {

        SignUpManager manager = MyApplication.getInstance().getSignupManager();

        String temp = manager.getFirstName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_fname.setText(temp);
        }

        temp = manager.getLastName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_lname.setText(temp);
        }

        temp = manager.getGender();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_gender.setText(temp);
        }

        temp = manager.getEmail();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_email.setText(temp);
        }

        temp = manager.getBirthDate();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_date.setText(temp);
        }

        temp = manager.getBirthMonth();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_month.setText(temp);
        }

        temp = manager.getBirthYear();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_year.setText(temp);
        }

        temp = manager.getPhoneNumber();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            et_phone_number.setText(temp);
        }

    }


    //  Validation
    public boolean validation() {
        if (TextUtils.isEmpty(et_fname.getText().toString())) {
            signupActivity.getErrorDialog("Masukkan nama depan");
            return false;
        }
        if (TextUtils.isEmpty(et_lname.getText().toString())) {
            signupActivity.getErrorDialog("Masukkan nama belakang");
            return false;
        }
        if (TextUtils.isEmpty(et_gender.getText().toString())) {
            signupActivity.getErrorDialog("Pilih jenis kelamin");
            return false;
        }
        if (TextUtils.isEmpty(et_email.getText().toString())) {
            signupActivity.getErrorDialog("Masukkan alamat email");

            return false;
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
                signupActivity.getErrorDialog("Masukkan alamat email yang benar");
                return false;
            }
        }
        if (TextUtils.isEmpty(et_date.getText().toString())) {
            signupActivity.getErrorDialog("Pilih tanggal");
            return false;
        }
        if (TextUtils.isEmpty(et_month.getText().toString())) {
            signupActivity.getErrorDialog("Pilih Bulan");
            return false;
        }
        if (TextUtils.isEmpty(et_year.getText().toString())) {
            signupActivity.getErrorDialog("Pilih tahun");
            return false;
        }
        if (TextUtils.isEmpty(et_phone_number.getText().toString()) || et_phone_number.getText().toString().length() < 8) {
            signupActivity.getErrorDialog("Masukkan nomor ponsel");
            return false;
        }
        return true;
    }


    // genderSelection
    private void genderSelection(final EditText et) {
        final String[] list = getResources().getStringArray(R.array.gender_array);

        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), list, null);
        dialog.title("Jenis kelamin");
        dialog.cancelText("Batal").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                et.setText(list[position]);
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    // date selection
    private void daySelection(final EditText et) {
        List<String> list1 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.date_array)));

        if (checkLeapYear(Integer.parseInt(et_year.getText().toString()))) {
            String month = et_month.getText().toString();
            switch (month) {
                case "1":
                case "3":
                case "5":
                case "7":
                case "8":
                case "10":
                case "12":
                    break;
                case "2":
                    list1.remove("30");
                    list1.remove("31");
                    break;
                default:
                    list1.remove("31");
                    break;
            }
            list = list1.toArray(new String[0]);
        } else {
            String month = et_month.getText().toString();
            switch (month) {
                case "1":
                case "3":
                case "5":
                case "7":
                case "8":
                case "10":
                case "12":
                    break;
                case "2":
                    list1.remove("29");
                    list1.remove("30");
                    list1.remove("31");
                    break;
                default:
                    list1.remove("31");
                    break;
            }
            list = list1.toArray(new String[0]);
        }

        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), list, null);
        dialog.title("Tanggal");
        dialog.cancelText("Batal").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                et.setText(list[position]);
                dialog.dismiss();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    // month selection
    private void monthSelection(final EditText et) {
        final String[] list = getResources().getStringArray(R.array.month_array);

        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), list, null);
        dialog.title("Bulan");
        dialog.cancelText("Batal").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                et.setText(list[position]);
                dialog.dismiss();
                setDate();
                holdDate = et_date.getText().toString();
                Log.d("DATE_HOLD", holdDate + "");
            }

            @Override
            public void onCancel() {

            }
        });

    }

    // year selection
    private void yearSelection(final EditText et) {

        final ArrayList<String> list = new ArrayList<>();
        int year = calendar.get(Calendar.YEAR);

        for (int i = 100; i > 0; i--) {
            list.add(year + "");
            year--;
        }

        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), list.toArray(new String[0]), null);
        dialog.title("Tahun");
        dialog.cancelText("Batal").show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                et.setText(list.get(position));
                String tmp = list.get(position);

                dialog.dismiss();
                setDate();
            }

            @Override
            public void onCancel() {

            }
        });

    }

    public void saveValueInSharedPrefence() {
        // MyPreferenceManager manager =MyApplication.getInstance().getPrefManager();
        SignUpManager manager = MyApplication.getInstance().getSignupManager();

        String First_name = et_fname.getText().toString().trim();
        First_name = First_name.replace(" ", "");
        manager.setFirstName(First_name);

        String Secount_name = et_lname.getText().toString().trim();
        Secount_name = Secount_name.replace(" ", "");
        manager.setLastName(Secount_name);

        manager.setGender(et_gender.getText().toString());
        manager.setEmail(et_email.getText().toString());
        manager.setBirthDate(et_date.getText().toString());
        manager.setBirthMonth(et_month.getText().toString());
        manager.setBirthYear(et_year.getText().toString());
        manager.setPhoneNumber(et_phone_number.getText().toString());
    }


    public boolean checkLeapYear(int year) {
        return year % 400 == 0 || year % 100 != 0 && year % 4 == 0;
    }

    public void setDate() {
        {
            String month = et_month.getText().toString();
            switch (month) {
                case "1":
                case "3":
                case "5":
                case "7":
                case "8":
                case "10":
                case "12":
                    break;
                case "2":

                    if (!checkLeapYear(Integer.parseInt(et_year.getText().toString()))) {
                        if (Integer.parseInt(et_date.getText().toString()) > 28)
                            et_date.setText("28");
                    } else {
                        if (Integer.parseInt(et_date.getText().toString()) > 28)
                            et_date.setText("29");
                    }

                    break;
                default:
                    et_date.setText("30");
                    break;
            }
        }
    }

}