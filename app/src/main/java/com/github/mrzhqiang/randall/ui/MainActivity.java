package com.github.mrzhqiang.randall.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mrzhqiang.randall.R;

public class MainActivity extends AppCompatActivity {

  private TextView mTextMessage;

  public final BottomNavigationView.OnNavigationItemSelectedListener
      mOnNavigationItemSelectedListener = this::itemSelected;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTextMessage = (TextView) findViewById(R.id.message);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

  private boolean itemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.navigation_main:
        Toast.makeText(MainActivity.this, "兰达尔", Toast.LENGTH_SHORT).show();
        return true;
      case R.id.navigation_bookmark:
        Toast.makeText(MainActivity.this, "书签", Toast.LENGTH_SHORT).show();
        return true;
      case R.id.navigation_me:
        Toast.makeText(MainActivity.this, "我", Toast.LENGTH_SHORT).show();
        return true;
      default:
        return false;
    }
  }
}
