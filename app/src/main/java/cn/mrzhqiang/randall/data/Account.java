package cn.mrzhqiang.randall.data;

import cn.mrzhqiang.helper.TimeHelper;
import java.util.Date;

/**
 * 用户账户，包括uid、用户名、密码，以及相应的本地属性
 */
public final class Account {

  /** 数据库id，用来更新字段或删除记录 */
  public long id = 0;

  /** 地狱之门生成的uid */
  public long uid;
  /** 用户输入的账号，或未来开发生成小号而自动创建 */
  public String username;
  /** 用户输入的密码，或小号的随机密码 */
  public String password;

  /** 用户设定的一个别名，如果没有输入的话，将会是账号+id的形式 */
  public String alias;
  /** 状态：已删除、无效、默认、离线、在线 */
  public Status status = Status.DEFAULT;
  /** 更新时间：当使用一个账号进入游戏，将获得这个值的更新 */
  public Date updated;
  /** 创建时间：默认的排序指标，其他还可以选择：别名、状态、更新时间 */
  public Date created;

  public String alias() {
    if (alias == null) {
      return "账号" + id;
    }
    return alias;
  }

  public String update() {
    if (updated == null) {
      updated = new Date();
    }
    return TimeHelper.showTime(updated);
  }

  public String create() {
    if (created == null) {
      created = new Date();
    }
    return TimeHelper.showTime(created);
  }

  @Override public String toString() {
    return "alias:" + alias + ", status:" + status;
  }

  /** 从数据库保存的code生成状态枚举 */
  public static Status from(int code) {
    if (code == -2) {
      return Status.DELETE;
    }
    if (code == -1) {
      return Status.INVALID;
    }
    if (code == 1) {
      return Status.OFFLINE;
    }
    if (code == 2) {
      return Status.ONLINE;
    }
    return Status.DEFAULT;
  }

  public enum Status {
    DELETE("已删除", Integer.MIN_VALUE), INVALID("无效", -1), DEFAULT("未认证", 0), OFFLINE("离线",
        1), ONLINE("在线", 2),;

    final String value;
    final int code;

    Status(String value, int code) {
      this.value = value;
      this.code = code;
    }

    public int code() {
      return code;
    }

    @Override public String toString() {
      return value;
    }
  }
}
