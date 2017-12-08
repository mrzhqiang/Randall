package com.github.mrzhqiang.randall;

import com.github.mrzhqiang.smith.BaseApp;

public final class RandallApp extends BaseApp {

  public static final String BASE_URL = "http://haowanba.com";

  @Override public boolean debug() {
    return BuildConfig.DEBUG;
  }

  @Override public String baseUrl() {
    return BASE_URL;
  }
}
