package com.github.mrzhqiang.randall.binding;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.design.widget.BottomNavigationView;

@BindingMethods({
    @BindingMethod(type = BottomNavigationView.class, attribute = "android:navigationItemSelected", method = "setOnNavigationItemSelectedListener"),
}) public class BottomNavigationBindingAdapter {
}
