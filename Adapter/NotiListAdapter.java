package id.meteor.alfamind.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.meteor.alfamind.Model.NotificationModel;
import id.meteor.alfamind.R;

public class NotiListAdapter extends ArrayAdapter<NotificationModel> {
    Context context;
    ArrayList notiList;
    ArrayList<NotificationModel> notificationList;

    public NotiListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<NotificationModel> notis) {
        super(context, resource, notis);

        this.context = context;
        notiList = new ArrayList<>(notis);
    }

    @Override
    public int getCount() {
        return notiList.size();
    }

    @Nullable
    @Override
    public NotificationModel getItem(int position) {
        return (NotificationModel) notiList.get(position);
    }

    public void setData(ArrayList<NotificationModel> notis) {

        notiList = new ArrayList<>(notis);
        this.notifyDataSetChanged();
    }

    public class ViewHolder {

        TextView txvTitle, txvDate, txvContent;
        ImageView imvDot;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder vHolder;

        if (convertView == null) {

            vHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_noti, parent, false);

            vHolder.txvTitle = convertView.findViewById(R.id.txv_title);
            vHolder.txvDate = convertView.findViewById(R.id.txv_date);
            vHolder.txvContent = convertView.findViewById(R.id.txv_content);
            vHolder.imvDot = convertView.findViewById(R.id.imv_dot);

            convertView.setTag(vHolder);
        } else {

            vHolder = (ViewHolder) convertView.getTag();
        }

        vHolder.txvTitle.setText(getItem(position).getTitle());
        vHolder.txvDate.setText(getItem(position).getDate());
        vHolder.txvContent.setText(getItem(position).getContent());

        boolean isRead = getItem(position).isRead();

        if (isRead) {
            vHolder.imvDot.setVisibility(View.INVISIBLE);
        } else {
            vHolder.imvDot.setVisibility(View.VISIBLE);
        }



        return convertView;
    }

}
