package com.github.mrzhqiang.smith.db;

/**
 * 数据库异常，作为运行时异常的扩展
 *
 * @author mrZQ
 */

public class DbException extends RuntimeException {
  public DbException(String message) {
    super(message);
  }
}
