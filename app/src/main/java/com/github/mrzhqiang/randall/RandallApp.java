package com.github.mrzhqiang.randall;

import com.github.mrzhqiang.smith.SmithApp;

public final class RandallApp extends SmithApp {

  public static final String BASE_URL = "http://haowanba.com";

  @Override public boolean debug() {
    return BuildConfig.DEBUG;
  }

  @Override public String baseUrl() {
    return BASE_URL;
  }
}
