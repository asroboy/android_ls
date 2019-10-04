package com.lapaksembako.app.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lapaksembako.app.R;
import com.lapaksembako.app.WebActivity;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Slider;


/**
 * A placeholder fragment containing a simple view.
 */
public class PromoFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    Slider slider;

    private PageViewModel pageViewModel;

    public static PromoFragment newInstance(int index, Slider slider) {
        PromoFragment fragment = new PromoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        fragment.slider = slider;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_promo, container, false);
        final ImageView imageView = root.findViewById(R.id.imageView2);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(this)
                .load(Common.BASE_URL_SLIDER + slider.getImage())
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "Promo");
                bundle.putString("url", slider.getUrl());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        return root;
    }
}