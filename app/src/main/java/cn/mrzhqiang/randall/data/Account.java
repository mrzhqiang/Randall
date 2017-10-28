package cn.mrzhqiang.randall.data;

/**
 * 用户账户，包括uid、用户名、密码，以及相应的本地属性
 */
public final class Account {

  /** 数据库id，用来更新字段或删除记录 */
  public long id = 0;

  /** 用户输入的账号，或未来开发生成小号而自动创建 */
  public String username;
  /** 用户输入的密码，或小号的随机密码 */
  public String password;

  /** 用户设定的一个别名，如果没有输入的话，将会是账号+id的形式 */
  public String alias;
  /** 状态：已删除、无效、默认、离线、在线 */
  public Status status = Status.DEFAULT;

  /** 安全地取得别名 */
  public String alias() {
    if (alias == null) {
      return "账号" + id;
    }
    return alias;
  }

  @Override public String toString() {
    return "id:" + id + ", username:" + username + ", alias:" + alias();
  }

  /** 通过数据库取出的下标转换为状态枚举 */
  public static Status from(int index) {
    return Status.values()[index];
  }

  /** 状态枚举 */
  public enum Status {
    DELETE("已删除"), INVALID("无效"), DEFAULT("未认证"), OFFLINE("离线"), ONLINE("在线"),;

    final String value;

    Status(String value) {
      this.value = value;
    }

    @Override public String toString() {
      return value;
    }
  }
}
