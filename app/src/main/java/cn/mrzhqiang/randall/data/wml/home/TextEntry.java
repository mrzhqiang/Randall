package cn.mrzhqiang.randall.data.wml.home;

import org.simpleframework.xml.Text;

/**
 * <p>
 * Created by mrZQ on 2017/10/9.
 */
public class TextEntry implements Entry {

    @Text
    public String text;

    @Override public Type type() {
        return Type.TEXT;
    }
}
