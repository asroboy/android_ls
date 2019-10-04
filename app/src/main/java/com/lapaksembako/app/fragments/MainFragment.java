package com.lapaksembako.app.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lapaksembako.app.ItemsInCategoryActivity;
import com.lapaksembako.app.KomisiActivity;
import com.lapaksembako.app.MainBottomNavigationActivity;
import com.lapaksembako.app.PoinActivity;
import com.lapaksembako.app.R;
import com.lapaksembako.app.ReferralActivity;
import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.FlashSaleGridAdapter;
import com.lapaksembako.app.adapter.ItemGridviewAdapter;
import com.lapaksembako.app.adapter.KategoriGridviewAdapter;
import com.lapaksembako.app.adapter.PromoPagerAdapter;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetCategory;
import com.lapaksembako.app.api_model.GetDownline;
import com.lapaksembako.app.api_model.GetItems;
import com.lapaksembako.app.api_model.GetPromo;
import com.lapaksembako.app.api_model.GetPromoItem;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.OnFragmentInteractionListener;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.Kategori;
import com.lapaksembako.app.model.Promo;
import com.lapaksembako.app.model.Slider;
import com.lapaksembako.app.model.User;
import com.lapaksembako.app.view.ExpandableHeighGridView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user_login";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private OnFragmentInteractionListener mListener;
    ApiInterface apiInterface;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(User user) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM1, user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    User user;

    Timer swipeTimer = new Timer();
    int NUM_PAGES = 4;
    int currentPage = 0;
    boolean touched = false;
    Handler handler = new Handler();
    Runnable update;
    ViewPager viewPager;
    ExpandableHeighGridView gridView, gridViewKategori;
    ArrayList<Item> gridArray = new ArrayList<Item>();

    ArrayList<Kategori> gridKategoriArray = new ArrayList<Kategori>();
    TextView textView19, tvKomisi, tvPoin;
    LinearLayout linReferral, linKomisi, linPoin;
    Core core;
    int komisi = 0, poin = 0;
    ArrayList<Slider> sliders = new ArrayList<>();
    LinearLayout linFlash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        viewPager = v.findViewById(R.id.view_pager);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final PromoPagerAdapter promoPagerAdapter = new PromoPagerAdapter(getContext(), getChildFragmentManager(), sliders);
        viewPager.setAdapter(promoPagerAdapter);
        viewPager.setVisibility(View.GONE);
        startPagerAutoSwipe();


        //DATA ITEM
        linReferral = v.findViewById(R.id.lin_referral);
        linKomisi = v.findViewById(R.id.linKomisi);
        linPoin = v.findViewById(R.id.linPoin);
        gridView = v.findViewById(R.id.gridView);
        textView19 = v.findViewById(R.id.textView19);
        tvKomisi = v.findViewById(R.id.textView17);
        tvPoin = v.findViewById(R.id.textView21);
        linFlash = v.findViewById(R.id.linFlash);
        linFlash.setVisibility(View.GONE);
        gridViewKategori = v.findViewById(R.id.gridViewKategori);
        gridViewKategori.setExpanded(true);


        core = new Core(user);
        getFlash();
        core.getSliders(new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                ArrayList<Slider> sliders1 = data.getParcelableArrayListExtra("sliders");
                if (sliders1.size() > 0) {
                    for (Slider slider : sliders1) {
                        sliders.add(slider);
                    }
                    promoPagerAdapter.notifyDataSetChanged();
                    viewPager.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(Intent data) {

            }
        });
        core.getBalance(user.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                int balance = data.getIntExtra("balance", 0);
                komisi = balance;
                tvKomisi.setText("Rp " + balance);
            }

            @Override
            public void onFailed(Intent data) {

            }
        });
        core.getPoin(user.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                poin = data.getIntExtra("poin", 0);
                tvPoin.setText(String.valueOf(poin));
            }

            @Override
            public void onFailed(Intent data) {

            }
        });
        getItems20();
        linReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReferralActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                intent.putExtra("bundle", bundle);
                getActivity().startActivity(intent);
            }
        });

        linKomisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), KomisiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                bundle.putInt("balance", komisi);
                intent.putExtra("bundle", bundle);
                getActivity().startActivity(intent);
            }
        });
        linPoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PoinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                bundle.putInt("poin", poin);
                intent.putExtra("bundle", bundle);
                getActivity().startActivity(intent);
            }
        });
        //FLASH SALE
        RecyclerView myRecyclerView = v.findViewById(R.id.cardView);
        myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getContext());
        myLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (gridArray.size() > 0 & myRecyclerView != null) {
            myRecyclerView.setAdapter(new FlashSaleGridAdapter(gridArray, getContext()));
        }
        myRecyclerView.setLayoutManager(myLayoutManager);


        //Referreal / Downline
        getDownline();
        //CATEGORY
        getKategories();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void getDownline() {
        if (user != null) {
            tvKomisi.setText("Rp. " + user.getBalance());
            tvPoin.setText(String.valueOf(user.getPoin()));
            Log.d(Common.TAG, "Get downline user id " + user.getId());
            Call<GetDownline> downlineCall = apiInterface.getDownline(user.getId());

            downlineCall.enqueue(new Callback<GetDownline>() {
                @Override
                public void onResponse(Call<GetDownline> call, Response<GetDownline> response) {
                    if (response.isSuccessful()) {
                        Common.logD("Jumlah Downline" + response.body().getJumlahDownline());
                        textView19.setText(String.valueOf(response.body().getJumlahDownline()));
                    } else {
                        Common.logE("Request gagal " + response.message());
                    }

                }

                @Override
                public void onFailure(Call<GetDownline> call, Throwable t) {
                    Common.logE("Failure " + t.getMessage());
                }
            });
        }


    }

    private ArrayList<Kategori> getKategories() {
        final ArrayList<Kategori> kategoris = new ArrayList<>();
        Call<GetCategory> kategoriCall = apiInterface.getCategory();
        kategoriCall.enqueue(new Callback<GetCategory>() {
            @Override
            public void onResponse(Call<GetCategory> call, Response<GetCategory> response) {
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, response.body().getStatus() + " " + response.body().getKategoris().size());
                    if (response.body().getKategoris() != null) {
                        for (Kategori kategori : response.body().getKategoris()) {
                            kategoris.add(kategori);
                            gridKategoriArray.add(kategori);
                        }
                        KategoriGridviewAdapter kategoriGridAdapter = new KategoriGridviewAdapter(getContext(), R.layout.row_grid_kategori, gridKategoriArray);
                        gridViewKategori.setAdapter(kategoriGridAdapter);
                        gridViewKategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(getActivity(), ItemsInCategoryActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("user", user);
                                bundle.putInt("categoryId", kategoris.get(i).getId());
                                bundle.putString("categoryName", kategoris.get(i).getNama());
                                intent.putExtra("bundle", bundle);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetCategory> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
            }
        });
        return kategoris;
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


    private void startPagerAutoSwipe() {
        update = new Runnable() {
            public void run() {
                if (!touched) {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    if (currentPage < NUM_PAGES)
                        viewPager.setCurrentItem(currentPage++, true);
                }
            }
        };
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 10, 3000);
    }

    private void getItems20() {
        Call<GetItems> flashSaleCall = apiInterface.getItems20();
        flashSaleCall.enqueue(new Callback<GetItems>() {
            @Override
            public void onResponse(Call<GetItems> call, Response<GetItems> response) {
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, response.body().getStatus() + " " + response.body().getItems().size());
                    if (response.body().getItems() != null) {
                        for (int i = 0; i < response.body().getItems().size(); i++) {
                            Item item = response.body().getItems().get(i);
                            item.setImageResId(R.drawable.no_image);
                            gridArray.add(item);
                        }
                        gridView.setExpanded(true);
                        ItemGridviewAdapter customGridAdapter = new ItemGridviewAdapter(getActivity(), R.layout.row_grid, gridArray, user, new Core.OnCallCompleteListener() {
                            @Override
                            public void onSuccess(Intent data) {
                                int code = data.getIntExtra("ACTION_CODE", 0);
                                if (code == Common.ACTION_LIKE) {
//                                    ((MainBottomNavigationActivity) getActivity()).getSupportActionBar().setTitle("Favorit");
//                                    ((MainBottomNavigationActivity) getActivity()).replaceFragment(WishListFragment.newInstance("",user));
                                }
                                if (code == Common.ACTION_CART) {
                                    Log.d(Common.TAG, "success add to chart");
                                    ((MainBottomNavigationActivity) getActivity()).getSupportActionBar().setTitle("Cart");
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
                        gridView.setAdapter(customGridAdapter);
                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetItems> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
            }
        });
    }

    private void getFlash() {
        Call<GetPromo> flashSaleCall = apiInterface.getFlashSale();
        flashSaleCall.enqueue(new Callback<GetPromo>() {
            @Override
            public void onResponse(Call<GetPromo> call, Response<GetPromo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getPromos().size() > 0) {
                        Promo flashPromo = response.body().getPromos().get(0);
                        getFlashItems(flashPromo);
                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetPromo> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
            }
        });
    }

    private void getFlashItems(Promo flashPromo) {
        Call<GetPromoItem> flashSaleCall = apiInterface.getFlashSaleItems(flashPromo.getId());
        flashSaleCall.enqueue(new Callback<GetPromoItem>() {
            @Override
            public void onResponse(Call<GetPromoItem> call, Response<GetPromoItem> response) {
                if (response.isSuccessful()) {
                    if (response.body().getPromoItems().size() > 0) {
                        linFlash.setVisibility(View.VISIBLE);
                        for (Item item : response.body().getPromoItems()) {
                            gridArray.add(item);
                        }
                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetPromoItem> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
            }
        });
    }
}
