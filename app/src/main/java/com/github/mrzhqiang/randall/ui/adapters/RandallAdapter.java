package com.github.mrzhqiang.randall.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ItemAccountBinding;
import com.github.mrzhqiang.randall.ui.LoginActivity;
import com.github.mrzhqiang.randall.viewmodel.ItemAccountViewHolder;
import com.github.mrzhqiang.smith.db.Account;
import java.util.List;

public class RandallAdapter extends RecyclerView.Adapter<ItemAccountViewHolder> {

  public final ObservableList<Account> dataList = new ObservableArrayList<>();

  @Override public ItemAccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemAccountBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.item_account, parent, false);
    binding.setItemAccount(new ItemAccountViewHolder(binding.getRoot()));
    return binding.getItemAccount();
  }

  @Override public void onBindViewHolder(ItemAccountViewHolder holder, int position) {
    holder.bind(dataList.get(position));
    holder.itemView.setOnClickListener(v -> {
      Context context = v.getContext();
      Intent intent = new Intent(context, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
      intent.putExtra("login", dataList.get(holder.getAdapterPosition()).login());
      context.startActivity(intent);
    });
  }

  @Override public int getItemCount() {
    return dataList.size();
  }

  @Override public long getItemId(int position) {
    return dataList.get(position).hashCode();
  }

  public void updateData(List<Account> accounts) {
    dataList.clear();
    dataList.addAll(accounts);
    notifyDataSetChanged();
  }
}
