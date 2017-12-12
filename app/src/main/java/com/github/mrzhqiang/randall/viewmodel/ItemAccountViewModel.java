package com.github.mrzhqiang.randall.viewmodel;

import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.github.mrzhqiang.smith.db.Account;

public class ItemAccountViewModel extends RecyclerView.ViewHolder {

  public final ObservableField<String> username = new ObservableField<>();
  public final ObservableField<String> status = new ObservableField<>("未知");

  public ItemAccountViewModel(View itemView) {
    super(itemView);
  }

  public void bind(Account account) {
    username.set(account.username());
    if (account.data() != null) {
      status.set("已成功登陆过");
    } else {
      status.set("无效或未认证");
    }
  }
}
