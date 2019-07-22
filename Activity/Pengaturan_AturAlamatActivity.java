package id.meteor.alfamind.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import id.meteor.alfamind.Base.BaseActivity;
import id.meteor.alfamind.Database.Address;
import id.meteor.alfamind.Database.MySQLiteHelper;
import id.meteor.alfamind.Database.TableAddress;
import id.meteor.alfamind.R;

public class Pengaturan_AturAlamatActivity extends BaseActivity {

    private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase db;
    private LinearLayout details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan__atur_alamat);

        mySQLiteHelper = new MySQLiteHelper(this);
        db = mySQLiteHelper.getReadableDatabase();
        ImageView back_btn = findViewById(R.id.back_btn);
        TextView next = findViewById(R.id.next);
        details = findViewById(R.id.details);

        setData();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Pengaturan_AturAlamatActivity.this, Pengaturan_UbahAlamatActivity.class);
                i.putExtra("TITLE", "Tambah Alamat");
                i.putExtra("NEXT", "TAMBAH");
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setData() {
        details.removeAllViews();
        ArrayList<Address> addresses = TableAddress.getAllAddress(db);
        if (addresses != null) {
            for (int i = 0; i < addresses.size(); i++) {
                final Address address = addresses.get(i);
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.addressdetails_view, null);
                details.addView(view);

                TextView line1 = view.findViewById(R.id.line1);
                TextView line2 = view.findViewById(R.id.line2);
                TextView line3 = view.findViewById(R.id.line3);
                TextView line4 = view.findViewById(R.id.line4);
                TextView edit = view.findViewById(R.id.edit);
                TextView hapus = view.findViewById(R.id.hapus);

                line1.setText(address.getName() + " - " + address.getNumber());
                line2.setText(address.getAddress());
                line3.setText("RT/RW " + address.getRt() + "/" + address.getRw());
                line4.setText(address.getProvinsi() + ", " + address.getKabupaten() + ", " + address.getKecamatan() + " " + address.getKode());

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Pengaturan_AturAlamatActivity.this, Pengaturan_UbahAlamatActivity.class);
                        i.putExtra("TITLE", "Ubah Alamat");
                        i.putExtra("NEXT", "UBAH");
                        i.putExtra("ID", address.getId() + "");
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                });

                hapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAlertDialog(address.getId());
                    }
                });
            }

        }
    }

    private void showAlertDialog(final String id) {
        db = mySQLiteHelper.getWritableDatabase();

        Log.d("Dialog", "Alert");
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Apakah Anda yakin ingin menghapus alamat ini dari daftar alamat Anda?");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TableAddress.deleteAddress(id, db);
                setData();
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

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }
}

