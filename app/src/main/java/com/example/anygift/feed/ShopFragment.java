package com.example.anygift.feed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.User;
import com.example.anygift.adapters.ShopGridAdapter;
import com.example.anygift.model.Model;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    RecyclerView gridRV;
    ShopGridAdapter adapter;
    List<Integer> images;
    List<String> titles;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog dialog;
    NavigationView navigationView;
    Dialog tryDialog;
    TextView popUpPrice,coinsTv;
    ImageView popUpCoinsIcon;
    Button popUpCancel, popUpSaveBtn,backToCardBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        backToCardBtn = view.findViewById(R.id.shop_back_to_card_btn);
        String cardId = ShopFragmentArgs.fromBundle(getArguments()).getCardId();

        if(!cardId.isEmpty()){
            backToCardBtn.setVisibility(View.VISIBLE);
            backToCardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString("giftCardId",cardId);
                    Navigation.findNavController(view).navigate(R.id.action_global_cardsDetailsFragment,args);
                }
            });
        }
        titles = new ArrayList<>();
        images = new ArrayList<>();


        titles.add("Cost : 45$");
        titles.add("Cost : 90$");
        titles.add("Cost : 220$");
        titles.add("Cost : 435$");
        titles.add("Cost : 860$");
        titles.add("Cost : 1$");
        images.add(R.drawable.fifty);
        images.add(R.drawable.handred);
        images.add(R.drawable.twofifty);
        images.add(R.drawable.fivehandred);
        images.add(R.drawable.taulsend);
        images.add(R.drawable.coin_0);

        gridRV = view.findViewById(R.id.shop_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        gridRV.setLayoutManager(gridLayoutManager);
        adapter = new ShopGridAdapter(getContext(), titles,images);
        gridRV.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                createNewShopDialog(position);
            }
        });


        return view;
    }

    public void createNewShopDialog(int pos){
        tryDialog = new Dialog(getActivity());
        tryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tryDialog.setContentView(R.layout.shop_popup);
        popUpCoinsIcon = tryDialog.findViewById(R.id.shop_popup_icon_iv);
        popUpPrice = tryDialog.findViewById(R.id.shop_popup_price_tv);
        popUpSaveBtn = tryDialog.findViewById(R.id.shop_popup_buy_btn);
        popUpCancel = tryDialog.findViewById(R.id.shop_popup_cancel_btn);

        popUpCoinsIcon.setImageResource(images.get(pos));
        popUpPrice.setText(titles.get(pos));

        tryDialog.show();
        tryDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        tryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tryDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        tryDialog.getWindow().setGravity(Gravity.BOTTOM);

        popUpSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double num;
                switch (pos) {
                    case  0:
                       num = 50;
                       break;
                    case  1:
                        num = 100;
                        break;
                    case  2:
                        num = 250;
                        break;
                    case  3:
                        num = 500;
                        break;
                    case  4:
                        num = 1000;
                        break;
                    case  5:
                        num = 1;
                        break;
                    default: num = 0;
                }

                Model.instance.addCoinsToUser(Model.instance.getSignedUser().getId(),
                        num, new Model.userReturnListener() {
                            @Override
                            public void onComplete(User user, String message) {
                                Model.instance.getSignedUser().setCoins(user.getCoins());
                                navigationView = getActivity().findViewById(R.id.Navigation_view);
                                View header= (View)navigationView.getHeaderView(0);
                                coinsTv = header.findViewById(R.id.header_coins_tv);
                                coinsTv.setText(Model.instance.getSignedUser().getCoins().toString());
                                Toast.makeText(getContext(), "Thank You !! Spend your Gcoins wisely :)", Toast.LENGTH_SHORT).show();
                                tryDialog.dismiss();
                            }
                        });

            }
        });

        popUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryDialog.dismiss();
            }
        });

    }
}