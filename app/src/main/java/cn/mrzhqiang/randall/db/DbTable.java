package cn.mrzhqiang.randall.db;

import android.support.annotation.NonNull;
import android.util.LongSparseArray;

import java.util.List;

/**
 * 数据表的基本结构接口
 * <p>
 * Created by mrZQ on 2017/4/5.
 */
public interface DbTable {
  /**
   * 表的创建语句
   */
  @NonNull String getCreateSql();

  /**
   * 表的升级语句，通过对应版本号获取SQL升级命令
   */
  @NonNull LongSparseArray<List<String>> getUpgrade();
}
