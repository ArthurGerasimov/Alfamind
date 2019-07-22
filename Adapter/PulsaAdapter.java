package id.meteor.alfamind.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.meteor.alfamind.Activity.ChekOutErrorActivity;
import id.meteor.alfamind.Activity.PembayaranActivity;
import id.meteor.alfamind.Model.PulsaModel;
import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;
import id.meteor.alfamind.helper.MyApplication;

/**
 * Created by bodacious on 17/1/18.
 */

public class PulsaAdapter extends RecyclerView.Adapter<PulsaAdapter.MyViewHolder> {

    private Context mContext;
    private List<PulsaModel> PulsaModel;

    public PulsaAdapter(Context mContext, List<PulsaModel> productList) {
        this.mContext = mContext;
        this.PulsaModel = productList;
    }

    // create Holder class ....
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView normal_price, harga_price, masa_text,margin;

        public MyViewHolder(View view) {
            super(view);
            normal_price = view.findViewById(R.id.normal_price);
            harga_price = view.findViewById(R.id.harga_price);
            masa_text = view.findViewById(R.id.masa_text);
            margin = view.findViewById(R.id.margin);
        }
    }


    @Override
    public PulsaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pulsa, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PulsaModel model = PulsaModel.get(position);

        holder.normal_price.setText(model.getPRODUCT_NAME());
        if (!model.getDESK_PROD().equalsIgnoreCase("null")) {
            holder.masa_text.setText(model.getDESK_PROD());
        }
        holder.harga_price.setText("Harga Rp " + getFormat(model.getHARGA()));

        if (MyApplication.getInstance().getPrefManager().getIntipMargin()) {
            if (model.getMARGIN() != null && !model.getMARGIN().equalsIgnoreCase("") && !model.getMARGIN().equalsIgnoreCase("null"))
                holder.margin.setText("Margin : Rp " + getFormat(model.getMARGIN()));
        }else
            holder.margin.setVisibility(View.GONE);

        holder.masa_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAlertDialog("YES", position);
            }
        });

        holder.harga_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.getInstance().getPrefManager().getNomorHP().length()>7){
                if (MyApplication.getInstance().getPrefManager().getIsLogged()){
                if (MyApplication.getInstance().getPrefManager().getNomorHP() != null) {
                    Intent i = new Intent(mContext, PembayaranActivity.class);
                    i.putExtra("BALANCE", model.getHARGA() + "");
                    i.putExtra("PLU", model.getPLU() + "");
                    i.putExtra("TYPE", model.getJENIS() + "");
                    mContext.startActivity(i);
                    ((Activity) mContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);}
                }
                else {
                    mContext.startActivity(new Intent(mContext, ChekOutErrorActivity.class));
                    ((Activity) mContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }}
                else
                    getErrorDialog("periksa nomor telepon");
            }
        });
    }

    @Override
    public int getItemCount() {
        return PulsaModel.size();
    }

    public String getFormat(String str) {
        str = str.replaceAll("\\.", "");
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.reverse();
        char arr[] = stringBuffer.toString().toCharArray();
        String NUM = "";
        int count = 0;
        for (int j = 0; j < arr.length; j++) {
            ++count;
            if (count == 4) {
                count = 1;
                NUM = NUM + "." + arr[j];

            } else {
                NUM = NUM + "" + arr[j];

            }
        }

        stringBuffer = new StringBuffer(NUM);
        stringBuffer.reverse();

        return String.valueOf(stringBuffer);


    }

    public void getErrorDialog(final String str) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String[] list = {str};
                final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(mContext, list, null);
                dialog.title("");
                dialog.cancelText("Ok");
                dialog.show();
            }
        });

    }

}
