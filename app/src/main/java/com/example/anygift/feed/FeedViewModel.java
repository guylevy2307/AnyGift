package com.example.anygift.feed;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.anygift.Retrofit.Card;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;

import java.util.ArrayList;
import java.util.List;

public class FeedViewModel extends ViewModel {
    private List<Card> gfList;

    public FeedViewModel() {
        gfList = new ArrayList<>();
        refreshList(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {

            }
        });
    }

    public List<Card> getList() {
        return gfList;
    }

    public void refreshList(Model.cardsReturnListener listener) {
        Model.instance.getAllCards(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                gfList.clear();
                gfList.addAll(cards);
                listener.onComplete(gfList, "Cards are HERE");
            }
        });
    }
}
