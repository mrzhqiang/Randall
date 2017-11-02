package cn.mrzhqiang.randall.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import cn.mrzhqiang.randall.data.HomePage;
import cn.mrzhqiang.randall.data.LoginPage;
import cn.mrzhqiang.randall.model.AccountModel;
import cn.mrzhqiang.randall.model.RandallModel;
import cn.mrzhqiang.randall.net.Result;
import com.squareup.picasso.Picasso;

/**
 * 新的账号：先添加到本地数据库，同时验证账号密码是否有效；当不存在时，可选注册
 */
public final class AddAccountViewModel {

}
