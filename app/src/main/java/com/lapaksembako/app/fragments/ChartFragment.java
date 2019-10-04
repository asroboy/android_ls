package com.lapaksembako.app.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lapaksembako.app.MainBottomNavigationActivity;
import com.lapaksembako.app.PesananActivity;
import com.lapaksembako.app.R;
import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.MyItemChartRecyclerViewAdapter;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.OnListFragmentInteractionListener;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ChartFragment extends Fragment implements View.OnClickListener {
    private OnListFragmentInteractionListener mListener;
    ArrayList<Item> items;
    Core core;
    User user;
    RecyclerView recyclerView;
    int totalBelanja = 0;

    Button buttonTotal, buttonBelanjaLagi, buttonCheckout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChartFragment() {
    }


    public static ChartFragment newInstance(User user) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        fragment.user = user;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewX = inflater.inflate(R.layout.fragment_item_chart_list, container, false);
        core = new Core(user);
        recyclerView = viewX.findViewById(R.id.list);
        buttonTotal = viewX.findViewById(R.id.buttonTotal);
        buttonBelanjaLagi = viewX.findViewById(R.id.buttonBelanjaLagi);
        buttonCheckout = viewX.findViewById(R.id.buttonCheckout);
        buttonBelanjaLagi.setOnClickListener(this);
        buttonCheckout.setOnClickListener(this);

        core.getItemsCart(user, false, new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                items = data.getParcelableArrayListExtra("items");
                if (items != null) {
                    for (Item item : items) {
                        totalBelanja += (item.getQuantity() * item.getHarga());
                    }
                    Log.d(Common.TAG, "items size : " + items.size());

                    // Set the adapter
                    if (recyclerView instanceof RecyclerView) {
                        final Context context = recyclerView.getContext();
                        if (core.getCart() == null) {
                            core.checkAvailableCart(false, new Core.OnCallCompleteListener() {
                                @Override
                                public void onSuccess(Intent data) {
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    recyclerView.setAdapter(new MyItemChartRecyclerViewAdapter(user, context, items, mListener, core.getCart(), buttonTotal, totalBelanja, onDataChangedListener));
                                }

                                @Override
                                public void onFailed(Intent data) {

                                }
                            });
                        } else {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(new MyItemChartRecyclerViewAdapter(user, context, items, mListener, core.getCart(), buttonTotal, totalBelanja, onDataChangedListener));
                        }

                    }
                }
                buttonTotal.setText("Total Rp " + totalBelanja);

            }

            @Override
            public void onFailed(Intent data) {

            }
        });

        return viewX;
    }


    MyItemChartRecyclerViewAdapter.OnDataChangedListener onDataChangedListener = new MyItemChartRecyclerViewAdapter.OnDataChangedListener() {
        @Override
        public void onChanged(Item item, int position, int total) {
            items.remove(position);
            items.add(position, item);
            totalBelanja = total;
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonBelanjaLagi) {
            ((MainBottomNavigationActivity) getActivity()).replaceFragment(MainFragment.newInstance(user));
        }
        if (id == R.id.buttonCheckout) {
            if (items != null) {
                if (items.size() > 0) {
                    Intent intent = new Intent(getActivity(), PesananActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("items", items);
                    bundle.putParcelable("user", user);
                    bundle.putInt("total", totalBelanja);
                    bundle.putInt("cart_id", core.getCart().getId());
                    intent.putExtra("bundle", bundle);
                    getActivity().startActivityForResult(intent, Common.REQUEST_PESANAN);
                } else {
                    Common.showWarningDialog(getActivity(), "Tidak ada item yang dibeli", new Common.OnDialogActionSelected() {
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
            } else {
                Common.showWarningDialog(getActivity(), "Tidak ada item yang dibeli", new Common.OnDialogActionSelected() {
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


        }
    }
}
