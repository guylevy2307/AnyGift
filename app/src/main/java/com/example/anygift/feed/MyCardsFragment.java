package com.example.anygift.feed;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.Retrofit.LoginResult;
import com.example.anygift.model.Model;
import com.example.anygift.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


public class MyCardsFragment extends Fragment {
    View view;
    MyCardsViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    TextView userName,userEmail,userPhone,userAddress,numOfSold,numOfBought,soldInCoins,boughtInCoins;
    ImageView userImage,editIv,addCardIv;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(MyCardsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_cards, container, false);
        //getActivity().setTitle("AnyGift - MyCards");
        swipeRefresh = view.findViewById(R.id.my_cards_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshGiftCardsList());
        RecyclerView list = view.findViewById(R.id.MyCards_list_rv);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager horizontalLayout2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        list.setLayoutManager(horizontalLayout2);
        adapter = new MyCardsFragment.MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyCardsFragment.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = viewModel.getList().getValue().get(position).getValue();
                String id = viewModel.getList().getValue().get(position).getId();
                Log.d("TAG", "Gift card in value of: " + val);
                Navigation.findNavController(v).navigate(MyCardsFragmentDirections.actionMyCardsFragmentToCardsDetailsFragment(id));

            }
        });

        userName = view.findViewById(R.id.my_cards_user_name_tv);
        userImage = view.findViewById(R.id.my_cards_avater_iv);
        userEmail = view.findViewById(R.id.my_cards_email_tv);
        userPhone = view.findViewById(R.id.my_cards_phone_tv);
        userAddress = view.findViewById(R.id.my_cards_address_tv);
        numOfSold = view.findViewById(R.id.my_cards_sold_counter_tv);
        numOfBought = view.findViewById(R.id.my_cards_bought_counter_tv);
        soldInCoins = view.findViewById(R.id.my_cards_sold_in_coins_tv);
        boughtInCoins = view.findViewById(R.id.my_cards_bought_in_coins_tv);
        editIv = view.findViewById(R.id.my_cards_edit_iv);
        addCardIv = view.findViewById(R.id.my_cards_add_card_iv);

        editIv.setOnClickListener(v -> Navigation.findNavController(v).navigate(MyCardsFragmentDirections.actionMyCardsFragmentToEditProfileFragment()));
        addCardIv.setOnClickListener(v-> Navigation.findNavController(v).navigate(MyCardsFragmentDirections.actionGlobalAddCardFragment()));

        setUserUI();
        setHasOptionsMenu(true);
        viewModel.getList().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getListLoadingState().getValue() == Model.GiftListLoadingState.loading);
        Model.instance.getListLoadingState().observe(getViewLifecycleOwner(), ListLoadingState -> {
            if (ListLoadingState == Model.GiftListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }

        });
        return view;

    }

    private void setUserUI() {
        LoginResult user = Model.instance.getSignedUser();

        userName.setText(user.getFirstName() + " " + user.getLastName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhone());
        userAddress.setText(user.getAddress());
        if (user.getProfilePicture() != null) {
            Picasso.get().load(user.getProfilePicture()).into(userImage);
            userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            userImage.setClipToOutline(true);
        }
//        numOfSold.setText(user.getName());
//        numOfBought.setText(user.getName());
//        soldInCoins.setText(user.getName());
//        boughtInCoins.setText(user.getName());

    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyCardsFragment.OnItemClickListener listener;
        TextView cardValue, cardValTag;
        ImageView cardImage;
        int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardValue = itemView.findViewById(R.id.my_card_row_amount_in_card_tv);
            cardImage = itemView.findViewById(R.id.my_card_row_iv);
            cardValTag = itemView.findViewById(R.id.my_card_row_price_now_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(v, position);
                }
            });
        }

        public void bindView(int position) {
            String gfEmail = viewModel.getList().getValue().get(position).getOwnerEmail();
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (!viewModel.getList().getValue().get(position).getDeleted() && gfEmail.equals(userEmail)) {
                cardValue.setText(String.valueOf(viewModel.getList().getValue().get(position).getValue()));
                Picasso.get().load(viewModel.getList().getValue().get(position).getGiftCardImageUrl()).into(cardImage);
                this.position = position;
            } else {
                cardValue.setVisibility(View.GONE);
                cardImage.setVisibility(View.GONE);
                cardValTag.setVisibility(View.GONE);
            }
        }
    }

    interface OnItemClickListener {

        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyCardsFragment.MyViewHolder> {
        MyCardsFragment.OnItemClickListener listener;

        public void setOnItemClickListener(MyCardsFragment.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyCardsFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.my_card_row, parent, false);
            MyCardsFragment.MyViewHolder holder = new MyCardsFragment.MyViewHolder(view);
            holder.listener = listener;
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyCardsFragment.MyViewHolder holder, int position) {
            holder.bindView(position);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getList().getValue() == null)
                return 0;
            return viewModel.getList().getValue().size();
        }
    }
}