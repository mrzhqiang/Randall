package cn.mrzhqiang.randall.data.wml.home;

import org.simpleframework.xml.Attribute;

/**
 * <p>
 * Created by mrZQ on 2017/10/8.
 */
public class ImgEntry implements Entry {

    @Attribute
    private String src;
    @Attribute(required = false)
    private String alt;

    public String getImg() {
        if (src.startsWith("http")) {
            return src;
        }
        if (src.startsWith(".")) {
            return "http://haowanba.com" + src.substring(1);
        }
        return "http://haowanba.com" + src;
    }

    public String getAlt() {
        return alt;
    }

    @Override public Type type() {
        return Type.IMG;
    }
}
