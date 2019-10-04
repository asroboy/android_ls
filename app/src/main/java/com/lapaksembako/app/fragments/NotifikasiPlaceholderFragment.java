package com.lapaksembako.app.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lapaksembako.app.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class NotifikasiPlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static NotifikasiPlaceholderFragment newInstance(int index) {
        NotifikasiPlaceholderFragment fragment = new NotifikasiPlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
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
        View root = inflater.inflate(R.layout.fragment_notifikasi, container, false);
//        final ImageView imageView = root.findViewById(R.id.imageView2);
//        final TextView textView = root.findViewById(R.id.textView);
//        pageViewModel.getIndex().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(@Nullable Integer i) {
//                imageView.setImageDrawable(getResources().getDrawable(QUOC_TOUR_IMAGES[i - 1]));
//            }
//        });
        return root;
    }
}