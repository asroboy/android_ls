package com.lapaksembako.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lapaksembako.app.R;
import com.lapaksembako.app.model.Item;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;

public class CartListAdapter extends ArrayAdapter<Item> implements View.OnClickListener {
    private ArrayList<Item> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtPrice;
        NumberPicker numberPicker;
    }

    public CartListAdapter(ArrayList<Item> data, Context context) {
        super(context, R.layout.row_item_cart, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Item dataModel = (Item) object;

        switch (v.getId()) {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " + dataModel.getAddress(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_cart, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.textViewNominal);
            viewHolder.txtPrice = convertView.findViewById(R.id.textView51);
            viewHolder.numberPicker = convertView.findViewById(R.id.number_picker);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getNama());
        viewHolder.txtPrice.setText("Rp " + dataModel.getHarga());
        viewHolder.numberPicker.setValue(1);
        viewHolder.txtName.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}

