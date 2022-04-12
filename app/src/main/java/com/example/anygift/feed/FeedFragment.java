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
import com.example.anygift.model.Model;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class FeedFragment extends Fragment {
    FeedViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    TextView nameTv;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
       // getActivity().setTitle("AnyGift - Feed");
        swipeRefresh = view.findViewById(R.id.giftCardlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshGiftCardsList());
        RecyclerView list = view.findViewById(R.id.cards_list_rv);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager horizontalLayout2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        list.setLayoutManager(horizontalLayout2);
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        nameTv = view.findViewById(R.id.cards_list_user_name_tv);
        nameTv.setText("Hello " + Model.instance.getSignedUser().getFirstName() +" and welcome to the gift card trading platform. Find evry gift card buy or trade with your own cards.");
        adapter.setOnItemClickListener(new FeedFragment.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = viewModel.getList().get(position).getValue();
                String id = viewModel.getList().get(position).getId();
                Log.d("TAG", "Gift card in value of: " + val);
                Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCardsDetailsFragment(id));
            }
        });
        setHasOptionsMenu(true);
//        viewModel.getList().observe(getViewLifecycleOwner(), list1 -> refresh());
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

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public OnItemClickListener listener;
        TextView cardValue, cardValTag;
        ImageView cardImage;
        int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardValue = itemView.findViewById(R.id.cards_list_row_amount_in_card_tv);
            cardImage = itemView.findViewById(R.id.cards_list_row_iv);
            cardValTag = itemView.findViewById(R.id.listRow_tv_value);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(v, position);
                }
            });
        }

        public void bindView(int position) {
                cardValue.setText(String.valueOf(viewModel.getList().get(position).getValue()));
//                Picasso.get().load(viewModel.getList().get(position).getCardType()).into(cardImage);
                this.position = position;

        }
    }

    interface OnItemClickListener {

        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<FeedFragment.MyViewHolder> {
        OnItemClickListener listener;

        public void setOnItemClickListener(FeedFragment.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public FeedFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cards_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.listener = listener;

            /* use progress bar
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    pb.setVisibility(View.INVISIBLE);
                }
            }, 2500);*/

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull FeedFragment.MyViewHolder holder, int position) {

            ((MyViewHolder) holder).bindView(position);

        }

        @Override
        public int getItemCount() {
            if (viewModel.getList() == null)
                return 0;
            return viewModel.getList().size();
        }
    }
}