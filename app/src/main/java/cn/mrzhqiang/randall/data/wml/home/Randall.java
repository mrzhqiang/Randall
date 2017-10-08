package cn.mrzhqiang.randall.data.wml.home;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * <p>
 * Created by mrZQ on 2017/10/8.
 */
@Root(name = "html")
public class Randall {
    @Element
    public Head head;
    @Element
    public Body body;
}

