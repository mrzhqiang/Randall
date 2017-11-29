package cn.mrzhqiang.smith.haowanba;

import android.os.Parcelable;
import cn.mrzhqiang.smith.html.Link;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue public abstract class Home implements Parcelable {

  public abstract String title();

  public abstract List<Link> links();

  public static Home create(String title, List<Link> links) {
    return new AutoValue_Home(title, links);
  }
}