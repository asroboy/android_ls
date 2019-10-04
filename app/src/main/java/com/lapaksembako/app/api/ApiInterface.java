package com.lapaksembako.app.api;

import com.lapaksembako.app.api_model.GetAddress;
import com.lapaksembako.app.api_model.GetAkunBank;
import com.lapaksembako.app.api_model.GetBalance;
import com.lapaksembako.app.api_model.GetBalanceHistory;
import com.lapaksembako.app.api_model.GetBalanceSetting;
import com.lapaksembako.app.api_model.GetCart;
import com.lapaksembako.app.api_model.GetCategory;
import com.lapaksembako.app.api_model.GetCity;
import com.lapaksembako.app.api_model.GetDelivery;
import com.lapaksembako.app.api_model.GetDownline;
import com.lapaksembako.app.api_model.GetFaq;
import com.lapaksembako.app.api_model.GetItemIsLike;
import com.lapaksembako.app.api_model.GetItems;
import com.lapaksembako.app.api_model.GetOneCategory;
import com.lapaksembako.app.api_model.GetOtherPage;
import com.lapaksembako.app.api_model.GetPhotos;
import com.lapaksembako.app.api_model.GetPoin;
import com.lapaksembako.app.api_model.GetPoinHistory;
import com.lapaksembako.app.api_model.GetPromo;
import com.lapaksembako.app.api_model.GetPromoItem;
import com.lapaksembako.app.api_model.GetProvince;
import com.lapaksembako.app.api_model.GetReferral;
import com.lapaksembako.app.api_model.GetSliders;
import com.lapaksembako.app.api_model.GetTransaction;
import com.lapaksembako.app.api_model.GetUserDaftar;
import com.lapaksembako.app.api_model.GetUserLogin;
import com.lapaksembako.app.api_model.PostCart;
import com.lapaksembako.app.api_model.PostResult;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Balance;
import com.lapaksembako.app.model.OtherPage;
import com.lapaksembako.app.model.Poin;
import com.lapaksembako.app.model.Transaction;
import com.lapaksembako.app.model.TransactionDetail;
import com.lapaksembako.app.model.TransferProof;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login?token=" + Common.keyApi)
    Call<GetUserLogin> postUserLogin(@Field("mobile") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST("daftar?token=" + Common.keyApi)
    Call<GetUserDaftar> postUserDaftar(
            @Field("nama") String nama,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("password") String password,
            @Field("referrer_id") String referrerId
    );


    @FormUrlEncoded
    @POST("update_profile?token=" + Common.keyApi)
    Call<GetUserDaftar> postUpdateProfile(
            @Field("nama") String nama,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("user_id") int userId
    );

    @GET("faq?token=" + Common.keyApi)
    Call<GetFaq> getFaq();


    @GET("category?token=" + Common.keyApi)
    Call<GetCategory> getCategory();

    @GET("category/{category_id}?token=" + Common.keyApi)
    Call<GetOneCategory> getCategoryById(@Path("category_id") int idCategory);


    @GET("referral/{user_id}?token=" + Common.keyApi)
    Call<GetDownline> getDownline(@Path("user_id") int userId);


    @GET("flash_sale?token=" + Common.keyApi)
    Call<GetPromo> getFlashSale();

    @GET("flash_sale_items/{flash_id}?token=" + Common.keyApi)
    Call<GetPromoItem> getFlashSaleItems(@Path("flash_id") int flashId);

    @GET("list_address/{user_id}?token=" + Common.keyApi)
    Call<GetAddress> getListAdddress(@Path("user_id") int userId);

    @GET("default_address/{user_id}?token=" + Common.keyApi)
    Call<GetAddress> getListDefaultAdddress(@Path("user_id") int userId);


    @GET("list_province?token=" + Common.keyApi)
    Call<GetProvince> getListProvince();

    @GET("list_city/{province_id}?token=" + Common.keyApi)
    Call<GetCity> getListCity(@Path("province_id") int idProvince);

    @GET("akun_bank/{user_id}?token=" + Common.keyApi)
    Call<GetAkunBank> getAkunBank(@Path("user_id") int userId);


    @FormUrlEncoded
    @POST("simpan_akun_bank?token=" + Common.keyApi)
    Call<PostResult> postSimpanAkunBank(
            @Field("bank_name") String bankName,
            @Field("bank_account") String bankAccount,
            @Field("account_number") String accountNumber,
            @Field("id_member") int userId
    );


    @FormUrlEncoded
    @POST("simpan_alamat?token=" + Common.keyApi)
    Call<PostResult> postSimpanAlamat(
            @Field("id_province") int idProvince,
            @Field("id_city") int idCity,
            @Field("address") String address,
            @Field("postal_code") String postalCode,
            @Field("is_default") String isDefault,
            @Field("user_id") int userId
    );


    @GET("list_items/{category_id}?token=" + Common.keyApi)
    Call<GetItems> getItemsInCategory(@Path("category_id") int categoryId);

    @GET("list_items_20?token=" + Common.keyApi)
    Call<GetItems> getItems20();

    @Multipart
    @POST("image_profile?token=" + Common.keyApi)
    Call<PostResult> uploadImageProfile(@Part MultipartBody.Part file, @Part("user_id") RequestBody requestBody);

    @GET("ambil_item_keranjang?token=" + Common.keyApi)
    Call<GetItems> getItemsCart(@Query("user_id") int userId, @Query("wholesale_status") boolean isGrosir);

    @GET("periksa_keranjang?token=" + Common.keyApi)
    Call<GetCart> getCart(@Query("user_id") int userId, @Query("wholesale_status") boolean isGrosir);

    @POST("tambah_keranjang?token=" + Common.keyApi)
    @FormUrlEncoded
    Call<PostCart> postCart(@Field("user_id") int userId, @Field("wholesale_status") boolean isGrosir);

    @FormUrlEncoded
    @POST("tambah_item_ke_cart?token=" + Common.keyApi)
    Call<PostCart> postItemToCart(@Field("id_cart") int idCart, @Field("id_product") int idProduct, @Field("product_price") int price, @Field("quantity") int qty, @Field("total_price") int total);

    @FormUrlEncoded
    @POST("hapus_item_darie_cart?token=" + Common.keyApi)
    Call<PostCart> deleteItemFromCart(@Field("id_cart") int idCart, @Field("id_product") int idProduct);

    @FormUrlEncoded
    @POST("update_qty?token=" + Common.keyApi)
    Call<PostResult> updateQty(@Field("id_cart") int idCart, @Field("id_product") int idProduct, @Field("quantity") int qty);

    @FormUrlEncoded
    @POST("suka?token=" + Common.keyApi)
    Call<PostResult> like(@Field("id_product") int idProduct, @Field("id_member") int idMember);

    @FormUrlEncoded
    @POST("tidak_suka?token=" + Common.keyApi)
    Call<PostResult> dislike(@Field("id_product") int idProduct, @Field("id_member") int idMember);

    @GET("item_disukai/{user_id}?token=" + Common.keyApi)
    Call<GetItems> getWishList(@Path("user_id") int userId);

    @GET("is_item_disukai?token=" + Common.keyApi)
    Call<GetItemIsLike> isItemLiked(@Query("user_id") int userId, @Query("id_product") int productId);


    @GET("get_referral?token=" + Common.keyApi)
    Call<GetReferral> getReferral(@Query("user_id") int userId);

    @GET("get_balance?token=" + Common.keyApi)
    Call<GetBalance> getBalances(@Query("user_id") int userId);

    @GET("get_history_balance?token=" + Common.keyApi)
    Call<GetBalanceHistory> getBalanceHistory(@Query("user_id") int userId);

    @GET("get_poin?token=" + Common.keyApi)
    Call<GetPoin> getPoin(@Query("user_id") int userId);

    @GET("get_history_poin?token=" + Common.keyApi)
    Call<GetPoinHistory> getPoinHistory(@Query("user_id") int userId);

    @GET("get_delivery_price?token=" + Common.keyApi)
    Call<GetDelivery> getDeliveryPrice();

    @FormUrlEncoded
    @POST("withdraw?token=" + Common.keyApi)
    Call<PostResult> withdraw(@Field("id_member") int idMember, @Field("m_type") String type,
                              @Field("bank_name") String bankName, @Field("bank_account") String bankAccount,
                              @Field("account_number") String accountNumber, @Field("nominal") int nominal,
                              @Field("status") String status);


    @POST("create_transaction?token=" + Common.keyApi)
    Call<PostResult> createTransaction(@Body Transaction transaction);

    @POST("create_transaction_detail?token=" + Common.keyApi)
    Call<PostResult> createTransactionDetail(@Body List<TransactionDetail> transactionDetailList);


    @GET("get_pending_transaction?token=" + Common.keyApi)
    Call<GetTransaction> getPendingTransaction(@Query("user_id") int userId);

    @GET("get_image_items?token=" + Common.keyApi)
    Call<GetPhotos> getProductPhotos(@Query("id_product") int idProduct);

    @GET("get_slider?token=" + Common.keyApi)
    Call<GetSliders> getSliders();

    @GET("get_syarat_dan_ketentuan?token=" + Common.keyApi)
    Call<GetOtherPage> getSyaratDanKetentuan();


    @GET("get_tos?token=" + Common.keyApi)
    Call<GetOtherPage> getTOS();


    @GET("get_balance_setting?token=" + Common.keyApi)
    Call<GetBalanceSetting> getBalanceSetting();


    @Multipart
    @POST("bukti_transfer?token=" + Common.keyApi)
    Call<PostResult> buktiTransfer(@Part MultipartBody.Part file, @Part("transaction_code") RequestBody transaction_code,
                                   @Part("id_member") RequestBody id_member,@Part("bank_name") RequestBody bank_name,
                                   @Part("bank_account") RequestBody bank_account,@Part("account_number") RequestBody account_number,
                                   @Part("nominal") RequestBody nominal);


    @POST("komisi?token=" + Common.keyApi)
    Call<PostResult> simpanKomisi(@Body Balance balance);


    @POST("poin?token=" + Common.keyApi)
    Call<PostResult> simpanPoin(@Body Poin poin);

}
