package cn.mrzhqiang.randall;

import cn.mrzhqiang.randall.ui.model.AccountModel;
import cn.mrzhqiang.randall.ui.model.RandallModel;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
  // create return method
  Picasso picasso();

  // add inject method
  void inject(AccountModel accountModel);

  void inject(RandallModel randallModel);
}
