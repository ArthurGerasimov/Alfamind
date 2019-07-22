package id.meteor.alfamind.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import id.meteor.alfamind.Activity.Jaringan_AnggotaActivity;
import id.meteor.alfamind.Activity.Jaringan_Anggota_DetailActivity;
import id.meteor.alfamind.Model.DownlinksModel;
import id.meteor.alfamind.R;

/**
 * Created by bodacious on 15/5/18.
 */

public class AnggotaAdapter extends ArrayAdapter<DownlinksModel> {

    private Context context;
    private ArrayList<DownlinksModel> list;

    public AnggotaAdapter(@NonNull Context context, ArrayList list) {
        super(context, R.layout.mutasi_point_history);
        this.context = context;
        this.list = list;
    }

    public DownlinksModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView==null){
             viewHolder = new ViewHolder();

            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.mutasi_point_history, parent, false);

            viewHolder.date = convertView.findViewById(R.id.date);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.point = convertView.findViewById(R.id.point);
            viewHolder.img = convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DownlinksModel downlinksModel = list.get(position);

        viewHolder.date.setText(dateFormat(downlinksModel.getMember_since()));
        viewHolder.name.setText(downlinksModel.getName()+" - "+downlinksModel.getPhone_mobile());
        viewHolder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_next_black2, 0);

        if (downlinksModel.getStatus().equalsIgnoreCase("Active")) {
            viewHolder.point.setText("Aktif");
            viewHolder.point.setTextColor(context.getResources().getColor(R.color.dark_green));
        } else {
            viewHolder.point.setText("Belum Top up");
            viewHolder.point.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Jaringan_Anggota_DetailActivity.class);
                intent.putExtra("name", "" + downlinksModel.getName());
                intent.putExtra("date", "" + dateFormat(downlinksModel.getMember_since()));
                intent.putExtra("phone", "" + downlinksModel.getPhone_mobile());
                intent.putExtra("address", "" + downlinksModel.getAddress());
                intent.putExtra("email", "" + downlinksModel.getEmail());
                context.startActivity(intent);
                ((Jaringan_AnggotaActivity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        return convertView;
    }

    public class ViewHolder{
        private TextView date,name,point;
        private ImageView img;
    }

    public String dateFormat(String str) {
        String[] year = str.split("/");
        str = year[0];
        switch (year[1]) {
            case "01":
                str = str + " Januari ";
                break;
            case "02":
                str = str + " Februari ";
                break;
            case "03":
                str = str + " Maret ";
                break;
            case "04":
                str = str + " April ";
                break;
            case "05":
                str = str + " Mei ";
                break;
            case "06":
                str = str + " Juni ";
                break;
            case "07":
                str = str + " Juli ";
                break;
            case "08":
                str = str + " Agustus ";
                break;
            case "09":
                str = str + " September ";
                break;
            case "10":
                str = str + " Oktober ";
                break;
            case "11":
                str = str + " November ";
                break;
            case "12":
                str = str + " Desember ";
                break;
        }
        str = str + year[2];
        return str;
    }
}
