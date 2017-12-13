package com.github.mrzhqiang.randall.viewmodel;

import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.net.Login;

public class ItemAccountViewHolder extends RecyclerView.ViewHolder {

  public final ObservableField<String> name = new ObservableField<>();
  public final ObservableField<String> status = new ObservableField<>("未知");

  public ItemAccountViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(Account account) {
    name.set(account.showName());

    Login login = account.login();
    if (login.equals(Login.EMPTY)) {
      status.set("未认证");
    } else {
      status.set(login.lastGame().showContent());
    }
  }
}
