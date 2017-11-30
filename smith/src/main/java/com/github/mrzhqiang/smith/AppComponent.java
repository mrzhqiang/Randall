package com.github.mrzhqiang.smith;

import com.github.mrzhqiang.smith.model.SmithModel;
import com.github.mrzhqiang.smith.model.AccountModel;
import com.squareup.picasso.Picasso;
import dagger.Component;
import javax.inject.Singleton;

/**
 * 应用级别的依赖注入接口
 *
 * @author mrZQ
 */
@Singleton @Component(modules = AppModule.class) public interface AppComponent {

  Picasso picasso();

  void inject(SmithModel smithModel);

  void inject(AccountModel accountModel);
}