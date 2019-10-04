package com.lapaksembako.app.model;

import com.google.gson.annotations.SerializedName;

public class TransactionDetail {

    @SerializedName("id_transaction_detail")
    int idTransactionDetail;
    @SerializedName("transaction_code")
    String transactionCode;
    @SerializedName("id_product")
    int idProduct;
    @SerializedName("product_price")
    int productPrice;
    @SerializedName("promo_discount")
    int promoDiscount;
    @SerializedName("voucher_code")
    String voucherCode;
    @SerializedName("vouche_discount")
    int voucheDiscount;
    @SerializedName("quantity")
    int quantity;
    @SerializedName("total_price")
    int totalPrice;
    @SerializedName("created_date")
    String createdDate;
    @SerializedName("updated_date")
    String updatedDate;
    @SerializedName("created_by")
    String createdBy;
    @SerializedName("update_by")
    String updatedBy;

    public int getIdTransactionDetail() {
        return idTransactionDetail;
    }

    public void setIdTransactionDetail(int idTransactionDetail) {
        this.idTransactionDetail = idTransactionDetail;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(int promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public int getVoucheDiscount() {
        return voucheDiscount;
    }

    public void setVoucheDiscount(int voucheDiscount) {
        this.voucheDiscount = voucheDiscount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
