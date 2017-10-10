package cn.mrzhqiang.randall;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class JsoupUnitTest {

    public static void main(String[] args) throws IOException {
        URL url = new URL("http://haowanba.com");
        Document home = Jsoup.parse(url, (int) TimeUnit.SECONDS.toMillis(10));
//        System.out.println(home);
        String title = home.title();
        System.out.println(title);

        Element body = home.body();
        Elements bodyList = body.children();
//        System.out.println(bodyList);
        Element first = bodyList.first();
        parseNode(first);
    }

    public static void parseNode(Node node) {
        String nodeName = node.nodeName();

        switch (nodeName) {
            case "p":
                for (Node child : node.childNodes()) {
                    parseNode(child);
                }
                break;
            case "a":
                Element a = (Element) node;
                System.out.print(a.text());
                System.out.print(a.absUrl("href"));
                break;
            case "img":
//                System.out.println("这是一个图片链接");
                System.out.print(node.absUrl("src"));
                break;
            case "br":
                System.out.println("");
                break;
            case "#text":
                System.out.print(node);
                break;
        }
    }

}
