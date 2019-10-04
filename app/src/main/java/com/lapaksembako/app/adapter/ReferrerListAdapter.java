package com.lapaksembako.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapaksembako.app.R;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.DownloadImageTask;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

public class ReferrerListAdapter extends ArrayAdapter<User> implements View.OnClickListener {
    private ArrayList<User> dataSet;
    Context mContext;

    public ReferrerListAdapter(Context context, int resource, ArrayList<User> data) {
        super(context, resource, data);
        this.dataSet = data;
        this.mContext = context;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView imageView;
    }


    @Override
    public void onClick(View v) {
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_refer, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.textViewNominal);
            viewHolder.imageView = convertView.findViewById(R.id.imageViewUserRefer);

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
        viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_profile_black));
        if (dataModel.getProfilePic() != null) {
            if (!dataModel.getProfilePic().equals("")) {
                new DownloadImageTask(viewHolder.imageView)
                        .execute(Common.BASE_URL_PROFILE + dataModel.getProfilePic());
            }
        }
        viewHolder.txtName.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}

