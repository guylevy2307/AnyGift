package com.example.anygift.feed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.Retrofit.CoinTransaction;
import com.example.anygift.Retrofit.LoginResult;
import com.example.anygift.Retrofit.User;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardsDetailsFragment extends Fragment {
    View view;
    FeedViewModel viewModel;
    UserViewModel userViewModel;
    private TextView name;
    private TextView value;
    private TextView buyAt;
    private Button deleteBtn;
    private Button mapBtn, editBtn;
    private ImageView userImage;
    private ImageView giftCardImage;
    Card giftCard = null;
    List<Category> categoryList;

    public CardsDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Testing testing = new Testing();

        view = inflater.inflate(R.layout.fragment_cards_details, container, false);
        //getActivity().setTitle("AnyGift - CardsDetails");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String giftCardId = CardsDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);

        List<Card> list = viewModel.getList();

        for (Card gc : list) {
            if (gc.getId().equals(giftCardId))
                giftCard = gc;
        }
        userImage = view.findViewById(R.id.details_picture_iv);
//        userViewModel.getUserById(giftCard.getOwner(), new UserViewModel.GetUserListener() {
//            @Override
//            public void onComplete(User user) {
//                if (user.getImageUrl() != null) {
//                    Picasso.get().load(user.getImageUrl()).into(userImage);
//                    userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    userImage.setClipToOutline(true);
//                }
//            }
//        });
        name = view.findViewById(R.id.card_details_username_tv);
        Model.instance.getUserRetrofit(giftCard.getOwner(), new Model.userReturnListener() {
            @Override
            public void onComplete(User user) {
                name.setText(user.getFirstName() + " " + user.getLastName());
                if (!user.getProfilePicture().isEmpty()) {
                    Picasso.get().load(user.getProfilePicture()).into(userImage);
                    userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    userImage.setClipToOutline(true);

                }
            }
        });
//        name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(CardsDetailsFragmentDirections.actionCardsDetailsFragmentToUserDetailsFragment(giftCard.getOwnerEmail()));
//            }
//        });
        value = view.findViewById(R.id.details_giftvalue_tv);
        mapBtn = view.findViewById(R.id.cardDetails_mapBtn);
        String val = Double.toString(giftCard.getValue());
        value.setText(val);
        buyAt = view.findViewById(R.id.details_buyatval_tv);
        String price = Double.toString(giftCard.getCalculatedPrice());
        buyAt.setText(price);
        giftCardImage = view.findViewById(R.id.details_giftpic_iv);
//        Picasso.get().load(giftCard.getCardType()).into(giftCardImage);
        deleteBtn = view.findViewById(R.id.details_delete_btn);
        editBtn = view.findViewById(R.id.details_edit_btn2);
        editBtn.setEnabled(true);
        if (giftCard.getOwner().equals(Model.instance.signedUser_new.getId())) {
            deleteBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
        }


//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                deleteBtn.setEnabled(false);
//                giftCard.setDeleted(true);
//                Model.instance.modelFirebase.addGiftCard(giftCard, new Model.AddGiftCardListener() {
//                    @Override
//                    public void onComplete() {
//                        Navigation.findNavController(view).navigateUp();
//                        Snackbar mySnackbar = Snackbar.make(view, "GiftCard Deleted!", BaseTransientBottomBar.LENGTH_LONG);
//                        mySnackbar.show();
//                    }
//                });
//
//            }
//        });

//        mapBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String[] cordinate = giftCard.getLatAndLong().split(",");
//                String uri = "http://maps.google.com/maps?q=loc:" + cordinate[0] + "," + cordinate[1];
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                intent.setPackage("com.google.android.apps.maps");
//                startActivity(intent);
//            }
//        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(CardsDetailsFragmentDirections.actionCardsDetailsFragmentToEditCardsDetailsFragment(giftCard.getId()));
            }
        });
        return view;
    }
}