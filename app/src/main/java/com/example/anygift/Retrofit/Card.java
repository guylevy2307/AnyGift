package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Card {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("value")
    @Expose
    private Double value;
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("expirationDate")
    @Expose
    private Long expirationDate;
    @SerializedName("lastUpdate")
    @Expose
    private String lastUpdate;
    @SerializedName("cardType")
    @Expose
    private String cardType;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("isForSale")
    @Expose
    private Boolean isForSale;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("calculatedPrice")
    @Expose
    private Double calculatedPrice;
    @SerializedName("precentageSaved")
    @Expose
    private String precentageSaved;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getIsForSale() {
        return isForSale;
    }

    public void setIsForSale(Boolean isForSale) {
        this.isForSale = isForSale;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(Double calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public String getPrecentageSaved() {
        return precentageSaved;
    }

    public void setPrecentageSaved(String precentageSaved) {
        this.precentageSaved = precentageSaved;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public static HashMap<String, Object> mapToAddCard(Double price, Double value, String cardNumber,
                                                       String cardType, String owner, boolean isForSale,
                                                       Long expirationDate) {
        return new HashMap<String, Object>() {{
            put("price", price);
            put("value", value);
            put("cardNumber", cardNumber);
            put("cardType", cardType);
            put("owner", owner);
            put("isForSale", isForSale);
            put("expirationDate", expirationDate);
        }};
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", value=" + value +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate=" + expirationDate +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", cardType='" + cardType + '\'' +
                ", owner='" + owner + '\'' +
                ", isForSale=" + isForSale +
                ", createdAt='" + createdAt + '\'' +
                ", calculatedPrice=" + calculatedPrice +
                ", precentageSaved='" + precentageSaved + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}