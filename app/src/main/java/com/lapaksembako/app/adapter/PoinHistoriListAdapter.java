package com.lapaksembako.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lapaksembako.app.R;
import com.lapaksembako.app.model.Poin;

import java.util.ArrayList;

public class PoinHistoriListAdapter extends ArrayAdapter<Poin> implements View.OnClickListener {
    private ArrayList<Poin> dataSet;
    Context mContext;

    public PoinHistoriListAdapter(Context context, int resource, ArrayList<Poin> data) {
        super(context, resource, data);
        this.dataSet = data;
        this.mContext = context;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtKeterenagan, txtNominal, textTgl;
    }


    @Override
    public void onClick(View v) {
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Poin dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_transaksi_hist, parent, false);

            viewHolder.txtKeterenagan = convertView.findViewById(R.id.textViewKeterangan);
            viewHolder.txtNominal = convertView.findViewById(R.id.textViewNominal);
            viewHolder.textTgl = convertView.findViewById(R.id.textViewTglTransaksi);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;
        String keterangan = "";
        if (dataModel.getStatus().equals("Recieved")) {
            keterangan = "Anda menerima poin sebesar " + dataModel.getNextBalance();
        } else if (dataModel.getStatus().equals("Used")) {
            keterangan = "Anda menggunakan poin sebesar " + dataModel.getNextBalance();
        }  else {
            keterangan = "Anda menerima poin sebesar " + dataModel.getNextBalance();
        }
        viewHolder.txtKeterenagan.setText(keterangan);
        viewHolder.txtNominal.setText("Rp " + dataModel.getNominal());
        viewHolder.textTgl.setText(dataModel.getCreatedDate());

        viewHolder.txtKeterenagan.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}

