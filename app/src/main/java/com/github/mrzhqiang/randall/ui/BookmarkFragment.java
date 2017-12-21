package com.github.mrzhqiang.randall.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.FragmentBookmarkBinding;

public class BookmarkFragment extends Fragment {

  public static BookmarkFragment newInstance() {
    Bundle args = new Bundle();
    BookmarkFragment fragment = new BookmarkFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_bookmark, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentBookmarkBinding binding = DataBindingUtil.bind(view);
    binding.setBookmark(this);
  }
}
