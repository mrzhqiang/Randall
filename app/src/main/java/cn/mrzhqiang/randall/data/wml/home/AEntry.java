package cn.mrzhqiang.randall.data.wml.home;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * <p>
 * Created by mrZQ on 2017/10/8.
 */
public class AEntry implements Entry {

    @Attribute
    private String href;
    @Text
    private String value;

    public String getHref() {
        if (href.startsWith("http")) {
            return href;
        }
        if (href.startsWith(".")) {
            return "http://haowanba.com" + href.substring(1);
        }
        return "http://haowanba.com" + href;
    }

    public String getValue() {
        return value;
    }

    @Override public Type type() {
        return Type.A;
    }
}
