package com.lapaksembako.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lapaksembako.app.AlamatActivity;
import com.lapaksembako.app.EditProfilActivity;
import com.lapaksembako.app.R;
import com.lapaksembako.app.TambahAkunBankActivity;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetAddress;
import com.lapaksembako.app.api_model.GetAkunBank;
import com.lapaksembako.app.api_model.GetPromo;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.DownloadImageTask;
import com.lapaksembako.app.helper.OnFragmentInteractionListener;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.AkunBank;
import com.lapaksembako.app.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }


    User userLogin;
    String alamat = "";
    String bank = "";
    TextView tvUsername, tvNoHp, tvEmail, tvAlamat, textViewEditProfile, tvAkunBank;
    ImageView imageViewProfile;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(User userLogin) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userLogin);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userLogin = getArguments().getParcelable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        imageViewProfile = v.findViewById(R.id.imageView9);
        tvUsername = v.findViewById(R.id.tvUsername);
        tvNoHp = v.findViewById(R.id.tvNoHp);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvAlamat = v.findViewById(R.id.tvAlamat);
        textViewEditProfile = v.findViewById(R.id.textViewEditProfile);
        tvAkunBank = v.findViewById(R.id.tvBank);
        if (userLogin != null) {
            tvUsername.setText(userLogin.getNama());
            tvNoHp.setText(userLogin.getPhone());
            tvEmail.setText(userLogin.getEmail());
            tvAlamat.setText("");
            if (userLogin.getProfilePic() != null) {
                if (!userLogin.getProfilePic().equals("")) {
                    Glide.with(this)
                            .load(Common.BASE_URL_PROFILE + userLogin.getProfilePic())
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageViewProfile);
//                    new DownloadImageTask(imageViewProfile)
//                            .execute(Common.BASE_URL_PROFILE + userLogin.getProfilePic());
                }
            }
        }
        getLstAddress();
        textViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfilActivity.class);
                intent.putExtra("user", userLogin);
                intent.putExtra("alamat", alamat);
                intent.putExtra("bank", bank);
                getActivity().startActivityForResult(intent, Common.REQUEST_UPDATE_PROFILE);
            }
        });


        getAkunBank();

        return v;
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

    ApiInterface apiInterface;

    private void getLstAddress() {
        Call<GetAddress> flashSaleCall = apiInterface.getListDefaultAdddress(userLogin.getId());
        flashSaleCall.enqueue(new Callback<GetAddress>() {
            @Override
            public void onResponse(Call<GetAddress> call, Response<GetAddress> response) {
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, response.body().getStatus() + " " + response.body().getAddress().size());
                    if (response.body().getAddress() != null) {
                        if (response.body().getAddress().size() > 0) {
                            Address a = response.body().getAddress().get(0);
                            tvAlamat.setText(a.getAddress());
                            alamat = a.getAddress();
                        } else {
                            tvAlamat.setText("+ Tambah alamat");
                            tvAlamat.setTextColor(getResources().getColor(R.color.yellow));
                            tvAlamat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    tambahAlamat();
                                }
                            });
                        }

                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetAddress> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
            }
        });
    }


    private void getAkunBank() {
        Call<GetAkunBank> flashSaleCall = apiInterface.getAkunBank(userLogin.getId());
        flashSaleCall.enqueue(new Callback<GetAkunBank>() {
            @Override
            public void onResponse(Call<GetAkunBank> call, Response<GetAkunBank> response) {
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, response.body().getStatus() + " " + response.body().getAkunBanks().size());
                    if (response.body().getAkunBanks() != null) {
                        if (response.body().getAkunBanks().size() > 0) {
                            AkunBank a = response.body().getAkunBanks().get(0);
                            bank = a.getBankAccount() + " - " + a.getAccountNumber();
                            tvAkunBank.setText(bank);
                        } else {
                            tvAkunBank.setText("+ Tambah Akun Bank");
                            tvAkunBank.setTextColor(getResources().getColor(R.color.yellow));
                            tvAkunBank.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    tambahAkunBank();
                                }
                            });
                        }

                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetAkunBank> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
            }
        });
    }


    private void tambahAlamat() {
        Intent intent = new Intent(getActivity(), AlamatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userLogin);
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent, Common.REQUEST_ADD_ADDRESS);
    }

    private void tambahAkunBank() {
        Intent intent = new Intent(getActivity(), TambahAkunBankActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userLogin);
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent, Common.REQUEST_ADD_BANK);
    }
}
