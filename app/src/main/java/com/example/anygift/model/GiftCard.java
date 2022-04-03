package com.example.anygift.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Entity
public class GiftCard {
    @PrimaryKey
    @NonNull
    String id="";
    String cardName;
    double value;
    String expirationDate;
    double wantedPrice;
    private Boolean isDeleted=false;
    private Long lastUpdated;
    private String ownerEmail;
    String latAndLong;

    public GiftCard(String cardName, double value, String expirationDate, double wantedPrice, String ownerEmail ,String latAndLong) {
        this.cardName = cardName;
        this.value = value;
        this.expirationDate = expirationDate;
        this.wantedPrice = wantedPrice;
        this.ownerEmail = ownerEmail;
        this.id=String.valueOf((cardName+ " " +ownerEmail).hashCode());
        this.latAndLong=latAndLong;
    }

    public enum Type{
        GENERAL,
        CLOTHING,
        VACATIONS,
        SPORTS
    }
    private Type cardType;
    String giftCardImageUrl;

    public String getGiftCardImageUrl() {
        return giftCardImageUrl;
    }

    public void setGiftCardImageUrl(String giftCardImageUrl) {
        this.giftCardImageUrl = giftCardImageUrl;
    }

    public GiftCard(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public double getWantedPrice() {
        return wantedPrice;
    }

    public void setWantedPrice(double wantedPrice) {
        this.wantedPrice = wantedPrice;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Type getCardType() {
        return cardType;
    }

    public void setCardType(Type cardType) {
        this.cardType = cardType;
    }

    public String getLatAndLong() {
        return latAndLong;
    }

    public void setLatAndLong(String latAndLong) {
        this.latAndLong = latAndLong;
    }

    public Map<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("cardName", cardName);
        result.put("value",String.valueOf(value));
        result.put("wantedPrice", String.valueOf(wantedPrice));
        result.put("imageUrl", giftCardImageUrl);
        result.put("lastUpdated", FieldValue.serverTimestamp().toString());
        result.put("expirationDate",expirationDate);
        result.put("ownerEmail", ownerEmail);
        result.put("isDeleted", String.valueOf(isDeleted));
        result.put("type", String.valueOf(cardType));
        result.put("latAndLong", latAndLong);
        return result;
    }

    public void fromMap(Map<String, String> map){
        id = (String)map.get("id");
        cardName = (String)map.get("cardName");
        value = new Double(map.get("value"));
        wantedPrice = new Double(map.get("wantedPrice") );
        expirationDate = (String) map.get("expirationDate");
        cardType=  Type.valueOf(map.get("type"));
        ownerEmail = (String)map.get("ownerEmail");
        Timestamp ts = new Timestamp (new Date(map.get("lastUpdated")));
        isDeleted = new Boolean (map.get("isDeleted"));
        lastUpdated = ts.getSeconds();
        giftCardImageUrl = (String)map.get("imageUrl");
        latAndLong=(String)map.get("latAndLong");
        //long time = ts.toDate().getTime();
    }

}
