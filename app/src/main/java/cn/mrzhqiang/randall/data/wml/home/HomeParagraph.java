package cn.mrzhqiang.randall.data.wml.home;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.List;

/**
 * <p>
 * Created by mrZQ on 2017/10/8.
 */
public class HomeParagraph implements Paragraph {
    @ElementListUnion({
                              @ElementList(entry = "img", type = ImgEntry.class, inline = true),
                              @ElementList(entry = "br", type = BrEntry.class, inline = true),
                              @ElementList(entry = "a", type = AEntry.class, inline = true),
                      })
    public List<Entry> list;
}
