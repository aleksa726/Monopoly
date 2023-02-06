package com.example.monopoly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monopoly.databinding.ViewHolderMatchBinding;
import com.example.monopoly.game.Game;
import com.example.monopoly.game.data.DateTimeUtil;
import com.example.monopoly.game.data.GameEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchHistoryAdapter extends RecyclerView.Adapter<MatchHistoryAdapter.MatchHistoryViewHolder> {

    public interface Callback<T>{
        void invoke(T parameter);
    }

    private List<GameEntity> gameEntities = new ArrayList<>();
    private MainActivity mainActivity;
    private MatchHistoryViewModel matchHistoryViewModel;
    private Callback<Integer> callback;

    public MatchHistoryAdapter(MainActivity mainActivity, Callback<Integer> callback){
        this.mainActivity = mainActivity;
        this.matchHistoryViewModel = new ViewModelProvider(mainActivity).get(MatchHistoryViewModel.class);
        this.callback = callback;
    }

    public void setGameEntities(List<GameEntity> gameEntities){
        this.gameEntities = gameEntities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderMatchBinding viewHolderMatchBinding = ViewHolderMatchBinding.inflate(
                layoutInflater,
                parent,
                false
        );
        return new MatchHistoryViewHolder(viewHolderMatchBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull MatchHistoryViewHolder holder, int position) {
        holder.bind(gameEntities.get(position));
    }


    @Override
    public int getItemCount() {
        return this.gameEntities.size();
    }

    public class MatchHistoryViewHolder extends RecyclerView.ViewHolder{

        public ViewHolderMatchBinding binding;

        public MatchHistoryViewHolder(@NonNull ViewHolderMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(GameEntity gameEntity){
            binding.idLabel.setText((int) gameEntity.getId() + " ");
            binding.dateLabel.setText(DateTimeUtil.getSimpleDateFormatDateOnly().format(gameEntity.getDateStart()));
            binding.numberOfPlayersLabel.setText(gameEntity.getNumberOfPlayers() + " ");
            Date start = gameEntity.getDateStart();
            Date end = gameEntity.getDateEnd();
            long diff = end.getTime() - start.getTime();
            long diffMinutes = diff / (60 * 1000);
            binding.durationlabel.setText((int) diffMinutes + " ");
            binding.winnerLabel.setText(gameEntity.getWinner());

            binding.simulateButton.setOnClickListener(v -> {
                int gameIndex = getAdapterPosition();
                callback.invoke(gameIndex);
            });
        }
    }
}
