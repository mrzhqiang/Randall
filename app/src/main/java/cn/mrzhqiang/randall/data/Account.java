package cn.mrzhqiang.randall.data;

import cn.mrzhqiang.helper.TimeHelper;
import java.util.Date;

/**
 * 用户账户，包括uid、用户名、密码，以及相应的
 */
public final class Account {

  public long id = 0;

  public long uid;
  public String username;
  public String password;

  public String alias;
  public Status status = Status.DEFAULT;
  public boolean isAvailable = true;
  public Date updated;
  public Date created;

  public String alias() {
    if (alias == null) {
      return "账号" + id;
    }
    return alias;
  }

  public String update() {
    if (updated == null) return TimeHelper.showTime(System.currentTimeMillis());
    return TimeHelper.showTime(updated.getTime());
  }

  public String create() {
    if (created == null) return TimeHelper.showTime(System.currentTimeMillis());
    return TimeHelper.showTime(created.getTime());
  }

  /** 从数据库保存的code生成状态枚举 */
  public static Status from(int code) {
    if (code == -1) {
      return Status.INVALID;
    }
    if (code == 1) {
      return Status.OFLINE;
    }
    if (code == 2) {
      return Status.ONLINE;
    }
    return Status.DEFAULT;
  }

  public enum Status {
    INVALID("无效", -1), DEFAULT("未认证", 0), OFLINE("离线", 1), ONLINE("在线", 2),;

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
