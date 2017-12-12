package com.github.mrzhqiang.randall.ui.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ItemAccountBinding;
import com.github.mrzhqiang.randall.viewmodel.ItemAccountViewModel;
import com.github.mrzhqiang.smith.db.Account;
import java.util.List;

public class RandallAdapter extends RecyclerView.Adapter<ItemAccountViewModel> {

  public final ObservableList<Account> dataList = new ObservableArrayList<>();

  @Override public ItemAccountViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemAccountBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.item_account, parent, false);
    binding.setItemAccount(new ItemAccountViewModel(binding.getRoot()));
    return binding.getItemAccount();
  }

  @Override public void onBindViewHolder(ItemAccountViewModel holder, int position) {
    holder.bind(dataList.get(position));
  }

  @Override public int getItemCount() {
    return dataList.size();
  }

  @Override public long getItemId(int position) {
    return dataList.get(position).hashCode();
  }

  public void updateData(List<Account> accounts) {
    if (accounts != null) {
      dataList.clear();
      dataList.addAll(accounts);
      notifyDataSetChanged();
    }
  }
}
