package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

    @SerializedName("id_product")
    int id;

    @SerializedName("product_name")
    String nama;

    @SerializedName("selling_price")
    int harga;

    @SerializedName("id_product_category")
    int categoryId;

    @SerializedName("description")
    String description;

    @SerializedName("short_description")
    String shortDescription;

    @SerializedName("stock")
    int stock;

    @SerializedName("id_unit")
    int idUnit;

    @SerializedName("sku")
    String sku;

    @SerializedName("discount")
    int discount;

    @SerializedName("views")
    int views;

    @SerializedName("slug")
    String slug;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @SerializedName("quantity")
    int quantity;

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    @SerializedName("photo_file_name")
    String photoFileName;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    boolean isLiked = false;


    int imageResId = 0;


    public Item(String nama, int harga, int resIcon) {
        this.nama = nama;
        this.harga = harga;
        this.imageResId = resIcon;
    }

    protected Item(Parcel in) {
        id = in.readInt();
        nama = in.readString();
        harga = in.readInt();
        categoryId = in.readInt();
        description = in.readString();
        shortDescription = in.readString();
        stock = in.readInt();
        idUnit = in.readInt();
        sku = in.readString();
        discount = in.readInt();
        views = in.readInt();
        slug = in.readString();
        imageResId = in.readInt();
        isLiked = in.readInt() != 0;
        photoFileName = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nama);
        parcel.writeInt(harga);
        parcel.writeInt(categoryId);
        parcel.writeString(description);
        parcel.writeString(shortDescription);
        parcel.writeInt(stock);
        parcel.writeInt(idUnit);
        parcel.writeString(sku);
        parcel.writeInt(discount);
        parcel.writeInt(views);
        parcel.writeString(slug);
        parcel.writeInt(imageResId);
        parcel.writeByte((byte) (isLiked ? 1 : 0));
        parcel.writeString(photoFileName);
        parcel.writeInt(quantity);
    }
}
