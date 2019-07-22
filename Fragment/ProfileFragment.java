package id.meteor.alfamind.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import id.meteor.alfamind.Activity.Forgot_PasswordActivity;
import id.meteor.alfamind.Activity.ProfileActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.MyPreferenceManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView email, name, phone, gender, dateOfBirth, city, address, provinsi, kabupaten, kecamatan, zipcode, couster_id, nama_toko, bankName, bankAccountNumber, bankOwnerName;
    private MyPreferenceManager manager;
    private RoundedImageView profile_image;
    private View view;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profile, container, false);

            profile_image = view.findViewById(R.id.profile_image);
            name = view.findViewById(R.id.name);
            couster_id = view.findViewById(R.id.couster_id);
            email = view.findViewById(R.id.email);
            phone = view.findViewById(R.id.phone);
            gender = view.findViewById(R.id.gender);
            dateOfBirth = view.findViewById(R.id.dateOfBirth);
            city = view.findViewById(R.id.city);
            address = view.findViewById(R.id.address);
            provinsi = view.findViewById(R.id.provinsi);
            kabupaten = view.findViewById(R.id.kabupaten);
            kecamatan = view.findViewById(R.id.kecamatan);
            zipcode = view.findViewById(R.id.zipcode);
            TextView editProfile = view.findViewById(R.id.edit_profile);
            TextView updatePassword = view.findViewById(R.id.update_password);
            nama_toko = view.findViewById(R.id.nama_toko);
            bankName = view.findViewById(R.id.bankName);
            bankAccountNumber = view.findViewById(R.id.bankAccountNumber);
            bankOwnerName = view.findViewById(R.id.bankOwnerName);

            if (getArguments().getString("OPEN").equalsIgnoreCase("EDITPROFILE")){
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.profile_container, new ProfileUpdateFragment()).commit();
            }

            view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();

                }
            });

            editProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.profile_container, new ProfileUpdateFragment()).commit();
                }
            });

            updatePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Forgot_PasswordActivity.class);
                    intent.putExtra("flag", false);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        setData();
        super.onResume();
    }

    private void setData() {
        manager = MyApplication.getInstance().getPrefManager();

        String temp = manager.getFirstName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            name.setText(temp);
        }
        temp = manager.getImage();
        String imPath = manager.getImagePath();
        Log.d("IMAGE_PATH", imPath + "   " + temp + "");
        if (imPath != null && !imPath.equals("null") && !imPath.equals("")) {
            if (new File(imPath).exists()) {
                Bitmap bitmap = getBitmap(imPath);
                Log.d("bitmapPATH", bitmap + "");
                profile_image.setImageBitmap(bitmap);
            } else {
                if (MyApplication.isNetworkAvailable(getActivity())) {
                    downloadImage(temp, profile_image);
                } else {
                    ((ProfileActivity) getActivity()).showSnackBar();
                }

            }
        } else {
            if (temp != null && !temp.equals("null") && !temp.equals("")) {
                if (MyApplication.isNetworkAvailable(getActivity())) {
                    downloadImage(temp, profile_image);
                } else {
                    ((ProfileActivity) getActivity()).showSnackBar();
                }
            }
        }


        temp = manager.getCoustmerId();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            couster_id.setText("Customer " + temp);
        }


        temp = manager.getEmail();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            email.setText(temp);
        }

        temp = manager.getPhoneNumber();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            phone.setText(temp);
        }

        temp = manager.getGender();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            if (temp.equalsIgnoreCase("M") || temp.equalsIgnoreCase("F")) {
                if (temp.equalsIgnoreCase("M"))
                    gender.setText("Laki-laki");
                else
                    gender.setText("Perempuan");
            } else
                gender.setText(temp);
        }

        temp = manager.getDOB();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            dateOfBirth.setText(temp);
        }

        temp = manager.getAddress();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            address.setText(temp);
        }

        temp = manager.getCity();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            city.setText(temp);
        }
        temp = manager.getZipCode();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            zipcode.setText(temp);
        }

        temp = manager.getState();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            provinsi.setText(temp);
        }

        temp = manager.getDistrict();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            kabupaten.setText(temp);
        }

        temp = manager.getkecamatan();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            kecamatan.setText(temp);
        }

        temp = manager.getShopName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            nama_toko.setText(temp);
        }

        temp = manager.getBankName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            bankName.setText(temp);
        }

        temp = manager.getBankAccountNumber();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            bankAccountNumber.setText(temp);
        }

        temp = manager.getBankOwnerName();
        if (temp != null && !temp.equals("null") && !temp.equals("")) {
            bankOwnerName.setText(temp);
        }

    }

    public Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {

            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            return bitmap;
        }
    }


    public void downloadImage(String url, final RoundedImageView imageView) {
        Log.d("ImageUrl", "  " + url + "");
        Glide.with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(100, 100) {


                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        if (imageView != null) {
                            imageView.setImageBitmap(resource);
                            saveImage(resource);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
    }


    public File saveImage(Bitmap bitmap) {
        File filename;
        try {
            String path = Environment.getExternalStorageDirectory().toString();

            File file = new File(path + "/AlfaMind/User/");
            file.mkdirs();
            filename = new File(path + "/AlfaMind/User/" + "IMG_PROFILE" + ".jpg");
            FileOutputStream out = new FileOutputStream(filename);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            MyApplication.getInstance().getPrefManager().setImagePath(filename.getPath());
            out.flush();
            out.close();
            return filename;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


}
