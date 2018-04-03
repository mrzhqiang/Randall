package com.github.mrzhqiang.randall.binding;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v7.widget.RecyclerView;

@BindingMethods({
    @BindingMethod(type = RecyclerView.class, attribute = "android:adapter", method = "setAdapter"),
    @BindingMethod(type = RecyclerView.class, attribute = "android:layoutManager", method = "setLayoutManager"),
    @BindingMethod(type = RecyclerView.class, attribute = "android:itemAnimator", method = "setItemAnimator"),
})
public final class RecyclerViewBindingAdapter {
}
