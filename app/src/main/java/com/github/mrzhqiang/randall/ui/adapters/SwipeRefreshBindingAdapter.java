package com.github.mrzhqiang.randall.ui.adapters;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v4.widget.SwipeRefreshLayout;

@BindingMethods({
    @BindingMethod(type = SwipeRefreshLayout.class, attribute = "android:onRefresh", method = "setOnRefreshListener"),
    @BindingMethod(type = SwipeRefreshLayout.class, attribute = "android:isRefreshing", method = "setRefreshing"),
}) public class SwipeRefreshBindingAdapter {

}
