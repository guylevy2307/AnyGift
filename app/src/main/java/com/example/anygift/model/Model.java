package com.example.anygift.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.anygift.MyApplication;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.Retrofit.Income;
import com.example.anygift.Retrofit.Outcome;
import com.example.anygift.Retrofit.UploadImageResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Model {
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public com.example.anygift.Retrofit.User signedUser;
    public ModelRetrofit modelRetrofit = new ModelRetrofit();
    public MutableLiveData<List<GiftCard>> giftCardsList = new MutableLiveData<>();
    public List<Category> categories = new ArrayList<>();
    public List<CardType> cardTypes = new ArrayList<>();

    private Model() {
        signedUser = new com.example.anygift.Retrofit.User();

        ListLoadingState.setValue(GiftListLoadingState.loaded);
    }

    public void setCardTypes() {
        getAllCardTypes(new cardTypesReturnListener() {
            @Override
            public void onComplete(List<CardType> cts) {
                for (CardType ct : cts) {
                    downloadImage(ct.getId() + ".jpeg", new byteArrayReturnListener() {
                        @Override
                        public void onComplete(Bitmap bitmap) {
                            ct.setPicture(bitmap);
                            cardTypes.add(ct);
                        }
                    });
                }
            }
        });
    }

    //Node- Retrofit
    public interface StringListener {
        void onComplete(String message);
    }

    public interface userLoginListener {
        void onComplete(com.example.anygift.Retrofit.User user, String message);
    }

    public interface userReturnListener {
        void onComplete(com.example.anygift.Retrofit.User user, String message);
    }

    public interface cardReturnListener {
        void onComplete(Card card, String message);
    }

    public interface cardsReturnListener {
        void onComplete(List<Card> cards, String message);
    }
    public interface cardsListener{
        void onComplete(List<Card> cards);
    }

    public interface uploadImageListener {
        void onComplete(UploadImageResult uploadImageResult);
    }

    public interface cardTypesReturnListener {
        void onComplete(List<CardType> cts);
    }

    public interface categoriesReturnListener {
        void onComplete(List<Category> cat);
    }

    public interface incomeListener {
        void onComplete(Income income);
    }

    public interface outComeListener {
        void onComplete(Outcome outcome);
    }

    public interface coinTransactionListener {
        void onComplete(String message);
    }

    public interface booleanReturnListener {
        void onComplete(Boolean result, String message);
    }

    public interface byteArrayReturnListener {
        void onComplete(Bitmap bitmap);
    }

    //NO AUTHENTICAION
    public void login(HashMap<String, Object> map, userLoginListener listener) {
        modelRetrofit.login(map, listener);
    }

    public void logout(StringListener listener) {
        modelRetrofit.logout(listener);
    }

    public void downloadImage(String image, byteArrayReturnListener l) {
        modelRetrofit.downloadImage(image, l);
    }

    public void getUserRetrofit(String user_id, userReturnListener listener) {
        modelRetrofit.getUser(user_id, listener);
    }

    public void uploadImage(byte[] imageBytes, uploadImageListener listener) {
        modelRetrofit.uploadImage(imageBytes, listener);
    }

    public void getAllCategories(categoriesReturnListener l) {
        modelRetrofit.getAllCategories(l);
    }

    public void getAllCardTypes(cardTypesReturnListener l) {
        modelRetrofit.getAllCardTypes(l);
    }

    public void getUserIncome(incomeListener listener) {
        modelRetrofit.getUserIncome(listener);
    }

    public void getUserOutCome(outComeListener listener) {
        modelRetrofit.getUserOutCome(listener);
    }

    //NO AUTHENTICAION
    public void addUserRetrofit(HashMap<String, Object> map, userReturnListener listener) {
        modelRetrofit.addUser(map, listener);
    }

    public void addCoinTransaction(HashMap<String, Object> map, coinTransactionListener listener) {
        modelRetrofit.addCoinTransaction(map, listener);
    }

    public void updateUser(HashMap<String, Object> map, userLoginListener listener) {
        modelRetrofit.updateUser(map, listener);
    }

    public void addCardRetrofit(HashMap<String, Object> map, cardReturnListener listener) {
        modelRetrofit.addCard(map, listener);
    }

    public void getAllCards(cardsReturnListener listener) {
        modelRetrofit.getAllCards(listener);
    }

    public void getAllUserCards(cardsReturnListener listener) {
        modelRetrofit.getAllUserCards(listener);
    }

    public void getAllFeedCardsForSale(cardsReturnListener listener) {
        modelRetrofit.getAllCards(new cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                String user_id = modelRetrofit.getUserId();
                System.out.println("size before: " + cards.size());
                List<Card> cs = cards.stream().filter(c -> c.getIsForSale() && !c.getOwner().equals(user_id)).collect(Collectors.toList());
                System.out.println("size after: " + cards.size());
                listener.onComplete(cs, "Cards filtered");
            }
        });
    }

    public interface mapStringToCardsArrayListener {
        void onComplete(HashMap<String, ArrayList<Card>> map);
    }

    public void getallcardsByCardType(mapStringToCardsArrayListener listener) {
        getAllFeedCardsForSale(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                System.out.println(message);
                System.out.println(cards);
                Model.instance.modelRetrofit.getAllCardTypes(new Model.cardTypesReturnListener() {
                    @Override
                    public void onComplete(List<CardType> cts) {
                        if (cts != null) {
                            HashMap<String, ArrayList<Card>> map = new HashMap<>();
                            for (CardType c : cts) {
                                map.put(c.getId(), new ArrayList<>());
                            }
                            for (Card c : cards) {
                                ArrayList<Card> arrayList = map.get(c.getCardType());
                                arrayList.add(c);
                                map.put(c.getCardType(), arrayList);
                            }
                            for (CardType c : cts) {
                                map.put(c.getName(), map.get(c.getId()));
                                map.remove(c.getId());
                            }
                            System.out.println(map);
                            listener.onComplete(map);
                        }
                    }
                });
            }
        });
    }

    public interface CardsListListener {
        void onComplete(List<Card> cards);
    }

    public void updateCardRetrofit(String card_id, HashMap<String, Object> map, cardReturnListener listener) {
        modelRetrofit.updateCard(card_id, map, listener);
    }

    public void getCardRetrofit(String card_id, cardReturnListener listener) {
        modelRetrofit.getCard(card_id, listener);
    }


    public void deleteCardRetrofit(String card_id, cardReturnListener listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isDeleted", true);
        HashMap<String, Object> m = Card.mapToUpdateCard(card_id, map);
        updateCardRetrofit(card_id, m, listener);
    }

    public void addCoinsToUser(String user_id, double coinsToAdd, userReturnListener listener) {
        HashMap<String, Double> map = new HashMap<String, Double>();
        map.put("coins", coinsToAdd);
        modelRetrofit.addCoinsToUser(user_id, map, listener);

    }


    //Firebase
    public enum GiftListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<GiftListLoadingState> ListLoadingState = new MutableLiveData<GiftListLoadingState>();

    public MutableLiveData<GiftListLoadingState> getListLoadingState() {
        return ListLoadingState;
    }

    public interface VoidListener {
        void onComplete();
    }


    public interface AddGiftCardListener {
        void onComplete();
    }

    /**
     * Authentication
     */
    public void isTokenValid(booleanReturnListener listener) {

        if (modelRetrofit.getAccessToken().isEmpty())
            listener.onComplete(false, "Empty Token");
        else
            modelRetrofit.refreshToken(message -> {
                System.out.println(message);
                modelRetrofit.authenticateToken(listener);
            });

    }

    public com.example.anygift.Retrofit.User getSignedUser() {
        return signedUser;
    }

    public void setCurrentUser(com.example.anygift.Retrofit.User user) {
        signedUser = user;
    }


    /**
     * search cards
     */
    public void searchCards(int day,int month,int year,String price,String cardTypeId,cardsListener listener){
        List<Card> result = new ArrayList<>();
        getAllCards(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                Log.d("TAG", "maxPrice: "+price);
                Log.d("TAG", "onDateSet: "+day+"/"+month+"/"+year);
                if(price.isEmpty()){
                    if(day==0){//filter by spinner only
                        result.addAll(cards.stream().filter(c->c.getCardType().equals(cardTypeId)).collect(Collectors.toList()));
                        Log.d("TAG", "filter by type only");
                    }else{
                        if(cardTypeId.equals("Any")) {//spinner is empty, filter by date only
                            result.addAll(cards.stream().filter(c->c.getExpirationDate() <= Utils.convertDateToLong(Integer.toString(day),Integer.toString(month),Integer.toString(year)).longValue()).collect(Collectors.toList()));
                            Log.d("TAG", "filter by date only");
                        }else {
                            //filter by date & typeSpinner
                            result.addAll(cards.stream().filter(c->c.getExpirationDate() <= Utils.convertDateToLong(Integer.toString(day),Integer.toString(month),Integer.toString(year)).longValue()
                                    && c.getCardType().equals(cardTypeId)).collect(Collectors.toList()));
                            Log.d("TAG", "filter by type spinner and date only");
                        }
                    }
                }else{
                    if(day==0){
                        if(cardTypeId.equals("Any")){//spinner  and date are empty, filter by price only
                            result.addAll(cards.stream().filter(c->c.getCalculatedPrice()<=Double.valueOf(price)).collect(Collectors.toList()));
                            Log.d("TAG", "filter by price only");
                        }else{
                            //filter by maxPrice & typeSpinner
                            result.addAll(cards.stream().filter(c->c.getCardType().equals(cardTypeId) && c.getCalculatedPrice()<=Double.valueOf(price)).collect(Collectors.toList()));
                            Log.d("TAG", "filter by maxPrice & typeSpinner");
                        }
                    }else{
                        if(cardTypeId.equals("Any")){//spinner is empty, filter by price & date
                            result.addAll(cards.stream().filter(c->c.getExpirationDate() <= Utils.convertDateToLong(Integer.toString(day),Integer.toString(month),Integer.toString(year)).longValue()
                                    && c.getCalculatedPrice()<=Double.valueOf(price)).collect(Collectors.toList()));
                            Log.d("TAG", "filter by date and price");
                        }else {
                            //filter by maxPrice & typeSpinner & date
                            result.addAll(cards.stream().filter(c->c.getCardType().equals(cardTypeId) && c.getCalculatedPrice()<=Double.valueOf(price)
                                    && c.getExpirationDate() <= Utils.convertDateToLong(Integer.toString(day),Integer.toString(month),Integer.toString(year)).longValue()).collect(Collectors.toList()));
                            Log.d("TAG", "filter by date, type and price");
                        }
                    }
                }
                listener.onComplete(result);
            }
        });

    }
}
