package com.lapaksembako.app.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lapaksembako.app.MainBottomNavigationActivity;
import com.lapaksembako.app.R;
import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.ItemGridviewAdapter;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.User;
import com.lapaksembako.app.view.ExpandableHeighGridView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_USER = "user";

    User user;
    Core core;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ItemGridviewAdapter customGridAdapter = null;
    ArrayList<Item> gridArray = new ArrayList<Item>();

    public WishListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param user   User Login.
     * @return A new instance of fragment WishListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishListFragment newInstance(String param1, User user) {
        WishListFragment fragment = new WishListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            user = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wish_list, container, false);
        final ExpandableHeighGridView gridViewWishList = v.findViewById(R.id.gridViewWishList);
        core = new Core(user);

        customGridAdapter = new ItemGridviewAdapter(getActivity(), R.layout.row_grid, gridArray, user, new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                int code = data.getIntExtra("ACTION_CODE", 0);
                Log.i(Common.TAG, " ===============TRIGGER==================== ");
                if (code == Common.ACTION_DISLIKE) {
                    Log.i(Common.TAG, " ===============DISLIKE==================== ");
                    int productId = data.getIntExtra("product_id", 0);
                    int idx = 0;
                    for (Item i : gridArray) {
                        if (i.getId() == productId) {
                            gridArray.remove(idx);
                        }

                        idx++;
                    }
                    Log.i(Common.TAG, " SIZE GRID " + gridArray.size());
                    synchronized (customGridAdapter) {
                        customGridAdapter.notifyDataSetChanged();
                    }

                }
                if (code == Common.ACTION_CART) {
                    Log.d(Common.TAG, "success add to chart");
                    ((MainBottomNavigationActivity) getActivity()).getSupportActionBar().setTitle("Keranjang Belanja");
                    ((MainBottomNavigationActivity) getActivity()).replaceFragment(ChartFragment.newInstance(user));
                }

            }

            @Override
            public void onFailed(Intent data) {
                Log.d(Common.TAG, "failed add to chart");
                Common.showWarningDialog(getActivity(), "Gagal memproses, silahkan coba lagi.", new Common.OnDialogActionSelected() {
                    @Override
                    public void onPositiveClicked(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeClicked(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            }
        });
        gridViewWishList.setAdapter(customGridAdapter);
        core.getWishList(user.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                ArrayList<Item> items = data.getParcelableArrayListExtra("items");
                Log.d(Common.TAG, "Result size " + items.size());
                for (Item item : items) {
                    Log.d(Common.TAG, "ID : " + item.getId());
                    Log.d(Common.TAG, item.getNama());
                    gridArray.add(item);
                }
                customGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Intent data) {

            }
        });


        return v;
    }

}
