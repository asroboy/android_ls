package com.lapaksembako.app.action;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.lapaksembako.app.R;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetAddress;
import com.lapaksembako.app.api_model.GetBalance;
import com.lapaksembako.app.api_model.GetBalanceHistory;
import com.lapaksembako.app.api_model.GetBalanceSetting;
import com.lapaksembako.app.api_model.GetCart;
import com.lapaksembako.app.api_model.GetDelivery;
import com.lapaksembako.app.api_model.GetItemIsLike;
import com.lapaksembako.app.api_model.GetItems;
import com.lapaksembako.app.api_model.GetOneCategory;
import com.lapaksembako.app.api_model.GetOtherPage;
import com.lapaksembako.app.api_model.GetPhotos;
import com.lapaksembako.app.api_model.GetPoin;
import com.lapaksembako.app.api_model.GetPoinHistory;
import com.lapaksembako.app.api_model.GetReferral;
import com.lapaksembako.app.api_model.GetSliders;
import com.lapaksembako.app.api_model.GetTransaction;
import com.lapaksembako.app.api_model.PostCart;
import com.lapaksembako.app.api_model.PostResult;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.Balance;
import com.lapaksembako.app.model.Cart;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.Poin;
import com.lapaksembako.app.model.ProductPhoto;
import com.lapaksembako.app.model.Slider;
import com.lapaksembako.app.model.Transaction;
import com.lapaksembako.app.model.TransactionDetail;
import com.lapaksembako.app.model.TransferProof;
import com.lapaksembako.app.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class Core {

    User user;
    ApiInterface apiInterface;
    Cart cart;


    public interface OnCallCompleteListener {
        void onSuccess(Intent data);

        void onFailed(Intent data);
    }

    public Core() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public Core(User user) {
        this.user = user;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }


    public void getAddress(final OnCallCompleteListener listener) {
        final ArrayList<Address> addresses = new ArrayList<>();
        Call<GetAddress> getAddressCall = apiInterface.getListAdddress(user.getId());
        getAddressCall.enqueue(new Callback<GetAddress>() {
            @Override
            public void onResponse(Call<GetAddress> call, Response<GetAddress> response) {
                if (response.body().getAddress().size() > 0) {
                    for (Address address : response.body().getAddress()) {
                        addresses.add(address);
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("alamats", addresses);
                        listener.onSuccess(intent);
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("message", "Problem occured");
                    intent.putExtra("status", "failed");
                    listener.onFailed(intent);
                }
            }

            @Override
            public void onFailure(Call<GetAddress> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                Intent intent = new Intent();
                intent.putExtra("message", t.getMessage());
                intent.putExtra("status", "failed");
                listener.onFailed(intent);
            }
        });
    }

    public void addToCart(final Item item, final int qty, final boolean isGrosir, final OnCallCompleteListener listener) {
        checkAvailableCart(isGrosir, new OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                if (getCart() == null) {
                    Log.d(Common.TAG, "cart is null");
                    Log.d(Common.TAG, "Creating cart");
                    Call<PostCart> postCartCall = apiInterface.postCart(user.getId(), isGrosir);
                    Log.d(Common.TAG, bodyToString(postCartCall.request().body()));
                    postCartCall.enqueue(new Callback<PostCart>() {
                        @Override
                        public void onResponse(Call<PostCart> call, Response<PostCart> response) {
                            if (response.body().getCartId() > 0) {
                                int idCart = response.body().getCartId();
                                postItemCart(idCart, item, qty, listener);
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra("message", response.body().getMessage());
                                intent.putExtra("status", "failed");
                                intent.putExtra("ACTION_CODE", Common.ACTION_CART);
                                listener.onFailed(intent);
                            }

                        }

                        @Override
                        public void onFailure(Call<PostCart> call, Throwable t) {
                            Log.d(Common.TAG, "Failure " + t.getMessage());
                            Intent intent = new Intent();
                            intent.putExtra("message", t.getMessage());
                            intent.putExtra("status", "failed");
                            intent.putExtra("ACTION_CODE", Common.ACTION_CART);
                            listener.onFailed(intent);
                        }
                    });
                } else {
                    Log.d(Common.TAG, "cart is exist");
                    int idCart = getCart().getId();
                    postItemCart(idCart, item, qty, listener);
                }
            }

            @Override
            public void onFailed(Intent data) {
                listener.onFailed(new Intent());
            }
        });

    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private void postItemCart(int idCart, @NonNull Item item, int qty, final OnCallCompleteListener listener) {
        int total = qty * item.getHarga();
        Call<PostCart> postCartCall = apiInterface.postItemToCart(idCart, item.getId(), item.getHarga(), qty, total);
        Log.d(Common.TAG, "body : " + bodyToString(postCartCall.request().body()));
        postCartCall.enqueue(new Callback<PostCart>() {
            @Override
            public void onResponse(Call<PostCart> call, Response<PostCart> response) {
                if (response.body().getCartId() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("status", "success");
                    intent.putExtra("message", response.body().getMessage());
                    intent.putExtra("ACTION_CODE", Common.ACTION_CART);
                    listener.onSuccess(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("message", response.body().getMessage());
                    intent.putExtra("status", "failed");
                    intent.putExtra("ACTION_CODE", Common.ACTION_CART);
                    listener.onFailed(intent);
                }
            }

            @Override
            public void onFailure(Call<PostCart> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                Intent intent = new Intent();
                intent.putExtra("message", t.getMessage());
                intent.putExtra("status", "failed");
                intent.putExtra("ACTION_CODE", Common.ACTION_CART);
                listener.onFailed(intent);
            }
        });
    }

    public void checkAvailableCart(boolean isGrosir, final OnCallCompleteListener listener) {
        Log.d(Common.TAG, "Checking available cart");
        Call<GetCart> getCartCall = apiInterface.getCart(user.getId(), isGrosir);
        getCartCall.enqueue(new Callback<GetCart>() {
            @Override
            public void onResponse(Call<GetCart> call, Response<GetCart> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getCarts() != null) {
                            Intent intent = new Intent();
                            if (response.body().getCarts().size() > 0) {
                                Log.d(Common.TAG, "Cart added " + response.body().getCarts().get(0).getId());
                                setCart(response.body().getCarts().get(0));
                                intent.putExtra("cart_id", response.body().getCarts().get(0).getId());
                                listener.onSuccess(intent);
                            } else {
                                listener.onSuccess(intent);
                            }
                        }
                    }
                } else {
                    listener.onFailed(new Intent());
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetCart> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });

    }

    public Cart getCart() {
        return cart;
    }

    public ArrayList<Item> getItemsCart(User user, boolean isWholeSale, final OnCallCompleteListener listener) {
        final ArrayList<Item> items = new ArrayList<>();
        Call<GetItems> getItemsCartCall = apiInterface.getItemsCart(user.getId(), isWholeSale);
        getItemsCartCall.enqueue(new Callback<GetItems>() {
            @Override
            public void onResponse(Call<GetItems> call, Response<GetItems> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent();
                    if (response.body() != null) {
                        if (response.body().getItems() != null) {
                            Log.d(Common.TAG, "Result size : " + response.body().getItems().size());
                            if (response.body().getItems().size() > 0) {
                                for (Item item : response.body().getItems()) {
                                    items.add(item);
                                }

                            }
                        }

                    }

                    intent.putParcelableArrayListExtra("items", items);
                    listener.onSuccess(intent);
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                    listener.onFailed(null);
                }

            }

            @Override
            public void onFailure(Call<GetItems> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(null);
            }
        });
        return items;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void updateQty(int qty, int idCart, int productId) {
        Call<PostResult> postUpdateQty = apiInterface.updateQty(idCart, productId, qty);
        Log.d(Common.TAG, "body : " + bodyToString(postUpdateQty.request().body()));
        postUpdateQty.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {

            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());

            }
        });
    }

    /**
     * Like an item product
     *
     * @param userId
     * @param prooductId
     * @param listener
     */
    public void like(int userId, int prooductId, final OnCallCompleteListener listener) {
        Call<PostResult> postLike = apiInterface.like(prooductId, userId);
        Log.d(Common.TAG, "Menyukai item");
        Log.d(Common.TAG, "body : " + bodyToString(postLike.request().body()));
        postLike.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Intent intent = new Intent();
                intent.putExtra("ACTION_CODE", Common.ACTION_LIKE);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    /**
     * Dislike an item product
     *
     * @param userId
     * @param prooductId
     * @param listener
     */
    public void dislike(int userId, final int prooductId, final OnCallCompleteListener listener) {
        Call<PostResult> postLike = apiInterface.dislike(prooductId, userId);
//        Log.d(Common.TAG, "body : " + bodyToString(postLike.request().body()));
        postLike.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Intent intent = new Intent();
                intent.putExtra("ACTION_CODE", Common.ACTION_DISLIKE);
                intent.putExtra("product_id", prooductId);
                listener.onSuccess(new Intent(intent));
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void getWishList(int userId, final OnCallCompleteListener listener) {
        Call<GetItems> getWishList = apiInterface.getWishList(userId);
        Log.d(Common.TAG, "body : " + bodyToString(getWishList.request().body()));
        getWishList.enqueue(new Callback<GetItems>() {
            @Override
            public void onResponse(Call<GetItems> call, Response<GetItems> response) {
                Log.d(Common.TAG, "response : " + response.raw());
                ArrayList<Item> products = new ArrayList<>();
                if (response.body() != null) {
                    for (Item item : response.body().getItems()) {
                        products.add(item);
                    }
                }
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("items", products);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetItems> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void isItemLiked(int userId, int productId, final OnCallCompleteListener listener) {
        Call<GetItemIsLike> getIsItemLiked = apiInterface.isItemLiked(userId, productId);
        getIsItemLiked.enqueue(new Callback<GetItemIsLike>() {
            @Override
            public void onResponse(Call<GetItemIsLike> call, Response<GetItemIsLike> response) {
                Intent intent = new Intent();
                if (response.body() != null) {
                    intent.putExtra("islike", response.body().isLike());
                }
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetItemIsLike> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void getReferral(int userId, final OnCallCompleteListener listener) {
        Call<GetReferral> getIsItemLiked = apiInterface.getReferral(userId);
//        Log.d(Common.TAG, "body : " + bodyToString(getIsItemLiked.request().body()));
        Log.d(Common.TAG, "user id : " + userId);
        getIsItemLiked.enqueue(new Callback<GetReferral>() {
            @Override
            public void onResponse(Call<GetReferral> call, Response<GetReferral> response) {
                Intent intent = new Intent();
                if (response.body() != null) {
                    intent.putExtra("referral_code", response.body().getReferralCode());
                    ArrayList<User> refers = new ArrayList<>();
                    for (User u : response.body().getReferrers()) {
                        refers.add(u);
                    }
                    intent.putParcelableArrayListExtra("referrer", refers);
                }
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetReferral> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void getDefaultAddress(final OnCallCompleteListener listener) {
        Log.d(Common.TAG, "getDefaultAddress for User ID " + user.getId());
        Call<GetAddress> defaultAdddress = apiInterface.getListDefaultAdddress(user.getId());
        defaultAdddress.enqueue(new Callback<GetAddress>() {
            @Override
            public void onResponse(Call<GetAddress> call, Response<GetAddress> response) {
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, response.body().getStatus() + " ALAMATs " + response.body().getAddress().size());
                    if (response.body() != null) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        if (response.body().getAddress().size() > 0) {
                            bundle.putParcelable("alamat", response.body().getAddress().get(0));
                        }
                        intent.putExtra("bundle", bundle);
                        listener.onSuccess(intent);
                    } else {
                        listener.onSuccess(new Intent());
                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                    listener.onSuccess(new Intent());
                }

            }

            @Override
            public void onFailure(Call<GetAddress> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void getBalance(int userId, final OnCallCompleteListener listener) {
        Call<GetBalance> getBalance = apiInterface.getBalances(userId);
        Log.d(Common.TAG, "user id : " + userId);
        getBalance.enqueue(new Callback<GetBalance>() {
            @Override
            public void onResponse(Call<GetBalance> call, Response<GetBalance> response) {
                Intent intent = new Intent();
                if (response.body() != null) {
                    for (Balance u : response.body().getBalances()) {
                        intent.putExtra("balance", u.getNominal());
                    }
                }
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetBalance> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void getDeliveryPrice(final OnCallCompleteListener listener) {
        Call<GetDelivery> getDeliveryPrice = apiInterface.getDeliveryPrice();
        getDeliveryPrice.enqueue(new Callback<GetDelivery>() {
            @Override
            public void onResponse(Call<GetDelivery> call, Response<GetDelivery> response) {
                Intent intent = new Intent();
                if (response.body() != null) {
                    if (response.body().getDeliveryPrices().size() > 0)
                        intent.putExtra("delivery_price", response.body().getDeliveryPrices().get(0).getPrice());
                }

                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetDelivery> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void getSejarahTransaksi(int userId, final OnCallCompleteListener listener) {
        Call<GetBalanceHistory> getBalance = apiInterface.getBalanceHistory(userId);
        Log.d(Common.TAG, "user id : " + userId);
        getBalance.enqueue(new Callback<GetBalanceHistory>() {
            @Override
            public void onResponse(Call<GetBalanceHistory> call, Response<GetBalanceHistory> response) {
                Intent intent = new Intent();
                ArrayList<Balance> balances = new ArrayList<>();
                if (response.body() != null) {
                    for (Balance u : response.body().getBalances()) {
                        balances.add(u);
                    }
                }
                intent.putExtra("total_balance", response.body().getBalance());
                intent.putParcelableArrayListExtra("balances", balances);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetBalanceHistory> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void getPoin(int userId, final OnCallCompleteListener listener) {
        Call<GetPoin> getPoinCall = apiInterface.getPoin(userId);
        Log.d(Common.TAG, "user id : " + userId);
        getPoinCall.enqueue(new Callback<GetPoin>() {
            @Override
            public void onResponse(Call<GetPoin> call, Response<GetPoin> response) {
                Intent intent = new Intent();
                if (response.body() != null) {
                    for (Poin u : response.body().getPoins()) {
                        intent.putExtra("poin", u.getNominal());
                    }
                }
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetPoin> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void getSejarahPoin(int userId, final OnCallCompleteListener listener) {
        Call<GetPoinHistory> getBalance = apiInterface.getPoinHistory(userId);
        Log.d(Common.TAG, "user id : " + userId);
        getBalance.enqueue(new Callback<GetPoinHistory>() {
            @Override
            public void onResponse(Call<GetPoinHistory> call, Response<GetPoinHistory> response) {
                Intent intent = new Intent();
                ArrayList<Poin> poins = new ArrayList<>();
                if (response.body() != null) {
                    for (Poin u : response.body().getPoins()) {
                        poins.add(u);
                    }
                }
                intent.putParcelableArrayListExtra("poins", poins);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetPoinHistory> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void withdraw(int userId, String type, String bankName, String bankAccount,
                         String accountNumber, int nominal,
                         String status, final OnCallCompleteListener listener) {

        Call<PostResult> witdrawCall = apiInterface.withdraw(userId, type, bankName, bankAccount,
                accountNumber, nominal, status);
        Log.d(Common.TAG, "body : " + bodyToString(witdrawCall.request().body()));
        Log.d(Common.TAG, "user id : " + userId);
        witdrawCall.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                listener.onSuccess(new Intent());
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void createTransaction(final Transaction transaction, final OnCallCompleteListener listener) {
        Call<PostResult> cTransaction = apiInterface.createTransaction(transaction);
        Log.d(Common.TAG, "body : " + bodyToString(cTransaction.request().body()));
        cTransaction.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Intent intent = new Intent();
                intent.putExtra("code", transaction.getTransactionCode());
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void createTransactionDetail(final List<TransactionDetail> transactionDetails, final OnCallCompleteListener listener) {
        Call<PostResult> cTransaction = apiInterface.createTransactionDetail(transactionDetails);
        Log.d(Common.TAG, "body : " + bodyToString(cTransaction.request().body()));
        cTransaction.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Intent intent = new Intent();
//                intent.putExtra("code", transaction.getTransactionCode());
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void getPendingTransaction(final OnCallCompleteListener listener) {
        Call<GetTransaction> getTransactionCall = apiInterface.getPendingTransaction(user.getId());
        Log.d(Common.TAG, "user id : " + user.getId());
        getTransactionCall.enqueue(new Callback<GetTransaction>() {
            @Override
            public void onResponse(Call<GetTransaction> call, Response<GetTransaction> response) {
                Intent intent = new Intent();
                ArrayList<Transaction> transactions = new ArrayList<>();
                if (response.body() != null) {
                    for (Transaction transaction : response.body().getTransactions()) {
                        transactions.add(transaction);
                    }

                }

                intent.putParcelableArrayListExtra("transactions", transactions);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetTransaction> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void getProductPhotos(int idProduct, final OnCallCompleteListener listener) {

        Call<GetPhotos> getPhotosCall = apiInterface.getProductPhotos(idProduct);
        Log.d(Common.TAG, "user id : " + user.getId());
        getPhotosCall.enqueue(new Callback<GetPhotos>() {
            @Override
            public void onResponse(Call<GetPhotos> call, Response<GetPhotos> response) {
                Intent intent = new Intent();
                ArrayList<ProductPhoto> productPhotos = new ArrayList<>();
                if (response.body() != null) {
                    for (ProductPhoto productPhoto : response.body().getPhotos()) {
                        productPhotos.add(productPhoto);
                    }
                }

                intent.putParcelableArrayListExtra("photos", productPhotos);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetPhotos> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void getCategoryById(int idCategory, final OnCallCompleteListener listener) {

        Call<GetOneCategory> getPhotosCall = apiInterface.getCategoryById(idCategory);
        Log.d(Common.TAG, "user id : " + user.getId());
        getPhotosCall.enqueue(new Callback<GetOneCategory>() {
            @Override
            public void onResponse(Call<GetOneCategory> call, Response<GetOneCategory> response) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (response.body() != null) {
                    bundle.putParcelable("category", response.body().getKategori());
                }
                intent.putExtra("bundle", bundle);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetOneCategory> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void getSliders(final OnCallCompleteListener listener) {

        Call<GetSliders> getSliderCall = apiInterface.getSliders();
//        Log.d(Common.TAG, "user id : " + ();
        getSliderCall.enqueue(new Callback<GetSliders>() {
            @Override
            public void onResponse(Call<GetSliders> call, Response<GetSliders> response) {
                ArrayList<Slider> sliders = new ArrayList<>();
                if (response.body() != null) {
                    for (Slider slider : response.body().getSliders()) {
                        sliders.add(slider);
                    }
                }
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("sliders", sliders);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetSliders> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void getKebijakanPrivasi(final OnCallCompleteListener listener) {
        Call<GetOtherPage> getKebijakanCall = apiInterface.getSyaratDanKetentuan();
        getKebijakanCall.enqueue(new Callback<GetOtherPage>() {
            @Override
            public void onResponse(Call<GetOtherPage> call, Response<GetOtherPage> response) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (response.body() != null) {
                    bundle.putParcelable("page", response.body().getOtherPage());
                }
                intent.putExtra("bundle", bundle);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetOtherPage> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void getTOS(final OnCallCompleteListener listener) {
        Call<GetOtherPage> getKebijakanCall = apiInterface.getTOS();
        getKebijakanCall.enqueue(new Callback<GetOtherPage>() {
            @Override
            public void onResponse(Call<GetOtherPage> call, Response<GetOtherPage> response) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (response.body() != null) {
                    bundle.putParcelable("page", response.body().getOtherPage());
                }
                intent.putExtra("bundle", bundle);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetOtherPage> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }


    public void uploadTransferProof(String filePath, TransferProof transferProof, final OnCallCompleteListener listener) {
        Log.d(Common.TAG, "FilePath " + filePath);
        File file = new File(filePath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        RequestBody transactionCode = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transferProof.getTransactionCode()));
        RequestBody isMember = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getId()));
        RequestBody bankName = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transferProof.getBankName()));
        RequestBody bankAcccount = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transferProof.getBankAccount()));
        RequestBody accountNumber = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transferProof.getAccountNumber()));
        RequestBody nominal = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transferProof.getNominal()));
        Call<PostResult> callS = apiInterface.buktiTransfer(part, transactionCode,isMember, bankName, bankAcccount, accountNumber, nominal);
        callS.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Log.i(Common.TAG, "Upload Bukti Transfer " + response.raw());
                listener.onSuccess(new Intent());
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.e(Common.TAG, t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void getBalanceSetting(final OnCallCompleteListener listener) {
        Call<GetBalanceSetting> getKebijakanCall = apiInterface.getBalanceSetting();
        getKebijakanCall.enqueue(new Callback<GetBalanceSetting>() {
            @Override
            public void onResponse(Call<GetBalanceSetting> call, Response<GetBalanceSetting> response) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (response.body() != null) {
                    bundle.putParcelable("balance-setting", response.body().getBalanceSetting());
                }
                intent.putExtra("bundle", bundle);
                listener.onSuccess(intent);
            }

            @Override
            public void onFailure(Call<GetBalanceSetting> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void simpanKomisi(Balance balance, final OnCallCompleteListener listener) {
        Call<PostResult> simpanKomisi = apiInterface.simpanKomisi(balance);
        simpanKomisi.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Log.i(Common.TAG, "Simpa komisi " + response.body().getMessage());
                listener.onSuccess(new Intent());
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }

    public void simpanPoin(Poin poin, final OnCallCompleteListener listener) {
        Call<PostResult> simpanPoin = apiInterface.simpanPoin(poin);
        simpanPoin.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Log.i(Common.TAG, "Simpa Poin " + response.body().getMessage());
                listener.onSuccess(new Intent());
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                listener.onFailed(new Intent());
            }
        });
    }
}
