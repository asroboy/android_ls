package com.lapaksembako.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lapaksembako.app.BuktiPembayaranActivity;
import com.lapaksembako.app.R;
import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.TransactionListAdapter;
import com.lapaksembako.app.helper.OnFragmentInteractionListener;
import com.lapaksembako.app.model.Transaction;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SejarahBelanjaDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SejarahBelanjaDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "status";

    // TODO: Rename and change types of parameters
    private String status;
    User mUser;
    Core core;
    ArrayList<Transaction> transactions;

    private OnFragmentInteractionListener mListener;

    public SejarahBelanjaDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param status Parameter 1.
     * @param user   Parameter 2.
     * @return A new instance of fragment SejarahBelanjaDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SejarahBelanjaDetailFragment newInstance(String status, User user) {
        SejarahBelanjaDetailFragment fragment = new SejarahBelanjaDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, status);
        fragment.setArguments(args);
        fragment.mUser = user;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(ARG_PARAM1);
        }
    }

    ListView listViewHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sejarah_belanja_detail, container, false);
        listViewHistory = view.findViewById(R.id.listViewHistory);

        core = new Core(mUser);
        transactions = new ArrayList<>();
        final TransactionListAdapter adapter = new TransactionListAdapter(transactions, getActivity());
        listViewHistory.setAdapter(adapter);
        listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), BuktiPembayaranActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", mUser);
                bundle.putParcelable("invoice", transactions.get(i));
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, 200);
            }
        });
        if (status.equals("pending")) {
            core.getPendingTransaction(new Core.OnCallCompleteListener() {
                @Override
                public void onSuccess(Intent data) {
                    ArrayList<Transaction> t = data.getParcelableArrayListExtra("transactions");
                    for (Transaction transaction : t) {
                        transactions.add(transaction);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(Intent data) {

                }
            });
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
