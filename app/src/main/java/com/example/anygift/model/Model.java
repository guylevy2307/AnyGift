package com.example.anygift.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.anygift.MyApplication;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.Retrofit.CoinTransaction;
import com.example.anygift.Retrofit.Income;
import com.example.anygift.Retrofit.LoginResult;
import com.example.anygift.Retrofit.Outcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    public ModelFirebase modelFirebase = new ModelFirebase();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public User signedUser;
    public LoginResult signedUser_new;
    public ModelRetrofit modelRetrofit = new ModelRetrofit();
    public MutableLiveData<List<GiftCard>> giftCardsList = new MutableLiveData<>();
    public List<Category> categories = new ArrayList<>();

    private Model() {
        signedUser = new User();
        signedUser_new = new LoginResult();
        ListLoadingState.setValue(GiftListLoadingState.loaded);
    }

    public LiveData<List<GiftCard>> getAll() {
        if (giftCardsList.getValue() == null) {
            refreshGiftCardsList();
        }
        return giftCardsList;
    }

    //Node- Retrofit
    public interface StringListener {
        void onComplete(String message);
    }

    public interface userLoginListener{
        void onComplete(LoginResult loginResult,String message);
    }
    public interface userReturnListener {
        void onComplete(com.example.anygift.Retrofit.User user);
    }
    public interface cardReturnListener {
        void onComplete(Card card);
    }

    public interface cardsReturnListener {
        void onComplete(List<Card> cards,String message);
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


    public void login(HashMap<String, String> map, userLoginListener listener) {
        modelRetrofit.login(map, listener);
    }

    public void getUserRetrofit(String user_id, userReturnListener listener) {
        modelRetrofit.getUser(user_id, listener);
    }

    public void getAllCategories(categoriesReturnListener l) {
        modelRetrofit.getAllCategories(l);
    }

    public void getAllCardTypes(cardTypesReturnListener l) {
        modelRetrofit.getAllCardTypes(l);
    }

    public void getUserIncome(String user_id,incomeListener listener) {
        modelRetrofit.getUserIncome(user_id, listener);
    }

    public void getUserOutCome(String user_id,outComeListener listener) {
        modelRetrofit.getUserOutCome(user_id, listener);
    }

    public void addUserRetrofit(HashMap<String,Object> map, userReturnListener listener){
        modelRetrofit.addUser(map, listener);
    }
    public void addCoinTransaction(HashMap<String,Object> map, coinTransactionListener listener){
        modelRetrofit.addCoinTransaction(map, listener);
    }

    public void updateUser(String user_id, HashMap<String,Object> map, userLoginListener listener){
        modelRetrofit.updateUser(user_id,map, listener);
    }

    public void addCardRetrofit(HashMap<String,Object> map, cardReturnListener listener){
        modelRetrofit.addCard(map, listener);
    }

    public void getAllCards(cardsReturnListener listener){
        modelRetrofit.getAllCards(listener);
    }

    public void getAllUserCards(String user_id, cardsReturnListener listener){
        modelRetrofit.getAllUserCards(user_id, listener);
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

    public interface GetAllGiftCardListener {
        void onComplete();
    }

    public interface GetUserListener {
        void onComplete(User user);
    }

    public void refreshGiftCardsList() {
        ListLoadingState.setValue(GiftListLoadingState.loading);

        //1. get local last update date
        final SharedPreferences sp = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);
        executor.execute(() -> {
            List<GiftCard> gcList = AppLocalDb.db.giftCardDao().getAll();
            giftCardsList.postValue(gcList);
        });
        //2. get all updated record from firebase from the last update date
        modelFirebase.getAllProducts(lastUpdated, new ModelFirebase.GetAllGiftCardListener() {
            @Override
            public void onComplete(List<GiftCard> result) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        long lastU = 0;
                        for (GiftCard gf : result) {
                            AppLocalDb.db.giftCardDao().insertAll(gf);
                            if (gf.getLastUpdated() > lastU) {
                                lastU = gf.getLastUpdated();
                            }
                        }
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("lastUpdated", lastU)
                                .commit();

                        List<GiftCard> stList = AppLocalDb.db.giftCardDao().getAll();
                        giftCardsList.postValue(stList);
                        ListLoadingState.postValue(GiftListLoadingState.loaded);


                    }
                });
            }
        });
    }


    public interface AddGiftCardListener {
        void onComplete();
    }

    public void addGiftCard(final GiftCard giftCard, final AddGiftCardListener listener) {
        modelFirebase.addGiftCard(giftCard, listener);
    }


    public void updateGiftCard(final GiftCard giftCard, final AddGiftCardListener listener) {
        modelFirebase.updateGiftCard(giftCard, listener);
    }


    public interface GetGiftCardListener {
        void onComplete(GiftCard giftCard);
    }

    public void getGiftCard(String email, GetGiftCardListener listener) {
        modelFirebase.getGiftCard(email, listener);
    }

     /*
     //upload image
      public interface Listener<T> {
            void onComplete(T result);
        }


      public interface UploadImageListener extends Listener<String>{ }

      */


    public interface UploadImageListener {
        void onComplete(String url);
    }


    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }

    public interface UploadUserImageListener {
        void onComplete(String url);
    }

    public void uploadUserImage(Bitmap imageBmp, String name, final UploadUserImageListener listener) {
        modelFirebase.uploadUserImage(imageBmp, name, listener);
    }


    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }

    //user
    public interface AddUserListener {
        void onComplete();
    }

    public void addUser(final User user, final AddUserListener listener) {
        modelFirebase.addUser(user, new AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public void updateUser(final User user, final AddUserListener listener) {
        modelFirebase.updateUser(user, listener);
    }


    public ModelFirebase getModelFirebase() {
        return this.modelFirebase;
    }

    /**
     * Authentication
     */
    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

    public LoginResult getSignedUser() {
        return signedUser_new;
    }

    public void setCurrentUser(GetUserListener listener) {
        modelFirebase.setCurrentUser(user -> {
            signedUser = user;
            listener.onComplete(user);
        });
    }
}
